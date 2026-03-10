package com.iflytek.astron.console.hub.service.space;

import com.iflytek.astron.console.commons.response.ApiResult;
import com.iflytek.astron.console.hub.dto.space.InviteRecordAddDto;
import com.iflytek.astron.console.hub.enums.space.InviteRecordTypeEnum;
import com.iflytek.astron.console.hub.dto.space.BatchChatUserVO;
import com.iflytek.astron.console.hub.dto.space.ChatUserVO;
import com.iflytek.astron.console.hub.dto.space.InviteRecordVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InviteRecordBizService {

    ApiResult<String> spaceInvite(List<InviteRecordAddDto> dtos);

    ApiResult<String> enterpriseInvite(List<InviteRecordAddDto> dtos);

    ApiResult<String> acceptInvite(Long inviteId);

    ApiResult<String> refuseInvite(Long inviteId);

    ApiResult<String> revokeEnterpriseInvite(Long inviteId);

    ApiResult<String> revokeSpaceInvite(Long inviteId);

    InviteRecordVO getRecordByParam(String param);

    List<ChatUserVO> searchUser(String mobile, InviteRecordTypeEnum type);

    List<ChatUserVO> searchUsername(String username, InviteRecordTypeEnum type);

    ApiResult<BatchChatUserVO> searchUserBatch(MultipartFile file);

    ApiResult<BatchChatUserVO> searchUsernameBatch(MultipartFile file);
}
