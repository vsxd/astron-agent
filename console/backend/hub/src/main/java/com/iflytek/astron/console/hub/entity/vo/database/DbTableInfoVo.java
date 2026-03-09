package com.iflytek.astron.console.hub.entity.vo.database;

import lombok.Data;

import java.util.List;

@Data
public class DbTableInfoVo {

    private String Label;

    private String value;

    private List<DbTableInfoVo> children;
}
