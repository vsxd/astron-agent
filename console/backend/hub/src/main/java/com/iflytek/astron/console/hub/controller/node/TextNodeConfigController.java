package com.iflytek.astron.console.hub.controller.node;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.entity.table.node.TextNodeConfig;
import com.iflytek.astron.console.hub.handler.UserInfoManagerHandler;
import com.iflytek.astron.console.hub.service.node.TextNodeConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

/**
 * @Author clliu19
 * @Date: 2025/3/10 09:17
 */
@RestController
@RequestMapping("/textNode/config")
@Tag(name = "Text node management interface")
public class TextNodeConfigController {
    @Resource
    private TextNodeConfigService textNodeConfigService;

    @PostMapping("/save")
    public ApiResult<Object> save(@RequestBody TextNodeConfig textNodeConfig, HttpServletRequest httpServletRequest) {
        String userId = UserInfoManagerHandler.getUserId();
        textNodeConfig.setUid(userId);
        return ApiResult.success(textNodeConfigService.saveInfo(textNodeConfig));
    }

    @GetMapping("/list")
    public ApiResult<Object> list() {
        String uid = UserInfoManagerHandler.getUserId();
        return ApiResult.success(textNodeConfigService.list(new LambdaQueryWrapper<TextNodeConfig>()
                .in(TextNodeConfig::getUid, Arrays.asList(uid, -1))
                .orderByDesc(TextNodeConfig::getCreateTime)));
    }

    @GetMapping("/delete")
    public ApiResult<Object> delete(Long id) {
        return ApiResult.success(textNodeConfigService.getBaseMapper().delete(new LambdaQueryWrapper<TextNodeConfig>().eq(TextNodeConfig::getId, id)));
    }

    @PostMapping("/update")
    public ApiResult<Object> update(@RequestBody TextNodeConfig textNodeConfig) {
        textNodeConfig.setUpdateTime(new Date());
        return ApiResult.success(textNodeConfigService.updateById(textNodeConfig));
    }
}
