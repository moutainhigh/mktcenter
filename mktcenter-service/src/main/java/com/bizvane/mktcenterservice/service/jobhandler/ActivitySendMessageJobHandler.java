package com.bizvane.mktcenterservice.service.jobhandler;

import com.bizvane.members.facade.es.vo.MembersInfoSearchVo;
import com.bizvane.members.facade.es.vo.WxChannelInfoSearchVo;
import com.bizvane.members.facade.models.MbrLevelModel;
import com.bizvane.members.facade.service.api.MemberInfoApiService;
import com.bizvane.members.facade.service.api.MemberLevelApiService;
import com.bizvane.mktcenterfacade.models.po.MktActivityPO;
import com.bizvane.mktcenterfacade.models.po.MktActivityPOExample;
import com.bizvane.mktcenterfacade.models.po.MktMessagePO;
import com.bizvane.mktcenterfacade.models.po.MktMessagePOExample;
import com.bizvane.mktcenterfacade.models.vo.ActivityVO;
import com.bizvane.mktcenterservice.common.award.Award;
import com.bizvane.mktcenterservice.common.award.MemberMessageSend;
import com.bizvane.mktcenterservice.common.enums.ActivityTypeEnum;
import com.bizvane.mktcenterservice.mappers.MktActivityOrderPOMapper;
import com.bizvane.mktcenterservice.mappers.MktActivityPOMapper;
import com.bizvane.mktcenterservice.mappers.MktActivityUpgradePOMapper;
import com.bizvane.mktcenterservice.mappers.MktMessagePOMapper;
import com.bizvane.utils.responseinfo.ResponseData;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agan on 2018/9/6.
 */
@JobHandler(value="sendMessageActivity")
@Component
public class ActivitySendMessageJobHandler extends IJobHandler {
    @Autowired
    private MktActivityPOMapper mktActivityPOMapper;
    @Autowired
    private MktMessagePOMapper mktMessagePOMapper;
    @Autowired
    private MemberInfoApiService memberInfoApiService;
    @Autowired
    private MemberLevelApiService memberLevelApiService;
    @Autowired
    private MktActivityUpgradePOMapper mktActivityUpgradePOMapper;
    @Autowired
    private MktActivityOrderPOMapper mktActivityOrderPOMapper;
    @Autowired
    private Award award;
    @Autowired
    private MemberMessageSend memberMessage;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        System.out.println("job执行参数 "+param);
        ReturnT returnT = new ReturnT();
        System.out.println("开始执行活动");
        //查询该活动是否已经审核
        MktActivityPOExample examplem = new MktActivityPOExample();
        examplem.createCriteria().andActivityCodeEqualTo(param).andValidEqualTo(true);
        List<MktActivityPO> mktActivityPOs = mktActivityPOMapper.selectByExample(examplem);
        MktActivityPO mktActivityPO = mktActivityPOs.get(0);
        ActivityVO activityVO = new ActivityVO();
        BeanUtils.copyProperties(mktActivityPO,activityVO);
            //查询消息集合
            MktMessagePOExample example = new MktMessagePOExample();
            example.createCriteria().andBizIdEqualTo(mktActivityPO.getMktActivityId());
            List<MktMessagePO> ListMktMessage = mktMessagePOMapper.selectByExampleWithBLOBs(example);
            if (!CollectionUtils.isEmpty(ListMktMessage)){
                //判断是什么类型的活动 然后给不同的会员发送消息
                //分页查询会员信息发送短信
                MembersInfoSearchVo membersInfoSearchVo = new MembersInfoSearchVo();
                membersInfoSearchVo.setPageNumber(1);
                membersInfoSearchVo.setPageSize(10000);
                membersInfoSearchVo.setBrandId(mktActivityPO.getSysBrandId());
                membersInfoSearchVo.setSysCompanyId(mktActivityPO.getSysCompanyId());
                //开卡活动的
                if (mktActivityPO.getActivityType()== ActivityTypeEnum.ACTIVITY_TYPE_REGISGER.getCode()){
                    membersInfoSearchVo.setCardStatus(2);
                }
                //升级活动的
                if (mktActivityPO.getActivityType()== ActivityTypeEnum.ACTIVITY_TYPE_UPGRADE.getCode()){
                    ActivityVO vo = new ActivityVO();
                    vo.setActivityCode(param);
                    List<ActivityVO> activityUpgradeList = mktActivityUpgradePOMapper.getActivityUpgradeList(vo);
                    //判断是否是全部会员
                    if(!activityUpgradeList.get(0).getMbrLevelCode().equals("0")){
                        //查询该会员下一个等级
                        ResponseData<MbrLevelModel> mbrLevelModels = memberLevelApiService.queryOnLevel(Long.parseLong(activityUpgradeList.get(0).getMbrLevelCode()));
                        MbrLevelModel  mbrLevel = mbrLevelModels.getData();
                        List<Long> level = new ArrayList<>();
                        level.add(mbrLevel.getMbrLevelId());
                        membersInfoSearchVo.setLevelID(level);
                    }

                }
                //消费活动的
                if (mktActivityPO.getActivityType()== ActivityTypeEnum.ACTIVITY_TYPE_ORDER.getCode()){
                    ActivityVO vo = new ActivityVO();
                    vo.setActivityCode(param);
                    List<ActivityVO> activityOrderList = mktActivityOrderPOMapper.getActivityOrderList(vo);
                    //判断是否是全部会员
                    if(!activityOrderList.get(0).getMbrLevelCode().equals("0")){
                        List<Long> level = new ArrayList<>();
                        level.add(Long.valueOf(activityOrderList.get(0).getMbrLevelCode()));
                        membersInfoSearchVo.setLevelID(level);
                    }

                }
                //如果是开卡活动
                if (mktActivityPO.getActivityType()== ActivityTypeEnum.ACTIVITY_TYPE_REGISGER.getCode()){
                    memberMessage.sendDXmessage(ListMktMessage, membersInfoSearchVo);
                    //查询对应的会员  发送微信模板消息
                    WxChannelInfoSearchVo wxChannelInfoSearchVo = new WxChannelInfoSearchVo();
                    wxChannelInfoSearchVo.setPageNum(1);
                    wxChannelInfoSearchVo.setPageSize(10000);
                    wxChannelInfoSearchVo.setFocus(2);
                    wxChannelInfoSearchVo.setCardStatus(1);
                    wxChannelInfoSearchVo.setMiniProgram((byte) 1);
                    memberMessage.sendWXmessage(ListMktMessage, wxChannelInfoSearchVo,activityVO);
                }else{
                    memberMessage.getMemberList(ListMktMessage, membersInfoSearchVo,activityVO);
                }

            }

        returnT.setCode(0);
        returnT.setContent("活动执行完毕");
        returnT.setMsg("success");
        return returnT;
    }
}
