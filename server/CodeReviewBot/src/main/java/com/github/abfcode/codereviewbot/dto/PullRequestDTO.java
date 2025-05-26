package com.github.abfcode.codereviewbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PullRequestDTO(long id, String title, String description, AuthorDTO author, BranchDetailsDTO source,
                             BranchDetailsDTO destination, LinksDto links, String state) {
}
