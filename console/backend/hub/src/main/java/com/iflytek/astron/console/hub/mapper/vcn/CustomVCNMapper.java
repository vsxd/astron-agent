package com.iflytek.astron.console.hub.mapper.vcn;

import com.iflytek.astron.console.hub.dto.vcn.CustomV2VCNDto;
import org.apache.ibatis.annotations.Param;

public interface CustomVCNMapper {

    CustomV2VCNDto getVcnByCode(@Param("vcnCode") String vcnCode);

}
