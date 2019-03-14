package com.bizvane.mktcenterservice.service.impl;
import com.alibaba.fastjson.JSON;
import com.bizvane.centercontrolservice.models.po.AppletFunctionPO;
import com.bizvane.centercontrolservice.rpc.AppletRouteServiceRpc;
import com.bizvane.centerstageservice.rpc.SysCheckConfigServiceRpc;
import com.bizvane.centerstageservice.rpc.SysCheckServiceRpc;
import com.bizvane.couponfacade.interfaces.CouponDefinitionServiceFeign;
import com.bizvane.couponfacade.interfaces.CouponQueryServiceFeign;
import com.bizvane.couponfacade.interfaces.SendCouponServiceFeign;
import com.bizvane.fitmentservice.models.Rpc.MenberMadeServiceRpc;
import com.bizvane.members.facade.models.MemberInfoModel;
import com.bizvane.members.facade.service.api.IntegralRecordApiService;
import com.bizvane.members.facade.service.api.MemberInfoApiService;
import com.bizvane.messagefacade.interfaces.SendCommonMessageFeign;
import com.bizvane.mktcenterfacade.interfaces.*;
import com.bizvane.mktcenterfacade.models.po.*;
import com.bizvane.mktcenterfacade.models.vo.MktTaskRecordVO;
import com.bizvane.mktcenterfacade.models.vo.ShareSuccessVO;
import com.bizvane.mktcenterfacade.models.vo.TaskVO;
import com.bizvane.mktcenterservice.common.enums.CheckStatusEnum;

import com.bizvane.mktcenterfacade.models.bo.TaskAwardBO;
import com.bizvane.mktcenterfacade.models.bo.TaskBO;
import com.bizvane.mktcenterfacade.models.bo.TotalStatisticsBO;
import com.bizvane.mktcenterservice.mappers.*;


import com.bizvane.mktcenterservice.common.award.Award;
import com.bizvane.mktcenterservice.common.constants.SystemConstants;
import com.bizvane.mktcenterservice.common.constants.TaskConstants;

import com.bizvane.mktcenterservice.common.job.JobUtil;
import com.bizvane.mktcenterservice.common.utils.CodeUtil;
import com.bizvane.mktcenterservice.common.utils.TaskParamCheckUtil;
import com.bizvane.mktcenterservice.common.utils.TimeUtils;

import com.bizvane.utils.enumutils.SysResponseEnum;
import com.bizvane.utils.jobutils.JobClient;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections.CollectionUtils;
import java.text.ParseException;
import java.util.*;
/**
 * @author gengxiaoyu
 * @date on 2018/7/16 14:14
 * @description
 * @Copyright (c) 2018 上海商帆信息科技有限公司-版权所有
 */
