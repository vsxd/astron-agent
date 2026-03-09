package com.iflytek.astron.console.hub.entity.vo.repo;

import lombok.Data;

@Data
public class DeleteRepoVO {
    private String app_id;
    private String repo_id;
    private String bot_id;
}
