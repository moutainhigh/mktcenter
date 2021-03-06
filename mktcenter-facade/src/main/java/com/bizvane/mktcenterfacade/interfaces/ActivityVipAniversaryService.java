package com.bizvane.mktcenterfacade.interfaces;

import com.bizvane.centerstageservice.models.po.SysCheckPo;
import com.bizvane.members.facade.models.MemberInfoModel;
import com.bizvane.mktcenterfacade.models.bo.ActivityBO;
import com.bizvane.mktcenterfacade.models.vo.PageForm;
import com.bizvane.mktcenterfacade.models.vo.ActivityVO;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;

/**
 * Created by pc on 2018/9/7.
 */
public interface ActivityVipAniversaryService {


    /**
     * 查询纪念日活动列表
     * @param vo
     * @return
     */
    public ResponseData<ActivityVO> getActivityVipAniversaryList(ActivityVO vo, PageForm pageForm, SysAccountPO stageUser);

    /**
     * 新增纪念日活动
     * @param bo
     * @return
     */
    public ResponseData<Integer> addActivityVipAniversary(ActivityBO bo, SysAccountPO stageUser);

    /**
     * 查询活动详情
     * @param
     * @return
     */
    public ResponseData<ActivityBO> selectActivityVipAniversaryById(String businessCode);

    /**
     * 修改活动
     * @return
     */
    public ResponseData<Integer> updateActivityAniversary(ActivityBO bo, SysAccountPO stageUser);


    /**
     * 纪念日活动定时发送奖励
     * @param activityBirthday
     * @param memberInfo
     */
    public void AniversaryReward(ActivityVO activityBirthday, MemberInfoModel memberInfo);
}
