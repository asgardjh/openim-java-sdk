package org.ccs.openim.chat.req;

import lombok.Data;

import java.util.List;

/**
 * @author chenjh
 */
@Data
public class FindUserFullInfoReq {
    private List<String> userIDs;
}
