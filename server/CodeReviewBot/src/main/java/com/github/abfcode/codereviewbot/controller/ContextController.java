package com.github.abfcode.codereviewbot.controller;

import com.github.abfcode.codereviewbot.service.BitbucketApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/context")
@RequiredArgsConstructor
public class ContextController {

    private final BitbucketApiService bitbucketApiService;

    @GetMapping("/pr/{pullRequestId}")
    public ResponseEntity<String> getPullRequestDetails(@PathVariable String pullRequestId) {
        return ResponseEntity.ok(bitbucketApiService.getPullRequestDetails(pullRequestId).toString());
    }

}
