package com.iflytek.astron.console.hub.mapper.repo;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.astron.console.hub.entity.dto.UploadDocTaskDto;
import com.iflytek.astron.console.hub.entity.table.repo.UploadDocTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper interface
 * </p>
 *
 * @author xxzhang23
 * @since 2024-01-09
 */
@Mapper


public interface UploadDocTaskMapper extends BaseMapper<UploadDocTask> {
    List<UploadDocTaskDto> selectUploadDocTaskDtoBySourcesId(@Param("sourcesIds") List<String> sourcesIds, @Param("appId") String appId);
}
