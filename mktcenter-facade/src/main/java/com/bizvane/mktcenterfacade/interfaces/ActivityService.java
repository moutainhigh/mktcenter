package com.bizvane.mktcenterfacade.interfaces;

import com.bizvane.centerstageservice.models.po.SysCheckPo;
import com.bizvane.couponfacade.models.vo.CouponSendMemberListResponseVO;
import com.bizvane.couponfacade.models.vo.SendCouponSimpleRequestVO;
import com.bizvane.members.facade.models.MemberInfoModel;
import com.bizvane.members.facade.vo.WxChannelInfoVo;
import com.bizvane.messagefacade.models.vo.MemberMessageVO;
import com.bizvane.messagefacade.models.vo.SysSmsConfigVO;
import com.bizvane.mktcenterfacade.models.po.MktActivityPO;
import com.bizvane.mktcenterfacade.models.po.MktMessagePO;
import com.bizvane.mktcenterfacade.models.responsevo.StoreActivityResponseVO;
import com.bizvane.mktcenterfacade.models.vo.*;
import com.bizvane.mktcenterfacade.models.bo.ActivityAnalysisCountBO;
import com.bizvane.mktcenterfacade.models.bo.AwardBO;
import com.bizvane.mktcenterfacade.models.bo.CtivityAnalysisBO;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author chen.li
 * @date on 2018/7/6 19:49
 * @description
 *
 */
public interface ActivityService {

    /**
     * 禁用/停止活动
     * @param vo
     * @return
     */
    public ResponseData<Integer> updateActivityById(ActivityVO vo, SysAccountPO sysAccountPO);

    /**
     * 查询效果分析统计
     * @param bo
     * @return
     */
    public ResponseData<CtivityAnalysisBO> getActivityAnalysisCountpage(ActivityAnalysisCountBO bo, PageForm pageForm);

    public void sendMessage(List<MktMessagePO> messageVOList, MemberInfoModel memberInfo, ActivityVO activityVO);

    public void sendPoints(ActivitySmartVO vo, AwardBO awardBO, MemberInfoModel memberInfo);

    public void sendShort(MktMessagePO mktMessagePO, AwardBO awardBO, SysSmsConfigVO sysSmsConfigVO, MemberInfoModel memberInfo);

    public void sendWx(MktMessagePO mktMessagePO, AwardBO awardBO, MemberMessageVO memberMessageVO, MemberInfoModel memberInfo,ActivitySmartVO vo);

    public  void sendCoupon(ActivitySmartVO vo, AwardBO awardBO, SendCouponSimpleRequestVO sendCouponSimpleRequestVO, MemberInfoModel memberInfo);

    public void sendRegisterWx(List<MktMessagePO> messageVOList, WxChannelInfoVo wxChannelInfoVo,ActivityVO activityVO);

    public void sendDx(List<MktMessagePO> messageVOList, MemberInfoModel memberInfo);

    /**
     * 活动、任务效果分析“发行优惠券”添加会员明细弹框；
     * @return
     */
    ResponseData<PageInfo<CouponSendMemberListResponseVO>> findCouponSendResultActivity(Long id, Integer type, SysAccountPO stageUser,
                                                                                        PageForm pageForm,String name,String cardNo);
    /**
     * 前端调用店铺
     */
    public  List<Long>  getActivityWhiteStoreIds(WhiteStoreVO vo);

    /**
     * 扫描二维码进行判断
     * @param vo
     * @return
     */
    public  ResponseData<Integer> judgeMember(JudgeMemberVO vo);

    public ResponseData<PageInfo<StoreActivityResponseVO>> getActivityList(StoreActivityVO storeActivityVO);
}
