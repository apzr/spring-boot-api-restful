package com.uxlt.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Application implements AsyncConfigurer {

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new ExceptionHandler();
	}

	@Slf4j(topic="异步线程池运行时异常捕获器")
	static class ExceptionHandler implements AsyncUncaughtExceptionHandler {
		@Override
		public void handleUncaughtException(Throwable e, Method method, Object... params) {//全局捕获异步执行异常并处理
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
	        PrintStream printStream = new PrintStream(outputStream);
	        e.printStackTrace(printStream);
	        log.error("异步异常 {}",outputStream.toString());
		}
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(400);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();

        return executor;
	}
}

