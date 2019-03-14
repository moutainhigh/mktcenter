package com.bizvane.mktcenterservice.controllers.rpc;

import com.bizvane.mktcenterfacade.interfaces.ActivityPriceService;
import com.bizvane.mktcenterfacade.interfaces.ActivityPrizeServiceWX;
import com.bizvane.mktcenterfacade.models.po.MktActivityPrizePO;
import com.bizvane.mktcenterfacade.models.po.MktActivityPrizeRecordPO;
import com.bizvane.mktcenterfacade.models.vo.ActivityPrizeBO;
import com.bizvane.utils.responseinfo.ResponseData;
import com.bizvane.utils.tokens.SysAccountPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by agan on 2018/12/19.
 */
@RestController
@RequestMapping("activityPrizeRpc")
public class ActivityPrizeRpcController {

    @Autowired
    private ActivityPrizeServiceWX activityPrizeServiceWX;
    @Autowired
    private ActivityPriceService activityPriceService;
    /**
     * 小程序获取中奖纪录 和中奖纪录轮播
     * @param po
     * @return
     */
    @RequestMapping("getPrizeRecordListRpc")
    ResponseData<List<MktActivityPrizeRecordPO>> getPrizeRecordListRpc(@RequestBody MktActivityPrizeRecordPO po){
        return activityPrizeServiceWX.getPrizeRecordList(po);
    }

    /**
     * 获取抽奖活动规则
     * @param
     * @return
     */
    @RequestMapping("selectPrizeList")
    ResponseData<ActivityPrizeBO> selectPrizeList(@RequestParam("activePriceCode") String activePriceCode){
        //获取操作人信息
        SysAccountPO stageUser =new SysAccountPO();
//        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        return activityPriceService.selectActivityPrice(activePriceCode);
    }

    /**
     * 执行抽奖活动
     * @param activePriceCode
     * @return
     */
    @RequestMapping("executeActivityPrize")
    ResponseData<MktActivityPrizePO> executeActivityPrize(@RequestParam("activePriceCode") String activePriceCode,@RequestParam("memberCode") String memberCode){
        return activityPrizeServiceWX.executeActivityPrize(activePriceCode,memberCode);
    }
}