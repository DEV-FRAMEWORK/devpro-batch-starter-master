package com.ktds.devpro.batch.job.sample.db2db.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ktds.devpro.batch.job.sample.db2db.domain.SampleUserBo;
import com.ktds.devpro.batch.job.sample.db2db.domain.TmpUserVo;


/**
*
* 데이터 가공 처리
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
public class SampleUserItemProcessor implements ItemProcessor<TmpUserVo, SampleUserBo>{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public SampleUserBo process(final TmpUserVo tmpUserVo) throws Exception {
		
		SampleUserBo SampleUserBo = new SampleUserBo();
		SampleUserBo.setUserId(tmpUserVo.getUserId());
		SampleUserBo.setPassword(tmpUserVo.getPassword());
		SampleUserBo.setUserName(tmpUserVo.getUserName());
		
		return SampleUserBo;
	}

}
