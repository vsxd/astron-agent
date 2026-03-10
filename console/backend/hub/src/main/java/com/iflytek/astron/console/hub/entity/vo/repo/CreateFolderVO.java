package com.iflytek.astron.console.hub.entity.vo.repo;

import lombok.Data;

import java.util.List;

@Data
public class CreateFolderVo {
    private Long id;
    private Long repoId;
    private String name;
    private Long parentId;
    private List<String> tags;
}
