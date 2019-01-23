package com.bizvane.mktcenterservice.interfaces;

import com.bizvane.utils.responseinfo.ResponseData;


/**
 * @author yy
 * @create 2019-01-10 11:19
 */
public interface ActivityStatisticsService {

    public ResponseData statisticsData(Long activityId, int code,String memberCode);

    public ResponseData getAllDate(Long activityId);

    public void schedule();

    public ResponseData activityAnalysis(Long activityId,String time);


    public ResponseData curveData(Long activityId,String time,int type);


    public ResponseData addActivityIdsSet(Long activityId);


    public ResponseData deleteActivityIdsSet(Long activityId);
}
