package com.iflytek.astron.console.hub.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class HtmlFileVO {
    private List<String> htmlAddressList;
    private Long repoId;
    private Long parentId;
}
