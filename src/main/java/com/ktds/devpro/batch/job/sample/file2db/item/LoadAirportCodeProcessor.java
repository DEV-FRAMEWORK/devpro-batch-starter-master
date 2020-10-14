package com.ktds.devpro.batch.job.sample.file2db.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.stereotype.Component;

import com.ktds.devpro.batch.job.sample.file2db.domain.AirportCodeBo;
import com.ktds.devpro.batch.job.sample.file2db.domain.AirportVo;

/**
*
* Processor Sample
* 
* 여기서는 AirportVo (원데이터) 를 가공 및 필터링함
* - 필터링 사항: 국제공항만 Write 함 
* - 가공 사항: 공항 및 Airport 는 빼고, 지역만 남김  
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
@Component
public class LoadAirportCodeProcessor implements ItemProcessor<AirportVo, AirportCodeBo> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public AirportCodeBo process(AirportVo item) throws Exception {

		logger.debug("- {}", item);
		
		// Filtering 
		if(item.getEngFullName().endsWith("International Airport")) {
			return null;
		}
		
		// [AA가이드] Skip 처리하는 샘플..
		if(item.getIataCode().isEmpty()) {
			throw new FlatFileFormatException("Airport Format invalid", item.toString());
		}
		
		AirportCodeBo airportCodeBo = new AirportCodeBo();
		
		// 가공
		airportCodeBo.setAirportCd(item.getIataCode());
		
		String shortKorName = item.getKorFullName().replaceAll("국제공항",  "");
		airportCodeBo.setAirportKorNm(shortKorName);
		
		String shortEngName = item.getEngFullName().replaceAll("International Airport", "");
		airportCodeBo.setAirportEngNm(shortEngName);
		
		return airportCodeBo;
	}

}
