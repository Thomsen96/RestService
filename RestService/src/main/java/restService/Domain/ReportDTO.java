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
    public String getCustomerId() {
      return customerId;
    }
    public void setCustomerId(String customerId) {
      this.customerId = customerId;
    }
    public String getMerchantId() {
      return merchantId;
    }
    public void setMerchantId(String merchantId) {
      this.merchantId = merchantId;
    }
    public String getToken() {
      return token;
    }
    public void setToken(String token) {
      this.token = token;
    }
    public String getAmount() {
      return amount;
    }
    public void setAmount(String amount) {
      this.amount = amount;
    }
  }

  public static class Customer {
    public ArrayList<CustomerPayment> payments;

    public Customer(ArrayList<CustomerPayment> payments) {
      this.payments = payments;
    }

    public Customer() {
    }

    @Override
    public String toString() {
      return "Customer [payments=" + payments + "]";
    }

    public ArrayList<CustomerPayment> getPayments() {
      return payments;
    }

    public void setPayments(ArrayList<CustomerPayment> payments) {
      this.payments = payments;
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
		public String getMerchantId() {
			return merchantId;
		}
		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
	}

	public static class Merchant {
		ArrayList<MerchantPayment> payments;

		public Merchant(ArrayList<MerchantPayment> payments) {
			this.payments = payments;
		}

		public Merchant() {
		}

		public ArrayList<MerchantPayment> getPayments() {
			return payments;
		}

		public void setPayments(ArrayList<MerchantPayment> payments) {
			this.payments = payments;
		}

		@Override
		public String toString() {
			return "Merchant [payments=" + payments + "]";
		}
	}
  
}
