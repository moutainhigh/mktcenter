package com.bizvane.mktcenterservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bizvane.appletservice.rpc.ReceiveSocketSendServiceRpc;
import com.bizvane.centerstageservice.models.po.SysStorePo;
import com.bizvane.couponfacade.interfaces.CouponDefinitionServiceFeign;
import com.bizvane.couponfacade.interfaces.SendCouponServiceFeign;
import com.bizvane.couponfacade.models.po.CouponDefinitionMoneyPO;
import com.bizvane.couponfacade.models.vo.SendCouponSimpleRequestVO;
import com.bizvane.members.facade.enums.IntegralChangeTypeEnum;
import com.bizvane.members.facade.service.api.IntegralChangeApiService;
import com.bizvane.members.facade.service.api.MemberInfoApiService;
import com.bizvane.members.facade.service.card.request.IntegralChangeRequestModel;
import com.bizvane.members.facade.service.card.response.IntegralChangeResponseModel;
import com.bizvane.members.facade.vo.MemberInfoApiModel;
import com.bizvane.messagefacade.interfaces.TemplateMessageServiceFeign;
import com.bizvane.messagefacade.models.vo.ActivityMessageVO;
import com.bizvane.mktcenterfacade.interfaces.ActivityRedPacketService;
import com.bizvane.mktcenterfacade.interfaces.ActivityStatisticsService;
import com.bizvane.mktcenterfacade.interfaces.TaskService;
import com.bizvane.mktcenterfacade.models.bo.ActivityRedPacketBO;
import com.bizvane.mktcenterfacade.models.bo.ActivityRedPacketListBO;
import com.bizvane.mktcenterfacade.models.bo.MktActivityRedPacketRecordBO;
import com.bizvane.mktcenterfacade.models.po.*;
import com.bizvane.mktcenterfacade.models.vo.RedPacketSocketVO;
import com.bizvane.mktcenterservice.mappers.MktActivityPOMapper;
import com.bizvane.mktcenterservice.mappers.MktActivityRedPacketRecordPOMapper;

import com.bizvane.mktcenterfacade.models.vo.ActivityPriceParamVO;
import com.bizvane.mktcenterfacade.models.vo.ActivityRedPacketVO;
import com.bizvane.mktcenterservice.common.job.JobUtil;
import com.bizvane.mktcenterservice.common.locktools.DistributedLocker;
import com.bizvane.mktcenterservice.common.utils.CodeUtil;
import com.bizvane.mktcenterservice.common.utils.TimeUtils;
import com.bizvane.mktcenterservice.mappers.MktActivityRedPacketPOMapper;
import com.bizvane.mktcenterservice.mappers.MktActivityRedPacketSumPOMapper;
import com.bizvane.utils.jobutils.JobClient;
import com.bizvane.utils.jobutils.XxlJobInfo;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import com.bizvane.utils.tokens.TokenUtils;
import com.bizvane.wechatfacade.interfaces.QRCodeServiceFeign;
import com.bizvane.wechatfacade.models.vo.CreateMiniprgmQRCodeRequestVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: lijunwei
 * @Time: 2019/1/11 13:55
 */
@Slf4j
@Service
public class ActivityRedPacketServiceImpl implements ActivityRedPacketService {
    @Autowired
    private MktActivityPOMapper mktActivityPOMapper;
    @Autowired
    private QRCodeServiceFeign qrCodeServiceFeign;
    @Autowired
    private MktActivityRedPacketPOMapper mktActivityRedPacketPOMapper;
    @Autowired
    private MktActivityRedPacketSumPOMapper mktActivityRedPacketSumPOMapper;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CouponDefinitionServiceFeign couponDefinitionServiceFeign;
    @Autowired
    private MktActivityRedPacketRecordPOMapper mktActivityRedPacketRecordPOMapper;
    @Autowired
    private IntegralChangeApiService integralChangeApiService;
    @Autowired
    private SendCouponServiceFeign sendCouponServiceFeign;
    @Autowired
    private ActivityStatisticsService activityStatisticsService;

    @Autowired
    private TemplateMessageServiceFeign templateMessageServiceFeign;
    @Autowired
    private MemberInfoApiService memberInfoApiService;
    @Autowired
    private DistributedLocker distributedLocker;

