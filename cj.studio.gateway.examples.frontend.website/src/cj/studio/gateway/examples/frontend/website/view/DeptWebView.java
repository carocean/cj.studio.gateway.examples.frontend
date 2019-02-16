package cj.studio.gateway.examples.frontend.website.view;

import java.util.Date;
import java.util.List;

import cj.studio.ecm.annotation.CjBridge;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.gateway.examples.backend.usercenter.bo.DeptmentBO;
import cj.studio.gateway.examples.backend.usercenter.stub.IDeptStub;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.studio.gateway.stub.annotation.CjStubRef;

@CjBridge(aspects = "@rest")
@CjService(name = "/dept.html")
public class DeptWebView implements IGatewayAppSiteWayWebView {

	@CjStubRef(remote = "rest://backend/uc/", stub = IDeptStub.class)
	IDeptStub dept;
	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
		DeptmentBO bo=new DeptmentBO();
		bo.setCreateDate(new Date());
		bo.setLeader("cj");
		bo.setName("wangk");
		dept.save(bo);
		
		List<DeptmentBO> list=dept.getAll();
		System.out.println(list.size());
		
		int i=dept.testArg(bo);
		System.out.println("---"+i);
	}

}
