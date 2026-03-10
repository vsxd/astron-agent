package com.iflytek.astron.console.hub.entity.vo.group;

import lombok.Data;

import java.util.List;

@Data
public class DeleteGroupUserVo {
    private Long tagId;
    private List<String> uids;
}
