package restService.Domain;

import javax.json.bind.annotation.JsonbCreator;

public class AccountDTO {
  public String accountNumber;

  
  public AccountDTO() {
  }
  
  @JsonbCreator
  public AccountDTO(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }
}