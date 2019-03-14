package com.bizvane.mktcenterservice.controllers;

import com.bizvane.members.facade.service.api.ExtendPropertyApiService;
import com.bizvane.members.facade.service.api.MemberInfoApiService;
import com.bizvane.members.facade.service.api.MemberLevelApiService;
import com.bizvane.members.facade.vo.ExtendPropertyVO;
import com.bizvane.mktcenterfacade.interfaces.TaskProfileService;
import com.bizvane.mktcenterfacade.models.bo.TaskBO;
import com.bizvane.mktcenterfacade.models.vo.CheckTaskVO;
import com.bizvane.mktcenterfacade.models.vo.ProfileSuccessVO;
import com.bizvane.mktcenterfacade.models.vo.TaskAnalysisVo;

import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import com.bizvane.utils.tokens.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
/**
 * @author gengxiaoyu
 * @date on 2018/7/13 13:39
 * @description
 * @Copyright (c) 2018 上海商帆信息科技有限公司-版权所有
 */
@RestController
@RequestMapping("taskProfile")
public class TaskProfileController {
    @Autowired
    private TaskProfileService taskProfileService;
    @Autowired
    private MemberLevelApiService memberLevelApiService;
    @Autowired
    private MemberInfoApiService memberInfoApiService;
    @Autowired
    private ExtendPropertyApiService extendPropertyApiService;

    /**
     * 创建任务
     * @return
     */
    @RequestMapping("addTask")
    public ResponseData<Integer> addTask(TaskBO bo, HttpServletRequest request) throws ParseException {
        SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);
//        SysAccountPO sysAccountPo=new SysAccountPO();
//        sysAccountPo.setBrandId(96L);
//        sysAccountPo.setName("测试测试");
//        sysAccountPo.setSysAccountId(12867L);
//        sysAccountPo.setSysCompanyId(3841L);
        bo.getTaskVO().setTaskType(1);
        return  taskProfileService.addTask(bo, sysAccountPo);
    }
    /**
     * 审核完善资料任务
     */
    @RequestMapping("checkTaskProfile")
    public ResponseData<Integer> checkTaskById(CheckTaskVO vo, HttpServletRequest request) throws ParseException {
        //获取操作人信息
        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        //审核任务
        ResponseData<Integer> integerResponseData = taskProfileService.checkTaskProfileById(vo,stageUser);
        return integerResponseData;
    }
    /**
     * 修改任务
     * @param bo
     * @param request
     * @return
     */
    @RequestMapping("updateTask")
    public ResponseData<Integer> updateTask(TaskBO bo, HttpServletRequest request){
        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        return taskProfileService.updateTask(bo,stageUser);
    }

    /**
     * 效果分析
     * @return  TaskAnalysisVo vo
     */
    @RequestMapping("getTaskProfileRecordByTime")
    public ResponseData getTaskProfileRecordByTime(TaskAnalysisVo vo, HttpServletRequest request)throws Exception{
        SysAccountPO sysAccountPo = TokenUtils.getStageUser(request);
        vo.setBrandId(sysAccountPo.getBrandId());
        return taskProfileService.getTaskProfileRecordByTime(vo);
    }

    /**
     * 执行完善资料的任务奖励
     */
    @RequestMapping("doAwardProfile")
    public  void   doAwardProfile(ProfileSuccessVO vo){
        taskProfileService.doAwardProfile(vo);
    }

    /**
     * 获取品牌下 完善资料的字段code和名称
     * @param sysBrandId
     * @return
     */
    @RequestMapping("getExtendProperty")
    public  ResponseData<List<ExtendPropertyVO>> getMemberField(Long sysBrandId){
        return taskProfileService.getMemberField(sysBrandId);
    }
}
