package com.iflytek.astron.console.hub.controller.workflow;

import com.iflytek.astron.console.commons.annotation.space.SpacePreAuth;
import com.iflytek.astron.console.commons.constant.ResponseEnum;
import com.iflytek.astron.console.hub.entity.workflow.Workflow;
import com.iflytek.astron.console.commons.exception.BusinessException;
import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.util.space.SpaceInfoUtil;
import com.iflytek.astron.console.hub.entity.biz.workflow.ChatBizReq;
import com.iflytek.astron.console.hub.entity.biz.workflow.ChatResumeReq;
import com.iflytek.astron.console.hub.entity.biz.workflow.WorkflowDebugDto;
import com.iflytek.astron.console.hub.entity.common.PageData;
import com.iflytek.astron.console.hub.entity.common.Pagination;
import com.iflytek.astron.console.hub.entity.dto.*;
import com.iflytek.astron.console.hub.entity.dto.eval.WorkflowComparisonSaveReq;
import com.iflytek.astron.console.hub.entity.dto.talkagent.TalkAgentConfigDto;
import com.iflytek.astron.console.hub.entity.table.workflow.WorkflowComparison;
import com.iflytek.astron.console.hub.entity.table.workflow.WorkflowDialog;
import com.iflytek.astron.console.hub.entity.table.workflow.WorkflowFeedback;
import com.iflytek.astron.console.hub.entity.tool.McpServerTool;
import com.iflytek.astron.console.hub.entity.vo.McpServerToolDetailVO;
import com.iflytek.astron.console.hub.entity.vo.WorkflowVo;
import com.iflytek.astron.console.hub.handler.UserInfoManagerHandler;
import com.iflytek.astron.console.hub.service.workflow.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * Workflow-related interface controller.
 *
 * <p>
 * Features and specifications:
 * <ul>
 * <li>Use constructor injection (with {@code @RequiredArgsConstructor}), avoiding field
 * injection.</li>
 * <li>Enable {@code @Validated} + JSR 380/381 annotations for input parameter validation.</li>
 * <li>Unified logging and exceptions: maintain existing return wrappers (ApiResult/Result/custom
 * ResponseResultBody) for external interfaces.</li>
 * <li>Add necessary response headers and exception boundaries for SSE, file import/export.</li>
 * </ul>
 */
@RestController
@RequestMapping("/workflow")
@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Workflow management interface")
public class WorkflowController {


    private final WorkflowService workflowService;
    private final TalkAgentConfigService talkAgentService;
    private final WorkflowExportService workflowExportService;

    // ---------------------- Basic Information ----------------------

    /**
     * Get workflow list.
     *
     * @param pagination Pagination parameters (required)
     * @param search Search keyword (optional)
     * @param flowId Workflow unique identifier (optional)
     * @param status Publish status: -1=all, 0=unpublished, 1=published (optional)
     * @param order Sort order: 1=by creation time, 2=by update time (optional)
     * @param spaceId Space ID (optional, fallback to SpaceInfoUtil in service layer if empty)
     * @return Paginated data
     */
    @GetMapping("/list")
    @SpacePreAuth(
            key = "WorkflowController_list_GET",
            module = "Workflow",
            point = "Workflow List",
            description = "Workflow List")
    public ApiResult<PageData<WorkflowVo>> list(
            @NotNull(message = "Pagination parameters cannot be null") Pagination pagination,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String flowId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer order,
            @RequestParam(required = false) Long spaceId) throws UnsupportedEncodingException {

        if (pagination.isEmpty()) {
            throw new BusinessException(ResponseEnum.PAGE_SEPARATOR_MISS);
        }
        return ApiResult.success(workflowService.listPage(
                spaceId, pagination.getCurrent(), pagination.getPageSize(), search, status, order, flowId));
    }

    /**
     * Get workflow details.
     *
     * @param id Workflow primary key ID
     */
    @GetMapping
    @SpacePreAuth(
            key = "WorkflowController_detail_GET",
            module = "Workflow",
            point = "Workflow Details",
            description = "Workflow Details")
    public ApiResult<WorkflowVo> detail(@RequestParam @NotBlank String id, @RequestParam(required = false) Long spaceId) {
        return ApiResult.success(workflowService.detail(id, spaceId));
    }

