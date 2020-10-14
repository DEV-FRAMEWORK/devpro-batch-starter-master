package com.ktds.devpro.batch.job.sample.db2db.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ktds.devpro.batch.job.sample.db2db.domain.SampleUserBo;


/**
 *
 * 샘플 맵퍼 클래스
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
@Mapper()
public interface SampleUserMapper {

    /**
     *  사용자 정보 등록
     * @param sampleUserBo
     * @return insert row 카운트
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    int insertSampleUser(SampleUserBo sampleUserBo);
    
    
}
