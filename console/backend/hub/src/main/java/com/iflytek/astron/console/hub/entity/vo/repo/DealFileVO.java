package com.iflytek.astron.console.hub.entity.vo.repo;

import com.iflytek.astron.console.hub.entity.pojo.SliceConfig;
import lombok.Data;

import java.util.List;

@Data
public class DealFileVo {

    private Long repoId;
    // Files to be sliced list
    private List<String> fileIds;

    private List<SparkFileVo> sparkFiles;
    /**
     * Slice configuration { "type":"1" 0:auto slice 1:custom slice, "seperator":["|"," "],
     * "lengthRange":[120, 256] } { "type":"0", "seperator":["/n"], "lengthRange":[256, 256] }
     */
    private SliceConfig sliceConfig;

    private String tag;

    private Integer indexType;

    private Integer isBackTask;

    private Integer isBackEmbedding;
}