    /**
     * Create workflow.
     */
    @PostMapping
    @SpacePreAuth(
            key = "WorkflowController_create_POST",
            module = "Workflow",
            point = "Workflow Creation",
            description = "Workflow Creation")
    public ApiResult<Object> create(@RequestBody @NotNull WorkflowReq createDto, HttpServletRequest request) {
        return ApiResult.success(workflowService.create(createDto, request));
    }

    /**
     * Update workflow information.
     */
    @PutMapping
    @SpacePreAuth(
            key = "WorkflowController_update_PUT",
            module = "Workflow",
            point = "Workflow Editing",
            description = "Workflow Editing")
    public ApiResult<Workflow> update(@RequestBody @NotNull WorkflowReq updateDto) {
        return ApiResult.success(workflowService.updateInfo(updateDto));
    }

    /**
     * Delete workflow (logical deletion).
     */
    @DeleteMapping
    public ApiResult delete(
            @RequestParam(required = false) Long id, @RequestParam(required = false) Long spaceId) {
        return workflowService.logicDelete(id, spaceId);
    }

    /**
     * Create copy.
     */
    @GetMapping("/clone")
    public ApiResult<Object> clone(@RequestParam @NotNull Long id) {
        return ApiResult.success(workflowService.clone(id));
    }

    /**
     * Internal clone (with simple password validation).
     *
     * <p>
     * Note: Retain existing logic, only return standard error response when validation fails.
     */
    @PostMapping("/internal-clone")
    public ApiResult<Object> cloneV2(
            @RequestBody CloneFlowReq req,
            HttpServletRequest request) {
        if (!"xfyun".equals(req.getPassword())) {
            return ApiResult.error(ResponseEnum.INCORRECT_PASSWORD);
        }
        return ApiResult.success(workflowService.cloneForXfYun(req.getMaasId(), SpaceInfoUtil.getSpaceId(), req.getFlowType(), req.getBotId(), req.getFlowConfig(), request));
    }

    /**
     * Build workflow.
     */
    @PostMapping("/build")
    @SpacePreAuth(
            key = "WorkflowController_build_POST",
            module = "Workflow",
            point = "Workflow Build",
            description = "Workflow Build")
    public ApiResult<Object> build(@RequestBody @NotNull WorkflowReq buildDto) throws InterruptedException {
        return ApiResult.success(workflowService.build(buildDto));
    }

    // ---------------------- Nodes and Dialogs ----------------------

    @PostMapping("/node/debug/{nodeId}")
    public ApiResult<Object> nodeDebug(@PathVariable("nodeId") @NotBlank String nodeId, @RequestBody @NotNull WorkflowDebugDto debugDto) {
        return ApiResult.success(workflowService.nodeDebug(nodeId, debugDto));
    }

    @PostMapping("/dialog")
    public ApiResult<Object> saveDialog(@RequestBody @NotNull WorkflowDialog dialog) {
        return ApiResult.success(workflowService.saveDialog(dialog));
    }

    @GetMapping("/dialog/list")
    public ApiResult<List<WorkflowDialog>> listDialog(@RequestParam @NotNull Long workflowId, @RequestParam(required = false) Integer type) {
        return ApiResult.success(workflowService.listDialog(workflowId, type));
    }

    @GetMapping("/dialog/clear")
    public ApiResult<Object> clearDialog(@RequestParam @NotNull Long workflowId, @RequestParam(required = false) Integer type) {
        return ApiResult.success(workflowService.clearDialog(workflowId, type));
    }

    // ---------------------- Publish Control ----------------------

    @GetMapping("/can-publish")
    public ApiResult<Object> canPublish(@RequestParam @NotNull Long id) {
        return ApiResult.success(workflowService.getById(id).getCanPublish());
    }

    @GetMapping("/can-publish-set")
    public ApiResult<Object> canPublishSet(@RequestParam Long id) {
        log.info("workflow[{}] set unpublished ,operator = {}", id, UserInfoManagerHandler.get());
        return ApiResult.success(workflowService.canPublishSet(id));
    }

    @GetMapping("/can-publish-set-not")
    public ApiResult<Object> canPublishSetNot(@RequestParam @NotNull Long id) {
        log.info("workflow[{}] set unpublished, operator={}", id, UserInfoManagerHandler.get());
        return ApiResult.success(workflowService.canPublishSetNot(id));
    }

    // ---------------------- Run/Evaluation/Square ----------------------

