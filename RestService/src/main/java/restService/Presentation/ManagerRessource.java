package restService.Presentation;


import javax.ws.rs.*;
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