    /**
     * 新增
     */
    @Override
    public ResponseData<JSONObject> addActivityRedPacket(ActivityRedPacketBO bo, HttpServletRequest request) throws ParseException {
        ResponseData<JSONObject> responseData = new ResponseData<>();
        SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);

//        SysAccountPO sysAccountPo = new SysAccountPO();
//        sysAccountPo.setSysAccountId(96L);
//        sysAccountPo.setSysCompanyId(2L);
//        sysAccountPo.setBrandId(96L);
//        sysAccountPo.setAccountCode("15328634678");
//        sysAccountPo.setName("不啊哟删除");

        String activeRedPacketCode = CodeUtil.getActiveRedPacketCode();
        Date date = new Date();
        MktActivityPOWithBLOBs activityPO = bo.getActivityPO();
        Date startTime = activityPO.getStartTime();
        Boolean runStatus = TimeUtils.ifImmediatelyRun(startTime);
        log.info("红包 addActivityRedPacket status:" + runStatus);
        if (runStatus) {
            activityPO.setActivityStatus(2);
        } else {
            activityPO.setActivityStatus(1);
            //  jobUtil.addStartRedPacketJob(sysAccountPo, activityPO, activeRedPacketCode);
            jobUtil.addStartPrizeJob(sysAccountPo, activityPO, activeRedPacketCode);
        }
        // jobUtil.addEndStartRedPacketJob(sysAccountPo, activityPO, activeRedPacketCode);
        jobUtil.addEndPrizeJob(sysAccountPo, activityPO, activeRedPacketCode);


        activityPO.setCheckStatus(3);
        //activityPO.setQrCodeUrl(weixinUrl);
        activityPO.setActivityCode(activeRedPacketCode);
        activityPO.setActivityType(12);
        activityPO.setSysCompanyId(sysAccountPo.getSysCompanyId());
        activityPO.setSysBrandId(sysAccountPo.getBrandId());
        activityPO.setCreateUserId(sysAccountPo.getSysAccountId());
        activityPO.setCreateUserName(sysAccountPo.getName());
        activityPO.setCreateDate(date);
        mktActivityPOMapper.insertSelective(activityPO);

        MktActivityRedPacketPO activityRedPacketPO = bo.getActivityRedPacketPO();
        activityRedPacketPO.setSysCompanyId(sysAccountPo.getSysCompanyId());
        activityRedPacketPO.setSysBrandId(sysAccountPo.getBrandId());
        activityRedPacketPO.setMktActivityId(activityPO.getMktActivityId());
        activityPO.setSysBrandId(sysAccountPo.getBrandId());
        activityPO.setCreateUserId(sysAccountPo.getSysAccountId());
        activityPO.setCreateUserName(sysAccountPo.getName());
        activityPO.setCreateDate(date);
        mktActivityRedPacketPOMapper.insertSelective(activityRedPacketPO);

        MktActivityRedPacketSumPO redPacketSumPO = new MktActivityRedPacketSumPO();
        redPacketSumPO.setMktActivityId(activityPO.getMktActivityId());
        redPacketSumPO.setSysCompanyId(sysAccountPo.getSysCompanyId());
        redPacketSumPO.setActivityCode(activityPO.getActivityCode());
        redPacketSumPO.setActivityName(activityPO.getActivityName());
        redPacketSumPO.setActivityTime(TimeUtils.getDataNum(activityPO.getStartTime(), activityPO.getEndTime()));
        redPacketSumPO.setStartTime(activityPO.getStartTime());
        redPacketSumPO.setEndTime(activityPO.getEndTime());
        redPacketSumPO.setSysBrandId(sysAccountPo.getBrandId());
        redPacketSumPO.setCreateUserId(sysAccountPo.getSysAccountId());
        redPacketSumPO.setCreateUserName(sysAccountPo.getName());
        redPacketSumPO.setCreateDate(date);
        mktActivityRedPacketSumPOMapper.insertSelective(redPacketSumPO);

        // activityStatisticsService.addActivityIdsSet(activityPO.getMktActivityId());

