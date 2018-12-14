package cj.studio.gateway.examples.frontend.website;

import cj.studio.ecm.Scope;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.ecm.net.io.MemoryContentReciever;
import cj.studio.ecm.net.io.MemoryInputChannel;
import cj.studio.ecm.net.io.MemoryOutputChannel;
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
		IOutputer back = selector.select("backend");// 回发

		MemoryInputChannel in = new MemoryInputChannel(8192);
		Frame f1 = new Frame(in, "post /uc/ http/1.1");
		f1.contentType("application/x-www-form-urlencoded");

		MemoryContentReciever mcr = new MemoryContentReciever();
		f1.content().accept(mcr);
		in.begin(null);
		byte[] b = "name=zhaoxb&type=1&age=10&dept=国务院".getBytes();
		in.done(b, 0, b.length);

		MemoryOutputChannel out = new MemoryOutputChannel();
		Circuit c1 = new Circuit(out, "http/1.1 200 ok");

		back.send(f1, c1);

		byte[] ret = out.readFully();
		c.content().writeBytes(ret);

		back.closePipeline();
	}

}
