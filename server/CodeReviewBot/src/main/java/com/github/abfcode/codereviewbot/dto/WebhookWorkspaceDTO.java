package com.github.abfcode.codereviewbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WebhookWorkspaceDTO(String slug) {
}
