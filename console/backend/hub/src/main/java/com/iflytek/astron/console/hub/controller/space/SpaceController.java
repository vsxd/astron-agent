package com.iflytek.astron.console.hub.controller.space;


import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.commons.annotation.RateLimit;
import com.iflytek.astron.console.commons.annotation.space.EnterprisePreAuth;
import com.iflytek.astron.console.commons.annotation.space.SpacePreAuth;
import com.iflytek.astron.console.hub.dto.space.SpaceAddDto;
import com.iflytek.astron.console.hub.dto.space.SpaceUpdateDto;
import com.iflytek.astron.console.hub.entity.space.Space;
import com.iflytek.astron.console.commons.util.space.EnterpriseInfoUtil;
import com.iflytek.astron.console.hub.dto.space.EnterpriseSpaceCountVo;
import com.iflytek.astron.console.hub.dto.space.SpaceVo;
import com.iflytek.astron.console.hub.service.space.SpaceService;
import com.iflytek.astron.console.hub.service.space.SpaceBizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Space
 */
@Slf4j
@RestController
@RequestMapping("/space")
@Tag(name = "Space")
@Validated
public class SpaceController {
    @Resource
    private SpaceService spaceService;
    @Resource
    private SpaceBizService spaceBizService;

    @GetMapping("/check-name")
    @Operation(summary = "Check if name exists")
    public ApiResult<Boolean> checkName(@RequestParam(value = "name") String name, @RequestParam(value = "id", required = false) Long id) {
        return ApiResult.success(spaceService.checkExistByName(name, id));
    }

    @GetMapping("/visit-space")
    @Operation(summary = "Visit space")
    public ApiResult<Space> visitSpace(@RequestParam(value = "spaceId", required = false) Long spaceId) {
        return spaceBizService.visitSpace(spaceId);
    }

    @GetMapping("/recent-visit-list")
    @Operation(summary = "Recent visit list")
    public ApiResult<List<SpaceVo>> recentVisitList() {
        return ApiResult.success(spaceService.recentVisitList());
    }

    @GetMapping("/get-last-visit-space")
    @Operation(summary = "Recently visited space")
    public ApiResult<SpaceVo> getLastVisitSpace() {
        return ApiResult.success(spaceService.getLastVisitSpace());
    }

    @GetMapping("/personal-list")
    @Operation(summary = "Personal all spaces")
    public ApiResult<List<SpaceVo>> personalList(@RequestParam(value = "name", required = false) String name) {
        return ApiResult.success(spaceService.personalList(name));
    }

    @GetMapping("/personal-self-list")
    @Operation(summary = "Personal created by me")
    public ApiResult<List<SpaceVo>> personalSelfList(@RequestParam(value = "name", required = false) String name) {
        return ApiResult.success(spaceService.personalSelfList(name));
    }

    @GetMapping("/detail")
    @Operation(summary = "Space details")
    @SpacePreAuth(key = "SpaceController_detail_GET", requireSpaceId = true, module = "Space Management", point = "Get space details", description = "Get space details")
    public ApiResult<SpaceVo> detail() {
        return ApiResult.success(spaceService.getSpaceVo());
    }

    @GetMapping("/send-message-code")
    @Operation(summary = "Delete space send verification code")
    @RateLimit(dimension = "USER", window = 60, limit = 1)
    public ApiResult<String> sendMessageCode(@RequestParam("spaceId") Long spaceId) {
        return spaceBizService.sendMessageCode(spaceId);
    }

    @DeleteMapping("/delete-personal-space")
    @Operation(summary = "Space owner delete space")
    @RateLimit(dimension = "USER", window = 1, limit = 1)
    public ApiResult<String> deletePersonalSpace(@RequestParam("spaceId") Long spaceId, @RequestParam(value = "mobile", required = false) String mobile, @RequestParam("verifyCode") String verifyCode) {
        return spaceBizService.deleteSpace(spaceId, mobile, verifyCode);
    }

    @PostMapping("/oss-version-user-upgrade")
    @Operation(summary = "OSS version user upgrade to enterprise version")
    @RateLimit(dimension = "USER", window = 1, limit = 1)
    public ApiResult<Boolean> ossVersionUserUpgrade() {
        return spaceBizService.ossVersionUserUpgrade();
    }

