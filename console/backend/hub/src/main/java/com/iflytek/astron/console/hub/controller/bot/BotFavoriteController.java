package com.iflytek.astron.console.hub.controller.bot;

import com.iflytek.astron.console.hub.dto.bot.BotFavoritePageDto;
import com.iflytek.astron.console.hub.dto.bot.BotMarketForm;
import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.service.bot.BotFavoriteService;
import com.iflytek.astron.console.commons.util.I18nUtil;
import com.iflytek.astron.console.commons.util.RequestContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@Tag(name = "Assistant Favorites")
@RestController
@RequestMapping(value = "/bot/favorite")
@RequiredArgsConstructor
public class BotFavoriteController {

    private final BotFavoriteService botFavoriteService;

    @PostMapping(value = "/list")
    public ApiResult<BotFavoritePageDto> list(HttpServletRequest request, @RequestBody BotMarketForm botMarketForm) {
        String uid = RequestContextUtil.getUID();
        String langCode = I18nUtil.getLanguage();
        BotFavoritePageDto pageDto = botFavoriteService.selectPage(botMarketForm, uid, langCode);
        return ApiResult.success(pageDto);
    }

    @PostMapping(value = "/create")
    public ApiResult<Void> create(@RequestParam Integer botId) {
        String uid = RequestContextUtil.getUID();
        botFavoriteService.create(uid, botId);

        return ApiResult.success();
    }

    @PostMapping(value = "/delete")
    public ApiResult<Void> delete(@RequestParam Integer botId) {
        String uid = RequestContextUtil.getUID();
        botFavoriteService.delete(uid, botId);

        return ApiResult.success();
    }
}
