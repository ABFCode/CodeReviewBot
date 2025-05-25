import * as cdk from "aws-cdk-lib";
import { Construct } from "constructs";
import * as sqs from "aws-cdk-lib/aws-sqs";

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const myFirstQueue = new sqs.Queue(this, "MyFirstLogicalQueueID", {
      queueName: "cdk-hello-world-actual-queue-name",
      visibilityTimeout: cdk.Duration.seconds(300),
    });

    new cdk.CfnOutput(this, "MyQueueArnOutput", {
      value: myFirstQueue.queueArn,
      description: "The ARN of my first SQS queue",
      exportName: "myFirstQueueArnExport",
    });

    new cdk.CfnOutput(this, "MyQueueUrlOutput", {
      value: myFirstQueue.queueUrl,
      description: "The URL of my first SQS queue",
    });
  }
}