@Slf4j
@Service
public class TaskShareServiceImpl implements TaskShareService {
    @Autowired
    private MktTaskSharePOMapper mktTaskSharePOMapper;
    @Autowired
    private SysCheckConfigServiceRpc sysCheckConfigServiceRpc;
    @Autowired
    private SysCheckServiceRpc sysCheckServiceRpc;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private MktTaskPOMapper mktTaskPOMapper;
    @Autowired
    private MktCouponPOMapper mktCouponPOMapper;
    @Autowired
    private MktMessagePOMapper mktMessagePOMapper;
    @Autowired
    private IntegralRecordApiService integralRecordApiService;
    @Autowired
    private SendCouponServiceFeign sendCouponServiceFeign;
    @Autowired
    private MktTaskRecordPOMapper mktTaskRecordPOMapper;
    @Autowired
    private CouponQueryServiceFeign couponQueryService;
    @Autowired
    private Award award;
    @Autowired
    private CouponDefinitionServiceFeign couponDefinitionServiceFeign;
    @Autowired
    private JobClient jobClient;
    @Autowired
    private SendCommonMessageFeign sendCommonMessageFeign;
    @Autowired
    private MemberInfoApiService memberInfoApiService;
    @Autowired
    private  MenberMadeServiceRpc menberMadeServiceRpc;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskCouponService taskCouponService;
    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private TaskRecordService taskRecordService;
    @Autowired
    private AppletRouteServiceRpc appletRouteServiceRpc;
    /**
     * 获取站内链接 列表
     */
    @Override
    public ResponseData<List<AppletFunctionPO>>  getURLList(Long brandId){
        log.info("----getURLList获取URL列表---参数----"+brandId);
        ResponseData<List<AppletFunctionPO>> allAppletFunction = appletRouteServiceRpc.getAllAppletFunction(brandId);
       log.info("----getURLList获取URL列表---出参----"+JSON.toJSONString(allAppletFunction));
        return allAppletFunction;

    }
    /**
     * 添加微信分享任务
     * @param bo
     * @param stageUser
     * @return
     */
    @Transactional
    @Override
    public ResponseData<Integer> addTask(TaskBO bo, SysAccountPO stageUser) throws ParseException {
        //0.参数的检验
        ResponseData responseData = TaskParamCheckUtil.checkParam(bo);
        //参数校验不通过
        if(responseData.getCode()>0){
            return responseData;
        }
        TaskVO taskVO = bo.getTaskVO();
        taskVO.setValid(Boolean.TRUE);
        taskVO.setCreateDate(TimeUtils.getNowTime());
        taskVO.setCreateUserId(stageUser.getSysAccountId());
        taskVO.setCreateUserName(stageUser.getName());

        //1.生成任务编号
        String taskCode = CodeUtil.getTaskCode();
        //2.任务主表新增
        MktTaskPOWithBLOBs mktTaskPOWithBLOBs = new MktTaskPOWithBLOBs();
        BeanUtils.copyProperties(taskVO, mktTaskPOWithBLOBs);
        mktTaskPOWithBLOBs.setTaskCode(taskCode);
        //1.判断是否需要审核  1=需要审核   0=不需要
        Integer StagecheckStatus = taskService.getCenterStageCheckStage(mktTaskPOWithBLOBs);
        log.info("--判断是否需要审核  1=需要审核   0=不需要--"+StagecheckStatus);
        mktTaskPOWithBLOBs = taskService.isOrNoCheckState(mktTaskPOWithBLOBs,StagecheckStatus);
        Long mktTaskId = taskService.addTask(mktTaskPOWithBLOBs, stageUser);
        //将需要审核的任务添加到审核中心
        if(TaskConstants.FIRST.equals(StagecheckStatus)){
            log.info("--添加数据到审核表--"+StagecheckStatus);
            taskService.addCheckData(mktTaskPOWithBLOBs);
        }
        //3.任务消费表新增
        MktTaskSharePOWithBLOBs mktTaskSharePO = new MktTaskSharePOWithBLOBs();
        BeanUtils.copyProperties(taskVO, mktTaskSharePO);
        mktTaskSharePO.setMktTaskId(mktTaskId);
        this.insertShareTask(mktTaskSharePO,stageUser);

        //4.新增奖励新增   业务类型：1活动，2任务
        List<MktCouponPO> mktCouponPOList = bo.getMktCouponPOList();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mktCouponPOList)) {
            mktCouponPOList.stream().forEach(param -> {
                param.setBizId(mktTaskId);
                param.setBizType(TaskConstants.TASK_TYPE);
                taskCouponService.addTaskCoupon(param, stageUser);
            });
        }
        //5.新增消息新增  消息业务类型：1活动，2任务
        List<MktMessagePO> mktmessagePOList = bo.getMessagePOList();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mktmessagePOList)) {
            mktmessagePOList.stream().forEach(param -> {
                        param.setBizId(mktTaskId);
                        param.setBizType(TaskConstants.TASK_TYPE);
                        taskMessageService.addTaskMessage(param, stageUser);
                    }
            );
        }

        //6.处理任务
        taskService.doOrderTask(mktTaskPOWithBLOBs,mktmessagePOList,stageUser);

        responseData.setCode(SystemConstants.SUCCESS_CODE);
        responseData.setMessage(SystemConstants.SUCCESS_MESSAGE);
        return responseData;
    }
    /**
     * 修改任务
     * @param bo
     * @param stageUser
     * @return
     */
    @Transactional
    @Override
    public ResponseData<Integer> updateTask(TaskBO bo, SysAccountPO stageUser) {
        ResponseData responseData = new ResponseData();
        TaskVO taskVO = bo.getTaskVO();
        //接收需要修改的任务vo对象判断是否状态为待审核
        //状态为待审核或者被驳回时才可修改,其他状态为不可修改
        if (taskVO.getCheckStatus()!= CheckStatusEnum.CHECK_STATUS_PENDING.getCode()&&taskVO.getCheckStatus()!=CheckStatusEnum.CHECK_STATUS_REJECTED.getCode()){
            responseData.setCode(SysResponseEnum.FAILED.getCode());
            responseData.setMessage("该任务不可修改");
            return responseData;
        }
        //0.参数的检验
        responseData = TaskParamCheckUtil.checkParam(bo);
        //参数校验不通过
        if(responseData.getCode()>0){
            return responseData;
        }
        TaskVO vo = bo.getTaskVO();
        vo.setValid(Boolean.TRUE);
        vo.setModifiedDate(TimeUtils.getNowTime());
        vo.setModifiedUserName(stageUser.getName());
        vo.setModifiedUserId(stageUser.getSysAccountId());
        Long mktTaskId = vo.getMktTaskId();
        // String taskCode = vo.getTaskCode();
        //1.任务主表修改
        MktTaskPOWithBLOBs mktTaskPOWithBLOBs = new MktTaskPOWithBLOBs();
        BeanUtils.copyProperties(vo, mktTaskPOWithBLOBs);
        taskService.updateTask(mktTaskPOWithBLOBs, stageUser);

        //3.任务消费表修改
        MktTaskSharePOWithBLOBs mktTaskSharePO = new MktTaskSharePOWithBLOBs();
        BeanUtils.copyProperties(taskVO, mktTaskSharePO);
        mktTaskSharePO.setMktTaskId(mktTaskId);
        this.updateShareTask(mktTaskSharePO, stageUser);

        //4.奖励修改 biz_type 活动类型  1=活动
        taskCouponService.deleteTaskCoupon(mktTaskId, stageUser);
        List<MktCouponPO> mktCouponPOList = bo.getMktCouponPOList();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mktCouponPOList)) {
            mktCouponPOList.stream().forEach(param -> {
                param.setBizId(mktTaskId);
                param.setBizType(TaskConstants.TASK_TYPE);
                taskCouponService.addTaskCoupon(param, stageUser);
            });
        }
        //5.修改消息
        taskMessageService.deleteTaskMessage(mktTaskId,stageUser);
        List<MktMessagePO> mktmessagePOList = bo.getMessagePOList();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mktmessagePOList)) {
            mktmessagePOList.stream().forEach(param -> {
                        param.setBizId(mktTaskId);
                        param.setBizType(TaskConstants.TASK_TYPE);
                        taskMessageService.addTaskMessage(param, stageUser);
                    }
            );
        }
        //6.处理任务
        taskService.doOrderTask(mktTaskPOWithBLOBs,mktmessagePOList,stageUser);

        responseData.setCode(SystemConstants.SUCCESS_CODE);
        responseData.setMessage(SystemConstants.SUCCESS_MESSAGE);
        return responseData;
    }
    /**
     * 新增分享任务数据 单表
     * @param po
     * @return
     */
    public  Integer  insertShareTask(MktTaskSharePOWithBLOBs po,SysAccountPO stageUser){
        po.setSysCompanyId(stageUser.getSysCompanyId());
        po.setCreateUserId(stageUser.getSysAccountId());
        po.setCreateUserName(stageUser.getName());
        po.setCreateDate(new Date());
       return mktTaskSharePOMapper.insertSelective(po);
    }
    /**
     * 修改分享任务数据 单表
     * @return
     */
    public  Integer  updateShareTask(MktTaskSharePOWithBLOBs po,SysAccountPO stageUser){
        po.setModifiedUserId(stageUser.getSysAccountId());
        po.setModifiedUserName(stageUser.getName());
        po.setModifiedDate(new Date());

        MktTaskSharePOExample example = new MktTaskSharePOExample();
        example.createCriteria().andMktTaskIdEqualTo(po.getMktTaskId()).andValidEqualTo(Boolean.TRUE);
        return mktTaskSharePOMapper.updateByExampleSelective(po,example);
    }
    /**
     * 执行邀请任务的奖励
     */
    @Transactional
    @Override
    public  void   doAwardShare(ShareSuccessVO vo){
        log.info("分享任务的奖励--参数--"+ JSON.toJSONString(vo));
        Long mktTaskIdparam = vo.getMktTaskId();
        //被邀请人信息
        Date shareDate = vo.getShareDate();
        //邀请人的信息
        String memberCode = vo.getMemberCode();
        MemberInfoModel memeberDetail = taskService.getCompanyMemeberDetail(memberCode);
        log.info("分享任务的奖励--会员详情参数--"+ JSON.toJSONString(memeberDetail));
        Long companyId = memeberDetail.getSysCompanyId();
        Long brandId = memeberDetail.getBrandId();
        String cardNo = memeberDetail.getCardNo();
        Long serviceStoreId = memeberDetail.getServiceStoreId();
        //符合条件的任务列表
        List<TaskAwardBO> taskAwardList = taskService.getTaskShareAwardList(mktTaskIdparam,companyId, brandId, shareDate);
        log.info("分享任务的奖励--任务列表--"+ JSON.toJSONString(taskAwardList));
        if (CollectionUtils.isNotEmpty(taskAwardList)){
            taskAwardList.stream().
                filter(obj->{
                     Boolean isStoreLimit = obj.getStoreLimit();
                     String  StoreLimitList=obj.getStoreLimitList();
                     return !isStoreLimit || (serviceStoreId!=null) ||(StringUtils.isNotBlank(StoreLimitList) &&  obj.getStoreLimitList().contains(String.valueOf(serviceStoreId)));}).
                forEach(obj->{
                    //邀请开卡人数
                    Integer shareTimes = obj.getShareTimes();
                    MktTaskRecordVO recordVO = new MktTaskRecordVO();
                    recordVO.setSysBrandId(brandId);
                    recordVO.setTaskType(obj.getTaskType());
                    recordVO.setTaskId(obj.getMktTaskId());
                    recordVO.setMemberCode(memberCode);
                    // 获取会员是否已经成功参与过某一任务
                    Boolean isOrNoAward = taskRecordService.getIsOrNoAward(recordVO);
                    log.info("分享任务的奖励--已经成功参与--"+isOrNoAward+"--"+JSON.toJSONString(recordVO));
                    if (!isOrNoAward){
                        MktTaskRecordPO recordPO = new MktTaskRecordPO();
                        BeanUtils.copyProperties(recordVO,recordPO);
                        recordPO.setParticipateDate(shareDate);
                        recordPO.setSysCompanyId(companyId);
                        Long recordId = taskRecordService.addTaskRecord(recordPO);

                        //获取会员参与某一任务总次数
                        TotalStatisticsBO totalBO = taskRecordService.getTotalStatistics(recordVO);
                        log.info("分享任务的奖励--参与某一任务总次数--"+JSON.toJSONString(totalBO));
                        if (totalBO!=null && totalBO.getTotalTimes()!=null && totalBO.getTotalTimes().equals(shareTimes)){
                            recordPO.setRewarded(Integer.valueOf(1));
                            recordPO.setPoints(obj.getPoints());
                            recordPO.setSysCompanyId(recordId);
                            taskRecordService.updateTaskRecord(recordPO);
                            taskService.sendCouponAndPoint(memberCode,obj);
                        }

                    }

            });
        }

    }

    /**
     * 效果分析
     * @param date1
     * @param date2
     * @param stageUser
     * @param taskName
     * @param pageForm
     * @return
     */