    // ---------------------------------------------------Personal
    // Version--------------------------------------------------

    @PostMapping("/create-personal-space")
    @Operation(summary = "Personal create space")
    @RateLimit(dimension = "USER", window = 1, limit = 1)
    public ApiResult<Long> createPersonalSpace(@RequestBody @Valid SpaceAddDto spaceAddDto) {
        return spaceBizService.create(spaceAddDto, null);
    }

    @PostMapping("/update-personal-space")
    @Operation(summary = "Personal edit space information")
    @SpacePreAuth(key = "SpaceController_updatePersonalSpace_POST", requireSpaceId = true, module = "Space Management", point = "Edit space information", description = "Edit space information")
    @RateLimit(dimension = "USER", window = 1, limit = 1)
    public ApiResult<String> updatePersonalSpace(@RequestBody @Valid SpaceUpdateDto spaceUpdateDto) {
        return spaceBizService.updateSpace(spaceUpdateDto);
    }

    // ---------------------------------------------------Enterprise
    // Version--------------------------------------------------

    @PostMapping("/create-corporate-space")
    @Operation(summary = "Enterprise create space")
    @EnterprisePreAuth(key = "SpaceController_createCorporateSpace_POST", module = "Team/Enterprise Level Space Management", description = "Create space")
    @RateLimit(dimension = "USER", window = 1, limit = 1)
    public ApiResult<Long> createCorporateSpace(@RequestBody @Valid SpaceAddDto spaceAddDto) {
        return spaceBizService.create(spaceAddDto, EnterpriseInfoUtil.getEnterpriseId());
    }

    @DeleteMapping("/delete-corporate-space")
    @Operation(summary = "Enterprise delete space")
    @EnterprisePreAuth(key = "SpaceController_deleteCorporateSpace_DELETE", module = "Team/Enterprise Level Space Management", description = "Delete space")
    @RateLimit(dimension = "USER", window = 1, limit = 1)
    public ApiResult<String> deleteCorporateSpace(@RequestParam("spaceId") Long spaceId, @RequestParam("mobile") String mobile, @RequestParam("verifyCode") String verifyCode) {
        return spaceBizService.deleteSpace(spaceId, mobile, verifyCode);
    }

    @PostMapping("/update-corporate-space")
    @Operation(summary = "Enterprise edit space information")
    @EnterprisePreAuth(key = "SpaceController_updateCorporateSpace_POST", module = "Team/Enterprise Level Space Management", description = "Edit space information")
    @RateLimit(dimension = "USER", window = 1, limit = 1)
    public ApiResult<String> updateCorporateSpace(@RequestBody @Valid SpaceUpdateDto spaceUpdateDto) {
        return spaceBizService.updateSpace(spaceUpdateDto);
    }

    @GetMapping("/corporate-list")
    @Operation(summary = "Enterprise all spaces")
    @EnterprisePreAuth(key = "SpaceController_corporateList_GET", module = "Team/Enterprise Level Space Management", description = "Enterprise all spaces")
    public ApiResult<List<SpaceVo>> corporateList(@RequestParam(value = "name", required = false) String name) {
        return ApiResult.success(spaceService.corporateList(name));
    }

    @GetMapping("/corporate-count")
    @Operation(summary = "Enterprise all spaces count")
    @EnterprisePreAuth(key = "SpaceController_corporateCount_GET", module = "Team/Enterprise Level Space Management", description = "Enterprise all spaces count")
    public ApiResult<EnterpriseSpaceCountVo> corporateCount() {
        return ApiResult.success(spaceService.corporateCount());
    }

    @GetMapping("/corporate-join-list")
    @Operation(summary = "Enterprise my spaces")
    @EnterprisePreAuth(key = "SpaceController_corporateJoinList_GET", module = "Team/Enterprise Level Space Management", description = "Enterprise my spaces")
    public ApiResult<List<SpaceVo>> corporateJoinList(@RequestParam(value = "name", required = false) String name) {
        return ApiResult.success(spaceService.corporateJoinList(name));
    }

}
