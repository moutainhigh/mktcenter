package com.bizvane.mktcenterservice.controllers;

import com.bizvane.centerstageservice.models.po.SysCheckPo;
import com.bizvane.couponfacade.models.vo.CouponSendMemberListResponseVO;
import com.bizvane.mktcenterfacade.interfaces.*;
import com.bizvane.mktcenterfacade.models.bo.ActivityAnalysisCountBO;
import com.bizvane.mktcenterfacade.models.bo.CtivityAnalysisBO;
import com.bizvane.mktcenterfacade.models.vo.ActivityVO;
import com.bizvane.mktcenterfacade.models.vo.PageForm;
import com.bizvane.mktcenterfacade.models.vo.WhiteStoreVO;
import com.bizvane.mktcenterservice.common.enums.ActivityTypeEnum;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import com.bizvane.utils.tokens.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chen.li
 * @date on 2018/7/6 15:12
 * @description 营销活动
 *
 */
@RestController
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityOrderService activityOrderService;
    @Autowired
    private ActivitySigninService activitySigninService;
    @Autowired
    private ActivityBirthdayService activityBirthdayService;

    @Autowired
    private ActivityManualService activityManualService;
    @Autowired
    private ActivityEvaluationService activityEvaluationService;
    @Autowired
    private ActivityVipAniversaryService activityVipAniversaryService;

    /**
     * 禁用/启用活动
     * @param vo
     * @return
     */
    @RequestMapping("stopActivityById")
    public ResponseData<Integer> stopActivityById(ActivityVO vo, HttpServletRequest request){
        //获取操作人信息
        SysAccountPO stageUser =new SysAccountPO();
//        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        //禁用活动
        ResponseData<Integer> integerResponseData = activityService.updateActivityById(vo, stageUser);
        return integerResponseData;
    }

    /**
     * 活动审核
     * @param
     * @param request
     * @return
     */
    @RequestMapping("checkActivityById")
    public ResponseData<Integer> checkActivityById(SysCheckPo po, HttpServletRequest request){
        ResponseData responseData = new ResponseData();
        //获取操作人信息
        SysAccountPO stageUser =new SysAccountPO();
//        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        //开卡活动审核
        if(po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_REGISGER.getCode()){
            responseData = activityService.checkActivityById(po, stageUser);
        }
        //会员生日活动
        if(po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_BIRTHDAY.getCode()){
            responseData = activityBirthdayService.checkActivityBirthday(po,stageUser);
        }
        //入会纪念日活动
        if(po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_ANNIVERSARY.getCode()){
            responseData = activityVipAniversaryService.checkActivityVipAniversary(po,stageUser);
        }
        //会员消费活动审核
        if(po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_ORDER.getCode()){
            responseData =activityOrderService.checkActivityOrder(po,stageUser);
        }
        //会员签到活动审核
        if(po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_SIGNIN.getCode()){
            responseData = activitySigninService.checkActivitySignin(po,stageUser);

        }
        //评价奖励活动审核
        if(po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_EVALUATION.getCode()){
            responseData = activityEvaluationService.checkActivityEvaluation(po,stageUser);

        }
        //扫码领券&手动领券审核
        if(po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_QRCODE.getCode()||po.getBusinessType()==ActivityTypeEnum.ACTIVITY_TYPE_MANUAL.getCode()){
            responseData = activityManualService.checkActivity(po,stageUser);

        }
        return responseData;
    }

    /**
     * 查询效果分析统计
     * @param bo
     * @param pageForm
     * @return
     */
    @RequestMapping("getActivityAnalysisCountpage")
    public ResponseData<CtivityAnalysisBO> getActivityAnalysisCountpage(ActivityAnalysisCountBO bo, PageForm pageForm){
        return activityService.getActivityAnalysisCountpage(bo,pageForm);
    }



    /**
     * 活动、任务效果分析“发行优惠券”添加会员明细弹框；
     * @return
     */
    @RequestMapping("findCouponResultMemberListActivity")
    public ResponseData<PageInfo<CouponSendMemberListResponseVO>> findCouponResultMemberListActivity(@RequestParam(required = false) Long id,
                                                                                             @RequestParam(required = false) Integer type,
                                                                                             @RequestParam(required = false) String name,
                                                                                             @RequestParam(required = false) String cardNo,
                                                                                             HttpServletRequest request, PageForm pageForm){
        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        return activityService.findCouponSendResultActivity(id,type,stageUser,pageForm,name,cardNo);
    }

    /**
     * 白名单店铺
     * @param vo
     * @return
     */
    @RequestMapping("getActivityWhiteStoreIds")
    public List<Long> getActivityWhiteStoreIds(WhiteStoreVO vo){
        return activityService.getActivityWhiteStoreIds(vo);
    }

}
