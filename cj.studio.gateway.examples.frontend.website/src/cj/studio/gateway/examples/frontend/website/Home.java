package cj.studio.gateway.examples.frontend.website;

import cj.studio.ecm.Scope;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.studio.gateway.socket.pipeline.IOutputSelector;
import cj.studio.gateway.socket.pipeline.IOutputer;

@CjService(name = "/", scope = Scope.multiton)
public class Home implements IGatewayAppSiteWayWebView {
	@CjServiceRef(refByName = "$.output.selector")
	IOutputSelector selector;

	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
//		Element doc=ctx.html("/index.html");
//		test1(f,c);
		test2(f, c);
//		c.content().writeBytes(doc.html().getBytes());
	}

	void test2(Frame f, Circuit c) throws CircuitException {
		Frame req = new Frame("get /uc/ http/1.1");
		Circuit feeds = new Circuit("http/1.1 200 ok");
		IOutputer output = selector.select("uc");
		output.send(req, feeds);
		c.fillFrom(feeds);
//		output.closePipeline();
		output.releasePipeline();
	}

	

}
