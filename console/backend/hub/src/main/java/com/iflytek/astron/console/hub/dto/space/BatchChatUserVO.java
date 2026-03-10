package com.iflytek.astron.console.hub.dto.space;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "Batch user info")
public class BatchChatUserVo {

    private List<ChatUserVo> chatUserVos;
    @Schema(description = "Result file URL")
    private String resultUrl;
}
