package cj.studio.gateway.examples.frontend.website;

import cj.studio.ecm.Scope;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.frame.Circuit;
import cj.studio.ecm.frame.Frame;
import cj.studio.ecm.graph.CircuitException;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.studio.gateway.socket.pipeline.IOutputSelector;
import cj.studio.gateway.socket.pipeline.IOutputer;
@CjService(name="/" ,scope=Scope.multiton)
public class Home implements IGatewayAppSiteWayWebView {
	@CjServiceRef(refByName="$.output.selector")
	IOutputSelector selector;
	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
//		Element doc=ctx.html("/index.html");
		IOutputer output=selector.select("uc");
		Frame req=new Frame("get /uc/ http/1.1");
		Circuit feeds=new Circuit("http/1.1 200 ok");
		output.send(req, feeds);//之后再实现通过tcp/udt/ws/等异步协议获取backend的restfull消息接收机制（通过回调方法让backend推来，而后该frontend再推向浏览器
		c.content().writeBytes(feeds.content());
	}

}
