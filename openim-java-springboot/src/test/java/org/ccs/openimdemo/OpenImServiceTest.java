package org.ccs.openimdemo;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import junit.framework.TestCase;
import org.ccs.openim.admin.req.LoginReq;
import org.ccs.openim.admin.req.UserLoginCountReq;
import org.ccs.openim.admin.req.UserRegisterCountReq;
import org.ccs.openim.admin.resp.AdminLoginResp;
import org.ccs.openim.admin.resp.NewUserCountResp;
import org.ccs.openim.admin.resp.UserLoginCountResp;
import org.ccs.openim.api.conversation.req.GetAllConversationsReq;
import org.ccs.openim.api.conversation.resp.GetAllConversationsResp;
import org.ccs.openim.api.friend.req.GetPaginationFriendsReq;
import org.ccs.openim.api.friend.resp.GetPaginationFriendsResp;
import org.ccs.openim.api.msg.req.GetServerTimeReq;
import org.ccs.openim.api.msg.resp.GetServerTimeResp;
import org.ccs.openim.api.statistics.resp.UserRegisterCountResp;
import org.ccs.openim.api.user.req.GetUsersOnlineStatusReq;
import org.ccs.openim.api.user.resp.GetUsersOnlineStatusResp_SuccessResult;
import org.ccs.openim.api.user.resp.SingleDetail;
import org.ccs.openim.base.OpenImResult;
import org.ccs.openim.base.OpenImToken;
import org.ccs.openim.base.RequestPagination;
import org.ccs.openim.chat.req.SendVerifyCodeReq;
import org.ccs.openim.admin.clientconfig.resp.GetClientConfigResp;
import org.ccs.openim.chat.resp.LoginResp;
import org.ccs.openim.constants.IMPlatform;
import org.ccs.openim.service.OpenImService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenimDemoApplication.class)
@ActiveProfiles(value = {"openim", "dev"})
public class OpenImServiceTest {
    @Resource
    private OpenImService openImService;

    private static OpenImToken openImToken;

    @Before
    public void initToken() {
        if (openImToken == null) {
//            loginChat();
            loginAdmin();
        }
    }

    private void loginChat() {
        String operationId = IdUtil.fastUUID();
        org.ccs.openim.chat.req.LoginReq loginReq = new org.ccs.openim.chat.req.LoginReq();
        loginReq.setPlatform(5);
        loginReq.setAreaCode("+86");
        loginReq.setPhoneNumber("17607559255");
        loginReq.setPassword("16d7a4fca7442dda3ad93c9a726597e4");
        OpenImResult<LoginResp> result = openImService.chat().login(loginReq, operationId);
        if (result.isOk()) {
            LoginResp loginResp = result.getData();
            openImToken = new OpenImToken(operationId, loginResp.getImToken(), loginResp.getChatToken(), null, loginResp.getUserID());
        }
    }

    private void loginAdmin() {
        String operationId = IdUtil.fastUUID();
        LoginReq loginReq = new LoginReq();
        loginReq.setAccount("openIMAdmin");
        loginReq.setPassword("de84e3477e4fcddc54c9bfbeac4aca2d");
        OpenImResult<AdminLoginResp> result = openImService.admin().user().login(loginReq, operationId);
        if (result.isOk()) {
            AdminLoginResp loginResp = result.getData();
            openImToken = new OpenImToken(operationId, loginResp.getImToken(), null, loginResp.getAdminToken(), loginResp.getImUserID());
        }
    }

    @Test
    public void codeSend() {
        SendVerifyCodeReq req = new SendVerifyCodeReq();
        req.setPlatform(IMPlatform.WEB.getType());
        req.setInvitationCode("+86");
        req.setPhoneNumber("13430701035");
        OpenImResult result = openImService.chat().codeSend(req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void login() {
        String operationId = IdUtil.fastUUID();
        LoginReq loginReq = new LoginReq();
        loginReq.setAccount("openIMAdmin");
        loginReq.setPassword("de84e3477e4fcddc54c9bfbeac4aca2d");
        OpenImResult<AdminLoginResp> result = openImService.admin().user().login(loginReq, operationId);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void getUsersOnlineStatus() {
        GetUsersOnlineStatusReq req = new GetUsersOnlineStatusReq();
        req.setUserIDs(Arrays.asList(openImToken.getUserId()));
        OpenImResult<List<GetUsersOnlineStatusResp_SuccessResult>> result = openImService.api().user().getUsersOnlineStatus(openImToken, req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void getUsersOnlineTokenDetail() {
        GetUsersOnlineStatusReq req = new GetUsersOnlineStatusReq();
        req.setUserIDs(Arrays.asList(openImToken.getUserId()));
        OpenImResult<List<SingleDetail>> result = openImService.api().user().getUsersOnlineTokenDetail(openImToken, req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void getClientConfig() {
        OpenImResult<GetClientConfigResp> result = openImService.admin().clientConfig().getClientConfig(openImToken);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void newUserCount() {
        UserRegisterCountReq req = new UserRegisterCountReq();
        Long curTime = System.currentTimeMillis();
        req.setStart(curTime - 864000 * 1000 * 3);
        req.setEnd(curTime);
        OpenImResult<NewUserCountResp> result = openImService.admin().user().newUserCount(openImToken, req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void loginUserCount() {
        UserLoginCountReq req = new UserLoginCountReq();
        Long curTime = System.currentTimeMillis();
        req.setStart(curTime - 864000 * 1000 * 3);
        req.setEnd(curTime);
        OpenImResult<UserLoginCountResp> result = openImService.admin().user().loginUserCount(openImToken, req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void userRegisterCount() {
        UserRegisterCountReq req = new UserRegisterCountReq();
        Long curTime = System.currentTimeMillis();

        req.setStart(curTime);
        req.setEnd(curTime - 864000 * 1000 * 3);
        OpenImResult<UserRegisterCountResp> result = openImService.api().statistics().userRegisterCount(openImToken, req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }


    @Test
    public void getAllConversations() {
        GetAllConversationsReq req = new GetAllConversationsReq();
        req.setOwnerUserID(openImToken.getUserId());
        OpenImResult<GetAllConversationsResp> result = openImService.api().conversation().getAllConversations(openImToken, req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void getFriendList() {
        GetPaginationFriendsReq req = new GetPaginationFriendsReq();
        req.setUserID(openImToken.getUserId());
        req.setPagination(new RequestPagination(1, 10));
        OpenImResult<GetPaginationFriendsResp> result = openImService.api().friend().getFriendList(openImToken, req);
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }

    @Test
    public void getServerTime() {
        OpenImResult<GetServerTimeResp> result = openImService.api().msg().getServerTime(openImToken, new GetServerTimeReq());
        System.out.println(JSONUtil.toJsonStr(result));
        TestCase.assertTrue(result.getErrMsg(), result.isOk());
    }
}
