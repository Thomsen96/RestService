package restService.Domain;

public class DTO {

    public static class CreateAccount {
        public String accountId;
        public CreateAccount(String accountId) {
            this.accountId = accountId;
        }
        public CreateAccount() {

        }
        @Override
        public String toString() {
            return "CreateAccount [accountId=" + accountId + "]";
        }
    }

    public static class CreateAccountResponse {
        public String accountId;
        public CreateAccountResponse() {

        }
        public CreateAccountResponse(String accountId) {
            this.accountId = accountId;
        }
        @Override
        public String toString() {
            return "CreateAccountResponse [accountId=" + accountId + "]";
        }
    }

    public static class CreatePayment {
        public String token;
        public String merchant;
        public String amount;
        public String description;
        public CreatePayment(String token, String merchant, String amount, String description) {
            this.token = token;
            this.merchant = merchant;
            this.amount = amount;
            this.description = description;
        }
        public CreatePayment() {
        }
        @Override
        public String toString() {
            return "CreatePayment [amount=" + amount + ", description=" + description + ", merchant=" + merchant
                    + ", token=" + token + "]";
        }
    }

    public static class GetCustomerReport {
        public String customerId;
        public GetCustomerReport(String customerId) {
            this.customerId = customerId;
        }
        public GetCustomerReport() {

        }
        @Override
        public String toString() {
          return "GetCustomerReport [customerId=" + customerId + "]";
        }
    }


    public static class GetMerchantReport {
        public String merchantId;
        public GetMerchantReport(String merchantId) {
            this.merchantId = merchantId;
        }
        public GetMerchantReport() {

        }
        @Override
        public String toString() {
            return "GetMerchantReport [merchantId=" + merchantId + "]";
        }
    }

    public static class CreateTokens {
        public String customerId;
        public int numberOfTokens;
        public CreateTokens() {

        }
        @Override
        public String toString() {
            return "CreateTokens [customerId=" + customerId + ", numberOfTokens=" + numberOfTokens + "]";
        }
    }

}
