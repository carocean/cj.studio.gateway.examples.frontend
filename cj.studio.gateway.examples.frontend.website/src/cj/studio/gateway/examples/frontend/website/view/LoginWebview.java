package cj.studio.gateway.examples.frontend.website.view;

import org.jsoup.nodes.Element;

import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;

@CjService(name = "/public/login.html")
public class LoginWebview implements IGatewayAppSiteWayWebView {
	
	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
		Element doc=ctx.html(f.relativePath());
		c.content().writeBytes(doc.html().getBytes());
	}

}
