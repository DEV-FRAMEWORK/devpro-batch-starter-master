package com.ktds.devpro.batch.job.sample.file2db.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
*
* Tasklet 샘플
* [AA가이드] 한가지 일을 하는 Step 입니다. (Step 은 Tasklet와 Chunk로 나뉨) 
*  - Cleanup 을 하거나, 연결해서 받아오기, 전처리 등을 할 수 있습니다. 
*  - 샘플에서는 단순히 로그만 출력
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 10. 12.   이종혁     최초작성
* </pre>
*
* @author 이종혁 (shineljh@kt.com)
* @since 2017. 10. 12.
* @version 1.0.0
* @see
*
*/
@Component
public class AirportDbCleanupTasklet implements Tasklet{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		logger.info("Tasklet");

		return RepeatStatus.FINISHED;
	}
	
}
