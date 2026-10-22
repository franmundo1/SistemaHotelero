package lpda.SistemaHotelero.features.usuarios;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioRequestDTO;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(
        name = "Usuarios",
        description = "Gestión de usuarios y empleados del sistema hotelero"
)
public class UsuarioController {

    private final UsuarioService usuarioService;
    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios registrados en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }
    @Operation(
            summary = "Crear usuario",
            description = "Registra un nuevo empleado o usuario del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuario ya existente")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearEmpleado(
            @Valid @RequestBody UsuarioRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.crearEmpleado(dto));
    }
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idExterno
    ) {
        usuarioService.eliminar(idExterno);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Cambiar estado de usuario",
            description = "Permite activar o desactivar un usuario del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping("/{idExterno}/activo")
    public ResponseEntity<UsuarioResponseDTO> cambiarActivo(
            @PathVariable UUID idExterno,
            @RequestParam Boolean activo
    ) {
        return ResponseEntity.ok(usuarioService.cambiarActivo(idExterno, activo));
    }
}