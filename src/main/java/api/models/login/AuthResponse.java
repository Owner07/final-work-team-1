package api.models.login;

import lombok.Data;

@Data
public class AuthResponse {
    private String access_token;
}