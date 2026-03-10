package com.iflytek.astron.console.hub.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class HtmlFileVo {
    private List<String> htmlAddressList;
    private Long repoId;
    private Long parentId;
}
