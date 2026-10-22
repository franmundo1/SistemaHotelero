package lpda.SistemaHotelero.seguridad;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = switch (authException) {
            case BadCredentialsException badCredentialsException -> "Credenciales inválidas";
            case DisabledException disabledException -> "Cuenta deshabilitada";
            case LockedException lockedException -> "Cuenta bloqueada";
            case AccountExpiredException accountExpiredException -> "Cuenta expirada";
            case CredentialsExpiredException credentialsExpiredException -> "Credenciales expiradas";
            case InsufficientAuthenticationException insufficientAuthenticationException -> "Autenticación insuficiente";
            case AuthenticationServiceException authenticationServiceException -> "Error en el servicio de autenticación";
            default -> "Error de autenticación: " + authException.getMessage();
        };

        String jsonResponse = String.format(
                "{\"error\":\"%s\",\"status\":%d,\"path\":\"%s\"}",
                errorMessage,
                HttpServletResponse.SC_UNAUTHORIZED,
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}