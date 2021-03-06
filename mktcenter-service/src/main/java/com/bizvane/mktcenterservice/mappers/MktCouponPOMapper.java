package com.bizvane.mktcenterservice.mappers;

import com.bizvane.mktcenterfacade.models.po.MktCouponPO;
import com.bizvane.mktcenterfacade.models.po.MktCouponPOExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MktCouponPOMapper {

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	long countByExample(MktCouponPOExample example);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int deleteByExample(MktCouponPOExample example);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int deleteByPrimaryKey(Long mktCouponId);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int insert(MktCouponPO record);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int insertSelective(MktCouponPO record);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	List<MktCouponPO> selectByExample(MktCouponPOExample example);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	MktCouponPO selectByPrimaryKey(Long mktCouponId);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int updateByExampleSelective(@Param("record") MktCouponPO record, @Param("example") MktCouponPOExample example);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int updateByExample(@Param("record") MktCouponPO record, @Param("example") MktCouponPOExample example);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int updateByPrimaryKeySelective(MktCouponPO record);

	/**
	 * 只读. 
	 * @mbg.generated  2018-09-08 11:41:35
	 */
	int updateByPrimaryKey(MktCouponPO record);

	MktCouponPO selectMktActivityManualId(MktCouponPO mktCouponPO);

	MktCouponPO selectCouponCode(MktCouponPO mktCouponPO);
}