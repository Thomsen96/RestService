package dtu.services;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TokenClient {

	public static final String HTTP_LOCALHOST_8080 = "http://token-service:8182";
	public static final String HTTP_DOCKER_LOCALHOST_8189 = "http://rest-service:8189";
	public static String HTTP_CHOSEN_HOST_AND_PORT = HTTP_LOCALHOST_8080;
	private final Client client = ClientBuilder.newClient();

	public TokenClient(){
		// For CI in jenkins, we need to provide a Docker specific host:port combination
		if ("True".equals(System.getenv("IN_DOCKER_ENV"))){
			HTTP_CHOSEN_HOST_AND_PORT = HTTP_DOCKER_LOCALHOST_8189;
			System.out.println("Running in a Dockerfile has been detected. Changed the host and port.");
		}
	}

	public Response createTokens(int amount, String costumerId) {

		return client
				.target(HTTP_CHOSEN_HOST_AND_PORT)
				.path("tokens/" + costumerId + "/" + amount)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(null);
	}

	public Response getTokens(String costumerId) {
		return client.target(HTTP_CHOSEN_HOST_AND_PORT)
				.path("payments")
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get();
	}

	public Response deleteTokens(String customerId) {
		return client.target(HTTP_CHOSEN_HOST_AND_PORT)
		.path("tokens/" + customerId)
		.request(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.delete();
	}
}