    @PostMapping("/code/run")
    public ApiResult<Object> runCode(@RequestBody @NotNull Object runCodeData) {
        return ApiResult.success(workflowService.runCode(runCodeData));
    }

    @GetMapping("/square")
    public ApiResult<Object> square(
            @NotNull(message = "Pagination parameters cannot be null") Pagination pagination,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer tagFlag,
            @RequestParam(required = false) Integer tags) {
        if (pagination.isEmpty()) {
            throw new BusinessException(ResponseEnum.PAGE_SEPARATOR_MISS);
        }
        return ApiResult.success(workflowService.getSquare(pagination.getCurrent(), pagination.getPageSize(), search, tagFlag, tags));
    }

    @PostMapping("/public-copy")
    public ApiResult<Object> publicCopy(@RequestBody @NotNull WorkflowReq req) {
        return ApiResult.success(workflowService.publicCopy(req));
    }

    @GetMapping("/auto-add-eval-set-data")
    public ApiResult<Object> autoAddEvalSetData(@RequestParam @NotNull Long id) {
        return ApiResult.success(workflowService.getAutoAddEvalSetData(id));
    }

    @GetMapping("/node-template")
    public ApiResult<Object> getNodeTemplate(@RequestParam(required = false) Integer source) {
        return ApiResult.success(workflowService.getNodeTemplate(source));
    }

    /**
     * Whether it is a "Simple IO" workflow (affects evaluation templates).
     */
    @GetMapping("/is-simple-io")
    public ApiResult<Object> isSimpleIo(@RequestParam @NotNull Long id) {
        return ApiResult.success(workflowService.isSimpleIo(id));
    }

    @GetMapping("trainable-nodes")
    public ApiResult<Object> trainableNodes(@RequestParam @NotNull Long id) {
        return ApiResult.success(workflowService.trainableNodes(id));
    }

    @GetMapping("/eval-page-first-time")
    public ApiResult<Object> evalPageFirstTime(@RequestParam @NotNull Long id) {
        return ApiResult.success(workflowService.evalPageFirstTime(id));
    }

    // ---------------------- SSE (Chat) ----------------------

    /**
     * SSE chat interface.
     *
     * <p>
     * Note: If using Nginx as gateway, buffering has been disabled via "X-Accel-Buffering: no".
     */
    @PostMapping(path = "/chat", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter chat(@RequestBody @NotNull ChatBizReq bizReq, HttpServletResponse response, HttpServletRequest request) {
        response.addHeader("X-Accel-Buffering", "no");
        return workflowService.sseChat(bizReq);
    }

    @PostMapping(path = "/resume", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter resume(@RequestBody @NotNull ChatResumeReq bizReq, HttpServletResponse response, HttpServletRequest request) {
        response.addHeader("X-Accel-Buffering", "no");
        return workflowService.sseChatResume(bizReq);
    }

    // ---------------------- File Upload/Input Information ----------------------

    /**
     * File upload.
     */
    @PostMapping("/upload-file")
    public ApiResult<Object> uploadFile(@RequestParam("files") MultipartFile[] files, @RequestParam String flowId) {
        return ApiResult.success(workflowService.uploadFile(files, flowId));
    }

    @GetMapping("/get-inputs-yype")
    public ApiResult<Object> getInputsType(@RequestParam @NotBlank String flowId) {
        return ApiResult.success(workflowService.getInputsType(flowId));
    }

    @GetMapping("/get-inputs-info")
    public ApiResult<Object> getInputsInfo(@RequestParam @NotBlank String flowId) {
        return ApiResult.success(workflowService.getInputsInfo(flowId));
    }

    // ---------------------- Model Information / Error Information ----------------------

    @PostMapping("/get-model-info")
    public ApiResult<Object> getModelInfo(@RequestBody @NotNull WorkflowModelReq workflowReq) {
        return ApiResult.success(workflowService.getModelInfo(workflowReq));
    }

    @PostMapping("/get-node-error-info")
    public ApiResult<Object> getNodeErrorInfo(@RequestBody @NotNull WorkflowModelErrorReq workflowModelErrorReq) {
        return ApiResult.success(workflowService.getNodeErrorInfo(workflowModelErrorReq));
    }

    @PostMapping("/get-user-feedback-error-info")
    public ApiResult<Object> getUserFeedbackErrorInfo(@RequestBody @NotNull WorkflowModelErrorReq workflowModelErrorReq) {
        return ApiResult.success(workflowService.getUserFeedbackErrorInfo(workflowModelErrorReq));
    }

    // ---------------------- MCP Tools/Strategy ----------------------

    @GetMapping("/get-mcp-server-list")
    public ApiResult<Object> getMcpServerList(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "1000") Integer pageSize,
            HttpServletRequest request) {
        return ApiResult.success(workflowService.getMcpServerList(categoryId, pageNo, pageSize, request));
    }

    @GetMapping("/get-mcp-server-list-locally")
    public ApiResult<List<McpServerTool>> getMcpServerListLocally(@RequestParam(required = false) String categoryId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "1000") Integer pageSize,
            @RequestParam(required = false) Boolean authorized,
            HttpServletRequest request) {
        return ApiResult.success(workflowService.getMcpServerListLocally(categoryId, pageNo, pageSize, authorized, request));
    }

