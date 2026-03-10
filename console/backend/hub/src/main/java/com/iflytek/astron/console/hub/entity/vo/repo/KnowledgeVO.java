package com.iflytek.astron.console.hub.entity.vo.repo;

import lombok.Data;

import java.util.List;

@Data
public class KnowledgeVo {
    private String id;
    private Long fileId;
    private String content;
    private List<String> tags;
}
