package com.iflytek.astron.console.hub.entity.vo.group;

import lombok.Data;

import java.util.List;

@Data
public class GroupUserVo {
    private String name;
    private List<Long> userIds;
    private List<String> tagNames;
}