    @GetMapping("/get-agent-strategy")
    public ApiResult<Object> getAgentStrategy() {
        return ApiResult.success(workflowService.getAgentStrategy());
    }

    @GetMapping("/get-knowledge-pro-strategy")
    public ApiResult<Object> getKnowledgeProStrategy() {
        return ApiResult.success(workflowService.getKnowledgeProStrategy());
    }

    @PostMapping("/debug-server-tool")
    public ApiResult<Object> debugServerTool(@RequestBody @Validated McpToolReq req) {
        return ApiResult.success(workflowService.debugServerTool(req));
    }

    @GetMapping("/get-server-tool-detail")
    public ApiResult<Object> getServerToolDetail(@RequestParam @NotBlank String serverId) {
        return ApiResult.success(workflowService.getServerToolDetail(serverId));
    }

    @GetMapping("/get-server-tool-detail-locally")
    public ApiResult<McpServerToolDetailVO> getServerToolDetailLocally(@RequestParam String serverId) {
        return ApiResult.success(workflowService.getServerToolDetailLocally(serverId));
    }

    @GetMapping("/get-env-key")
    public ApiResult<Object> andEnvKey(@RequestParam @NotBlank String serverId, HttpServletRequest request) {
        return ApiResult.success(workflowService.andEnvKey(serverId, request));
    }

    @PostMapping("/push-env-key")
    public ApiResult<Object> pushEnvKey(@RequestBody @NotNull McpPushDto req, HttpServletRequest request) {
        return ApiResult.success(workflowService.pushEnvKey(req, request));
    }

    @GetMapping("/replace-appId")
    public ApiResult<Object> replaceAppId(@RequestParam @NotBlank String appId, @RequestParam @NotBlank String flowId) {
        return ApiResult.success(workflowService.replaceAppId(appId, flowId));
    }

    @GetMapping("/has-qa-node")
    public ApiResult<Object> hasQaNode(@RequestParam @NotNull Integer botId) {
        return ApiResult.success(workflowService.hasQaNode(botId));
    }

    // ---------------------- Prompt Comparison ----------------------

    @PostMapping("/add-comparisons")
    public ApiResult<Object> addComparisons(@RequestBody @NotNull WorkflowComparisonReq workflowComparisonReq) {
        return ApiResult.success(workflowService.addComparisons(workflowComparisonReq));
    }

    @PostMapping("/delete-comparisons")
    public ApiResult<Object> deleteComparisons(@RequestBody @NotNull WorkflowComparisonReq workflowComparisonReq) {
        return ApiResult.success(workflowService.deleteComparisons(workflowComparisonReq));
    }

    /**
     * Get user-created workflows (by status).
     */
    @GetMapping("/get-list-by-LLM")
    public ApiResult<Object> list(HttpServletRequest request, @RequestParam(required = false) String search) {
        return ApiResult.success(workflowService.listByStatus(request, search));
    }

    /**
     * Get workflow prompt comparison status.
     */
    @GetMapping("/get-workflow-prompt-status")
    public ApiResult<Object> getWorkflowPromptStatus(@RequestParam @NotNull Long id) {
        return ApiResult.success(workflowService.getWorkflowPromptStatus(id));
    }

    // ---------------------- Export/Import YAML ----------------------

