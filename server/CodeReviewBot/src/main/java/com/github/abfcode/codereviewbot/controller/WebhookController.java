package com.github.abfcode.codereviewbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class WebhookController {


    @PostMapping
    public ResponseEntity<String> handleBitbucketWebhook(@RequestBody String rawPayload,
                                                         @RequestHeader Map<String, String> headers,
                                                            @RequestHeader("x-event-key") String eventKey) {
        log.info("Received Bitbucket Webhook!");
        log.info("--- HEADERS ---");
        headers.forEach((key, value) -> log.info("{}: {}", key, value));
        log.info("--- RAW PAYLOAD ---");
        log.info(rawPayload);
        log.info("--- END OF PAYLOAD ---");

        return ResponseEntity.ok("Webhook received!");
    }

}
