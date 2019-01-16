package com.bizvane.mktcenterserviceimpl.controllers;

import com.bizvane.mktcenterservice.interfaces.ActivityStatisticsService;
import com.bizvane.utils.responseinfo.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yy
 * @create 2019-01-11 11:15
 */
@RestController
@RequestMapping("statistics")
@Slf4j
public class ActivityStatisticsController {

    @Autowired
    private ActivityStatisticsService activityStatisticsService;


    /**
     * 得到統計時間列表
     * @return
     */
    @RequestMapping("getAllDate")
    public ResponseData getAllDate(){
        log.info("enter ActivityStatisticsController method getAllDate");
        return activityStatisticsService.getAllDate();
    }

    /**
     * 曲线图数据
     * @param activityId
     * @param code
     * @param time
     * @return
     */
    @RequestMapping("curveData")
    public ResponseData curveData(@RequestParam("activityId") Long activityId,@RequestParam("code") int code,@RequestParam("time") String time){
        return activityStatisticsService.curveData(activityId,time,code);
    }

    @RequestMapping("test1")
    public void test1(){
        activityStatisticsService.schedule();
    }

    @RequestMapping("test")
    public ResponseData test(@RequestParam("activityId") Long activityId,@RequestParam("code") int code,@RequestParam("memberCode") String memberCode){
        return activityStatisticsService.statisticsData(activityId,code,memberCode);
    }

    /**
     * 活动分析
     * @param activityId
     * @param time
     * @return
     */
    @RequestMapping("activityAnalysis")
    public ResponseData activityAnalysis(@RequestParam("activityId") Long activityId,@RequestParam("time") String time){
        return activityStatisticsService.activityAnalysis(activityId,time);
    }
}