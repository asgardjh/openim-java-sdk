package org.ccs.openim.chat.req;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UpdateUserInfoReq {

    private String userID;
    private String account;
    private String phoneNumber;
    private String areaCode;
    @Email
    private String email;

    private String nickname;
    private String faceURL;
    private Integer gender;
    private Integer level;
    private Long birth;

    private Integer allowAddFriend;
    private Integer allowBeep;
    private Integer allowVibration;
    private Integer globalRecvMsgOpt;
}
