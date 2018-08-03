package com.bizvane.mktcenterserviceimpl.service.impl;

import com.bizvane.centerstageservice.models.po.SysCheckConfigPo;
import com.bizvane.members.facade.models.MemberInfoModel;
import com.bizvane.mktcenterservice.interfaces.TaskCouponService;
import com.bizvane.mktcenterservice.interfaces.TaskInviteService;
import com.bizvane.mktcenterservice.interfaces.TaskMessageService;
import com.bizvane.mktcenterservice.interfaces.TaskService;
import com.bizvane.mktcenterservice.models.bo.AwardBO;
import com.bizvane.mktcenterservice.models.bo.TaskBO;
import com.bizvane.mktcenterservice.models.bo.TaskDetailBO;
import com.bizvane.mktcenterservice.models.po.*;
import com.bizvane.mktcenterservice.models.vo.PageForm;
import com.bizvane.mktcenterservice.models.vo.TaskDetailVO;
import com.bizvane.mktcenterservice.models.vo.TaskVO;
import com.bizvane.mktcenterserviceimpl.common.award.Award;
import com.bizvane.mktcenterserviceimpl.common.constants.SystemConstants;
import com.bizvane.mktcenterserviceimpl.common.constants.TaskConstants;
import com.bizvane.mktcenterserviceimpl.common.enums.CheckStatusEnum;
import com.bizvane.mktcenterserviceimpl.common.enums.MktSmartTypeEnum;
import com.bizvane.mktcenterserviceimpl.common.enums.TaskStatusEnum;
import com.bizvane.mktcenterserviceimpl.common.utils.CodeUtil;
import com.bizvane.mktcenterserviceimpl.common.utils.TaskParamCheckUtil;
import com.bizvane.mktcenterserviceimpl.common.utils.TimeUtils;
import com.bizvane.mktcenterserviceimpl.mappers.MktTaskInvitePOMapper;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author chen.li
 * @date on 2018/7/24 10:54
 * @description
 * @Copyright (c) 2018 上海商帆信息科技有限公司-版权所有
 */
@Service
public class TaskInviteServiceImpl implements TaskInviteService {
    @Autowired
    protected TaskService taskService;

    @Autowired
    private TaskCouponService taskCouponService;

    @Autowired
    private TaskMessageService taskMessageService;

    @Autowired
    private MktTaskInvitePOMapper mktTaskInvitePOMapper;

    @Autowired
    private Award award;

    /**
     * 审核任务:任务id   任务状态
     * 审核状态：1未审核，2审核中，3已审核，4已驳回',
     */
    @Override
    public Integer checkInviteTask(TaskVO vo) {
        MktTaskInvitePO po = new MktTaskInvitePO();
        BeanUtils.copyProperties(vo, po);
        return mktTaskInvitePOMapper.updateByPrimaryKeySelective(po);

    }

    /**
     * 根据任务Id查询任务详情
     */
    @Override
    public List<TaskDetailBO> getInviteTaskDetails(Long mktTaskId) {
        return mktTaskInvitePOMapper.getInviteTaskDetails(mktTaskId);
    }
    /**
     * 查询任务列表
     *
     * @return
     */
    @Override
    public List<TaskVO> selectTask(TaskVO vo) {
        return mktTaskInvitePOMapper.getTaskList(vo);
    }

