package ou.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    public LoginRequest() {
    }

    private String userName;
    private String password;
}
