package com.bizvane.mktcenterservice.models.po;

import java.io.Serializable;
import java.util.Date;

public class MktTaskMsgPO implements Serializable {

	/**
	 * 只读. pkid. mkt_task_msg_id
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "pkid", name = "mktTaskMsgId", required = false, example = "")
	private Long mktTaskMsgId;
	/**
	 * 只读. 关联活动id. mkt_task_id
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "关联活动id", name = "mktTaskId", required = false, example = "")
	private Long mktTaskId;
	/**
	 * 只读. 关联的消息id. wx_msg_id
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "关联的消息id", name = "wxMsgId", required = false, example = "")
	private String wxMsgId;
	/**
	 * 只读. 消息类型. msg_type
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "消息类型", name = "msgType", required = false, example = "")
	private String msgType;
	/**
	 * 只读. 模板消息id. template_msg_id
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "模板消息id", name = "templateMsgId", required = false, example = "")
	private String templateMsgId;
	/**
	 * 只读. 开始发送时间. send_time
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "开始发送时间", name = "sendTime", required = false, example = "")
	private Date sendTime;
	/**
	 * 只读. 备注. remark
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "备注", name = "remark", required = false, example = "")
	private String remark;
	/**
	 * 只读. 创建人id. create_user_id
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "创建人id", name = "createUserId", required = false, example = "")
	private Long createUserId;
	/**
	 * 只读. 创建人. create_user_name
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "创建人", name = "createUserName", required = false, example = "")
	private String createUserName;
	/**
	 * 只读. 创建日期. create_date
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "创建日期", name = "createDate", required = false, example = "")
	private Date createDate;
	/**
	 * 只读. 修改人id. modified_user_id
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "修改人id", name = "modifiedUserId", required = false, example = "")
	private Long modifiedUserId;
	/**
	 * 只读. 修改人. modified_user_name
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "修改人", name = "modifiedUserName", required = false, example = "")
	private String modifiedUserName;
	/**
	 * 只读. 修改时间. modified_date
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "修改时间", name = "modifiedDate", required = false, example = "")
	private Date modifiedDate;
	/**
	 * 只读. 数据有效性：1=有效；0=无效. valid
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "数据有效性：1=有效；0=无效", name = "valid", required = false, example = "")
	private Boolean valid;
	/**
	 * 只读. 消息内容. msg_content
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	@io.swagger.annotations.ApiModelProperty(value = "消息内容", name = "msgContent", required = false, example = "")
	private String msgContent;
	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Long getMktTaskMsgId() {
		return mktTaskMsgId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setMktTaskMsgId(Long mktTaskMsgId) {
		this.mktTaskMsgId = mktTaskMsgId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Long getMktTaskId() {
		return mktTaskId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setMktTaskId(Long mktTaskId) {
		this.mktTaskId = mktTaskId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public String getWxMsgId() {
		return wxMsgId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setWxMsgId(String wxMsgId) {
		this.wxMsgId = wxMsgId == null ? null : wxMsgId.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public String getMsgType() {
		return msgType;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType == null ? null : msgType.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public String getTemplateMsgId() {
		return templateMsgId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setTemplateMsgId(String templateMsgId) {
		this.templateMsgId = templateMsgId == null ? null : templateMsgId.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Long getCreateUserId() {
		return createUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName == null ? null : createUserName.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Long getModifiedUserId() {
		return modifiedUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setModifiedUserId(Long modifiedUserId) {
		this.modifiedUserId = modifiedUserId;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public String getModifiedUserName() {
		return modifiedUserName;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setModifiedUserName(String modifiedUserName) {
		this.modifiedUserName = modifiedUserName == null ? null : modifiedUserName.trim();
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public Boolean getValid() {
		return valid;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public String getMsgContent() {
		return msgContent;
	}

	/**
	 * 只读. 
	 * @mbg.generated  2018-07-13 10:42:22
	 */
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent == null ? null : msgContent.trim();
	}
}