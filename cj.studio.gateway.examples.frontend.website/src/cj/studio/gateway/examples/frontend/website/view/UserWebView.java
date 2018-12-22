package cj.studio.gateway.examples.frontend.website.view;

import java.util.List;

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

@CjBridge(aspects = "@rest")
@CjService(name = "/user.html")
public class UserWebView implements IGatewayAppSiteWayWebView {

	@CjStubRef(remote = "rest://backend/uc/", stub = IUserStub.class)
	IUserStub user;
	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
		UserBO bo=user.getUser("1001479428");
		System.out.println("----1--"+bo);
		UserBO bo1=new UserBO();
		bo1.setAge(33);
		String id=f.hashCode()+"";
		bo1.setId(id);
		bo1.setName("wuj");
		user.save(bo1);
		UserBO bo2=user.getUser(id);
		System.out.println("----2--"+bo2);
		List<UserBO> list=user.query("wuj", 33);
		System.out.println("----3--"+list.size());
	}

}
