package net.guides.springboot.springbootasyncexample.service;

import net.guides.springboot.springbootasyncexample.model.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;


@Service
public class GitHubLookupService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException{
        logger.info("Lokking up" + user);

        String url = String.format("https://api.github.com/users/%s", user);
        User result = restTemplate.getForObject(url, User.class);
        Thread.sleep(1000);
        return CompletableFuture.completedFuture(result);
    }
}
