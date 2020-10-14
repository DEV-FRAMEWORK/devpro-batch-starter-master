package com.ktds.devpro.batch.job.sample.file2db.item;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.ktds.devpro.batch.job.sample.file2db.domain.AirportVo;

/**
*
* Reader Sample
*  - [AA 가이드] Reader는 Delegation 패턴을 사용하여, 다른 Reader 하나 또는 여러개 를 사용합니다.
*  - [AA 가이드] Job Parameter 를 가져올 때의 샘플 포함되어 있음. 
*    * @StepScope , @BeforeStep
* 
* 여기서는 FlatFileFilterReader 를 이용함
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 10. 13.   이종혁     최초작성
* </pre>
*
* @author 이종혁 (shineljh@kt.com)
* @since 2017. 10. 13.
* @version 1.0.0
* @see
*
*/

@StepScope
@Component
public class LoadAirportCodeReader implements ItemReader<AirportVo>, ItemStream{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String inputFilePath;
	
	public static final String INPUT_FILE_PARAM = "input.file.path";
	public static final String DEFAULT_INPUT_FILE = "airport_list.csv";
	
	private FlatFileItemReader<AirportVo> flatFileReader;

	private LineMapper<AirportVo> createLineMapper() {
		DefaultLineMapper<AirportVo> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(createLineTokenizer());
		lineMapper.setFieldSetMapper(createFieldMapper());

		return lineMapper;
	}

	private LineTokenizer createLineTokenizer() {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");

		lineTokenizer.setNames(new String[]{"engFullName", "korFullName", "iataCode", "icaoCode"});
		return lineTokenizer;
	}

	private  FieldSetMapper<AirportVo> createFieldMapper() {
		BeanWrapperFieldSetMapper<AirportVo> fieldMapper = new BeanWrapperFieldSetMapper<>();
		fieldMapper.setTargetType(AirportVo.class);
		return fieldMapper;
	}

	@BeforeStep
	void beforeStep(StepExecution stepExecution) {
		JobParameters jobParams = stepExecution.getJobParameters();
		inputFilePath = jobParams.getString(INPUT_FILE_PARAM, DEFAULT_INPUT_FILE);

		flatFileReader = new FlatFileItemReader<>();
		logger.debug("BEFORE Read: Input file Path: {}", inputFilePath);

		flatFileReader.setResource(new ClassPathResource(inputFilePath));
		flatFileReader.setLinesToSkip(1);	// AA Guide: Airport CSV 는 헤더가 있기 때문에 한줄 Skip합니다.
		flatFileReader.setLineMapper(createLineMapper());
	}
	
	//////////////////////////
	// Item Reader 
	 
	@Override
	public AirportVo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return flatFileReader.read();
	}

	///////////////////////////
	// ItemStream
	
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		flatFileReader.open(executionContext);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		flatFileReader.update(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		flatFileReader.close();
	}
}
