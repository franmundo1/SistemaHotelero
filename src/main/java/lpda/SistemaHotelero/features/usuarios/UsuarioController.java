package lpda.SistemaHotelero.features.usuarios;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioRequestDTO;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearEmpleado(
            @Valid @RequestBody UsuarioRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.crearEmpleado(dto));
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idExterno
    ) {
        usuarioService.eliminar(idExterno);
        return ResponseEntity.noContent().build();
    }
}