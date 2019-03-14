package com.bizvane.mktcenterservice.mappers;

import com.bizvane.mktcenterfacade.models.bo.MktActivityRedPacketRecordBO;
import com.bizvane.mktcenterfacade.models.po.MktActivityRedPacketRecordPO;
import com.bizvane.mktcenterfacade.models.po.MktActivityRedPacketRecordPOExample;
import com.bizvane.mktcenterfacade.models.vo.ActivityRedPacketVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MktActivityRedPacketRecordPOMapper {
    /**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	long countByExample(MktActivityRedPacketRecordPOExample example);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int deleteByExample(MktActivityRedPacketRecordPOExample example);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int deleteByPrimaryKey(Long mktActivityRedPacketRecordId);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int insert(MktActivityRedPacketRecordPO record);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int insertSelective(MktActivityRedPacketRecordPO record);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	List<MktActivityRedPacketRecordPO> selectByExample(MktActivityRedPacketRecordPOExample example);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	MktActivityRedPacketRecordPO selectByPrimaryKey(Long mktActivityRedPacketRecordId);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int updateByExampleSelective(@Param("record") MktActivityRedPacketRecordPO record,
			@Param("example") MktActivityRedPacketRecordPOExample example);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int updateByExample(@Param("record") MktActivityRedPacketRecordPO record,
			@Param("example") MktActivityRedPacketRecordPOExample example);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int updateByPrimaryKeySelective(MktActivityRedPacketRecordPO record);
	/**
	 * 只读. 
	 * @mbg.generated  2019-01-21 14:43:18
	 */
	int updateByPrimaryKey(MktActivityRedPacketRecordPO record);
	Integer  getRedPacketCount(@Param("type")Integer type,@Param("memberCode")String memberCode,@Param("sponsorCode")String sponsorCode,@Param("mktActivityId")Long mktActivityId);
	List<MktActivityRedPacketRecordBO> getRedPacketCoponRecord(ActivityRedPacketVO vo);
	List<MktActivityRedPacketRecordPO> getRedPacketZhuLiRecord (ActivityRedPacketVO vo);
}