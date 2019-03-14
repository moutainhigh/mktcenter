package com.bizvane.mktcenterservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.bizvane.couponfacade.interfaces.CouponQueryServiceFeign;
import com.bizvane.couponfacade.models.vo.CouponFindCouponCountResponseVO;
import com.bizvane.members.facade.models.MemberInfoModel;
import com.bizvane.members.facade.vo.ExtendPropertyVO;
import com.bizvane.mktcenterfacade.interfaces.*;
import com.bizvane.mktcenterfacade.models.po.*;
import com.bizvane.mktcenterfacade.models.vo.*;

import com.bizvane.mktcenterfacade.models.bo.TaskAwardBO;
import com.bizvane.mktcenterfacade.models.bo.TaskBO;
import com.bizvane.mktcenterfacade.models.bo.TaskDetailBO;


import com.bizvane.mktcenterservice.common.constants.SystemConstants;
import com.bizvane.mktcenterservice.common.constants.TaskConstants;
import com.bizvane.mktcenterservice.common.enums.CheckStatusEnum;
import com.bizvane.mktcenterservice.common.enums.TaskStatusEnum;
import com.bizvane.mktcenterservice.common.utils.CodeUtil;
import com.bizvane.mktcenterservice.common.utils.TaskParamCheckUtil;
import com.bizvane.mktcenterservice.mappers.MktMessagePOMapper;
import com.bizvane.mktcenterservice.mappers.MktTaskPOMapper;
import com.bizvane.mktcenterservice.mappers.MktTaskProfilePOMapper;
import com.bizvane.utils.enumutils.SysResponseEnum;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author gengxiaoyu
 * @date on 2018/7/16 14:13
 * @description 完善资料任务
 * @Copyright (c) 2018 上海商帆信息科技有限公司-版权所有
 */
@Service
@Slf4j
public class TaskProfileServiceImpl implements TaskProfileService {
    @Autowired
    private MktTaskPOMapper mktTaskPOMapper;
    @Autowired
    private MktMessagePOMapper mktMessagePOMapper;
    @Autowired
    private MktTaskProfilePOMapper mktTaskProfilePOMapper;
    @Autowired
    protected TaskService taskService;
    @Autowired
    private TaskCouponService taskCouponService;
    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private TaskRecordService taskRecordService;
    @Autowired
    private CouponQueryServiceFeign couponQueryService;

