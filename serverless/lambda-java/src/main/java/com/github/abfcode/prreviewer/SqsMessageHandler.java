package com.github.abfcode.prreviewer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqsMessageHandler implements RequestHandler<SQSEvent, Void> {
    private static final Logger log = LoggerFactory.getLogger(SqsMessageHandler.class);

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        log.info("Lambda function invoked with event: {}", sqsEvent);

        try{
            SQSEvent.SQSMessage message = sqsEvent.getRecords().get(0);
            String messageBody = message.getBody();
            log.info("Message ID: {}", message.getMessageId());
            log.info("Message body: {}", messageBody);
        }
        catch (Exception e){
            log.error("Error processing SQS message", e);
        }
        log.info("Finished processing SQS message");
        return null;
    }
}
