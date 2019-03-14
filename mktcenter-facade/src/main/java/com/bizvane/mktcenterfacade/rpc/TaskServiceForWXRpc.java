package com.bizvane.mktcenterfacade.rpc;

import com.bizvane.centercontrolservice.models.bo.AppletFunctionBO;
import com.bizvane.mktcenterfacade.models.bo.CouponIntegralExchangeBO;
import com.bizvane.mktcenterfacade.models.bo.TaskWXBO;
import com.bizvane.mktcenterfacade.models.bo.TaskWXDetailBO;
import com.bizvane.mktcenterfacade.models.po.MktConvertCouponRecordPO;
import com.bizvane.mktcenterfacade.models.vo.CouponIntegralExchangeVO;
import com.bizvane.mktcenterfacade.models.vo.CouponRecordVO;
import com.bizvane.mktcenterfacade.models.vo.TaskForWXVO;
import com.bizvane.utils.responseinfo.ResponseData;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Geng on 2018/8/9.
 */

@FeignClient(value = "${feign.client.mktcenter.name}",path = "${feign.client.mktcenter.path}/taskForWXRpc")
public interface TaskServiceForWXRpc {



    @RequestMapping("/getCompleteTask")
    @io.swagger.annotations.ApiModelProperty(value = "brandId,memberCode",name = "该会员已完成和未完成的任务列表", required = false,example = "")
    public ResponseData<PageInfo<TaskWXBO>> getCompleteTask(@RequestBody TaskForWXVO vo);

    @RequestMapping("/getTaskWXDetail")
    @io.swagger.annotations.ApiModelProperty(value = "taskId",name = "任务code", required = false,example = "")
    public  ResponseData<TaskWXDetailBO>  getTaskWXDetail(@RequestParam Long taskId);

    @RequestMapping("/getURLDetail")
    @io.swagger.annotations.ApiModelProperty(value = "TaskForWXVO",name = "获取url链接详情", required = false,example = "")
    public  ResponseData<AppletFunctionBO>  getURLDetail(@RequestBody TaskForWXVO vo);

    //积分兑换券
    //查询对换列表
    @RequestMapping("/getConvernCouponLists")
    public ResponseData<CouponIntegralExchangeBO> getConvernCouponLists(@RequestBody CouponRecordVO vo);
   //券详情和单价
    @RequestMapping("/getCouponAndPrice")
    public ResponseData<CouponIntegralExchangeVO> getCouponAndPrice(@RequestBody CouponRecordVO vo);
    //兑换
    @RequestMapping("/doConvernCoupon")
    public ResponseData<Integer> doConvernCoupon(@RequestBody CouponRecordVO vo);
    //查询已兑换
    @RequestMapping("/getConvernConpouByMember")
    public ResponseData<PageInfo<MktConvertCouponRecordPO>> getConvernConpouByMember(@RequestBody CouponRecordVO vo);
}