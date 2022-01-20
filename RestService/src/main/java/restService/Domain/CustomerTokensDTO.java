package restService.Domain;

public class CustomerTokensDTO {
		public String customerId;
		public Integer numberOfTokens;

		public CustomerTokensDTO() {

		}

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public Integer getNumberOfTokens() {
			return numberOfTokens;
		}

		public void setNumberOfTokens(Integer numberOfTokens) {
			this.numberOfTokens = numberOfTokens;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof CustomerTokensDTO)) return false;

			CustomerTokensDTO that = (CustomerTokensDTO) o;

			if (getCustomerId() != null ? !getCustomerId().equals(that.getCustomerId()) : that.getCustomerId() != null)
				return false;
			return getNumberOfTokens() != null ? getNumberOfTokens().equals(that.getNumberOfTokens()) : that.getNumberOfTokens() == null;
		}

		@Override
		public int hashCode() {
			int result = getCustomerId() != null ? getCustomerId().hashCode() : 0;
			result = 31 * result + (getNumberOfTokens() != null ? getNumberOfTokens().hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("{");
			sb.append("customerId='").append(customerId).append('\'');
			sb.append(", numberOfTokens=").append(numberOfTokens);
			sb.append('}');
			return sb.toString();
		}
	}