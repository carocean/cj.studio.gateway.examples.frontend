package cj.studio.gateway.examples.frontend.website.valve;

import cj.studio.ecm.Scope;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;
import cj.studio.ecm.net.http.HttpCircuit;
import cj.studio.ecm.net.http.HttpFrame;
import cj.studio.ecm.net.session.ISession;
import cj.studio.gateway.socket.pipeline.IAnnotationInputValve;
import cj.studio.gateway.socket.pipeline.IIPipeline;
import cj.ultimate.util.StringUtil;

@CjService(name="InputValve1",scope=Scope.multiton)
public class SecurityValve implements IAnnotationInputValve{
	
	@Override
	public void onActive(String inputName,  IIPipeline pipeline)
			throws CircuitException {
		pipeline.nextOnActive(inputName, this);
	}

	@Override
	public void flow(Object request, Object response, IIPipeline pipeline) throws CircuitException {
		if(request instanceof HttpFrame) {
			HttpFrame frame=(HttpFrame)request;
			if(frame.relativePath().startsWith("/public")) {
				pipeline.nextFlow(request, response, this);
				return;
			}
			ISession session=frame.session();
			if(session==null) {//放过资源
				pipeline.nextFlow(request, response, this);
				return;
			}
			String principals=(String)session.attribute("principals");
			if(StringUtil.isEmpty(principals)) {
				HttpCircuit c=(HttpCircuit)response;
				c.status("302");
				c.message("redirect url.");
				c.head("Location","/website/public/login.html");
				return;
			}
		}
		pipeline.nextFlow(request, response, this);
	}

	@Override
	public void onInactive(String inputName, IIPipeline pipeline) throws CircuitException {
		pipeline.nextOnInactive(inputName, this);
	}

	@Override
	public int getSort() {
		return 1;
	}

}
