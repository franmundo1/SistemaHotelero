package lpda.SistemaHotelero.features.usuarios;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.roles.RolEntity;
import lpda.SistemaHotelero.features.roles.RolRepository;
import lpda.SistemaHotelero.features.roles.enums.Rol;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioRequestDTO;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findByActivoTrue()
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public UsuarioResponseDTO crearEmpleado(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Ya existe un usuario con ese email");
        }

        if (Rol.ROLE_ADMIN.equals(dto.getRol())) {
            throw new BadRequestException("No se puede crear otro administrador desde este endpoint");
        }

        RolEntity rolEntity = rolRepository.findByNombre(dto.getRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + dto.getRol()));

        String passwordEncriptada = passwordEncoder.encode(dto.getPassword());

        UsuarioEntity usuario = usuarioMapper.toEntity(dto, rolEntity, passwordEncriptada);
        UsuarioEntity guardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(guardado);
    }

    @Transactional
    public void eliminar(UUID idExterno) {
        UsuarioEntity usuario = usuarioRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID externo: " + idExterno));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new BadRequestException("El usuario ya se encuentra inactivo");
        }

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
    @Transactional
    public UsuarioResponseDTO cambiarActivo(UUID idExterno, Boolean activo) {
        UsuarioEntity usuario = usuarioRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID externo: " + idExterno));

        usuario.setActivo(activo);

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }
}