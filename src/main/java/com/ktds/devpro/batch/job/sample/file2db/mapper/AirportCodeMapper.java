package com.ktds.devpro.batch.job.sample.file2db.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ktds.devpro.batch.job.sample.file2db.domain.AirportCodeBo;


@Mapper
public interface AirportCodeMapper {
	/**
     * 공항코드 입력
     * @param airportCodeBo
     * @return insert row 카운트
     */
    int insertAirportCode(AirportCodeBo airportCodeBo);
}
