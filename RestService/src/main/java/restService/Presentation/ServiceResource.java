package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.PaymentService;
import restService.Application.ReportService;
import restService.Application.TokenService;
import restService.Infrastructure.MessageQueueFactory;

import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
@Path("/services")
public class ServiceResource {

	private TokenService tokenService = new TokenService(new MessageQueueFactory().getMessageQueue());
	private PaymentService paymentService = new PaymentService(new MessageQueueFactory().getMessageQueue());
	private AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
	private ReportService reportService = new ReportService(new MessageQueueFactory().getMessageQueue());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/rest")
	public Response getRestServiceStatus() {
		try {
			return Response.status(Response.Status.OK).entity("Rest service ready").build();			
		} catch (Exception e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/token")
	public Response getTokenServiceStatus() {
		try {
			String status = tokenService.getStatus(UUID.randomUUID().toString());
			return Response.status(Response.Status.OK).entity(status).build();
		} catch (Exception e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/payment")
	public Response getPaymentServiceStatus() {

		try {
			String status = paymentService.getStatus(UUID.randomUUID().toString());
			return Response.status(Response.Status.OK).entity(status).build();
		} catch (Exception e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/account")
	public Response getAccountServiceStatus() {
		try {
			String status = accountService.getStatus(UUID.randomUUID().toString());
			return Response.status(Response.Status.OK).entity(status).build();
		} catch (Exception e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/report")
	public Response getReportServiceStatus() {
		try {
			String status = reportService.getStatus(UUID.randomUUID().toString());
			return Response.status(Response.Status.OK).entity(status).build();
		} catch (Exception e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(e.getMessage()).build();
		}
	}

}
