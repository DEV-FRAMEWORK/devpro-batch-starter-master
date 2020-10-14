package com.ktds.devpro.batch.job.sample.file2db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.ktds.devpro.batch.job.sample.file2db.domain.AirportCodeBo;
import com.ktds.devpro.batch.job.sample.file2db.domain.AirportVo;
import com.ktds.devpro.batch.job.sample.file2db.item.AirportDbCleanupTasklet;
import com.ktds.devpro.batch.job.sample.file2db.item.LoadAirportCodeProcessor;
import com.ktds.devpro.batch.job.sample.file2db.item.LoadAirportCodeReader;
import com.ktds.devpro.batch.job.sample.file2db.item.LoadAirportCodeWriter;
import com.ktds.devpro.batch.job.sample.file2db.listener.LoadAirportCodeSkipListener;

/**
*
* 배치 샘플
* - Job 이름: file2dbJob
* - 사용 파라미터: 
*  1) input.file.path: cvs 파일 명
* - 샘플 파라미터: file2dbJob input.file.path:airport_list.csv
* 
*  샘플 내용: 세계 공항 코드 (resources/sample/airport_org_code.csv) 에서 국제 공항만 추출하여 DB에 저장
*   1) 공항은 지역만 넣도록 변경: 인천 국제공항 -> 인천 
*   2) Code 는 IATA 코드만 사용할 것
형식:    영문명,한글명,IATA코드,ICAO코드
Incheon International Airport,인천 국제공항,ICN,RKSI ==>Incheon, 인천, ICN 
Gimpo International Airport,김포 국제공항,GMP,RKSS  ==> Gimpo, 김포, GMP
Gwangju Airport,광주 공항,KWJ,RKJJ ==> 필터링
*   
* 1. 두 단계로 진행하는 샘플 
*  - 첫번째는 cleanup 을 위한 tasklet (DB 삭제) 
*  - 두번째는 File에서 읽어, 필터링을 하고, 변형(translation) 을 하는 Chunk Step
* 2. Job Parameter 전달 가이드
* 3. Listner 추가 
*  - Job이 시작할 때, 끝날 때, 예외 발생할 때 처리할 수 있는 간단한 Listener 
*  - Step 이 시작할 때, 끝날 때, 예외 발생할 때 처리할 수 있는 간단한 Listener
*  
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 10. 11.   이종혁     최초작성
* </pre>
*
* @author 이종혁 (shineljh@kt.com)
* @since 2017. 10. 11.
* @version 1.0.0
* @see
*
*/
@Configuration
@EnableBatchProcessing
public class File2dbJobConfg {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
//    @Autowired
//	private SqlSessionFactory sqlSessionFactory;
    
    @Autowired
    private AirportDbCleanupTasklet airportDbCleanupTasklet;
    
    @Autowired
    private LoadAirportCodeReader loadAirportCodeReader;
    
    @Autowired 
    private LoadAirportCodeProcessor loadAirportCodeProcessor;
    
    @Autowired 
    private LoadAirportCodeWriter loadAirportCodeWriter;
    
    @Autowired
    private LoadAirportCodeSkipListener loadAirportCodeSkipListener;

    
    /////////////////////////////////////////////////////
    // Job DSL 
    //  - Step 의 Flow 를 기술함
    @Bean
    public Job file2dbJob() {
    		return jobBuilderFactory.get("file2dbJob")
    				.incrementer(new RunIdIncrementer())
    				.start(cleanupStep())
    				.next(loadAirportCodeStep())
    				.build();
    }
    
    /////////////////////////////////////////////////////
    // Step 
    
    @Bean
    public Step cleanupStep() {
    		return stepBuilderFactory.get("cleanupStep")
    				.tasklet(airportDbCleanupTasklet)
    				.build();
    }
    
