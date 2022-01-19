package restService.Presentation;

import restService.Application.AccountService;
import restService.Application.PaymentService;
import restService.Application.ReportService;
import restService.Application.AccountService.Role;
import restService.Application.TokenService;
import restService.Domain.Token;
import restService.Infrastructure.MessageQueueFactory;

import java.util.HashSet;
import java.util.UUID;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.EventResponse;
import messaging.MessageQueue;

@Path("/services")
public class ServiceResource {

	private TokenService tokenService = new TokenService(new MessageQueueFactory().getMessageQueue());
	private PaymentService paymentService = new PaymentService(new MessageQueueFactory().getMessageQueue());
	private AccountService accountService = new AccountService(new MessageQueueFactory().getMessageQueue());
	private ReportService reportService = new ReportService(new MessageQueueFactory().getMessageQueue());


	@GET
	@Path("/token")
	public Response getTokenServiceStatus() {
		return Response.status(Response.Status.OK).entity(tokenService.getStatus(UUID.randomUUID().toString()))
				.build();
	}

	@GET
	@Path("/payment")
	public Response getPaymentServiceStatus() {
		return Response.status(Response.Status.OK).entity(paymentService.getStatus(UUID.randomUUID().toString()))
				.build();
	}

	@GET
	@Path("/account")
	public Response getAccountServiceStatus() {
		return Response.status(Response.Status.OK).entity(accountService.getStatus(UUID.randomUUID().toString()))
				.build();
	}

	@GET
	@Path("/report")
	public Response getReportServiceStatus() {
		return Response.status(Response.Status.OK).entity(reportService.getStatus(UUID.randomUUID().toString()))
				.build();
	}

}