        CreateMiniprgmQRCodeRequestVO createMiniprgmQRCodeRequestVO = new CreateMiniprgmQRCodeRequestVO();
        createMiniprgmQRCodeRequestVO.setSysBrandId(sysAccountPo.getBrandId());
        createMiniprgmQRCodeRequestVO.setMiniProgramType("10");
        createMiniprgmQRCodeRequestVO.setPath("pages/template01/red-packet/main");
        createMiniprgmQRCodeRequestVO.setScene(activityPO.getMktActivityId()+"&"+activeRedPacketCode);
        log.info("addActivityRedPacket wexin param:" + JSON.toJSONString(createMiniprgmQRCodeRequestVO));
        ResponseData<String> qrCodeResponseData = qrCodeServiceFeign.createMiniprgmQRCode(createMiniprgmQRCodeRequestVO);
        log.info("addActivityRedPacket wexin result:" + JSON.toJSONString(qrCodeResponseData));
        String weixinUrl = qrCodeResponseData.getData();
        log.info("addActivityRedPacket wexin result picture:" + weixinUrl);
        if (StringUtils.isNotBlank(weixinUrl)){
            MktActivityPOWithBLOBs updateActivityPO=new MktActivityPOWithBLOBs();
            updateActivityPO.setMktActivityId(activityPO.getMktActivityId());
            updateActivityPO.setQrCodeUrl(weixinUrl);
            mktActivityPOMapper.updateByPrimaryKeySelective(updateActivityPO);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("qrCodeUrl", weixinUrl);
        jsonObject.put("activityCode", activeRedPacketCode);
        jsonObject.put("activityName", activityPO.getActivityName());
        responseData.setData(jsonObject);
//        responseData.setMessage(SysResponseEnum.SUCCESS.getMessage());
//        responseData.setCode(SysResponseEnum.SUCCESS.getCode());
        return responseData;
    }

    /**
     * 查询详情
     */
    @Override
    public ResponseData<ActivityRedPacketBO> selectActivityRedPacket(ActivityRedPacketVO vo) {
        ResponseData<ActivityRedPacketBO> responseData = new ResponseData<>();
        ActivityRedPacketBO activityBO = new ActivityRedPacketBO();
        Long mktActivityId = vo.getMktActivityId();
        String activityCode = vo.getActivityCode();
        MktActivityPOWithBLOBs mktActivityPOWithBLOBs = null;
        if (StringUtils.isNotBlank(activityCode)) {
            MktActivityPOExample example = new MktActivityPOExample();
            example.createCriteria().andActivityCodeEqualTo(activityCode).andValidEqualTo(Boolean.TRUE);
            List<MktActivityPOWithBLOBs> mktActivityPOWithBLOBsList = mktActivityPOMapper.selectByExampleWithBLOBs(example);
            if (CollectionUtils.isNotEmpty(mktActivityPOWithBLOBsList)) {
                mktActivityPOWithBLOBs = mktActivityPOWithBLOBsList.get(0);
            }
        } else {
            mktActivityPOWithBLOBs = mktActivityPOMapper.selectByPrimaryKey(mktActivityId);
        }
        activityBO.setActivityPO(mktActivityPOWithBLOBs);
        String storeLimitListStr = mktActivityPOWithBLOBs.getStoreLimitList();
        List<SysStorePo> storeList = new ArrayList<SysStorePo>();
        if (StringUtils.isNotBlank(storeLimitListStr)) {
            List<Long> storeIdList = Arrays.asList(storeLimitListStr.split(",")).stream().map(element -> Long.valueOf(element)).collect(Collectors.toList());
            //查询店铺列表
            storeList = taskService.getStoreListByIds(storeIdList);
            log.info("---------通过品牌Ids--" + JSON.toJSONString(storeList) + "-----获取店铺列表----------" + JSON.toJSONString(storeList));
            activityBO.setStoreList(storeList);
        }
        MktActivityRedPacketPOExample example = new MktActivityRedPacketPOExample();
        example.createCriteria().andMktActivityIdEqualTo(mktActivityPOWithBLOBs.getMktActivityId());
        MktActivityRedPacketPO mktActivityRedPacketPO = mktActivityRedPacketPOMapper.selectByExample(example).get(0);
        activityBO.setActivityRedPacketPO(mktActivityRedPacketPO);
        activityBO.setCouponDefinitionPO(couponDefinitionServiceFeign.findByIdRpc(mktActivityRedPacketPO.getCouponDefinitionId()).getData());
        responseData.setData(activityBO);
        return responseData;
    }

    /**
     * 只用来查询活动详情(不查店铺和券)
     *
     * @param vo
     * @return
     */
    @Override
    public ResponseData<ActivityRedPacketBO> selectActivityRedPacketDetail(ActivityRedPacketVO vo) {
        ResponseData<ActivityRedPacketBO> responseData = new ResponseData<>();
        ActivityRedPacketBO activityRedPacketBO = mktActivityPOMapper.selectActivityRedPacketDetail(vo);
        responseData.setData(activityRedPacketBO);
        return responseData;
    }