    /**
     * 新建完善资料任务
     * @param bo
     * @param stageUser
     * @return
     */
    @Override
    @Transactional
    public ResponseData<Integer> addTask(TaskBO bo, SysAccountPO stageUser) throws ParseException {
        //参数的检验
        ResponseData responseData = TaskParamCheckUtil.checkParam(bo);
        if(responseData.getCode()>0){
            return responseData;
        }
        TaskVO taskVO = bo.getTaskVO();
        MktTaskPOWithBLOBs mktTaskPOWithBLOBs = new MktTaskPOWithBLOBs();
        BeanUtils.copyProperties(taskVO, mktTaskPOWithBLOBs);
        mktTaskPOWithBLOBs.setTaskCode(CodeUtil.getTaskCode());
        //调用中台的审核配置,判断是否需要审核  1=需要审核   0=不需要
        Integer stagecheckStatus = taskService.getCenterStageCheckStage(mktTaskPOWithBLOBs);
        log.info("--完善资料任务从中台获取审核状态--"+stagecheckStatus);
        //设置相应的任务状态和任务状态
        mktTaskPOWithBLOBs = this.isOrNoCheckState(mktTaskPOWithBLOBs,stagecheckStatus);
        Long mktTaskId = taskService.addTask(mktTaskPOWithBLOBs, stageUser);
        //将需要审核的任务添加到审核中心
        if(TaskConstants.FIRST.equals(stagecheckStatus)){
            taskService.addCheckData(mktTaskPOWithBLOBs);
        }
        MktTaskProfilePOWithBLOBs mktTaskProfilePO = new MktTaskProfilePOWithBLOBs();
        BeanUtils.copyProperties(taskVO, mktTaskProfilePO);
        mktTaskProfilePO.setMktTaskId(mktTaskId);
        this.insertProfileTask(mktTaskProfilePO,stageUser);

        List<MktCouponPO> mktCouponPOList = bo.getMktCouponPOList();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mktCouponPOList)) {
            mktCouponPOList.stream().forEach(param -> {
                param.setBizId(mktTaskId);
                param.setBizType(TaskConstants.TASK_TYPE);
                taskCouponService.addTaskCoupon(param, stageUser);
            });
        }
        List<MktMessagePO> mktmessagePOList = bo.getMessagePOList();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mktmessagePOList)) {
            mktmessagePOList.stream().forEach(param -> {
                        param.setBizId(mktTaskId);
                        param.setBizType(TaskConstants.TASK_TYPE);
                        taskMessageService.addTaskMessage(param, stageUser);
                    }
            );
        }

        //处理任务
        taskService.doProfileTask(mktTaskPOWithBLOBs,mktmessagePOList,stageUser);

        responseData.setCode(SystemConstants.SUCCESS_CODE);
        responseData.setMessage(SystemConstants.SUCCESS_MESSAGE);
        return responseData;
    }

    /**
     * 完善任务的审核状态和执行状态的确认(完善资料任务使用)
     * @param po
     * @return
     * @throws ParseException
     */
    public MktTaskPOWithBLOBs isOrNoCheckState(MktTaskPOWithBLOBs po,Integer stagecheckStatus) throws ParseException {
        // stagecheckStatus判断是否需要审核  1=需要审核   0=不需要
        if (TaskConstants.FIRST.equals(stagecheckStatus)) {
            //待审核=1
            po.setCheckStatus(CheckStatusEnum.CHECK_STATUS_PENDING.getCode());
            //待执行=1
            po.setTaskStatus(TaskStatusEnum.TASK_STATUS_PENDING.getCode());
        } else {
            //已审核=3
            po.setCheckStatus(CheckStatusEnum.CHECK_STATUS_APPROVED.getCode());
            //执行中=2
            po.setTaskStatus(TaskStatusEnum.TASK_STATUS_EXECUTING.getCode());
            po.setStartTime(new Date());
        }
        return po;
    }


    /**
     * 新增完善资料的单表信息
     */
    public  ResponseData<Integer> insertProfileTask(MktTaskProfilePOWithBLOBs po,SysAccountPO stageUser){
        ResponseData<Integer> result = new ResponseData<Integer>(SysResponseEnum.FAILED.getCode(),SysResponseEnum.FAILED.getMessage(),null);
        po.setSysCompanyId(stageUser.getSysCompanyId());
        po.setCreateUserId(stageUser.getSysAccountId());
        po.setCreateUserName(stageUser.getName());
        po.setCreateDate(new Date());
        int i = mktTaskProfilePOMapper.insertSelective(po);
        MktTaskProfilePOExample mktTaskProfilePOExample = new MktTaskProfilePOExample();
        if (i>0){
            result.setCode(SysResponseEnum.SUCCESS.getCode());
            result.setMessage(SysResponseEnum.SUCCESS.getMessage());
        }
        return result;
    }
    /**
     * 审核完善资料任务
     */
    @Override
    public ResponseData<Integer> checkTaskProfileById(CheckTaskVO vo,SysAccountPO sysAccountPO) throws ParseException {
        log.info("checkTaskProfileById修改中台审核配置-----"+JSON.toJSONString(vo));
        ResponseData<Integer> responseData = new ResponseData<Integer>();
        Long mktTaskId = vo.getBusinessId();
        Integer businessType = vo.getBusinessType();
        Integer checkStatus = vo.getCheckStatus();
        String remark = vo.getRemark();
        Long sysCheckId = vo.getSysCheckId();
        String functionCode = vo.getFunctionCode();
        MktTaskPOWithBLOBs mktTaskPOWithBLOBs = new MktTaskPOWithBLOBs();
        if (TaskConstants.THREE.equals(checkStatus)) {
            mktTaskPOWithBLOBs.setTaskStatus(TaskStatusEnum.TASK_STATUS_EXECUTING.getCode());
            mktTaskPOWithBLOBs.setStartTime(new Date());
        }else{
            // 已驳回   待执行
            mktTaskPOWithBLOBs.setTaskStatus(TaskStatusEnum.TASK_STATUS_PENDING.getCode());
        }
        mktTaskPOWithBLOBs.setMktTaskId(mktTaskId);
        mktTaskPOWithBLOBs.setCheckStatus(checkStatus);
        mktTaskPOWithBLOBs.setRemark(remark);
        mktTaskPOWithBLOBs.setCreateDate(new Date());
        mktTaskPOWithBLOBs.setModifiedDate(new Date());
        mktTaskPOWithBLOBs.setModifiedUserId(sysAccountPO.getSysAccountId());
        mktTaskPOWithBLOBs.setModifiedUserName(sysAccountPO.getName());
        int i = mktTaskPOMapper.updateByPrimaryKeySelective(mktTaskPOWithBLOBs);
        //修改中台审核表的数据
        taskService.updateCheckData(mktTaskId,checkStatus,functionCode,sysAccountPO);
        log.info("checkTaskProfileById审核通过后的任务状态---checkStatus--"+checkStatus+"--TaskStatus--"+mktTaskPOWithBLOBs.getTaskStatus());
        if (i > 0) {
            //3=已审核
            if (TaskConstants.THREE.equals(checkStatus)) {
                MktMessagePOExample exampleMSG = new MktMessagePOExample();
                exampleMSG.createCriteria().andBizIdEqualTo(mktTaskId).andValidEqualTo(Boolean.TRUE);
                List<MktMessagePO> mktMessagePOS = mktMessagePOMapper.selectByExample(exampleMSG);
                //List<TaskDetailVO> taskDetails = taskService.getTaskDetailByTaskId(mktTaskId);
                MktTaskPOWithBLOBs mktTaskPOWithBLOBsData = mktTaskPOMapper.selectByPrimaryKey(mktTaskId);
                taskService.doProfileTask(mktTaskPOWithBLOBsData,mktMessagePOS,sysAccountPO);
            }
        }
        return responseData;
    }

    /**
     * 修改任务
     * @param bo
     * @param stageUser
     * @return
     */
    @Override
    @Transactional
    public ResponseData updateTask(TaskBO bo, SysAccountPO stageUser) {
        //0.参数的检验
        ResponseData responseData = TaskParamCheckUtil.checkParam(bo);
        //参数校验不通过
        if(responseData.getCode()>0){
            return responseData;
        }
        TaskVO taskVO = bo.getTaskVO();
        Long mktTaskId = taskVO.getMktTaskId();
        //1.任务主表修改
        MktTaskPOWithBLOBs mktTaskPOWithBLOBs = new MktTaskPOWithBLOBs();
        BeanUtils.copyProperties(taskVO, mktTaskPOWithBLOBs);
        taskService.updateTask(mktTaskPOWithBLOBs, stageUser);
        //3.任务完善资料表修改
        MktTaskProfilePOWithBLOBs mktTaskProfilePO = new MktTaskProfilePOWithBLOBs();
        BeanUtils.copyProperties(taskVO, mktTaskProfilePO);
        mktTaskProfilePO.setMktTaskId(mktTaskId);
        this.updateProfileTask(mktTaskProfilePO, stageUser);

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
        taskService.doProfileTask(mktTaskPOWithBLOBs,mktmessagePOList,stageUser);

        responseData.setCode(SystemConstants.SUCCESS_CODE);
        responseData.setMessage(SystemConstants.SUCCESS_MESSAGE);
        return responseData;

    }
    /**
     * 修改完善资料的单表信息
     */
    public  ResponseData<Integer> updateProfileTask(MktTaskProfilePOWithBLOBs po,SysAccountPO stageUser){
        ResponseData<Integer> result = new ResponseData<Integer>(SysResponseEnum.FAILED.getCode(),SysResponseEnum.FAILED.getMessage(),null);
        po.setModifiedUserId(stageUser.getSysAccountId());
        po.setModifiedUserName(stageUser.getName());
        po.setModifiedDate(new Date());
        int i = mktTaskProfilePOMapper.updateByPrimaryKeySelective(po);
        if (i>0){
            result.setCode(SysResponseEnum.SUCCESS.getCode());
            result.setMessage(SysResponseEnum.SUCCESS.getMessage());
        }
        return result;
    }
    /**
     * 查询完善资料任务列表
     */
    @Override
    public ResponseData<List<TaskDetailBO>>  getTaskProfileListApp(ProfileSuccessVO vo){
        ResponseData<List<TaskDetailBO>> responseData = new ResponseData<>();
        List<TaskDetailBO> taskProfileListApp = taskService.getTaskProfileListApp(vo);
        responseData.setData(taskProfileListApp);
        return responseData;

    }
    /**
     * 执行完善任务的奖励
     */
