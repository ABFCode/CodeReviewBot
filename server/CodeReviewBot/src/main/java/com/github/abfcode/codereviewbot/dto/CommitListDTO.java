package com.github.abfcode.codereviewbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommitListDTO(List<SingleCommitDTO> commits) {
}
