package com.github.abfcode.codereviewbot.dto;

public record PullRequestContextDataDTO(PullRequestDTO pullrequest,
                                        CommitListDTO commits, String diff, String diffstat) {
}
