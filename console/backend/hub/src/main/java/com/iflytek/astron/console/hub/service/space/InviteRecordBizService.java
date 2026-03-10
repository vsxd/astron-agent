package com.iflytek.astron.console.hub.service.space;

import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.dto.space.InviteRecordAddDto;
import com.iflytek.astron.console.hub.enums.space.InviteRecordTypeEnum;
import com.iflytek.astron.console.hub.dto.space.BatchChatUserVo;
import com.iflytek.astron.console.hub.dto.space.ChatUserVo;
import com.iflytek.astron.console.hub.dto.space.InviteRecordVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InviteRecordBizService {

    ApiResult<String> spaceInvite(List<InviteRecordAddDto> dtos);

    ApiResult<String> enterpriseInvite(List<InviteRecordAddDto> dtos);

    ApiResult<String> acceptInvite(Long inviteId);

    ApiResult<String> refuseInvite(Long inviteId);

    ApiResult<String> revokeEnterpriseInvite(Long inviteId);

    ApiResult<String> revokeSpaceInvite(Long inviteId);

    InviteRecordVo getRecordByParam(String param);

    List<ChatUserVo> searchUser(String mobile, InviteRecordTypeEnum type);

    List<ChatUserVo> searchUsername(String username, InviteRecordTypeEnum type);

    ApiResult<BatchChatUserVo> searchUserBatch(MultipartFile file);

    ApiResult<BatchChatUserVo> searchUsernameBatch(MultipartFile file);
}
