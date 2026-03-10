package com.iflytek.astron.console.hub.entity.table.relation;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("flow_repo_rel")
public class FlowRepoRel {
    String flowId;
    String repoId;
}
