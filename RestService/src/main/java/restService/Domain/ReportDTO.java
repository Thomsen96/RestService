package restService.Domain;

import java.util.ArrayList;

public class ReportDTO {

  public static class CustomerPayment {
    public String customerId;
    public String merchantId;
    public String token;
    public String amount;
    public CustomerPayment(String customerId, String merchantId, String token, String amount) {
      this.customerId = customerId;
      this.merchantId = merchantId;
      this.token = token;
      this.amount = amount;
    }
    public CustomerPayment() {
    }
    @Override
    public String toString() {
      return "CustomerPayment [amount=" + amount + ", customerId=" + customerId + ", merchantId=" + merchantId
          + ", token=" + token + "]";
    }
  }

  public static class Customer {
    ArrayList<CustomerPayment> payments;

    public Customer(ArrayList<CustomerPayment> payments) {
      this.payments = payments;
    }

    public Customer() {
    }

    @Override
    public String toString() {
      return "Customer [payments=" + payments + "]";
    }
  }
  

  public static class MerchantPayment {
    public String merchantId;
    public String token;
    public String amount;
    public MerchantPayment(String merchantId, String token, String amount) {
      this.merchantId = merchantId;
      this.token = token;
      this.amount = amount;
    }
    public MerchantPayment() {
    }
    @Override
    public String toString() {
      return "MerchantPayment [amount=" + amount + ", merchantId=" + merchantId
          + ", token=" + token + "]";
    }
  }

  public static class Merchant {
    ArrayList<MerchantPayment> payments;
  }
  
}
