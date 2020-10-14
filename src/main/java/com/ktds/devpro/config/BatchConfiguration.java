package com.ktds.devpro.config;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
*
* 글로벌 배치 설정 
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 10. 11.   이종     최초작성
* </pre>
*
* @author 이종혁 (shineljh@kt.com)
* @since 2017. 10. 11.
* @version 1.0.0
* @see
*
*/
@Configuration
public class BatchConfiguration {
	
	@Value("${devpro.batch.pool.maxSize:8}")
	private int batchMaxPoolSize;
	
	@Autowired 
	JobRepository jobRepository;
	
	/**
	 * Task Executor 설정 (Job Launcher 에서 사용)
	 * @return Thread Pool 기반의 Task Executor
	 */
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(batchMaxPoolSize);
		taskExecutor.afterPropertiesSet();
		
		return taskExecutor;
	}
	
	@Bean
	public JobLauncher jobLauncher() {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(taskExecutor());
		
		return jobLauncher;
	}
}
