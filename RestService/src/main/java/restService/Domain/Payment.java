package restService.Domain;
public class Payment {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((merchantId == null) ? 0 : merchantId.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Payment{");
		sb.append("customerId='").append(customerId).append('\'');
		sb.append(", merchantId='").append(merchantId).append('\'');
		sb.append(", token='").append(token).append('\'');
		sb.append(", amount='").append(amount).append('\'');
		sb.append('}');
		return sb.toString();
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (merchantId == null) {
			if (other.merchantId != null)
				return false;
		} else if (!merchantId.equals(other.merchantId))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	private String customerId;
	private String merchantId;
	private String token;
	private String amount;

	public Payment(String customerId, String merchantId, String token, String amount) {
		this.customerId = customerId;
		this.merchantId = merchantId;
		this.token = token;
		this.amount = amount;
	}

	public Payment() {

	}

	public String getCustomerId() {
		return customerId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public String getToken() {
		return token;
	}

	public String getAmount() {
		return amount;
	}
}
