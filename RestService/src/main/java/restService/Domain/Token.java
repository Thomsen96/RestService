package restService.Domain;

import java.util.Objects;

public class Token {
    public String customerId = null;
    public String tokenUuid = null;
    public Boolean tokenValidity;

    public Token (String id, String tokenId, Boolean valid) {
        this.customerId = id;
        this.tokenUuid = tokenId;
        this.tokenValidity = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(customerId, token.customerId) && Objects.equals(tokenUuid, token.tokenUuid) && Objects.equals(tokenValidity, token.tokenValidity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, tokenUuid, tokenValidity);
    }
}
