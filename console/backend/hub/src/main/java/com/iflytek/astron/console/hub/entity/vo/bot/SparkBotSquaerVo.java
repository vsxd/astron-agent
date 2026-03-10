package com.iflytek.astron.console.hub.entity.vo.bot;

import com.iflytek.astron.console.hub.entity.table.bot.SparkBot;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author clliu19
 * @date 2024/05/23/11:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SparkBotSquaerVo extends SparkBot {
    private static final long serialVersionUID = 1L;

    private String toolId;
}
