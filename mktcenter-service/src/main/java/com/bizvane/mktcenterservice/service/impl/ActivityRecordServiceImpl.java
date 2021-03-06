package com.bizvane.mktcenterservice.service.impl;

import com.bizvane.mktcenterfacade.interfaces.ActivityRecordService;
import com.bizvane.mktcenterfacade.models.po.MktActivityRecordPO;
import com.bizvane.mktcenterfacade.models.vo.MktActivityRecordVO;
import com.bizvane.mktcenterservice.mappers.MktActivityRecordPOMapper;
import com.bizvane.utils.enumutils.SysResponseEnum;
import com.bizvane.utils.responseinfo.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by agan on 2018/8/6.
 */
@Service
@Slf4j
public class ActivityRecordServiceImpl implements ActivityRecordService {
    @Autowired
    private MktActivityRecordPOMapper mktActivityRecordPOMapper;
    @Override
    public ResponseData<List<MktActivityRecordPO>> getActivityRecordPOList(MktActivityRecordVO vo) {
        ResponseData responseData = new ResponseData();
        List<MktActivityRecordPO> mktActivityRecordPOList = mktActivityRecordPOMapper.getActivityRecordPOList(vo);
        responseData.setData(mktActivityRecordPOList);
        responseData.setCode(SysResponseEnum.SUCCESS.getCode());
        responseData.setMessage(SysResponseEnum.SUCCESS.getMessage());
        return responseData;
    }
}
