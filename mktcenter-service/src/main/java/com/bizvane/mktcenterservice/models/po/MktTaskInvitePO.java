package com.bizvane.mktcenterservice.models.po;

import java.io.Serializable;
import java.util.Date;

public class MktTaskInvitePO implements Serializable {

	/**
	 * 只读. pkid. mkt_task_invite_id
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "pkid", name = "mktTaskInviteId", required = false, example = "")
	private Long mktTaskInviteId;
	/**
	 * 只读. 关联活动id. mkt_task_id
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "关联活动id", name = "mktTaskId", required = false, example = "")
	private Long mktTaskId;
	/**
	 * 只读. 邀请开卡人数. invite_num
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "邀请开卡人数", name = "inviteNum", required = false, example = "")
	private Integer inviteNum;
	/**
	 * 只读. 备注. remark
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "备注", name = "remark", required = false, example = "")
	private String remark;
	/**
	 * 只读. 创建人id. create_user_id
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "创建人id", name = "createUserId", required = false, example = "")
	private Long createUserId;
	/**
	 * 只读. 创建人. create_user_name
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "创建人", name = "createUserName", required = false, example = "")
	private String createUserName;
	/**
	 * 只读. 创建日期. create_date
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "创建日期", name = "createDate", required = false, example = "")
	private Date createDate;
	/**
	 * 只读. 修改人id. modified_user_id
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "修改人id", name = "modifiedUserId", required = false, example = "")
	private Long modifiedUserId;
	/**
	 * 只读. 修改人. modified_user_name
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "修改人", name = "modifiedUserName", required = false, example = "")
	private String modifiedUserName;
	/**
	 * 只读. 修改时间. modified_date
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "修改时间", name = "modifiedDate", required = false, example = "")
	private Date modifiedDate;
	/**
	 * 只读. 数据有效性：1=有效；0=无效. valid
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	@io.swagger.annotations.ApiModelProperty(value = "数据有效性：1=有效；0=无效", name = "valid", required = false, example = "")
	private Boolean valid;
	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Long getMktTaskInviteId() {
		return mktTaskInviteId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setMktTaskInviteId(Long mktTaskInviteId) {
		this.mktTaskInviteId = mktTaskInviteId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Long getMktTaskId() {
		return mktTaskId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setMktTaskId(Long mktTaskId) {
		this.mktTaskId = mktTaskId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Integer getInviteNum() {
		return inviteNum;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setInviteNum(Integer inviteNum) {
		this.inviteNum = inviteNum;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Long getCreateUserId() {
		return createUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName == null ? null : createUserName.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Long getModifiedUserId() {
		return modifiedUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setModifiedUserId(Long modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public String getModifiedUserName() {
		return modifiedUserName;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setModifiedUserName(String modifiedUserName) {
		this.modifiedUserName = modifiedUserName == null ? null : modifiedUserName.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public Boolean getValid() {
		return valid;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-25 16:02:16
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
}