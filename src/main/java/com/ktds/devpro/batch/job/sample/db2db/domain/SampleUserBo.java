package com.ktds.devpro.batch.job.sample.db2db.domain;

/**
 *
 * 샘플 사용자 도메인 클래스
 * <p>
 *
 * <pre>
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2017. 8. 20.   kt ds     최초작성
 * </pre>
 *
 * @author kt ds A.A(yu.chae@kt.com)
 * @since 2017. 8. 20.
 * @version 1.0.0
 * @see
 *
 */
public class SampleUserBo {

    /**
     * 사용자 아이디
     */
    private String userId;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 사용자 이름
     */
    private String userName;

    /**
     * 등록일
     */
    private String createDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SampleUserBo [userId=").append(userId).append(", password=").append(password)
				.append(", userName=").append(userName).append(", createDate=").append(createDate).append("]");
		return builder.toString();
	} 
}
