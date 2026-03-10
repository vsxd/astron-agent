package com.iflytek.astron.console.hub.entity.table.relation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bot_flow_rel")
public class BotFlowRel {
    @TableId(type = IdType.AUTO)
    Long id;
    Long botId;
    String flowId;
    Date createTime;
}
