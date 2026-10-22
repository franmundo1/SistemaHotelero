package lpda.SistemaHotelero.seguridad;

import lpda.SistemaHotelero.seguridad.dto.AuthDtoRequest;
import lpda.SistemaHotelero.seguridad.dto.AuthDtoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/auth")
@Tag(
        name = "Autenticación",
        description = "Endpoints relacionados con autenticación y generación de tokens JWT"
)
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            JwtService jwtService
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
    }
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y genera un token JWT válido para acceder a los recursos protegidos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDtoResponse> authenticateUser(
            @RequestBody AuthDtoRequest authRequest
    ) {
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthDtoResponse(token));
    }
}