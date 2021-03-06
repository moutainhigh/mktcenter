package com.bizvane.mktcenterservice.controllers;

import com.bizvane.mktcenterfacade.interfaces.ActivityManualService;
import com.bizvane.mktcenterfacade.models.bo.ActivityBO;
import com.bizvane.mktcenterfacade.models.vo.ActivityVO;
import com.bizvane.mktcenterfacade.models.vo.PageForm;
import com.bizvane.utils.tokens.SysAccountPO;
import com.bizvane.utils.tokens.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bizvane.utils.responseinfo.ResponseData;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("activityManual")
@Slf4j
public class ActivityManualController {

    @Autowired
    private ActivityManualService activityManualService;

     /**
      * 查询活动列表
      * */
    @RequestMapping("/getActivityList")
    public  ResponseData<ActivityVO> getActivityList(ActivityVO vo, PageForm  pageForm,HttpServletRequest request){
        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        return activityManualService.getActivityManualList(vo,pageForm,stageUser);
    }


    /**
     *创建活动
     */
    @RequestMapping("/addActivity")
   public  ResponseData<Integer> addActivity(ActivityBO bo,HttpServletRequest request){
        //取缓存
        SysAccountPO stageUser = TokenUtils.getStageUser(request);
        //SysAccountPO stageUser = new SysAccountPO();
       return  activityManualService.addActivityManual(bo,stageUser);

   }


   /**
   * 查询活动效果分析
   * */
   @RequestMapping("/getActivityManualEffect")
   public ResponseData<List<ActivityBO>>getActivityManualEffect(ActivityVO activityVO,HttpServletRequest request){
       // SysAccountPO stageUser = TokenUtils.getStageUser(request);
       SysAccountPO stageUser = new SysAccountPO();
      // activityVO.setSysBrandId(1L);
//       activityVO.setSysBrandId(stageUser.getBrandId());
       return activityManualService.getActivityManualEffect(activityVO);
 }

    /**
     * 查看活动详情
     */
    @RequestMapping("/selectActivityManualByBusinessCode")
    public ResponseData<ActivityBO> selectActivityManualByBusinessCode(String businessCode){
        return activityManualService.selectActivityManualByBusinessCode(businessCode);
    }

}
