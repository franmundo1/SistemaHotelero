package lpda.SistemaHotelero.seguridad;

import lpda.SistemaHotelero.seguridad.dto.AuthDtoRequest;
import lpda.SistemaHotelero.seguridad.dto.AuthDtoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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

    @PostMapping("/login")
    public ResponseEntity<AuthDtoResponse> authenticateUser(
            @RequestBody AuthDtoRequest authRequest
    ) {
        UserDetails user = authService.authenticate(authRequest);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthDtoResponse(token));
    }
}