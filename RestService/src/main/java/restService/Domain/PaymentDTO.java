package restService.Domain;

public class PaymentDTO {

	public String token;
	public String merchant;
	public String amount;
	public String description;

	public PaymentDTO() {

	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("PaymentDTO{");
		sb.append("token='").append(token).append('\'');
		sb.append(", merchant='").append(merchant).append('\'');
		sb.append(", amount='").append(amount).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
