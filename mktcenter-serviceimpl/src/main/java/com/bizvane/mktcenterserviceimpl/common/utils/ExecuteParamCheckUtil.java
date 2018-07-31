package com.bizvane.mktcenterserviceimpl.common.utils;

import com.bizvane.mktcenterservice.models.bo.OrderModelBo;
import com.bizvane.mktcenterservice.models.vo.ActivityVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by agan on 2018/7/30.
 */
public class ExecuteParamCheckUtil {
    /**
     * 针对会员类型 会员范围判断
     * @param memberTypeVo
     * @param memberType
     * @return
     */
    public static Boolean CheckMemberType(Integer memberTypeVo,Integer memberType){
        boolean  falg;
        if (memberType.equals(memberTypeVo)){
            falg = true;
        }else{
            falg=false;
        }
        return falg;
    }

    /**
     * 判断会员等级
     * @param levelId
     * @param mbrLevelCode
     * @return
     */
    public static Boolean CheckMbrLevelCode(Long levelId,String mbrLevelCode){
        boolean  falg;
        if (mbrLevelCode.equals("0") ||mbrLevelCode.equals(levelId)){
            falg = true;
        }else {
            falg=false;
        }
        return falg;
    }

    /**
     * 判断订单来源
     * @param orderFrom
     * @param orderSource
     * @return
     */
    public static Boolean CheckOrderFrom(Integer orderFrom,Integer orderSource){
        boolean  falg;
        if(0==orderSource || orderSource.equals(orderFrom)){
            falg = true;
        }else {
            falg=false;
        }
        return falg;
    }

    /**
     * 判断金额是否满足
     * @param payMoney
     * @param orderMinPrice
     * @return
     */
    public static Boolean CheckPayMoney(BigDecimal payMoney,BigDecimal orderMinPrice){
        boolean  falg;
        if (payMoney.compareTo(orderMinPrice) >=0){
            falg = true;
        }else{
            falg=false;
        }
        return falg;
    }

    /**
     * 判断商品是否满足
     * @param vo
     * @param activityVO
     * @return
     */
    public  static Boolean CheckCommodity(OrderModelBo vo,ActivityVO activityVO) {
        boolean falg = false;
        if (false == activityVO.getCommodityLimit()) {
            falg = true;
        } else {
            //判断是否是黑名单
            if (!StringUtils.isBlank(activityVO.getCommodityBlacklist())) {
                String commodityBlacklist = activityVO.getCommodityBlacklist();
                List<String> result = Arrays.asList(commodityBlacklist.split(","));
                String productNos = vo.getProductNos();
                List<String> productNoList = Arrays.asList(productNos.split(","));
                result.retainAll(productNoList);
                boolean fa=result.size()>0;
                    if (fa==true){
                        falg=false;
                    }else {
                        falg=true;
                    }

            }
            //判断是否是白名单
            if (!StringUtils.isBlank(activityVO.getCommodityWhitelist())) {
                String commodityWhitelist = activityVO.getCommodityWhitelist();
                List<String> result = Arrays.asList(commodityWhitelist.split(","));
                String productNos = vo.getProductNos();
                List<String> productNoList = Arrays.asList(productNos.split(","));
                    boolean contains = result.containsAll(productNoList);
                        falg=contains;
            }

        }
        return falg;
    }

    /**
     * 判断门店
     * @param vo
     * @param activityVO
     * @return
     */
    public  static Boolean CheckserviceStore(OrderModelBo vo,ActivityVO activityVO) {
        boolean falg = false;
        if (false == activityVO.getStoreLimit()) {
            falg = true;
        }else{
            //判断是都是黑名单
            if (!StringUtils.isBlank(activityVO.getStoreBlacklist())) {
                String StoreBlacklist = activityVO.getStoreBlacklist();
                List<String> result = Arrays.asList(StoreBlacklist.split(","));
                 boolean contains  =result.contains(vo.getServiceStoreId());
                 if (contains==true){
                     falg=false;
                 }else{
                     falg=true;
                 }
            }

            //判断是否是白名单
            if (!StringUtils.isBlank(activityVO.getStoreWhitelist())) {
                String StoreWhitelist = activityVO.getStoreWhitelist();
                List<String> result = Arrays.asList(StoreWhitelist.split(","));
                falg=result.contains(vo.getServiceStoreId());
            }

        }
        return falg;
    }
}
