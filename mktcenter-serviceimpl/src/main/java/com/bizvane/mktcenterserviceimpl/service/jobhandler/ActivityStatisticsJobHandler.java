package com.bizvane.mktcenterserviceimpl.service.jobhandler;

import com.bizvane.mktcenterservice.models.po.MktActivityPOExample;
import com.bizvane.mktcenterservice.models.po.MktActivityPOWithBLOBs;
import com.bizvane.mktcenterservice.models.po.MktActivityStatisticsPO;
import com.bizvane.mktcenterserviceimpl.common.constants.StatisticsConstants;
import com.bizvane.mktcenterserviceimpl.mappers.MktActivityPOMapper;
import com.bizvane.mktcenterserviceimpl.mappers.MktActivityStatisticsPOMapper;
import com.bizvane.utils.redisutils.RedisTemplateServiceImpl;
import com.qiniu.util.Json;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author yy
 * @create 2019-01-24 19:18
 */
@JobHandler(value = "redPackStatisticalData")
@Component
@Slf4j
public class ActivityStatisticsJobHandler extends IJobHandler {

    @Autowired
    private MktActivityPOMapper mktActivityPOMapper;
    @Autowired
    private RedisTemplateServiceImpl redisTemplateService;
    @Autowired
    private MktActivityStatisticsPOMapper mktActivityStatisticsPOMapper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("红包膨胀了   活动统计   任务开始执行 。。。。");
        //获取活动id列表
        MktActivityPOExample example = new MktActivityPOExample();
        example.createCriteria().andActivityTypeEqualTo(12).andActivityStatusEqualTo(2).andValidEqualTo(true);
        List<MktActivityPOWithBLOBs> activityIds = mktActivityPOMapper.selectByExampleWithBLOBs(example);
        if (CollectionUtils.isEmpty(activityIds)) {
            //证明昨天无活动触发 就此结束
            log.info("无活动id列表就此结束定时");
            return ReturnT.FAIL;
        }
        //String yesterday = StatisticsConstants.getYesterday(); TODO 测试先为当天
        String yesterday = StatisticsConstants.getCurrentDate();
        //获取昨天24小时内的访问量数据
        Map map = new TreeMap();
        for (MktActivityPOWithBLOBs activity:activityIds) {
            Long activityId = activity.getMktActivityId();
            for (int i = 0; i < 24; i++) {
                String key = StatisticsConstants.VISITORS_PREFIX + activityId + "_" + yesterday + "_" + i;
                Set hourMemberCodeSet = (Set) redisTemplateService.stringGetStringByKey(key);
                int count = hourMemberCodeSet == null ? 0 : hourMemberCodeSet.size();
                if (i < 10) {
                    map.put("0" + i + ":00", count);
                } else {
                    map.put(i + ":00", count);
                }
            }
            //查询昨天访问人数
            /*String visitorsKey = StatisticsConstants.VISITORS_PREFIX + activityId + "_" + yesterday;
            Set visitorsMemberCodeSet = (Set) redisTemplateService.stringGetStringByKey(visitorsKey);
            int visitorsCount = visitorsMemberCodeSet == null ? 0 : visitorsMemberCodeSet.size();*/
            //查询昨天发起会员数
            String launchMembersKey = StatisticsConstants.LAUNCH_MEMBERS + activityId + "_" + yesterday;
            Set launchMemberCodeSet = (Set) redisTemplateService.stringGetStringByKey(launchMembersKey);
            int launchMembersCount = launchMemberCodeSet == null ? 0 : launchMemberCodeSet.size();
            //查询助力昨天助力会员数
            String helpMembersKey = StatisticsConstants.HELP_MEMBERS + activityId + "_" + yesterday;
            Set helpMemberCodeSet = (Set) redisTemplateService.stringGetStringByKey(helpMembersKey);
            int helpMembersCount = helpMemberCodeSet == null ? 0 : helpMemberCodeSet.size();
            //查询注册会员数
            String registerMembersKey = StatisticsConstants.REGISTER_MEMBERS + activityId + "_" + yesterday;
            Set registerMemberCodeSet = (Set) redisTemplateService.stringGetStringByKey(registerMembersKey);
            int registerMembersCount = registerMemberCodeSet == null ? 0 : registerMemberCodeSet.size();
            //领劵数量
            String takeCouponKey = StatisticsConstants.TAKE_COUPON + activityId + "_" + yesterday;
            Set takeCouponMemberCodeSet = (Set) redisTemplateService.stringGetStringByKey(takeCouponKey);
            int takeCouponCount = takeCouponMemberCodeSet == null ? 0 : takeCouponMemberCodeSet.size();

            String totalLaunch = StatisticsConstants.TOTAL_LAUNCH_MEMBERS + activityId;
            Set totalLaunchSet = (Set) redisTemplateService.stringGetStringByKey(totalLaunch);
            int totalLaunchCount = totalLaunchSet == null ? 0 : totalLaunchSet.size();

            String totalHelp = StatisticsConstants.TOTAL_HELP_MEMBERS + activityId;
            Set totalHelpSet = (Set) redisTemplateService.stringGetStringByKey(totalHelp);
            int totalHelpCount = totalHelpSet == null ? 0 : totalHelpSet.size();

            String totalregister = StatisticsConstants.TOTAL_REGISTER_MEMBERS + activityId;
            Set totalregisterSet = (Set) redisTemplateService.stringGetStringByKey(totalregister);
            int totalregisterCount = totalregisterSet == null ? 0 : totalregisterSet.size();

            String totaltake = StatisticsConstants.TOTAL_TAKE_COUPON + activityId;
            Set totaltakeSet = (Set) redisTemplateService.stringGetStringByKey(totaltake);
            int totaltakeCount = totaltakeSet == null ? 0 : totaltakeSet.size();

            //存储到红包活动分析表中
            MktActivityStatisticsPO mktActivityStatisticsPO = new MktActivityStatisticsPO();
            mktActivityStatisticsPO.setSysCompanyId(activity.getSysCompanyId());
            mktActivityStatisticsPO.setSysBrandId(activity.getSysBrandId());
            mktActivityStatisticsPO.setMktActivityId(activityId);
            mktActivityStatisticsPO.setVisitorsCount(launchMembersCount + helpMembersCount);
            mktActivityStatisticsPO.setLaunchMembersCount(launchMembersCount);
            mktActivityStatisticsPO.setHelpMembersCount(helpMembersCount);
            mktActivityStatisticsPO.setRegisterMembersCount(registerMembersCount);
            mktActivityStatisticsPO.setTakeCouponCount(Long.parseLong(String.valueOf(takeCouponCount)));

            mktActivityStatisticsPO.setTotalVisitorsCount(Long.parseLong(String.valueOf(totalLaunchCount + totalHelpCount)));
            mktActivityStatisticsPO.setTotalLaunchMembersCount(Long.parseLong(String.valueOf(totalLaunchCount)));
            mktActivityStatisticsPO.setTotalHelpMembersCount(Long.parseLong(String.valueOf(totalHelpCount)));
            mktActivityStatisticsPO.setTotalRegisterMembersCount(Long.parseLong(String.valueOf(totalregisterCount)));
            mktActivityStatisticsPO.setTotalTakeCouponCount(Long.parseLong(String.valueOf(totaltakeCount)));

            String json = Json.encode(map);
            mktActivityStatisticsPO.setHourJsonData(json);
            /*Calendar calendar = Calendar.getInstance(); todo 测试先为当天
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -1);
            mktActivityStatisticsPO.setStatisticsTime(calendar.getTime());*/
            mktActivityStatisticsPO.setStatisticsTime(new Date());

            mktActivityStatisticsPO.setStatisticsType(StatisticsConstants.STATISTICS_TYPE);
            mktActivityStatisticsPO.setCreateDate(new Date());
            mktActivityStatisticsPO.setModifiedDate(new Date());
            mktActivityStatisticsPO.setValid(true);
            mktActivityStatisticsPO.setCreateUserName("系统自动执行定时");
            mktActivityStatisticsPO.setModifiedUserName("系统自动执行定时");
            mktActivityStatisticsPOMapper.insertSelective(mktActivityStatisticsPO);
        }
        log.info("红包膨胀了   活动统计   任务执行结束。。。。");
        return new ReturnT<>("红包活动统计任务成功执行完毕。");
    }
}
