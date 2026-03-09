package com.iflytek.astron.console.hub.entity.common;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueLabelTree {
    String value;
    String label;
    List<ValueLabelTree> children;
}
