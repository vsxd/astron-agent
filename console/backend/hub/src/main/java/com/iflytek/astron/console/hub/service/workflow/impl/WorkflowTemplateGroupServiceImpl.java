package com.iflytek.astron.console.hub.service.workflow.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iflytek.astron.console.hub.entity.workflow.WorkflowTemplateGroup;
import com.iflytek.astron.console.hub.mapper.WorkflowTemplateGroupMapper;
import com.iflytek.astron.console.hub.service.workflow.WorkflowTemplateGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * @author cherry
 */
@Service
@RequiredArgsConstructor
public class WorkflowTemplateGroupServiceImpl implements WorkflowTemplateGroupService {
    private final WorkflowTemplateGroupMapper workflowTemplateGroupMapper;

    @Override
    public List<WorkflowTemplateGroup> getTemplateGroup() {

        return workflowTemplateGroupMapper.selectList(Wrappers.lambdaQuery(WorkflowTemplateGroup.class)
                .eq(WorkflowTemplateGroup::getIsDelete, false));
    }
}
