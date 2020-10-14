package com.ktds.devpro.batch.job.sample.file2db.item;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktds.devpro.batch.job.sample.file2db.domain.AirportCodeBo;
import com.ktds.devpro.batch.job.sample.file2db.mapper.AirportCodeMapper;

/**
*
* Writer Sample
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
public class LoadAirportCodeWriter implements ItemWriter<AirportCodeBo> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AirportCodeMapper airportCodeMapper;

	@Override
	public void write(List<? extends AirportCodeBo> items) throws Exception {
		logger.info("Write Airport Code - chunck {}", items.size());
		for(AirportCodeBo airportCodeBo : items) {
			logger.debug("- insert {}", airportCodeBo);
			airportCodeMapper.insertAirportCode(airportCodeBo);
		}
	}

}