//    @Transactional
//    @Override
//    public ResponseData<TaskRecordVO> getTaskShareRecordByTime(Date date1, Date date2, SysAccountPO stageUser,String taskName,PageForm pageForm){
//        ResponseData responseData = new ResponseData();
//        TaskRecordVO taskRecordVO = new TaskRecordVO();
//
//        Long allPoints=0L;
//        Long allCountCoupon=0L;
//        Long allCountMbr=0L;
//        Long allinvalidCountCoupon=0L;
//
//
//        try {
//
//            //查出每个任务的一个list集合 根据任务名称 任务创建时间 任务类型 品牌id
//
//            TaskAnalysisVo taskAnalysisVo = new TaskAnalysisVo();
//            taskAnalysisVo.setDate2(date2);
//            taskAnalysisVo.setDate1(date1);
//            taskAnalysisVo.setBrandId(stageUser.getBrandId());
//            taskAnalysisVo.setTaskName(taskName);
//            taskAnalysisVo.setTaskType(TaskTypeEnum.TASK_TYPE_SHARE.getCode());
//            List<DayTaskRecordVo> dayTaskRecordVoList = mktTaskRecordPOMapper.getAnalysisResult(taskAnalysisVo);
//
//            //遍历dayTaskrecordVo  添加核销优惠券、任务参与人数、分享次数、
//            for (DayTaskRecordVo dayTaskRecordVo:dayTaskRecordVoList){
//                Long taskId = dayTaskRecordVo.getTaskId();
//
//                //根据taskid从任务分享表中查出
//                MktTaskSharePOExample mktTaskSharePOExample = new MktTaskSharePOExample();
//                mktTaskSharePOExample.createCriteria().andValidEqualTo(true).andMktTaskIdEqualTo(taskId);
//
//                List<MktTaskSharePO> mktTaskSharePOList = mktTaskSharePOMapper.selectByExample(mktTaskSharePOExample);
//                MktTaskSharePO mktTaskSharePO = mktTaskSharePOList.get(0);
//
//                //某个任务的参与人数
//                Long countPartMbr = mktTaskRecordPOMapper.countPartMbr(mktTaskSharePO);
//
//                //某个任务的参与人次
//
//               /* MktTaskRecordPOExample mktTaskRecordPOExample = new MktTaskRecordPOExample();
//                mktTaskRecordPOExample.createCriteria().andTaskIdEqualTo(taskId).andValidEqualTo(true);
//               Long countPartMbr= mktTaskRecordPOMapper.countByExample(mktTaskRecordPOExample);*/
//                dayTaskRecordVo.setOneTaskCountMbr(countPartMbr);
//
//                //某个任务的完成人数
//                Long oneTaskCountMbr = dayTaskRecordVo.getOneTaskCountMbr();
//                allCountMbr = allCountMbr+oneTaskCountMbr;
//
//                //根据taskid查出记录条数 即为该任务分享次数
//
//                MktTaskRecordPOExample mktTaskRecordPOExample1 = new MktTaskRecordPOExample();
//                mktTaskRecordPOExample1.createCriteria().andTaskIdEqualTo(taskId).andValidEqualTo(true);
//                Long oneTaskCountShareTimes = mktTaskRecordPOMapper.countByExample(mktTaskRecordPOExample1);
//                dayTaskRecordVo.setOneTaskShareTimes(oneTaskCountShareTimes);
//
//
//                //核销的优惠券
//
//                CouponEntityVO couponEntityVO = new CouponEntityVO();
//                couponEntityVO.setSendBusinessId(taskId);
//                couponEntityVO.setSendType("65");
//                couponEntityVO.setSysBrandId(stageUser.getBrandId());
//                ResponseData<CouponFindCouponCountResponseVO> responseVOResponseData = couponQueryService.findCouponCountByDate(couponEntityVO);
//                CouponFindCouponCountResponseVO data = responseVOResponseData.getData();
//                Long couponUsedSum = data.getCouponUsedSum();
//                dayTaskRecordVo.setOneTaskInvalidCountCoupon(couponUsedSum);
//
//                allinvalidCountCoupon=allinvalidCountCoupon+couponUsedSum;
//
//                //某个任务的发行优惠券 todo  到底是自己的还是调用？
//               /* Long oneTaskCountCoupon = dayTaskRecordVo.getOneTaskCountCoupon();
//                allCountCoupon = allCountCoupon+oneTaskCountCoupon;*/
//                Long oneTaskCountCoupon =data.getCouponSum();
//                allCountCoupon = allCountCoupon+oneTaskCountCoupon;
//                //赠送的积分数
//
//                Long oneTaskCountPoints = dayTaskRecordVo.getOneTaskPoints();
//                allPoints = allPoints+oneTaskCountPoints;
//
//            }
//
//            PageHelper.startPage(pageForm.getPageNum(),pageForm.getPageSize());
//            PageInfo<DayTaskRecordVo> pageInfo = new PageInfo<>(dayTaskRecordVoList);
//
//            taskRecordVO.setAllCountMbr(allCountMbr);
//            taskRecordVO.setAllinvalidCountCoupon(allinvalidCountCoupon);
//            taskRecordVO.setAllCountCoupon(allCountCoupon);
//            taskRecordVO.setAllPoints(allPoints);
//            taskRecordVO.setDayTaskRecordVoList(pageInfo);
//            responseData.setData(taskRecordVO);
//            responseData.setMessage(SysResponseEnum.SUCCESS.getMessage());
//            responseData.setCode(SysResponseEnum.SUCCESS.getCode());
//        }catch(Exception e){
//            e.printStackTrace();
//            responseData.setMessage(SysResponseEnum.FAILED.getMessage());
//            responseData.setCode(SysResponseEnum.FAILED.getCode());
//            return responseData;
//        }
//        return responseData;
//    }
}