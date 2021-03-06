package com.bizvane.mktcenterfacade.rpc;

import com.bizvane.mktcenterfacade.models.po.MktActivityPOWithBLOBs;
import com.bizvane.mktcenterfacade.models.po.MktActivityPrizeRecordPO;
import com.bizvane.mktcenterfacade.models.vo.ProbabilityVO;
import com.bizvane.utils.responseinfo.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.List;

/**
 * @Author: lijunwei
 * @Time: 2019/2/26 11:27
 */
@FeignClient(value = "${feign.client.mktcenter.name}",path = "${feign.client.mktcenter.path}/activityEGRpc")
public interface ActivityEGServiceRpc {
    @RequestMapping("getMktActivityPOWithBLOBs")
    public ResponseData<MktActivityPOWithBLOBs> getMktActivityPOWithBLOBs(@RequestBody ProbabilityVO vo);

    @RequestMapping("doEggFrenzy")
    public ResponseData<String> doEggFrenzy(@RequestBody ProbabilityVO vo) throws ParseException;

    @RequestMapping("getEGPrizeRecordList")
    public ResponseData<List<MktActivityPrizeRecordPO>> getEGPrizeRecordList(@RequestBody MktActivityPrizeRecordPO po);

    @RequestMapping("residueMemberNumber")
    public ResponseData<Integer>  residueMemberNumber(@RequestBody ProbabilityVO vo) throws ParseException;

    @RequestMapping("addMemberNumber")
    public ResponseData<Integer> addMemberNumber(@RequestBody ProbabilityVO vo) throws ParseException;

    @RequestMapping("getActivityId")
    public ResponseData<MktActivityPOWithBLOBs>  getActivityId(@RequestBody ProbabilityVO vo);
}
