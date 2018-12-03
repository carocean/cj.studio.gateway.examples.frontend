package cj.studio.gateway.examples.frontend.website;

import cj.studio.ecm.Scope;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.frame.Circuit;
import cj.studio.ecm.frame.Frame;
import cj.studio.ecm.frame.IFeedback;
import cj.studio.ecm.graph.CircuitException;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.studio.gateway.socket.pipeline.IOutputSelector;
import cj.studio.gateway.socket.pipeline.IOutputer;
import io.netty.buffer.ByteBuf;

@CjService(name = "/", scope = Scope.multiton)
public class Home implements IGatewayAppSiteWayWebView {
	@CjServiceRef(refByName = "$.output.selector")
	IOutputSelector selector;

	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
//		Element doc=ctx.html("/index.html");
//		test1(f,c);
		test2(f, c);
	}

	void test2(Frame f, Circuit c) throws CircuitException {
		Frame req = new Frame("get /uc/ http/1.1");
		Circuit feeds = new Circuit("http/1.1 200 ok");
		IOutputer output = selector.select("uc");
		output.send(req, feeds);
		c.copyFrom(feeds, true);
		output.closePipeline();
	}

	void test1(Frame f, Circuit c) throws CircuitException {
		Frame req = new Frame("get /uc/ http/1.1");
		Circuit feeds = new Circuit("http/1.1 200 ok", new IFeedback() {

			@Override
			public void begin(Circuit c) {
				// TODO Auto-generated method stub

			}

			@Override
			public void done(ByteBuf arg0, Circuit c) {
				c.content().writeBytes(arg0);
			}

			@Override
			public void write(ByteBuf arg0, Circuit c) throws CircuitException {
				c.content().writeBytes(arg0);
			}

		});
		IOutputer output = selector.select("uc");
		output.send(req, feeds);
		c.copyFrom(feeds, true);
	}

}
