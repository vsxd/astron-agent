package com.iflytek.astron.console.hub.entity.vo;

import lombok.*;

import java.util.List;

/**
 * @Author clliu19
 * @Date: 2025/8/18 18:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeVo {
    private Long id;
    private String key;
    private String name;
    private Integer sortOrder;
    private List<CategoryTreeVo> children;
    /**
     * SYSTEM / CUSTOM, used by frontend to identify source
     */
    private String source;
}
