package com.iflytek.astron.console.hub.entity.vo.repo;

import lombok.Data;

import java.util.List;

@Data
public class FileStatusVo {
    private String app_id;
    private String repo_id;
    private List<String> file_ids;
}
