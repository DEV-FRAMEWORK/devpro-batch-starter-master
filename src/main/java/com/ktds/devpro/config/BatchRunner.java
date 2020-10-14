package com.ktds.devpro.config;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
*
* DataSource Config 설정
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 9. 20.   kt ds     최초작성
* </pre>
*
* @author 이종혁 (shineljh@kt.com)
* @since 2017. 10. 11.
* @version 1.0.0
* @see
*
*/
@Component
public class BatchRunner implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	private ApplicationContext ctx;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	private static final String JOB_SUFFIX = "Job";				// Job은 Job 으로 끝내야 함
	private static final String KEY_JOB_NAME = "KEY_JOB_NAME";	// Job이름을 Map에 담을 키
	private static final String KEY_VALUE_DELIMETER = ":";		// Job Parameter 의 Key 와 Value 의 구분자 
	private static final String PARAM_EXEC_TIME = "time";			// 현재 시간을 파라미터로 전달 

	/**
	 * 커맨드 파라미터 표준: Argument 로 Job 이름, Job Parameter 이름, Job Parameter Value 을 전달함 
	 * 예) Job이름: SampleJob, 파라미터: fileName: sample.csv  dbTable: sample_table  
	 *   [Command]  sampleJob fileName:sample.csv dbTable:sample_table
	 */
	@Override
	public void run(String... args) throws Exception {
		logger.debug("Batch runner!!! Args{}", Arrays.asList(args));
		
		Map<String, String> jobParamMap = parseArguments(args);
		logger.debug("Param: {}", jobParamMap);
		
		String jobName = jobParamMap.get(KEY_JOB_NAME);
		jobParamMap.remove(KEY_JOB_NAME);
		
		Job execJob;
		
		try { 
			execJob = ctx.getBean(jobName, Job.class);
		} catch (BeansException e) {
			logger.error("Fail to Search Job: ({})", jobName);
			throw e;
		}
					
		JobParametersBuilder jobParamBuilder = new JobParametersBuilder();
		
		for(Map.Entry<String, String> aParamPair : jobParamMap.entrySet()) {
			jobParamBuilder.addString(aParamPair.getKey(), aParamPair.getValue());
		}
		jobParamBuilder.addLong(PARAM_EXEC_TIME, System.currentTimeMillis());
		
		JobParameters jobParams = jobParamBuilder.toJobParameters();
		
		logger.info("Job's name: {} {}", execJob.getName(), jobParams);		
		
		JobExecution jobExecution;
		
		try { 
			jobExecution = jobLauncher.run(execJob, jobParams);
		} catch (JobExecutionException | NullPointerException e) {
			logger.error("Fail to Job Launch. {}", jobLauncher);
			logger.error("{}", e);
			
			throw e;
		}
		
		while(jobExecution.isRunning()) {
			logger.debug("Wait for Job: {} ...", jobExecution.getStatus());
			Thread.sleep(1000);	
		}
		
		SpringApplication.exit(ctx, () -> { 
			if(jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
				logger.info("Job ({}) Success.", jobExecution.getJobInstance().getJobName());
				return 0;	//Success
				
			} else {
				logger.error("Job ({}) Failed. exitCode: ({})", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus());
				return 1;	//ERROR
			}
		});
	}
	
	private Map<String, String> parseArguments(String... args) {
		
		Map<String, String> jobParamMap = new HashMap<String, String>();

		
		for(String anArgs: args) {
			if(anArgs.startsWith("-")) {
				continue;
				
			} else if(anArgs.endsWith(JOB_SUFFIX)) {
				jobParamMap.put(KEY_JOB_NAME, anArgs);
				
			} else if(anArgs.contains(KEY_VALUE_DELIMETER)) {
				String[] paramPair = anArgs.split(KEY_VALUE_DELIMETER);
				jobParamMap.put(paramPair[0], paramPair[1]);
			}
		}
		
		return jobParamMap;
	}

}
