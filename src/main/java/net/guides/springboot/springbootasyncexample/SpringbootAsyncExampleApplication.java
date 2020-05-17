package net.guides.springboot.springbootasyncexample;

import net.guides.springboot.springbootasyncexample.model.User;
import net.guides.springboot.springbootasyncexample.service.GitHubLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;


@SpringBootApplication
@EnableAsync
public class SpringbootAsyncExampleApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringbootAsyncExampleApplication.class);

	@Autowired
	private GitHubLookupService gitHubLookupService;

	@Bean("threadPoolTaskExecutor")
	public TaskExecutor getAsyncExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(1000);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("Async-");
		return executor;
	}

	public static void main(String[] args) {

		SpringApplication.run(SpringbootAsyncExampleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		long start = System.currentTimeMillis();

		CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
		CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
		CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");
		CompletableFuture<User> page4 = gitHubLookupService.findUser("RameshMF");

		CompletableFuture.allOf(page1, page2, page3, page4);
		logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
		logger.info("--> " + page1.get());
		logger.info("--> " + page2.get());
		logger.info("--> " + page3.get());
		logger.info("--> " + page4.get());

	}
}