    /*
     * 공항코드 적재 Step
     * [AA가이드] Chunk Size 는 업무마다 달라, 해당 비즈니스에서 튜닝해 나가야 합니다.
     * - 반드시 한번에 하나씩 처리가 되어야 문제가 없는 비즈니스는 1로 설정하면 됩니다. 
     * - 모든 것을 하나의 트랜잭션으로 처리해야 할 때는 Integer.MAX 로 설정하면 됩니다.
     * - 100~500 건 정도는 일반적으로 무난합니다. (절대적이지는 않음)
     * 
     * [AA가이드] Reader와 Writer 는 별도 컴포넌트로 분리할 수도 있고, Config에 선언할 수도 있습니다.
     * - 비즈니스 로직이 들어가면 반드시 별도 컴포넌트로 분리하는 것은 필수이고, 
     *   로직 없이 설정만 한다고 하면 Project 팀에서 분리할지, Config에 직접 선언할지 결정하면 됩니다.
     * - 샘플에는 별도 컨포넌트로 분리해 두었으며, 
     *   직접 Config 선언 방식을 사용하고자 하면 아래 Reader, Writer 설정을 풀고 사용하면 됩니다.
     */
    @Bean
    public Step loadAirportCodeStep() {
    		return stepBuilderFactory.get("loadAirportCodeStep")
    				.<AirportVo, AirportCodeBo>chunk(20)			
    	//			.reader(reader(null))			
    				.reader(loadAirportCodeReader)
    				.processor(loadAirportCodeProcessor)
    				.writer(loadAirportCodeWriter)
    //			.writer(writer())
    				.faultTolerant()
    					.skip(DuplicateKeyException.class)			// 키 중복 나면 Skip 
    					.skip(FlatFileFormatException.class)			// @see Processor - 키가 없을 때 예외를 발생시키는 예제임. (코드 참조) 
    					.skip(DataIntegrityViolationException.class)	// 저장시 무결성 발생시 Skip  
    					.skipLimit(Integer.MAX_VALUE)			//Skip 제한 두지 않는 설정 
    					.listener(loadAirportCodeSkipListener)
    				.build();	
    }
    
    //////////////////////////////////////////////////////
    // Reader
    
    /*
     AA Guide:
     - Job Parameter 를 가져오는 샘플코드입니다.
     - Job Parameter 사용시 @StepScope 를 사용해야 합니다.
    */ 
//    @StepScope
//    @Bean(name="loadAirportCodeReader")
//    public FlatFileItemReader<AirportVo> reader(@Value("#{jobParameters['input.file.path']}") String filePath) {
//    		FlatFileItemReader<AirportVo> flatFileReader = new FlatFileItemReader<>();
//    		logger.debug("Input file Path: {}", filePath);
//    		
//    		
//    		flatFileReader.setResource(new ClassPathResource(filePath));
//    		flatFileReader.setLinesToSkip(1);	// AA Guide: Airport CSV 는 헤더가 있기 때문에 한줄 Skip합니다.
//    		flatFileReader.setLineMapper(createLineMapper());
//    		
//    		return flatFileReader;
//    }
//    
//    private LineMapper<AirportVo> createLineMapper() {
//    		DefaultLineMapper<AirportVo> lineMapper = new DefaultLineMapper<>();
//    		lineMapper.setLineTokenizer(createLineTokenizer());
//    		lineMapper.setFieldSetMapper(createFieldMapper());
// 
//        return lineMapper;
//    }
//    
//    private LineTokenizer createLineTokenizer() {
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(",");
//        
//        lineTokenizer.setNames(new String[]{"engFullName", "korFullName", "iataCode", "icaoCode"});
//        return lineTokenizer;
//    }
// 
//    private FieldSetMapper<AirportVo> createFieldMapper() {
//        BeanWrapperFieldSetMapper<AirportVo> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(AirportVo.class);
//        return fieldSetMapper;
//    }  
    
    //////////////////////////////////////////////////////
    // Writer
    // [AA 가이드] 간단한 Writer 는 아래와 같이 바로 Bean 으로 사용 가능합니다.
    // - 여러 DataSource 에 넣어야 한다거나 하면, 별도 Writer 로 구현해야 합니다. 
    // - 이번 샘플에서는 별도 Writer 로 구현해 보았습니다. 
    // @see Db2dbJob - Db Writer 에 대한 가이드는 Db2db 를 참고하세요. 
    
    /**
     * 출력 데이터 처리
     * @return
     */
//    @Bean(name="loadAirportCodeWriter")
//    public ItemWriter<AirportCodeBo> writer() {
//    		MyBatisBatchItemWriter<AirportCodeBo> itemWriter = new MyBatisBatchItemWriter<>();
//		itemWriter.setSqlSessionFactory(sqlSessionFactory);
//		itemWriter.setStatementId("com.ktds.devpro.batch.job.sample.file2db.mapper.AirportCodeMapper.insertAirportCode");
//		return itemWriter;
//		
//    }
}
