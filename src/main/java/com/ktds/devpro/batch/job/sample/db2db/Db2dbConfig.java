package com.ktds.devpro.batch.job.sample.db2db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktds.devpro.batch.job.sample.db2db.domain.SampleUserBo;
import com.ktds.devpro.batch.job.sample.db2db.domain.TmpUserVo;
import com.ktds.devpro.batch.job.sample.db2db.item.SampleUserItemProcessor;



/**
*
* 배치 업무 설정
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 9. 20.   kt ds     최초작성
* </pre>
*
* @author kt ds A.A(yu.chae@kt.com)
* @since 2017. 9. 20.
* @version 1.0.0
* @see
*
*/
@Configuration
@EnableBatchProcessing
public class Db2dbConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
	private SqlSessionFactory sqlSessionFactory;
       
    Logger logger = LoggerFactory.getLogger(this.getClass());
     
    /**
     * Job flow 설정
     * @return
     */
    @Bean(name="db2dbJob")
    public Job job() {
    	return jobBuilderFactory.get("job")
    			.start(step1())
    			.incrementer(new RunIdIncrementer())
    			.build();
  
    }
       
    /**
     * Task 처리 스텝 정의
     * @return
     */
    @Bean(name="db2dbStep1")
    public Step step1() {
        return stepBuilderFactory.get("db2DbStep1")
                .<TmpUserVo, SampleUserBo> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
	
    /**
     *  입력 데이터 처리<br>
     *  MyBatisPagingItemReader를 이용하여 입력 데이터를 DB로부터 읽어 들인다.
     * @return
     */
	@Bean(name="db2dbReader")
    public ItemReader<TmpUserVo> reader() {
		
		MyBatisPagingItemReader<TmpUserVo> itemReader = new MyBatisPagingItemReader<TmpUserVo>();
		itemReader.setQueryId("com.ktds.devpro.batch.job.sample.db2db.mapper.TmpUserMapper.selectTmpUserList");
		itemReader.setPageSize(10);
		itemReader.setSqlSessionFactory(sqlSessionFactory);
        
        return itemReader;
    }
    
    /**
     * 데이터 Transformation<br>
     * 읽은 데이터를 가공하거나 변경을 위해 Processor를 생성하고 비즈니스 로직을 구현한다.
     * @return
     */
	@Bean(name="db2dbProcessor")
    public ItemProcessor<TmpUserVo, SampleUserBo> processor() {
    	return new SampleUserItemProcessor();
	}
	
    /**
     * 출력 데이터 처리<br>
     * 간단히 DB에 데이터를 저장하는 경우는 writer() 메소드내에서 MyBatisBatchItemWriter를 이용하여 처리하고, 복잡도가 높은 경우 Custom Writer 클래스를 구현한다.
     * @return
     */
    @Bean(name="db2dbWriter")
    public ItemWriter<SampleUserBo> writer() {
    	
    	// 1. writer()에서 출력 데이터 처리
    	MyBatisBatchItemWriter<SampleUserBo> itemWriter = new MyBatisBatchItemWriter<SampleUserBo>();
		itemWriter.setSqlSessionFactory(sqlSessionFactory);
		itemWriter.setStatementId("com.ktds.devpro.batch.job.sample.db2db.mapper.SampleUserMapper.insertSampleUser");
		return itemWriter;

		// 2. Custom Writer에서 출력 데이터 처리
		// 1) Custom 클래스 작성
		// 2) mapper경로에 Mapper 클래스 작성
		// 3) 아래 return 주석 해제 및 위 1) 코드 삭제
    	//return new SampleUserMapper();
    }

}
