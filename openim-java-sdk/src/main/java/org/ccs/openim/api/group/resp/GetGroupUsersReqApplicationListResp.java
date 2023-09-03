package org.ccs.openim.api.group.resp;

import lombok.Data;
import org.ccs.openim.api.vo.GroupRequest;

import java.util.List;

@Data
public class GetGroupUsersReqApplicationListResp {
    private Integer total;
    private List<GroupRequest> groupRequests;
}
