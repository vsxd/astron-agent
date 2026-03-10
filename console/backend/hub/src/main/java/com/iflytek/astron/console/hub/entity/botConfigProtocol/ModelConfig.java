package com.iflytek.astron.console.hub.entity.botConfigProtocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModelConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Persona information
     */
    String instruct;
    /**
     * Planned model
     */
    ModelProperty plan;
    /**
     * Summary model
     */
    ModelProperty summary;
}
