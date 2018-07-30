package com.bizvane.mktcenterservice.interfaces;

import com.bizvane.mktcenterservice.models.bo.TaskBO;
import com.bizvane.mktcenterservice.models.po.MktTaskOrderPO;
import com.bizvane.mktcenterservice.models.vo.PageForm;
import com.bizvane.mktcenterservice.models.vo.TaskConsumeVO;
import com.bizvane.mktcenterservice.models.vo.TaskVO;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;

import java.util.List;

/**
 * @author chen.li
 * @date on 2018/7/16 14:06
 * @description
 * @Copyright (c) 2018 上海商帆信息科技有限公司-版权所有
 */
public interface TaskOrderService {
    /**
     * 查询任务
     * @return
     */
    public List<TaskVO> selectTask(TaskVO vo);

    /**
     * 新增任务
     * @param bo
     * @return
     */
    public ResponseData<Integer> addTask(TaskConsumeVO bo, SysAccountPO stageUser);

    /**
     * 修改任务
     * @param po
     * @param stageUser
     * @return
     */
    public ResponseData<Integer> updateTask(MktTaskOrderPO po,SysAccountPO stageUser);

    /**
     * 执行任务
     * @param
     * @return
     */
    public ResponseData<Integer> executeTask(TaskVO vo);

    /**
     * 修改消费任务
     * @return
     */
    public Integer updateOrderTask(MktTaskOrderPO po, SysAccountPO stageUser);


}