//    @Async
//    @Transactional
//    @Override
//    public  void   doAwardProfile(ProfileSuccessVO vo){
//        log.info("完善资料任务--参数---"+ JSON.toJSONString(vo));
//        Long mktTaskIdParam = vo.getMktTaskId();
//        //完善资料时间
//        Date profileDate = vo.getProfileDate();
//        //完善者的code
//        String memberCode = vo.getMemberCode();
//
//        MemberInfoModel memeberDetail = taskService.getCompanyMemeberDetail(memberCode);
//        log.info("完善资料任务--获取会员详情---"+ JSON.toJSONString(memeberDetail));
//        Long companyId = memeberDetail.getSysCompanyId();
//        Long brandId = memeberDetail.getBrandId();
//        String cardNo = memeberDetail.getCardNo();
//        Long serviceStoreId = memeberDetail.getServiceStoreId();
//        //符合条件的任务列表
//        List<TaskAwardBO> taskAwardList = taskService.getTaskProfileAwardList(mktTaskIdParam,companyId, brandId, null);
//        log.info("完善资料任务--任务列表---"+ JSON.toJSONString(taskAwardList));
//        if (CollectionUtils.isNotEmpty(taskAwardList)){
//            taskAwardList.stream().
//                    filter(obj->{
//                        Boolean isStoreLimit = obj.getStoreLimit();
//                        String  StoreLimitList=obj.getStoreLimitList();
//                        return !isStoreLimit || (serviceStoreId!=null) || (StringUtils.isNotBlank(StoreLimitList) &&  obj.getStoreLimitList().contains(String.valueOf(serviceStoreId)));}).
//                    forEach(obj->{
//                        log.info("完善资料任务--每个---"+ JSON.toJSONString(obj));
//                        MktTaskRecordVO recordVO = new MktTaskRecordVO();
//                        recordVO.setSysBrandId(brandId);
//                        recordVO.setTaskType(obj.getTaskType());
//                        recordVO.setTaskId(obj.getMktTaskId());
//                        recordVO.setMemberCode(memberCode);
//                        // 获取会员是否已经成功参与过某一活动
//                        Boolean isOrNoAward = taskRecordService.getIsOrNoAward(recordVO);
//                        log.info("完善资料任务--员是否已经成功参与---"+ isOrNoAward+"---"+JSON.toJSONString(obj));
//                        if (!isOrNoAward){
//                            MktTaskRecordPO recordPO = new MktTaskRecordPO();
//                            BeanUtils.copyProperties(recordVO,recordPO);
//                            recordPO.setParticipateDate(profileDate);
//                            recordPO.setRewarded(1);
//                            recordPO.setSysCompanyId(companyId);
//                            recordPO.setCreateDate(new Date());
//                            recordPO.setPoints(obj.getPoints());
//                            taskRecordService.addTaskRecord(recordPO);
//                            taskService.sendCouponAndPoint(memberCode,obj);
//                        }
//
//                    });
//        }
//
//    }
    @Async
    @Transactional
    @Override
    public  void   doAwardProfile(ProfileSuccessVO vo){
        log.info("完善资料任务--参数---"+ JSON.toJSONString(vo));
        Long mktTaskIdParam = vo.getMktTaskId();
        //完善资料时间
        Date profileDate = vo.getProfileDate();
        //完善者的code
        String memberCode = vo.getMemberCode();

        MemberInfoModel memeberDetail = taskService.getCompanyMemeberDetail(memberCode);
        log.info("完善资料任务--获取会员详情---"+ JSON.toJSONString(memeberDetail));
        Long companyId = memeberDetail.getSysCompanyId();
        Long brandId = memeberDetail.getBrandId();
        String cardNo = memeberDetail.getCardNo();
        Long serviceStoreId = memeberDetail.getServiceStoreId();
        //符合条件的任务列表
        List<TaskAwardBO> taskAwardList = taskService.getTaskProfileAwardList(mktTaskIdParam,companyId, brandId, null);
        log.info("完善资料任务--任务列表---"+ JSON.toJSONString(taskAwardList));
        if (CollectionUtils.isNotEmpty(taskAwardList)){
            taskAwardList.stream().
                    filter(obj->{
                        Boolean isStoreLimit = obj.getStoreLimit();
                        String  StoreLimitList=obj.getStoreLimitList();
                        return !isStoreLimit || (serviceStoreId!=null) || (StringUtils.isNotBlank(StoreLimitList) &&  obj.getStoreLimitList().contains(String.valueOf(serviceStoreId)));}).
                    forEach(obj->{
                        log.info("完善资料任务--每个---"+ JSON.toJSONString(obj));
                        MktTaskRecordVO recordVO = new MktTaskRecordVO();
                        recordVO.setSysBrandId(brandId);
                        recordVO.setTaskType(obj.getTaskType());
                        recordVO.setTaskId(obj.getMktTaskId());
                        recordVO.setMemberCode(memberCode);
                        // 获取会员是否已经成功参与过某一活动
                        Boolean isOrNoAward = taskRecordService.getIsOrNoAward(recordVO);
                        log.info("完善资料任务--员是否已经成功参与---"+ isOrNoAward+"---"+JSON.toJSONString(obj));
                        if (!isOrNoAward){
                            MktTaskRecordPO recordPO = new MktTaskRecordPO();
                            BeanUtils.copyProperties(recordVO,recordPO);
                            recordPO.setParticipateDate(profileDate);
                            recordPO.setRewarded(1);
                            recordPO.setSysCompanyId(companyId);
                            recordPO.setCreateDate(new Date());
                            recordPO.setPoints(obj.getPoints());
                            taskRecordService.addTaskRecord(recordPO);
                            taskService.sendCouponAndPoint(memberCode,obj);
                        }

                    });
        }

    }
    /**
     * 效果分析
     * @return
     */
    @Override
    @Transactional
    public ResponseData<TaskRecordVO> getTaskProfileRecordByTime(TaskAnalysisVo vo){
        ResponseData<TaskRecordVO> responseData = new ResponseData<TaskRecordVO>();
        //赠送总积分数
        Long allPoints =0L;
        //发行券总张数
        Long allCountCoupon=0L;
        //参与任务总人数
        Long allCountMbr=0L;
        //被核销优惠券总数
        Long allinvalidCountCoupon=0L;

        try {
            Long sysBrandId = vo.getBrandId();
            Integer taskType = vo.getTaskType();
            PageHelper.startPage(vo.getPageNumber(),vo.getPageSize());
            List<DayTaskRecordVo> prifleAnalysislists = mktTaskProfilePOMapper.getPrifleAnalysisResult(vo);
            PageInfo<DayTaskRecordVo> dayTaskRecordVoPage = new PageInfo<>(prifleAnalysislists);
            List<DayTaskRecordVo> analysislists = dayTaskRecordVoPage.getList();
            if (CollectionUtils.isNotEmpty(analysislists)){
                for (DayTaskRecordVo task: analysislists) {
                    String sendType = taskService.changeTaskType(taskType).getCouponTaskType();
                    //参与日期
                    String createDate = TaskConstants.DATE_FORMAT.format(task.getPartDate());
                    ResponseData<CouponFindCouponCountResponseVO> couponCount= couponQueryService.findCountBySendType(sendType,createDate,sysBrandId);
                    CouponFindCouponCountResponseVO data = couponCount.getData();
                    //任务的券总数量
                    Long couponSum = data.getCouponSum();
                    //任务的券核销总数量
                    Long couponUsedSum = data.getCouponUsedSum();

                    task.setOneTaskInvalidCountCoupon(couponUsedSum);
                    task.setDayCountCoupon(couponUsedSum);

                    allPoints=allPoints+task.getOneTaskPoints();
                    allCountCoupon= allCountCoupon+task.getOneTaskCountCoupon();
                    allCountMbr=allCountMbr+task.getDayCompleteCountMbr();
                    allinvalidCountCoupon = allinvalidCountCoupon+Long.valueOf(couponSum);
                }
            }
            TaskRecordVO taskRecordVO = new TaskRecordVO();
            taskRecordVO.setAllPoints(allPoints);
            taskRecordVO.setAllCountCoupon(allCountCoupon);
            taskRecordVO.setAllCountMbr(allCountMbr);
            taskRecordVO.setAllinvalidCountCoupon(allinvalidCountCoupon);
            taskRecordVO.setDayTaskRecordVoList(dayTaskRecordVoPage);

            responseData.setData(taskRecordVO);

        }catch (Exception e){
            e.printStackTrace();
            responseData.setMessage(SysResponseEnum.FAILED.getMessage());
            responseData.setCode(SysResponseEnum.FAILED.getCode());
            return responseData;
        }

        return responseData;
    }

    /**
     * 查询完善资料的字段
     * 微信小程序端是否可见,1=不可见,2=可见
     */
    @Override
    public  ResponseData<List<ExtendPropertyVO>> getMemberField(Long sysBrandId){
        ExtendPropertyVO extendPropertyVO = new ExtendPropertyVO();
        extendPropertyVO.setWxVisible("2");
        extendPropertyVO.setBrandId(sysBrandId);
//        return  wxAppletApiService.getBaseAndExtendPropertyList(extendPropertyVO);
        return null;
    }
}