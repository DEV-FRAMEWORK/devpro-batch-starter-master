package com.ktds.devpro.batch.job.sample.db2db.item;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ktds.devpro.batch.job.sample.db2db.domain.SampleUserBo;
import com.ktds.devpro.batch.job.sample.db2db.mapper.SampleUserMapper;



/**
*
* 데이터 저장(out) 처리
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
 * @param <T>
* @see
*
*/
public class SampleUserItemWriter implements ItemWriter<Object>{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SampleUserMapper SampleUserMapper;

	@Override
	public void write(List<? extends Object> items) throws Exception {
		
		for (Object SampleUserBo : items) {

			if (SampleUserBo instanceof SampleUserBo) {
				SampleUserMapper.insertSampleUser((SampleUserBo) SampleUserBo);
			}
		}
	}
	


}
	