    /**
     * 创建任务
     *
     * @param vo
     * @param stageUser
     * @return
     */
    @Transactional
    @Override
    public ResponseData<Integer> addTask(TaskDetailVO vo, SysAccountPO stageUser) throws ParseException {
        //0.参数的检验
        ResponseData responseData = TaskParamCheckUtil.checkParam(vo);
        if (responseData.getCode() < 0) {
            return responseData;
        }
        vo.setValid(Boolean.TRUE);
        vo.setCreateDate(TimeUtils.getNowTime());
        vo.setCreateUserId(stageUser.getSysAccountId());
        vo.setCreateUserName(stageUser.getName());

        //1.生成任务编号
        String taskCode = CodeUtil.getTaskCode();
        //2.任务主表新增
        MktTaskPOWithBLOBs mktTaskPOWithBLOBs = new MktTaskPOWithBLOBs();
        BeanUtils.copyProperties(vo, mktTaskPOWithBLOBs);
        mktTaskPOWithBLOBs.setTaskCode(taskCode);
        Long mktTaskId = taskService.addTask(mktTaskPOWithBLOBs, stageUser);

        //3.任务消费表新增
        MktTaskInvitePO mktTaskInvitePO = new MktTaskInvitePO();
        BeanUtils.copyProperties(vo, mktTaskInvitePO);
        mktTaskInvitePO.setMktTaskId(mktTaskId);
        this.insertInviteTask(mktTaskInvitePO, stageUser);

        //4.新增奖励新增  biz_type 活动类型  1=活动
        List<MktCouponPO> mktCouponPOList = vo.getMktCouponPOList();
        if (CollectionUtils.isNotEmpty(mktCouponPOList)) {
            mktCouponPOList.stream().forEach(param -> {
                param.setBizId(mktTaskId);
                param.setBizType(TaskConstants.TASK_TYPE);
                taskCouponService.addTaskCoupon(param, stageUser);
            });
        }
        //5.新增消息新增
        List<MktMessagePO> mktmessagePOList = vo.getMktmessagePOList();
        if (CollectionUtils.isNotEmpty(mktmessagePOList)) {
            mktmessagePOList.stream().forEach(param -> {
                        param.setBizId(mktTaskId);
                        param.setBizType(TaskConstants.TASK_TYPE);
                        taskMessageService.addTaskMessage(param, stageUser);
                    }
            );
        }

        //6.处理任务
        this.doOrderTask(vo);

        responseData.setCode(SystemConstants.SUCCESS_CODE);
        responseData.setMessage(SystemConstants.SUCCESS_MESSAGE);
        return responseData;
    }
    /**
     * 判断是否滞后执行
     */
    public MktTaskPOWithBLOBs isOrNoCheckState(MktTaskPOWithBLOBs po) throws ParseException {
        //1.判断是否需要审核  1:需要审核 0:不需要
        SysCheckConfigPo sysCheckConfigPo = new SysCheckConfigPo();
        sysCheckConfigPo.setSysBrandId(po.getSysBrandId());
        Integer checkStatus = taskService.getCheckStatus(sysCheckConfigPo);
        //判断时间是否滞后   2=滞后执行    3和1=立即执行
        Integer ImmediatelyRunStatus = TimeUtils.IsImmediatelyRun(po.getStartTime());
        if (TaskConstants.ZERO.equals(checkStatus)) {
            //待审核=1
            po.setCheckStatus(CheckStatusEnum.CHECK_STATUS_PENDING.getCode());
            //待执行=1
            po.setCheckStatus(TaskStatusEnum.TASK_STATUS_PENDING.getCode());
        } else {
            //已审核=3
            po.setCheckStatus(TaskConstants.THREE);
            if (TaskConstants.THREE.equals(ImmediatelyRunStatus)) {
                //待执行
                po.setCheckStatus(TaskStatusEnum.TASK_STATUS_PENDING.getCode());
            } else {
                //执行中
                po.setCheckStatus(TaskStatusEnum.TASK_STATUS_EXECUTING.getCode());
            }
        }
        return po;
    }
    /**
     * 判断时间是否滞后,已经是否立即执行,还是创建job执行
     */
    public void doOrderTask(TaskDetailVO vo) {
        //公司id
        Long sysCompanyId = vo.getSysCompanyId();
        //审核状态
        Integer checkStatus = vo.getCheckStatus();
        //执行状态
        Integer taskStatus = vo.getTaskStatus();
        //公司下的会员
        List<MemberInfoModel> companyMemebers = taskService.getCompanyMemebers(sysCompanyId);

        //已审核   执行中  执行时间小于当前时间 或等于当前时间
        if (TaskConstants.THREE.equals(checkStatus) && TaskConstants.SECOND.equals(taskStatus)) {
            //判断奖励
            List<MktCouponPO> mktCouponPOList = vo.getMktCouponPOList();
            if (CollectionUtils.isNotEmpty(mktCouponPOList) && CollectionUtils.isNotEmpty(companyMemebers)) {

            }
            //判断是否需要发送消息和短信
            List<MktMessagePO> mktmessagePOList = vo.getMktmessagePOList();
            if (CollectionUtils.isNotEmpty(mktmessagePOList)) {
                mktmessagePOList.stream().forEach(
                        message -> {
                            String msgType = message.getMsgType();
                            companyMemebers.stream().forEach(
                                    obj -> {
                                        AwardBO bo = new AwardBO();
                                        //会员code
                                        bo.setMemberCode(obj.getMemberCode());
                                        //券id
                                        //bo.setCouponDefinitionId();
                                        //发送类型
                                        //bo.setSendType()
                                        //任务务id
                                        bo.setSendBussienId(message.getBizId());
                                        bo.setChangeIntegral(vo.getPoints());
                                        bo.setMemberName(obj.getName());
                                        bo.setCardNo(obj.getCardNo());
                                        //1模板消息，2短信'
                                        if (TaskConstants.FIRST.equals(msgType)){
                                            //1券，2积分，3短信，4模板消息
                                            bo.setMktSmartType(MktSmartTypeEnum.SMART_TYPE_SMS.getCode());
                                        }else{
                                            bo.setMktSmartType(MktSmartTypeEnum.SMART_TYPE_WXMESSAGE.getCode());
                                        }

                                        award.execute(bo);
                                    }
                            );

                        }
                );
             }
        }

        //已审核   待执行,创建job
        if (TaskConstants.THREE.equals(checkStatus) && TaskConstants.FIRST.equals(taskStatus)) {
            //判断是否需要发送消息和短信
        }

    }
    /**
     * 更新消费任务
     *
     * @param stageUser
     * @return
     */
    @Transactional
    @Override
    public ResponseData updateInviteTask(TaskDetailVO vo, SysAccountPO stageUser) {
        //        mktTaskOrderPOMapper.updateByPrimaryKeySelective(po);
        //0.参数的检验
        ResponseData responseData = TaskParamCheckUtil.checkParam(vo);
        if (responseData.getCode() < 0) {
            return responseData;
        }
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
        MktTaskInvitePO mktTaskInvitePO = new MktTaskInvitePO();
        BeanUtils.copyProperties(vo, mktTaskInvitePO);
        this.modifieInviteTask(mktTaskInvitePO, stageUser);

        //4.奖励修改 biz_type 活动类型  1=活动
        taskCouponService.deleteTaskCoupon(mktTaskId, stageUser);
        List<MktCouponPO> mktCouponPOList = vo.getMktCouponPOList();
        if (CollectionUtils.isNotEmpty(mktCouponPOList)) {
            mktCouponPOList.stream().forEach(param -> {
                param.setBizId(mktTaskId);
                param.setBizType(TaskConstants.TASK_TYPE);
                taskCouponService.addTaskCoupon(param, stageUser);
            });
        }
        //5.修改消息
        taskMessageService.deleteTaskMessage(mktTaskId,stageUser);
        List<MktMessagePO> mktmessagePOList = vo.getMktmessagePOList();
        if (CollectionUtils.isNotEmpty(mktmessagePOList)) {
            mktmessagePOList.stream().forEach(param -> {
                        param.setBizId(mktTaskId);
                        param.setBizType(TaskConstants.TASK_TYPE);
                        taskMessageService.addTaskMessage(param, stageUser);
                    }
            );
        }

        responseData.setCode(SystemConstants.SUCCESS_CODE);
        responseData.setMessage(SystemConstants.SUCCESS_MESSAGE);
        return responseData;


    }

    /**
     * 新增消费任务
     */
    @Override
    public Integer insertInviteTask(MktTaskInvitePO po, SysAccountPO stageUser) {
        return mktTaskInvitePOMapper.insertSelective(po);
    }

    /**
     * 修改
     *
     * @param stageUser
     * @return
     */
    @Override
    public Integer modifieInviteTask(MktTaskInvitePO po, SysAccountPO stageUser) {
        MktTaskInvitePOExample example = new MktTaskInvitePOExample();
        example.createCriteria().andMktTaskIdEqualTo(po.getMktTaskId()).andValidEqualTo(Boolean.TRUE);

        return mktTaskInvitePOMapper.updateByExample(po, example);
    }
}
