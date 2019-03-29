package com.bizvane.mktcenterfacade.interfaces;

import com.bizvane.members.facade.vo.ExtendPropertyVO;
import com.bizvane.mktcenterfacade.models.bo.TaskBO;
import com.bizvane.mktcenterfacade.models.vo.TaskAnalysisVo;
import com.bizvane.mktcenterfacade.models.vo.TaskRecordVO;
import com.bizvane.mktcenterfacade.models.bo.TaskDetailBO;
import com.bizvane.mktcenterfacade.models.vo.CheckTaskVO;
import com.bizvane.mktcenterfacade.models.vo.ProfileSuccessVO;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;

import java.text.ParseException;
import java.util.List;

/**
 * @author chen.li
 * @date on 2018/7/16 14:06
 * @description
 *
 */
public interface TaskProfileService {

    /**
     * 添加完善资料任务
     * @param bo
     * @param stageUser
     * @return
     */
    public ResponseData<Integer> addTask(TaskBO bo, SysAccountPO stageUser) throws ParseException;

    /**
     * 审核完善资料任务
     */
    public ResponseData<Integer> checkTaskProfileById(CheckTaskVO vo,SysAccountPO sysAccountPO) throws ParseException;
    /**
     * 修改任务
     * @param bo
     * @param stageUser
     * @return
     */
    public ResponseData<Integer> updateTask(TaskBO bo,SysAccountPO stageUser);

    /**
     * 完善资料任务效果分析
     * @return
     */
    public ResponseData<TaskRecordVO> getTaskProfileRecordByTime(TaskAnalysisVo vo);

    /**
     * 执行完成任务的奖励
     * @param vo
     */
    public  void   doAwardProfile(ProfileSuccessVO vo);

    /**
     * 获取完善资料的字段code和名称
     * @param sysBrandId
     * @return
     */
    public  ResponseData<List<ExtendPropertyVO>> getMemberField(Long sysBrandId);


    public ResponseData<List<TaskDetailBO>>  getTaskProfileListApp(ProfileSuccessVO vo);

}
