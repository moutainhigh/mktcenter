package com.bizvane.messageservice.service.impl.wxtemplatemessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bizvane.centercontrolservice.models.po.SysSmsConfigPo;
import com.bizvane.centercontrolservice.rpc.SysSmsConfigServiceRpc;
import com.bizvane.couponservice.common.system.DictHelper;
import com.bizvane.members.facade.models.MemberInfoModel;
import com.bizvane.members.facade.service.api.MemberInfoApiService;
import com.bizvane.members.facade.service.api.WxChannelInfoApiService;
import com.bizvane.members.facade.vo.MemberInfoApiModel;
import com.bizvane.members.facade.vo.WxChannelInfoVo;
import com.bizvane.messageservice.common.constants.SysRespConstants;
import com.bizvane.messageservice.common.constants.TemplateMessageTypeConstant;
import com.bizvane.messageservice.service.SendSmsTemplateMessageService;
import com.bizvane.messageservice.service.WechatMessageLogService;
import com.bizvane.messageservice.template.UseTemplate;
import com.bizvane.messagefacade.interfaces.SendCommonMessageFeign;
import com.bizvane.messagefacade.models.po.MsgWxLogPO;
import com.bizvane.messagefacade.models.vo.CouponMessageVO;
import com.bizvane.messagefacade.models.vo.Result;
import com.bizvane.messagefacade.models.vo.SysSmsConfigVO;
import com.bizvane.utils.enumutils.SysResponseEnum;
import com.github.pagehelper.PageInfo;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.wechatfacade.models.po.WxPublicPO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 张迪
 * 
 * @优惠券短信发送接口，消费rocketmq消息，进行短信的发送
 * 
 * @date  2018/08/13
 */
@Service("sendSmsTemplateMessageCouponOffLineServiceImpl")
public class SendSmsTemplateMessageCouponOffLineServiceImpl implements SendSmsTemplateMessageService {

    private static final Logger logger = LoggerFactory.getLogger(SendSmsTemplateMessageCouponOffLineServiceImpl.class);
    @Autowired
    private WechatMessageLogService wechatMessageLogService;
    @Autowired
    private WxChannelInfoApiService wxChannelInfoApiService;
    
    @Autowired
    private MemberInfoApiService memberInfoApiService;


    @Autowired
    private SysSmsConfigServiceRpc sysSmsConfigServiceRpc;

    @Autowired
    private SendCommonMessageFeign sendCommonMessage;

    @Autowired
    private com.bizvane.messageservice.service.SendSmsPublicMessageService SendSmsPublicMessageService;

