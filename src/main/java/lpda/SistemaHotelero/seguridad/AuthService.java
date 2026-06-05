package lpda.SistemaHotelero.seguridad;

import lpda.SistemaHotelero.features.usuarios.UsuarioRepository;
import lpda.SistemaHotelero.seguridad.dto.AuthDtoRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager
    ) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    public UserDetails authenticate(AuthDtoRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.username(),
                        input.password()
                )
        );

        return usuarioRepository.findByEmail(input.username())
                .orElseThrow();
    }
}