package com.bizvane.mktcenterfacade.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.bizvane.mktcenterfacade.models.po.MktActivityPOWithBLOBs;
import com.bizvane.mktcenterfacade.models.po.MktActivityPrizeRecordPO;
import com.bizvane.mktcenterfacade.models.vo.*;
import com.bizvane.utils.responseinfo.ResponseData;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * @Author: lijunwei
 * @Time: 2019/2/25 10:17
 */
public interface ActivityGoldenEggsService {
    public ResponseData<JSONObject> addActivityGE(ActivityPriceBO bo, HttpServletRequest request) throws ParseException;
    public ResponseData<ActivityPriceBO> selectActivityGEById(ProbabilityVO vo);
    public ResponseData<ActivityPrizeBO> selectActivityGEByCode(String activePriceCode);
    public ResponseData<PageInfo<MktActivityPOWithBLOBs>> selectActivityEGLists(ActivityPriceParamVO vo, HttpServletRequest request);
    public ResponseData<JSONObject> updateActivityGE(ActivityPriceBO bo, HttpServletRequest request) throws ParseException;
    public ResponseData<Integer> stopActivityEG(MktActivityPOWithBLOBs po, HttpServletRequest request);
    public ResponseData<PageInfo<AnalysisPriceResultVO>> selectAnalysisEG(ActivityPriceParamVO vo, HttpServletRequest request);
    public ResponseData<PageInfo<MktActivityPrizeRecordPO>> selectPrizePeople(ActivityPriceParamVO vo);
    public ResponseData<String> doVerificationCoupon(ActivityPriceParamVO vo, HttpServletRequest request);
    public MktActivityPOWithBLOBs getMktActivityPOWithBLOBs(ProbabilityVO vo);

    public ResponseData<String> doEggFrenzy(ProbabilityVO vo) throws ParseException;
    public ResponseData<List<MktActivityPrizeRecordPO>> getEGPrizeRecordList(MktActivityPrizeRecordPO po);
    public ResponseData<Integer>  residueMemberNumber(ProbabilityVO vo) throws ParseException;
    public ResponseData<Integer> addMemberNumber(ProbabilityVO vo) throws ParseException;
    public ResponseData<MktActivityPOWithBLOBs>  getActivityId(ProbabilityVO vo);
}