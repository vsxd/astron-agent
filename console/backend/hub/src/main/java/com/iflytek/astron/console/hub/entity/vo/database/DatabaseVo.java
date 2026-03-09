package com.iflytek.astron.console.hub.entity.vo.database;

import com.iflytek.astron.console.hub.entity.table.database.DbInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatabaseVo extends DbInfo {

    String address;

    Long tbNum;

    Long botCount;
}