    /**
     * Export workflow as YAML.
     *
     * <p>
     * Note: Add filename to avoid browser downloading as unnamed file.
     */
    @GetMapping("/export/{id}")
    public void exportYaml(@PathVariable @NotNull Long id, HttpServletResponse response) {
        final Workflow entity = workflowService.getById(id);
        try {
            if (entity == null || StringUtils.isEmpty(entity.getData())) {
                throw new BusinessException(ResponseEnum.WORKFLOW_EXPORT_FAILED);
            }
            // Construct download filename: workflow-{id}.yaml
            final String filename =
                    URLEncoder.encode("workflow-" + id + ".yaml", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            response.setContentType("application/octet-stream");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename);

            workflowExportService.exportWorkflowDataAsYaml(entity, response.getOutputStream());
            response.flushBuffer();
        } catch (BusinessException e) {
            log.error("export yaml business error, id={}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("export yaml unexpected error, id={}", id, e);
            throw new BusinessException(ResponseEnum.WORKFLOW_EXPORT_FAILED);
        }
    }

    /**
     * Import workflow from YAML.
     */
    @PostMapping("/import")
    public ApiResult<Object> importWorkflow(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try (InputStream inputStream = file.getInputStream()) {
            return ApiResult.success(workflowExportService.importWorkflowFromYaml(inputStream, request));
        } catch (Exception e) {
            log.error("import workflow failed, filename={}", file.getOriginalFilename(), e);
            throw new BusinessException(ResponseEnum.WORKFLOW_IMPORT_FAILED);
        }
    }

    // ---------------------- Prompt Comparison (Save/List) ----------------------

    @PostMapping("/save-comparisons")
    public ApiResult<String> saveComparisons(@RequestBody @NotNull List<WorkflowComparisonSaveReq> workflowComparisonReqList) {
        return ApiResult.success(workflowService.saveComparisons(workflowComparisonReqList));
    }

    @GetMapping("/list-comparisons")
    public ApiResult<List<WorkflowComparison>> listComparisons(@RequestParam @NotBlank String promptId) {
        return ApiResult.success(workflowService.listComparisons(promptId));
    }

    // ---------------------- Feedback ----------------------

    @PostMapping("/feedback")
    public ApiResult<Void> feedback(@RequestBody @NotNull WorkflowFeedbackReq workflowFeedbackReq, HttpServletRequest request) {
        workflowService.feedback(workflowFeedbackReq, request);
        return ApiResult.success();
    }

    @GetMapping("/feedback-list")
    public ApiResult<List<WorkflowFeedback>> getFeedbackList(@RequestParam @NotBlank String flowId) {
        return ApiResult.success(workflowService.getFeedbackList(flowId));
    }

    // ---------------------- Advanced Configuration / Templates / Versions ----------------------

    /**
     * Get workflow advanced configuration (background image, etc.).
     */
    @GetMapping("/get-flow-advanced-config")
    public ApiResult<Object> getFlowAdvancedConfig(@RequestParam @NotNull Integer botId) {
        return ApiResult.success(workflowService.getFlowAdvancedConfig(botId));
    }

    /**
     * Agent node Prompt template list.
     */
    @GetMapping("/agent-node/prompt-template")
    public ApiResult<Object> promptTemplate(@NotNull(message = "Pagination parameters cannot be null") Pagination pagination, @RequestParam(required = false) String search) {
        if (pagination.isEmpty()) {
            throw new BusinessException(ResponseEnum.PAGE_SEPARATOR_MISS);
        }
        return ApiResult.success(workflowService.listPagePromptTemplate(pagination.getCurrent(), pagination.getPageSize(), search));
    }

    /**
     * Copy workflow protocol (source -> target).
     */
    @GetMapping("/copy-flow")
    public ApiResult<Object> copyFlow(@RequestParam @NotBlank String sourceFlowId, @RequestParam @NotBlank String targetFlowId) {
        return ApiResult.success(workflowService.copyFlow(sourceFlowId, targetFlowId));
    }

    /**
     * Get maximum version number for a specific FlowId.
     */
    @GetMapping("/get-max-version")
    public ApiResult<Object> getMaxVersion(@RequestParam @NotBlank String flowId) {
        return ApiResult.success(workflowService.getMaxVersionByFlowId(flowId));
    }

    /**
     * Obtain speech model configuration
     *
     * @param botId
     * @param version
     * @return
     */
    @GetMapping("/get-talk-agent-config")
    public ApiResult<TalkAgentConfigDto> getTalkAgentConfig(@RequestParam Integer botId, @RequestParam(required = false) String version, @RequestParam String type) {
        return ApiResult.success(talkAgentService.getTalkAgentConfig(botId, version, type));
    }
}
