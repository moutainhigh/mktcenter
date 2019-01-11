package com.bizvane.mktcenterservice.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.bizvane.mktcenterservice.models.bo.ActivityRedPacketBO;
import com.bizvane.mktcenterservice.models.vo.ActivityRedPacketVO;
import com.bizvane.utils.responseinfo.ResponseData;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @Author: lijunwei
 * @Time: 2019/1/11 13:54
 */
public interface ActivityRedPacketService {
    public ResponseData<JSONObject> addActivityRedPacket(ActivityRedPacketBO bo, HttpServletRequest request) throws ParseException;
    public ResponseData<ActivityRedPacketBO> selectActivityRedPacket(ActivityRedPacketVO vo);
}
