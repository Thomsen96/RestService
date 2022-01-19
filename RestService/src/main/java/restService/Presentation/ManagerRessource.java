package restService.Presentation;


import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import messaging.EventResponse;
import restService.Application.ReportService;

@Path("/manager")
public class ManagerRessource {

	ReportService reportService = new ReportService();
	
	@GET
	@Path("/reports")
	public Response getReport() {
    	try {
    		EventResponse outcome = reportService.getMerchantReport(UUID.randomUUID().toString(), "", ReportService.Role.MANAGER);
    		
            if (outcome.isSuccess()) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
            }
    	} catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}	
		
	}

}
