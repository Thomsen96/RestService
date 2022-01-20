package restService.Domain;

public class DTO {

    public static class CreateAccount {
        public String accountId;
        public CreateAccount(String accountId) {
            this.accountId = accountId;
        }
        public CreateAccount() {

        }
    }

    public static class CreateAccountResponse {
        public String accountId;
        public CreateAccountResponse() {

        }
        public CreateAccountResponse(String accountId) {
            this.accountId = accountId;
        }
    }

    public static class CreatePayment {
        public String token;
        public String merchant;
        public String amount;
        public String description;
    }

    public static class GetCustomerReport {
        public String customerId;
        public GetCustomerReport(String customerId) {
            this.customerId = customerId;
        }
        public GetCustomerReport() {

        }
    }


    public static class GetMerchantReport {
        public String merchantId;
        public GetMerchantReport(String merchantId) {
            this.merchantId = merchantId;
        }
        public GetMerchantReport() {

        }
    }

    public static class CreateTokens {
        public String customerId;
        public int numberOfTokens;
        public CreateTokens() {

        }
    }

}
