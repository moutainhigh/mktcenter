package com.bizvane.mktcenterservice.models.vo;

import com.bizvane.couponfacade.models.po.CouponDefinitionPO;
import com.bizvane.mktcenterservice.models.po.MktActivityPrizePO;
import lombok.Data;

/**
 * @Author: lijunwei
 * @Time: 2018/12/25 10:11
 */
@Data
public class MktActivityPrizeVO extends MktActivityPrizePO {
  private  CouponDefinitionPO couponDefinitionPO;
}
