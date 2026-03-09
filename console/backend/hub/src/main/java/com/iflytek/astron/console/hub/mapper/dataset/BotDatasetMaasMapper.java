package com.iflytek.astron.console.hub.mapper.dataset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.entity.dataset.BotDatasetMaas;
import com.iflytek.astron.console.hub.dto.dataset.DatasetStats;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BotDatasetMaasMapper extends BaseMapper<BotDatasetMaas> {

    List<DatasetStats> selectBotStatsMaps(@Param("datasetIds") List<Long> datasetIds);
}
