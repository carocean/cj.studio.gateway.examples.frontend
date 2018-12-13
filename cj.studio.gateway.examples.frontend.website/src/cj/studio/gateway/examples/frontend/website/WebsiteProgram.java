package cj.studio.gateway.examples.frontend.website;

import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.socket.Destination;
import cj.studio.gateway.socket.app.GatewayAppSiteProgram;
import cj.studio.gateway.socket.app.ProgramAdapterType;

@CjService(name = "$.cj.studio.gateway.app", isExoteric = true)
public class WebsiteProgram extends GatewayAppSiteProgram {

	@Override
	protected void onstart(Destination dest, String home, ProgramAdapterType type)
			throws CircuitException {
		
	}


}
