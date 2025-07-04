import * as cdk from "aws-cdk-lib";
import { Construct } from "constructs";
import * as sqs from "aws-cdk-lib/aws-sqs";
import * as apigw from "aws-cdk-lib/aws-apigateway";
import * as iam from "aws-cdk-lib/aws-iam";
import * as lambda from "aws-cdk-lib/aws-lambda";
import * as path from "path";
import { SqsEventSource } from "aws-cdk-lib/aws-lambda-event-sources";
import { Timeout } from "aws-cdk-lib/aws-stepfunctions";

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const prEventsQueue = new sqs.Queue(this, "MyFirstLogicalQueueID", {
      queueName: "cdk-hello-world-actual-queue-name",
      visibilityTimeout: cdk.Duration.seconds(300),
    });

    new cdk.CfnOutput(this, "MyQueueArnOutput", {
      value: prEventsQueue.queueArn,
      description: "The ARN of my first SQS queue",
      exportName: "myFirstQueueArnExport",
    });

    new cdk.CfnOutput(this, "MyQueueUrlOutput", {
      value: prEventsQueue.queueUrl,
      description: "The URL of my first SQS queue",
    });

    const apiGatewaySqsRole = new iam.Role(this, "ApiGatewaySqsRole", {
      assumedBy: new iam.ServicePrincipal("apigateway.amazonaws.com"),
    });

    prEventsQueue.grantSendMessages(apiGatewaySqsRole);

    const restApi = new apigw.RestApi(this, "PrWebhookApiGateway", {
      restApiName: "Bitbucket PR Webhook Receiver API",
      description: "Receives webhook events from Bitbucket for PRs",
      deployOptions: {
        stageName: "v1",
      },
    });

    const sqsIntegration = new apigw.AwsIntegration({
      service: "sqs",
      path: prEventsQueue.queueName,
      integrationHttpMethod: "POST",
      options: {
        credentialsRole: apiGatewaySqsRole,
        requestParameters: {
          "integration.request.header.Content-Type":
            "'application/x-www-form-urlencoded'",
        },
        requestTemplates: {
          "application/json":
            "Action=SendMessage&MessageBody=$util.urlEncode($input.body)",
        },
        integrationResponses: [
          {
            statusCode: "200",
            responseTemplates: {
              "application/json":
                '{"message": "Webhook received and queued successfully."}',
            },
          },
          {
            selectionPattern: "^\\[Error\\].*",
            statusCode: "500",
            responseTemplates: {
              "application/json": '{"message": "Failed to queue webhook."}',
            },
          },
        ],
      },
    });

    const webhookResource = restApi.root.addResource("webhook");

    webhookResource.addMethod("POST", sqsIntegration, {
      methodResponses: [{ statusCode: "200" }, { statusCode: "500" }],
    });

    new cdk.CfnOutput(this, "WebhookApiEndpointOutput", {
      value: restApi.urlForPath(webhookResource.path),
      description: "The URL of the API Gateway webhook endpoint",
    });

    const sqsProcessorLambdaRole = new iam.Role(
      this,
      "SqsProcessorLambdaRole",
      {
        assumedBy: new iam.ServicePrincipal("lambda.amazonaws.com"),
      }
    );

    sqsProcessorLambdaRole.addManagedPolicy(
      iam.ManagedPolicy.fromAwsManagedPolicyName(
        "service-role/AWSLambdaBasicExecutionRole"
      )
    );

    prEventsQueue.grantConsumeMessages(sqsProcessorLambdaRole);

    const sqsProcessorLambda = new lambda.Function(this, "SqsProcessorLambda", {
      runtime: lambda.Runtime.JAVA_17,
      code: lambda.Code.fromAsset(
        path.join(
          __dirname,
          "../../lambda-java/target/pr-reviewer-lambda-1.0.0.jar"
        )
      ),
      handler: "com.github.abfcode.prreviewer.SqsMessageHandler::handleRequest",
      role: sqsProcessorLambdaRole,
      memorySize: 512,
      timeout: cdk.Duration.seconds(30),
      environment: {
        SQS_QUEUE_URL: prEventsQueue.queueUrl,
      },
    });

    sqsProcessorLambda.addEventSource(
      new SqsEventSource(prEventsQueue, {
        batchSize: 1,
      })
    );
  }
}