    /**
     * @消费rocketmq短信发送
     *
     * @param rocketMsgId
     * @param messageBody
     * @return
     */
    @Override
    public Result<String> sendMessage(String rocketMsgId, String messageBody) {
    	Result<String> result = new Result<>();
    	
    	try {
    		logger.info(this.getClass().getName() + ".sendMessage入参：" + messageBody);

    		 if (StringUtils.isBlank(messageBody)) {
 	            logger.error(this.getClass().getName() + ".sendMessage入参为空");
 	            result.setStatus(SysRespConstants.SYSTEM_DATA_NOT_EMPTY.getStatus());
 		         result.setMsg(SysRespConstants.SYSTEM_DATA_NOT_EMPTY.getMsg());
 		         return result;
 	        }
 	        
 	        JSONObject jsonObject = JSON.parseObject(messageBody);
 	        
 	        //判断手机号
 	        if(StringUtils.isBlank(jsonObject.getString("phone"))){
 	        	 logger.error(jsonObject.getString("phone") + "手机号为空");
 	        	 result.setStatus(SysRespConstants.SENDSMS_PHONE_NOT_EMPTY.getStatus());
 		         result.setMsg(SysRespConstants.SENDSMS_PHONE_NOT_EMPTY.getMsg());
 		         return result;
 	        }
 	        
 	        if(StringUtils.isBlank(jsonObject.getLong("sysBrandId").toString())){
 	        	 logger.error("sysBrandId不能为空========================");
 	        	 result.setStatus(SysRespConstants.SYSTEM_DATA_NOT_EMPTY.getStatus());
 		         result.setMsg(SysRespConstants.SYSTEM_DATA_NOT_EMPTY.getMsg());
 		         return result;
 	        }
	        
	
	        String bussinessId = jsonObject.getString("rocketMQBussinessId");
	
	        // 记录发送消息日志
	        MsgWxLogPO insertPO = new MsgWxLogPO();
	        insertPO.setTemplateType(jsonObject.getString("bussinessModuleCode").replace("SMS_",""));
	        insertPO.setTemplateTypeName(DictHelper.getDict("template_type_sms", jsonObject.getString("bussinessModuleCode").replace("SMS_","")).getItemCodeName());
	        if(!(StringUtils.isBlank(jsonObject.getString("memberPhone")))){insertPO.setMemberPhone(jsonObject.getString("memberPhone"));}
	        insertPO.setRocketMsgId(rocketMsgId);insertPO.setResultInfo("发送中");if(!(StringUtils.isBlank(jsonObject.getString("sysBrandId")))){insertPO.setSysBrandId(jsonObject.getLong("sysBrandId"));}if(!(StringUtils.isBlank(jsonObject.getString("memberPhone")))){insertPO.setMemberPhone(jsonObject.getString("memberPhone"));}
	        insertPO.setBussinessId(bussinessId);
	        insertPO.setDataBody(messageBody);
	
	        Result<String> insertResult = this.wechatMessageLogService.insert(insertPO);
	//        Long wechatMessageLogId = insertResult.getData();
	
	        //获取短信通道 sysBrandId
	        Long sysBrandId = jsonObject.getLong("sysBrandId");
	        List<SysSmsConfigPo> listsyssmsconfiglist= sysSmsConfigServiceRpc.getByBrandIdSmsConfigList(sysBrandId);
	        //获取短信通道信息
	        ResponseData<Integer> results = new ResponseData<Integer>(SysResponseEnum.FAILED.getCode(),SysResponseEnum.FAILED.getMessage(),null);
	        if(StringUtils.isNotBlank(listsyssmsconfiglist.toString())){
	        	//SysSmsConfigVO syssmsconfigvo = mapper.convertValue(listsyssmsconfiglist.get(0), SysSmsConfigVO.class);
	        	SysSmsConfigVO syssmsconfigvo = new SysSmsConfigVO();
	        	//赋值短信通道的账号密码
	        	syssmsconfigvo.setChannel(listsyssmsconfiglist.get(0).getChannel());
	        	syssmsconfigvo.setChannelName(listsyssmsconfiglist.get(0).getChannelName());
	        	syssmsconfigvo.setChannelAccount(listsyssmsconfiglist.get(0).getChannelAccount());
	        	syssmsconfigvo.setChannelPassword(listsyssmsconfiglist.get(0).getChannelPassword());
	        	syssmsconfigvo.setChannelService(listsyssmsconfiglist.get(0).getChannelService());
	         	//替换数据
	        	String Content = SendSmsPublicMessageService.GetReplaceContent(jsonObject);
	        			
	        	syssmsconfigvo.setMsgContent(Content);
	        	//.replace("@appid@",app_id)
	        	//手机号码
	        	syssmsconfigvo.setPhone(jsonObject.getString("phone"));
	        //	syssmsconfigvo.setCountryCode(jsonObject.getString("countryCode")); //区号
	        	results =  sendCommonMessage.sendSmg(syssmsconfigvo);
	        	logger.info("results===================================="+results.toString());
	        	if(results.getCode()==0){
	        		logger.info("优惠券短信发送成功===================================="+results.toString());
	        		//表示发送成功
	        		 result.setStatus(SysRespConstants.SUCCESS.getStatus());
			         result.setMsg(SysRespConstants.SUCCESS.getMsg());
			         return result;
	        	}else{
	        		logger.info("优惠券短信发送成功===================================="+results.toString());
	        		 result.setStatus(SysRespConstants.FAILED.getStatus());
			         result.setMsg(SysRespConstants.FAILED.getMsg());
			         return result;
	        	}
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.info("SendSmsTemplateMessageCouponServiceImpl====="+e.getMessage().toString());
    	}
    	return result;
    }

}
