package com.github.abfcode.codereviewbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BitbucketWebhookPayloadDTO(WebhookRepositoryDTO repository, WebhookPullRequestDTO pullrequest) {
}