    /**
     * 禁用
     *
     * @param po
     * @param request
     * @return
     */
    @Override
    public ResponseData<Integer> stopActivityRedPacket(MktActivityPOWithBLOBs po, HttpServletRequest request) {
        ResponseData<Integer> responseData = new ResponseData<>();
        SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);
        po.setActivityStatus(4);
        po.setModifiedDate(new Date());
        po.setModifiedUserId(sysAccountPo.getSysAccountId());
        po.setModifiedUserName(sysAccountPo.getName());
        mktActivityPOMapper.updateByPrimaryKeySelective(po);

        //禁用后要清除所有的job
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setBizCode(po.getActivityCode());
        jobClient.removeByBiz(xxlJobInfo);

        //activityStatisticsService.deleteActivityIdsSet(po.getMktActivityId());

        return responseData;

    }

    /**
     * 查询活动列表
     */
    @Override
    public ResponseData<PageInfo<MktActivityPOWithBLOBs>> selectActivityRedPacketList(ActivityPriceParamVO vo, HttpServletRequest request) {
        ResponseData<PageInfo<MktActivityPOWithBLOBs>> responseData = new ResponseData<>();
        SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);
        vo.setBrandId(sysAccountPo.getBrandId());
        //vo.setBrandId(96L);
        vo.setActivityType(12);
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        List<MktActivityPOWithBLOBs> listparam = mktActivityPOMapper.selectActivityPriceLists(vo);
        if (CollectionUtils.isEmpty(listparam)) {
            listparam = new ArrayList<MktActivityPOWithBLOBs>();
        }
        PageInfo<MktActivityPOWithBLOBs> pageInfo = new PageInfo<>(listparam);
        responseData.setData(pageInfo);
        return responseData;
    }

    /**
     * 活动统计列表
     */
    @Override
    public ResponseData<PageInfo<ActivityRedPacketListBO>> selectActivityRedPacketAnalyzeLists(ActivityRedPacketVO vo, HttpServletRequest request) {
        ResponseData<PageInfo<ActivityRedPacketListBO>> responseData = new ResponseData<>();
        SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);
