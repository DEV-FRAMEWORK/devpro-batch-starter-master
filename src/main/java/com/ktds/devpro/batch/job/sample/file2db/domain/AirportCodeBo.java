package com.ktds.devpro.batch.job.sample.file2db.domain;
/**
*
* 공항 코드 도메인 코드
* 영문명,한글명,IATA코드
* 코드, 영문지명, 한글지명 
* Org: Incheon International Airport,인천 국제공항,ICN,RKSI
* Dest: ICN, Incheon, 인천 
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

public class AirportCodeBo {
	private String airportCd;		//공항 코드 IATA 코드
	private String airportEngNm;		//영문 지명 
	private String airportKorNm;		//한글 지명 
	
	public String getAirportCd() {
		return airportCd;
	}
	public void setAirportCd(String airportCd) {
		this.airportCd = airportCd;
	}
	public String getAirportEngNm() {
		return airportEngNm;
	}
	public void setAirportEngNm(String airportEngNm) {
		this.airportEngNm = airportEngNm;
	}
	public String getAirportKorNm() {
		return airportKorNm;
	}
	public void setAirportKorNm(String airportKorNm) {
		this.airportKorNm = airportKorNm;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AirportDestDomain [airportCd=").append(airportCd).append(", airportEngNm=").append(airportEngNm)
				.append(", airportKorNm=").append(airportKorNm).append("]");
		return builder.toString();
	}
	
	
}
