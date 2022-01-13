package dtu.services;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {

	public static final String HTTP_LOCALHOST_8080 = "http://localhost:8080";
	public static final String HTTP_DOCKER_LOCALHOST_8189 = "http://rest-service:8189";
	
	public static String HTTP_CHOSEN_HOST_AND_PORT = HTTP_LOCALHOST_8080;
	
	public static final String CUSTOMER_ENDPOINT = "/customer";

	
	private final Client client = ClientBuilder.newClient();
	
	public RestClient(){
		// For CI in jenkins, we need to provide a Docker specific host:port combination
		if ("True".equals(System.getenv("IN_DOCKER_ENV"))){
			HTTP_CHOSEN_HOST_AND_PORT = HTTP_DOCKER_LOCALHOST_8189;
			System.out.println("Running in a Dockerfile has been detected. Changed the host and port.");
		}
	}
	
    // Should offer the following:
	// /customer
    // 		/register
    // 		/deregister
    // 		/getTokens
    // 		/getReport
	
	public String rest(String endpoint) {
		return client
				.target(HTTP_CHOSEN_HOST_AND_PORT + endpoint)
		        .request(MediaType.TEXT_PLAIN)
		        .get(String.class);
	}
	
	public String getTokens(String customerID, Integer numberOfTokens) {
		return client
				.target(HTTP_CHOSEN_HOST_AND_PORT + CUSTOMER_ENDPOINT)
		        .request(MediaType.APPLICATION_JSON)
		        .get(String.class);
	}
	

}
