package cl.bancocloud.bff.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * Valida que el access token traiga en el claim "aud" el identificador
 * esperado del BFF/API, además del issuer que ya valida Spring por defecto.
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audienciaEsperada;

    public AudienceValidator(String audienciaEsperada) {
        this.audienciaEsperada = audienciaEsperada;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        List<String> audiencias = token.getAudience();

        if (audiencias != null && audiencias.contains(audienciaEsperada)) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "El token no contiene la audiencia esperada: " + audienciaEsperada,
                null
        );

        return OAuth2TokenValidatorResult.failure(error);
    }
}
