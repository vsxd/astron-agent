package com.iflytek.astron.console.hub.controller.tool;

import com.iflytek.astron.console.commons.annotation.space.SpacePreAuth;
import com.iflytek.astron.console.commons.constant.ResponseEnum;
import com.iflytek.astron.console.commons.exception.BusinessException;
import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.entity.dto.*;
import com.iflytek.astron.console.hub.service.tool.ToolBoxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tool")
@Slf4j
@Tag(name = "Plugin Management")
public class ToolBoxController {
    @Resource
    ToolBoxService toolBoxService;

    @PostMapping("/create-tool")
    @Operation(summary = "Create plugin")
    @SpacePreAuth(key = "ToolBoxController_createTool_POST")
    public ApiResult<Object> createTool(@RequestBody ToolBoxDto toolBoxDto) {
        if (toolBoxDto.getName() == null) {
            throw new BusinessException(ResponseEnum.TOOLBOX_NAME_EMPTY);
        }
        if (toolBoxDto.getDescription() == null) {
            throw new BusinessException(ResponseEnum.TOOLBOX_NAME_EMPTY);
        }
        return ApiResult.success(toolBoxService.createTool(toolBoxDto));
    }

    @PostMapping("/temporary-tool")
    @Operation(summary = "Temporarily save plugin")
    @SpacePreAuth(key = "ToolBoxController_temporaryTool_POST")
    public ApiResult<Object> temporaryTool(@RequestBody ToolBoxDto toolBoxDto) {
        if (toolBoxDto.getName() == null) {
            throw new BusinessException(ResponseEnum.TOOLBOX_NAME_EMPTY);
        }
        return ApiResult.success(toolBoxService.temporaryTool(toolBoxDto));
    }

    @PutMapping("/update-tool")
    @Operation(summary = "Edit plugin")
    @SpacePreAuth(key = "ToolBoxController_updateTool_PUT")
    public ApiResult<Object> updateTool(@RequestBody ToolBoxDto toolBoxDto) {
        return ApiResult.success(toolBoxService.updateTool(toolBoxDto));
    }

    @GetMapping("/list-tools")
    @Operation(summary = "Plugin paginated list")
    @SpacePreAuth(key = "ToolBoxController_listTools_GET")
    public ApiResult<Object> listTools(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "status", required = false) Integer status) {
        return ApiResult.success(toolBoxService.pageListTools(pageNo, pageSize, content, status));
    }

    @GetMapping("/detail")
    @Operation(summary = "Plugin details")
    @SpacePreAuth(key = "ToolBoxController_getDetail_GET")
    public ApiResult<Object> getDetail(@RequestParam("id") Long id, Boolean temporary) {
        return ApiResult.success(toolBoxService.getDetail(id, temporary));
    }

    @GetMapping("/get-tool-default-icon")
    @Operation(summary = "Plugin default icon")
    @SpacePreAuth(key = "ToolBoxController_getToolDefaultIcon_GET")
    public ApiResult<Object> getToolDefaultIcon() {
        return ApiResult.success(toolBoxService.getToolDefaultIcon());
    }

    @DeleteMapping("/delete-tool")
    @Operation(summary = "Delete plugin")
    @SpacePreAuth(key = "ToolBoxController_deleteTool_DELETE")
    public ApiResult<Object> deleteTool(@RequestParam("id") Long id) {
        return ApiResult.success(toolBoxService.deleteTool(id));
    }

    @PostMapping("/debug-tool")
    @Operation(summary = "Debug plugin")
    @SpacePreAuth(key = "ToolBoxController_debugToolV2_POST")
    public ApiResult<Object> debugToolV2(@RequestBody ToolBoxDto toolBoxDto) {
        return ApiResult.success(toolBoxService.debugToolV2(toolBoxDto));
    }

    @Operation(summary = "Plugin square query list")
    @PostMapping("/list-tool-square")
    @SpacePreAuth(key = "ToolBoxController_listToolSquare_POST")
    public ApiResult<Object> listToolSquare(@RequestBody ToolSquareDto dto) {
        return ApiResult.success(toolBoxService.listToolSquare(dto));
    }

    @Operation(summary = "Favorite/Unfavorite tool")
    @GetMapping("/favorite")
    @SpacePreAuth(key = "ToolBoxController_favorite_GET")
    public ApiResult<Object> favorite(@RequestParam("toolId") String toolId,
            @RequestParam("favoriteFlag") Integer favoriteFlag,
            @RequestParam("isMcp") Boolean isMcp) {
        return ApiResult.success(toolBoxService.favorite(toolId, favoriteFlag, isMcp));
    }

    @Operation(summary = "Get plugin version history")
    @GetMapping("/get-tool-version")
    @SpacePreAuth(key = "ToolBoxController_getToolVersion_GET")
    public ApiResult<List<ToolBoxVo>> getToolVersion(@RequestParam("toolId") String toolId) {
        return ApiResult.success(toolBoxService.getToolVersion(toolId));
    }

    @Operation(summary = "Get plugin latest version")
    @GetMapping("/get-tool-latestVersion")
    @SpacePreAuth(key = "ToolBoxController_getToolLatestVersion_GET")
    public ApiResult<Map<String, String>> getToolLatestVersion(@RequestParam("toolIds") List<String> toolIds) {
        return ApiResult.success(toolBoxService.getToolLatestVersion(toolIds));
    }

    @Operation(summary = "Plugin user operation history")
    @GetMapping("/add-tool-operateHistory")
    public ApiResult<Void> addToolOperateHistory(@RequestParam("toolId") String toolId) {
        toolBoxService.addToolOperateHistory(toolId);
        return ApiResult.success();
    }

    @Operation(summary = "User feedback")
    @PostMapping("/feedback")
    public ApiResult<Void> addToolOperateHistory(@RequestBody ToolBoxFeedbackReq toolBoxFeedbackReq) {
        toolBoxService.feedback(toolBoxFeedbackReq);
        return ApiResult.success();
    }

    @Operation(summary = "Publish tool to square")
    @GetMapping("/publish-square")
    public ApiResult<Void> publishSquare(Long id) {
        toolBoxService.publishSquare(id);
        return ApiResult.success();
    }

    @Operation(summary = "Export tool")
    @GetMapping("/export")
    // @SpacePreAuth(key = "ToolBoxController_exportTool_GET")
    public void exportTool(@RequestParam("id") Long id,
            @RequestParam(value = "type", required = false) Integer type,
            HttpServletResponse response) {
        toolBoxService.exportTool(id, type, response);
    }

    @Operation(summary = "Import tool")
    @PostMapping("/import")
    // @SpacePreAuth(key = "ToolBoxController_importTool_POST")
    public ApiResult<Object> importTool(@RequestParam("file") MultipartFile file) {
        return ApiResult.success(toolBoxService.importTool(file));
    }

}
