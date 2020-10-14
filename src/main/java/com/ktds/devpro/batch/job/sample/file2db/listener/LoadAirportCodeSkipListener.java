package com.ktds.devpro.batch.job.sample.file2db.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import com.ktds.devpro.batch.job.sample.file2db.domain.AirportCodeBo;
import com.ktds.devpro.batch.job.sample.file2db.domain.AirportVo;

/**
*
* Listner Sample
*  - [AA 가이드] 필요한 곳에 Listener 를 구현해서 배치 이벤트에 대한 처리를 할 수 있음 
*  - 도메인 별로 요구사항을 잘 따져서 처리해야 함. 샘플에서는 로깅만 하도록 하였음
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 10. 14.   이종혁     최초작성
* </pre>
*
* @author 이종혁 (shineljh@kt.com)
* @since 2017. 10. 14.
* @version 1.0.0
* @see
*
*/
@Component
public class LoadAirportCodeSkipListener implements SkipListener<AirportVo, AirportCodeBo> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static int counter = 0;
	
	@Override
	public void onSkipInRead(Throwable t) {
		logger.warn("{}[READER] Skip. {}", ++counter, t);
		
	}

	@Override
	public void onSkipInWrite(AirportCodeBo item, Throwable t) {
		logger.warn("{}[Writer] Skip item({}). {}", ++counter, item, t);
		
	}

	@Override
	public void onSkipInProcess(AirportVo item, Throwable t) {
		logger.warn("{}[Processor] Skip item({}). {}", ++counter, item, t);
		
	}

}