//       SysAccountPO sysAccountPo = new SysAccountPO();
//        sysAccountPo.setSysAccountId(96L);
//        sysAccountPo.setSysCompanyId(2L);
//        sysAccountPo.setBrandId(96L);
//        sysAccountPo.setAccountCode("15328634678");
//        sysAccountPo.setName("不啊哟删除");

        vo.setSysBrandId(sysAccountPo.getBrandId());
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        List<ActivityRedPacketListBO> listparam = mktActivityRedPacketSumPOMapper.selectActivityRedPacketAnalyzeLists(vo);
        if (CollectionUtils.isEmpty(listparam)) {
            listparam = new ArrayList<ActivityRedPacketListBO>();
        } else {
            listparam.stream().forEach(param -> {
                int dataNum = TimeUtils.getDataNumRed(param.getStartTime(), param.getEndTime());
                param.setResidueDates(dataNum);
                int days = param.getActivityTime() - dataNum;
                param.setGoingDates(days < 0 ? 0 : days);
            });
        }
        PageInfo<ActivityRedPacketListBO> pageInfo = new PageInfo<>(listparam);
        responseData.setData(pageInfo);
        return responseData;
    }

    /**
     * 查询领券的人列表
     */
    @Override
    public ResponseData<PageInfo<MktActivityRedPacketRecordBO>> getRedPacketCoponRecord(ActivityRedPacketVO vo, HttpServletRequest request) {
        ResponseData<PageInfo<MktActivityRedPacketRecordBO>> responseData = new ResponseData<>();
//        SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);
        Long mktActivityId = vo.getMktActivityId();

        MktActivityRedPacketPOExample example=new MktActivityRedPacketPOExample();
        example.createCriteria().andMktActivityIdEqualTo(mktActivityId).andValidEqualTo(Boolean.TRUE);
        List<MktActivityRedPacketPO> mktActivityRedPacketPOS = mktActivityRedPacketPOMapper.selectByExample(example);

        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        List<MktActivityRedPacketRecordBO> listparam = mktActivityRedPacketRecordPOMapper.getRedPacketCoponRecord(vo);
        if (CollectionUtils.isEmpty(listparam)) {
            listparam = new ArrayList<MktActivityRedPacketRecordBO>();
        } else {
            listparam.stream().forEach(param -> {
                MktActivityRedPacketPO activityRedPacketPO = mktActivityRedPacketPOS.get(0);
                Integer zhuliredPacketCount = mktActivityRedPacketRecordPOMapper.getRedPacketCount(2, null, param.getMemberCode(), mktActivityId);
                Integer reward = activityRedPacketPO.getCouponDenomination() + zhuliredPacketCount * activityRedPacketPO.getAddCouponDenomination();
                param.setHelpNumber(zhuliredPacketCount);
                param.setCouponName(reward+"元现金券");
            });
        }
        PageInfo<MktActivityRedPacketRecordBO> pageInfo = new PageInfo<>(listparam);
        responseData.setData(pageInfo);
        return responseData;
    }

    /**
     * 查询助力人员列表
     */
    @Override
    public ResponseData<List<MktActivityRedPacketRecordPO>> getRedPacketZhuLiRecord(ActivityRedPacketVO vo) {
        log.info("getRedPacketZhuLiRecord 查询助力人员列表 参数:" + JSON.toJSONString(vo));
        ResponseData<List<MktActivityRedPacketRecordPO>> responseData = new ResponseData<>();
        //SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);
        List<MktActivityRedPacketRecordPO> listparam = mktActivityRedPacketRecordPOMapper.getRedPacketZhuLiRecord(vo);
        if (CollectionUtils.isEmpty(listparam)) {
            listparam = new ArrayList<MktActivityRedPacketRecordPO>();
        }
        responseData.setData(listparam);
        return responseData;
    }

    @Override
    public ResponseData<RedPacketSocketVO> getRedPacketZhuLiRecordByAPP(ActivityRedPacketVO vo) {
        log.info("getRedPacketZhuLiRecordByAPP 查询助力人员列表app 参数:" + JSON.toJSONString(vo));
        ResponseData<RedPacketSocketVO> responseData = new ResponseData<>();

        MktActivityRedPacketPOExample packetPOExample = new MktActivityRedPacketPOExample();
        packetPOExample.createCriteria().andMktActivityIdEqualTo(vo.getMktActivityId()).andValidEqualTo(Boolean.TRUE);
        MktActivityRedPacketPO packetRecordPO = mktActivityRedPacketPOMapper.selectByExample(packetPOExample).get(0);

        List<MktActivityRedPacketRecordPO> dataAppZhuli = this.getRedPacketZhuLiRecord(vo).getData();
        System.out.println("小程序app助力socket: " + JSON.toJSONString(dataAppZhuli));
        Integer copouAppData = 0;
        // JSONObject jsonObject = new JSONObject();
        if (CollectionUtils.isEmpty(dataAppZhuli)) {
            copouAppData = packetRecordPO.getCouponDenomination();
        } else {
            copouAppData = packetRecordPO.getCouponDenomination() + dataAppZhuli.size() * packetRecordPO.getAddCouponDenomination();
        }
        System.out.println("小程序app助力socket 券金额 " + copouAppData);
//        jsonObject.put("list",dataAppZhuli);
//        jsonObject.put("couponMoney",copouAppData);
        RedPacketSocketVO redPacketSocketVO = new RedPacketSocketVO();
        redPacketSocketVO.setCopouAppData(copouAppData);
        redPacketSocketVO.setDataAppZhuli(dataAppZhuli);
        responseData.setData(redPacketSocketVO);
        return responseData;
    }

    /**
     * 判断会员是否  助力过  发起过  领券过
     */
    @Override
    public ResponseData<Integer> doIfActivityRedPacket(ActivityRedPacketVO vo) {
        log.info("doIfActivityRedPacket 判断 param :" + JSON.toJSONString(vo));
        ResponseData<Integer> responseData = new ResponseData<>();
        Integer type = vo.getType();
        String memberCode = vo.getMemberCode();
        String sponsorCode = vo.getSponsorCode();
        Long mktActivityId = vo.getMktActivityId();
        MktActivityRedPacketPOExample example = new MktActivityRedPacketPOExample();
        example.createCriteria().andMktActivityIdEqualTo(mktActivityId).andValidEqualTo(Boolean.TRUE);
        Integer redPacketCount = mktActivityRedPacketRecordPOMapper.getRedPacketCount(type, memberCode, sponsorCode, mktActivityId);
        Integer limitNum = 0;
        Integer zhuliCount = 0;
        if (2 == type) {
            limitNum = mktActivityRedPacketPOMapper.selectByExample(example).get(0).getLimitNum();
            zhuliCount = mktActivityRedPacketRecordPOMapper.getRedPacketCount(type, null, sponsorCode, mktActivityId);
        }
        if (1 == type && redPacketCount > 0) {
            responseData.setMessage("已经发起过此活动,不能再次发起!");
            responseData.setData(101);
            //responseData.setCode(101);
        } else if (2 == type && redPacketCount > 0) {
            responseData.setMessage("已经助力过此活动,不能再次助力!");
            responseData.setData(102);
            //responseData.setCode(102);
        } else if (2 == type && limitNum.equals(zhuliCount)) {
            responseData.setMessage("已达到助力的上线次数!");
            responseData.setData(103);
            // responseData.setCode(103);
        } else if (3 == type && redPacketCount > 0) {
            responseData.setMessage("已经领券,不能再次领取!");
            responseData.setData(104);
            //responseData.setCode(104);
        } else {
            responseData.setMessage("正常参与!");
            responseData.setData(0);
            // responseData.setCode(0);
        }
        return responseData;
    }

    /**
     * 添加记录 发起
     */
    @Transactional
    @Override
    public void andActivityRedPacketCreateRecord(ActivityRedPacketVO vo) {
        vo.setType(1);
        ActivityRedPacketBO bo = mktActivityPOMapper.selectActivityRedPacketDetail(vo);
        log.info("andActivityRedPacketCreateRecord 添加记录 param:" + JSON.toJSONString(vo) + "--活动详情-" + JSON.toJSONString(bo));
        Integer limitNum = bo.getActivityRedPacketPO().getLimitNum();
        Integer redPacketCount = mktActivityRedPacketRecordPOMapper.getRedPacketCount(1, vo.getMemberCode(), null, vo.getMktActivityId());
        if (redPacketCount > 0) {
            return;
        }
        vo.setInitiatorNum(1);
        this.doStatisticsRecored(vo, bo, null, null);
    }

    /**
     * 添加记录 助力
     */
    @Transactional
    @Override
    public synchronized ResponseData<Integer> andActivityRedPacketZhuliRecord(ActivityRedPacketVO vo) throws IOException {
        RLock lock = distributedLocker.lock(vo.getSponsorCode()+vo.getMktActivityId(), TimeUnit.SECONDS, 30L);
        ResponseData<Integer> responseData = new ResponseData<>();
        ActivityRedPacketBO bo = mktActivityPOMapper.selectActivityRedPacketDetail(vo);
        log.info("andActivityRedPacketZhuliRecord 添加记录 param:" + JSON.toJSONString(vo) + "--活动详情-" + JSON.toJSONString(bo));
        vo.setType(2);
        vo.setHelpNum(1);
        if (mktActivityRedPacketRecordPOMapper.getRedPacketCount(2, vo.getMemberCode(), null, vo.getMktActivityId()) > 0) {
            vo.setHelpNum(0);
        }
        Integer integerStaus = this.doStatisticsRecored(vo, bo, null, null);
        //通知app 新增了助力人
        RedPacketSocketVO appData = this.getRedPacketZhuLiRecordByAPP(vo).getData();
        appData.setMemberCode(vo.getSponsorCode());
        log.info("小程序app助力socket:" + JSON.toJSONString(appData));
        //todo 调用了applet
//        receiveSocketSendServiceRpc.receiveSocketSend(appData);

        this.sendMessage(vo, bo, appData);

        this.addCouponModelMoneyNum(vo, bo);
        if (integerStaus==0){
            responseData.setData(bo.getActivityRedPacketPO().getRewardIntegral());
        }

        distributedLocker.unlock(lock);
        return responseData;
    }

    public void sendMessage(ActivityRedPacketVO vo, ActivityRedPacketBO bo, RedPacketSocketVO appData) {
        ActivityMessageVO activityMessageVO = new ActivityMessageVO();
        log.info("红包 sendMessage 参数:" + JSON.toJSONString(activityMessageVO));
        activityMessageVO.setSysCompanyId(vo.getSysCompanyId());
        activityMessageVO.setMemberCode(vo.getSponsorCode());
        activityMessageVO.setSysBrandId(vo.getSysBrandId());
        activityMessageVO.setActivityName(bo.getActivityPO().getActivityName());
        activityMessageVO.setActivityInterests(String.valueOf(appData.getDataAppZhuli().size()));
        activityMessageVO.setTemplateType("RED_TEMPLATE_MESSAGE");
        MemberInfoApiModel members = new MemberInfoApiModel();
        members.setMemberCode(vo.getSponsorCode());
        activityMessageVO.setMemberName(memberInfoApiService.getSingleMemberModel(members).getData().getName());
        ResponseData<String> stringResponseData = templateMessageServiceFeign.sendTemplateMessage(activityMessageVO);
        log.info("红包 sendMessage 结果:" + JSON.toJSONString(stringResponseData));
    }

    /**
     * 为券模块添加券面额记录
     */
    public void addCouponModelMoneyNum(ActivityRedPacketVO vo, ActivityRedPacketBO bo) {
        CouponDefinitionMoneyPO po = new CouponDefinitionMoneyPO();
        po.setSysBrandId(vo.getSysBrandId());
        po.setCouponDefinitionId(String.valueOf(bo.getActivityRedPacketPO().getCouponDefinitionId()));
        po.setMemberCode(vo.getSponsorCode());
        po.setMoneyAdd(new BigDecimal(bo.getActivityRedPacketPO().getAddCouponDenomination()));
        po.setTaskId(bo.getActivityPO().getActivityCode()+vo.getSponsorCode());
        ResponseData<Object> objectResponseData = couponDefinitionServiceFeign.definitionMoneyRpc(po);
        log.info("addCouponModelMoneyNum 入参:" + JSON.toJSONString(po) + " ---结果" + JSON.toJSONString(objectResponseData));
    }

    /**
     * 添加记录  领券
     */
    @Transactional
    @Override
    public ResponseData<Integer> andActivityRedPacketSendCouponRecord(ActivityRedPacketVO vo) {
        ResponseData<Integer> responseData = new ResponseData<>();
        ActivityRedPacketBO bo = mktActivityPOMapper.selectActivityRedPacketDetail(vo);
        log.info("andActivityRedPacketSendCouponRecord 添加记录 param:" + JSON.toJSONString(vo) + "--活动详情-" + JSON.toJSONString(bo));
        MktActivityRedPacketRecordPOExample recordPOExample = new MktActivityRedPacketRecordPOExample();
        recordPOExample.createCriteria().andMemberCodeEqualTo(vo.getMemberCode()).andMktActivityIdEqualTo(vo.getMktActivityId()).andTypeEqualTo(1).andValidEqualTo(Boolean.TRUE);
        List<MktActivityRedPacketRecordPO> mktActivityRedPacketRecordPOS = mktActivityRedPacketRecordPOMapper.selectByExample(recordPOExample);
        if (CollectionUtils.isEmpty(mktActivityRedPacketRecordPOS)){
            vo.setType(1);
            vo.setInitiatorNum(1);
            this.doStatisticsRecored(vo, bo, null, null);
        }
        vo.setType(3);
        //查询助力人数
        Integer zhuliredPacketCount = mktActivityRedPacketRecordPOMapper.getRedPacketCount(2, null, vo.getMemberCode(), vo.getMktActivityId());
        vo.setGetCouponNum(1);
        String couponCode = this.sendCoupon(bo, vo);
        MktActivityRedPacketPO activityRedPacketPO = bo.getActivityRedPacketPO();
        Integer reward = activityRedPacketPO.getCouponDenomination() + zhuliredPacketCount * activityRedPacketPO.getAddCouponDenomination();
        this.doStatisticsRecored(vo, bo, couponCode, reward);
        responseData.setData(reward);
        log.info("andActivityRedPacketSendCouponRecord 拆红包返回值 result:"+JSON.toJSONString(responseData));
        return responseData;
    }

    //添加历史记录
    public Integer doStatisticsRecored(ActivityRedPacketVO vo, ActivityRedPacketBO bo, String couponCode, Integer reward) {
        log.info("doStatisticsRecored 添加历史记录 param :" + JSON.toJSONString(vo) + "--" + JSON.toJSONString(bo) + "--" + reward);

        Integer integerStatus=100;

        MktActivityRedPacketRecordPO recordPO = new MktActivityRedPacketRecordPO();
        BeanUtils.copyProperties(vo, recordPO);
        recordPO.setCouponDefinitionId(bo.getActivityRedPacketPO().getCouponDefinitionId());
        recordPO.setCouponName(bo.getActivityRedPacketPO().getCouponName());
        recordPO.setGetCouponDate(new Date());
        recordPO.setCouponCode(couponCode);
        recordPO.setRewardIntegral(bo.getActivityRedPacketPO().getRewardIntegral());
        recordPO.setAddCouponDenomination(bo.getActivityRedPacketPO().getAddCouponDenomination());
        recordPO.setCouponQuota(reward);
        recordPO.setCreateDate(new Date());

        if (2==vo.getType()){
             //判断是否超过最大助力次数
            Integer redPacketCount = mktActivityRedPacketRecordPOMapper.getRedPacketCount(2, null, vo.getSponsorCode(), vo.getMktActivityId());
            if (redPacketCount < bo.getActivityRedPacketPO().getLimitNum()){
                mktActivityRedPacketRecordPOMapper.insertSelective(recordPO);
//                mktActivityRedPacketRecordPOMapper.deleteByPrimaryKey(recordPO.getMktActivityRedPacketRecordId());
//                vo.setHelpNum(0);
                integerStatus = this.addPonint(bo, vo);
            }
        }else{
            mktActivityRedPacketRecordPOMapper.insertSelective(recordPO);
        }
        mktActivityRedPacketSumPOMapper.updateUpdateCount(vo);

        Integer type = vo.getType();
        //统计的4是领券
        activityStatisticsService.statisticsData(bo.getActivityPO().getMktActivityId(), type == 3 ? 4 : type, vo.getMemberCode());
        return  integerStatus;
    }

    /**
     * 添加积分
     */
    public Integer addPonint(ActivityRedPacketBO bo, ActivityRedPacketVO vo) {
        if (!bo.getActivityRedPacketPO().getDoIfReward()) {
            log.info("助力不送积分!");
            return 100;
        }
        IntegralChangeRequestModel integralRecordModel = new IntegralChangeRequestModel();
        integralRecordModel.setSysCompanyId(vo.getSysCompanyId());
        integralRecordModel.setBrandId(vo.getSysBrandId());
        integralRecordModel.setMemberCode(vo.getMemberCode());
        integralRecordModel.setBusinessType("28");
        //2=收入积分(新增积分)      1=支出积分(减少积分)
        integralRecordModel.setChangeType(IntegralChangeTypeEnum.INCOME.getCode());
        integralRecordModel.setChangeBills(bo.getActivityPO().getActivityCode());
        integralRecordModel.setChangeIntegral(bo.getActivityRedPacketPO().getRewardIntegral());
        log.info("红包 发送积分的参数--" + JSON.toJSONString(integralRecordModel));
        IntegralChangeResponseModel integralChangeResponseModel = integralChangeApiService.integralChangeOperate(integralRecordModel);
        log.info("红包 发积分结果打印======" + JSON.toJSONString(integralChangeResponseModel));
         return  integralChangeResponseModel.getCode();
    }

    /**
     * 发送券
     */
    public String sendCoupon(ActivityRedPacketBO bo, ActivityRedPacketVO vo) {
        SendCouponSimpleRequestVO onecouponVO = new SendCouponSimpleRequestVO();
        onecouponVO.setMemberCode(vo.getMemberCode());
        onecouponVO.setSendBussienId(vo.getMktActivityId());
        onecouponVO.setBusinessName(bo.getActivityPO().getActivityName());
        onecouponVO.setSendType("104");
        onecouponVO.setTaskId(bo.getActivityPO().getActivityCode()+vo.getMemberCode());
        onecouponVO.setCouponDefinitionId(bo.getActivityRedPacketPO().getCouponDefinitionId());
        onecouponVO.setBrandId(vo.getSysBrandId());
        log.info("红包 发送券的参数-----" + JSON.toJSONString(onecouponVO));
        // award.execute(bo);
        ResponseData<String> simple = sendCouponServiceFeign.simple(onecouponVO);
        log.info("红包 发送券的结果------" + JSON.toJSONString(simple));
        return simple.getData();
    }

    /**
     * 小程序领取记录展示
     *
     * @param vo
     * @return
     */
    @Override
    public ResponseData<List<MktActivityRedPacketRecordBO>> getRedPacketCoponAppRecord(ActivityRedPacketVO vo) {
        log.info("getRedPacketCoponAppRecord 小程序查询券记录 param" + JSON.toJSONString(vo));
        ResponseData<List<MktActivityRedPacketRecordBO>> responseData = new ResponseData<>();
        //Long mktActivityId = vo.getMktActivityId();
        PageHelper.startPage(vo.getPageNumber(), vo.getPageSize());
        List<MktActivityRedPacketRecordBO> listparam = mktActivityRedPacketRecordPOMapper.getRedPacketCoponRecord(vo);
        if (CollectionUtils.isEmpty(listparam)) {
            listparam = new ArrayList<MktActivityRedPacketRecordBO>();
        }
        responseData.setData(listparam);
        return responseData;
    }
}