package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.AccountService.Role;
import restService.Infrastructure.MessageQueueFactory;

import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/manager")
public class ManagerRessource {

  @GET
  @Path("/reports")
  public Response getReport() {
    return Response
        .status(Response.Status.OK)
        .entity(String.format("Report requested for manager"))
        .build();
  }

}
