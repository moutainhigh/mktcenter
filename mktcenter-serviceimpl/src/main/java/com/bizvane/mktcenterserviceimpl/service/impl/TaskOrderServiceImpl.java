package com.bizvane.mktcenterserviceimpl.service.impl;

import com.bizvane.mktcenterservice.interfaces.TaskOrderService;
import com.bizvane.mktcenterservice.models.bo.TaskBO;
import com.bizvane.mktcenterservice.models.vo.ActivityVO;
import com.bizvane.mktcenterservice.models.vo.TaskVO;
import com.bizvane.mktcenterserviceimpl.mappers.MktTaskOrderPOMapper;
import com.bizvane.mktcenterserviceimpl.mappers.MktTaskProfilePOMapper;
import com.bizvane.utils.commonutils.PageForm;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chen.li
 * @date on 2018/7/16 14:13
 * @description
 * @Copyright (c) 2018 上海商帆信息科技有限公司-版权所有
 */
@Service
public class TaskOrderServiceImpl implements TaskOrderService {

    @Autowired
    private MktTaskOrderPOMapper mktTaskOrderPOMapper;

    /**
     * 获取任务列表
     * @param vo
     * @param pageForm
     * @return
     */
    @Override
    public ResponseData<TaskVO> getTaskList(TaskVO vo, PageForm pageForm) {
        ResponseData responseData = new ResponseData();
        PageHelper.startPage(pageForm.getPageNumber(),pageForm.getPageSize());
        List<TaskVO> activityRegisterList = mktTaskOrderPOMapper.getTaskList(vo);
        PageInfo<TaskVO> pageInfo = new PageInfo<>(activityRegisterList);
        responseData.setData(pageInfo);
        return responseData;
    }

    @Override
    public ResponseData<Integer> addTask(TaskBO bo, SysAccountPO stageUser) {
        return null;
    }

    @Override
    public ResponseData<Integer> executeTask(TaskVO vo) {
        return null;
    }

    @Override
    public ResponseData<Integer> updateTask(TaskBO bo, SysAccountPO stageUser) {
        return null;
    }

    @Override
    public ResponseData<List<TaskVO>> selectTaskById(Long mktTaskId) {
        return null;
    }
}
