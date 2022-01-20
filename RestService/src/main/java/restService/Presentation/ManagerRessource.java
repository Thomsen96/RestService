package restService.Presentation;


import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.EventResponse;
import restService.Application.ReportService;
import restService.Domain.Payment;

@Path("/manager")
public class ManagerRessource {

	ReportService reportService = new ReportService();
	
	@GET
	@Path("/reports")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReport() {
    	try {
    		EventResponse outcome = reportService.getMerchantReport(UUID.randomUUID().toString(), "", ReportService.Role.MANAGER);
    		
            if (outcome.isSuccess()) {
            	Payment[] payments = outcome.getArgument(0, Payment[].class);
                return Response.status(Response.Status.OK).entity(payments).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(outcome.getErrorMessage()).build();
            }
    	} catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getStackTrace()).build();
    	}	
		
	}

}
