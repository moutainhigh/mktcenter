package com.bizvane.mktcenterserviceimpl.mappers;

import com.bizvane.mktcenterservice.models.po.MktActivityPrizePO;
import com.bizvane.mktcenterservice.models.po.MktActivityPrizePOExample;
import com.bizvane.mktcenterservice.models.vo.ActivityPriceParamVO;
import com.bizvane.mktcenterservice.models.vo.AnalysisPriceResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MktActivityPrizePOMapper {
    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    long countByExample(MktActivityPrizePOExample example);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int deleteByExample(MktActivityPrizePOExample example);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int deleteByPrimaryKey(Long mktActivityPrizeId);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int insert(MktActivityPrizePO record);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int insertSelective(MktActivityPrizePO record);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    List<MktActivityPrizePO> selectByExample(MktActivityPrizePOExample example);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    MktActivityPrizePO selectByPrimaryKey(Long mktActivityPrizeId);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int updateByExampleSelective(@Param("record") MktActivityPrizePO record, @Param("example") MktActivityPrizePOExample example);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int updateByExample(@Param("record") MktActivityPrizePO record, @Param("example") MktActivityPrizePOExample example);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int updateByPrimaryKeySelective(MktActivityPrizePO record);

    /**
     * 只读. 
     *
     * @mbg.generated 2018-12-18 15:22:31
     */
    int updateByPrimaryKey(MktActivityPrizePO record);

    /**
     *统计
     */
    List<AnalysisPriceResultVO> selectAnalysisPrice(ActivityPriceParamVO vo);
}