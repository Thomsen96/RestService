package dtu.services.Entities;
import java.util.UUID;

public class Token {

  private String customerId = null;
  private String uuid = null;

  public Token() {
  }
  public Token(String customerId) {
    this.customerId = customerId;
    setUuid(UUID.randomUUID().toString());
  }
  
  public Token(String customerId, String uuid) {
	    this.customerId = customerId;
	    this.setUuid(uuid);
	  }
  
  public String getCustomerId() {
    return customerId;
  }
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
  public String getUuid() {
    return uuid;
  }
public void setUuid(String uuid) {
	this.uuid = uuid;
}



  
}
