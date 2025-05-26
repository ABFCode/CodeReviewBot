package com.github.abfcode.codereviewbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LinksDto(LinkDTO commits, LinkDTO diff, LinkDTO diffstat) {
}
