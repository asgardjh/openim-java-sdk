package org.ccs.openim.chat.resp;

import lombok.Data;
import org.ccs.openim.chat.vo.UserFullInfo;

import java.util.List;

@Data
public class FindUserFullInfoResp {
    private List<UserFullInfo> users;
}
