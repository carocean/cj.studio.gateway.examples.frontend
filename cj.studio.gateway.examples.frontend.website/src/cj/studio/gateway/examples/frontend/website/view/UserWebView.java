package cj.studio.gateway.examples.frontend.website.view;

import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.gateway.examples.backend.usercenter.bo.UserBO;
import cj.studio.gateway.examples.backend.usercenter.stub.IUserStub;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.studio.gateway.stub.annotation.CjStubRef;

@CjBridge(aspects = "rest")
@CjService(name = "/user.html")
public class UserWebView implements IGatewayAppSiteWayWebView {

	@CjStubRef(remote = "rest://backend/uc/", stub = IUserStub.class)
	IUserStub user;
	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
		UserBO bo=user.getUser("1001479428");
		System.out.println("------"+bo);
	}

}
