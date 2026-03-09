package com.iflytek.astron.console.hub.entity.core.workflow.node;

import lombok.Data;

import java.util.List;

@Data
public class InputOutput {
    // Required
    String id;
    String fileType;
    String type;
    String name;
    Schema schema;
    List<String> allowedFileType;
    Boolean required;

}
