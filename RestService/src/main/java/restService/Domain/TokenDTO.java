package restService.Domain;


import java.util.List;

public class TokenDTO {
  List<String> tokens;

  public TokenDTO(List<String> tokens) {
    this.tokens = tokens;
  }

  public TokenDTO() {
  }

  public List<String> getTokens() {
    return tokens;
  }

  public void setTokens(List<String> tokens) {
    this.tokens = tokens;
  }

  @Override
  public String toString() {
    return "TokenDTO [tokens=" + tokens + "]";
  }
  
  
}
