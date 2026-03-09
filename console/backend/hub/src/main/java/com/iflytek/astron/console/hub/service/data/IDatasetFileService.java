package com.iflytek.astron.console.hub.service.data;

import com.iflytek.astron.console.hub.dto.dataset.DatasetStats;

import java.util.List;

public interface IDatasetFileService {

    List<DatasetStats> getMaasDataset(Long datasetId);

}
