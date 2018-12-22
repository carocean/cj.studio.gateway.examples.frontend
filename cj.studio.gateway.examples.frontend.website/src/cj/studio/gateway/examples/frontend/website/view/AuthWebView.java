package cj.studio.gateway.examples.frontend.website.view;

import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.Circuit;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.Frame;
import cj.studio.ecm.net.http.HttpFrame;
import cj.studio.gateway.socket.app.IGatewayAppSiteResource;
import cj.studio.gateway.socket.app.IGatewayAppSiteWayWebView;
import cj.studio.gateway.socket.io.XwwwFormUrlencodedContentReciever;

@CjService(name = "/public/auth.service")
public class AuthWebView implements IGatewayAppSiteWayWebView {

	@Override
	public void flow(Frame f, Circuit c, IGatewayAppSiteResource ctx) throws CircuitException {
		f.content().accept(new XwwwFormUrlencodedContentReciever() {

			@Override
			protected void done(Frame frame) throws CircuitException{
				String user=frame.parameter("user");
				String pwd=frame.parameter("pwd");
				HttpFrame hf=(HttpFrame)frame;
				hf.session().attribute("principals",user);
			}
		});
	}

}
