package com.iflytek.astron.console.hub.entity.dto;

import com.iflytek.astron.console.hub.entity.table.bot.SparkBot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SparkBotVO extends SparkBot {
    private static final long serialVersionUID = 1L;
    Integer authStatus;
    String address;
    Object appDetail;

    Boolean isFavorite;
    /**
     * This part is not clear whether it needs to be retrieved from the database, temporarily hardcoded
     */

    List<String> recommendedDialog;

    String domainName;

    Boolean isAdded;

    String openingRemark;
    String evalSetName;
}
