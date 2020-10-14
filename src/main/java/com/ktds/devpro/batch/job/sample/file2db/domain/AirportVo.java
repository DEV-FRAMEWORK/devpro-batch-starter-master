package com.ktds.devpro.batch.job.sample.file2db.domain;

/**
*
* 공항 코드 원 데이터
* 영문명,한글명,IATA코드,ICAO코드
* - Incheon International Airport,인천 국제공항,ICN,RKSI
* <p>
*
* <pre>
* 개정이력(Modification Information)·
* 수정일   수정자    수정내용
* ------------------------------------
* 2017. 10. 12.   이종혁     최초작성
* </pre>
*
* @author 이종혁 (shineljh@kt.com)
* @since 2017. 10. 12.
* @version 1.0.0
* @see
*
*/

public class AirportVo {
	private String engFullName;	//영문명 
	private String korFullName;	//한글명 
	private String iataCode;		//IATA Code 
	private String icaoCode;		//ICAO Code
	
	public String getEngFullName() {
		return engFullName;
	}
	public void setEngFullName(String engFullName) {
		this.engFullName = engFullName;
	}
	public String getKorFullName() {
		return korFullName;
	}
	public void setKorFullName(String korFullName) {
		this.korFullName = korFullName;
	}
	public String getIataCode() {
		return iataCode;
	}
	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}
	public String getIcaoCode() {
		return icaoCode;
	}
	public void setIcaoCode(String icaoCode) {
		this.icaoCode = icaoCode;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AirportOriginDomain [engFullName=").append(engFullName).append(", korFullName=")
				.append(korFullName).append(", iataCode=").append(iataCode).append(", icaoCode=").append(icaoCode)
				.append("]");
		return builder.toString();
	}
}
