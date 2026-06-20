package lpda.SistemaHotelero.features.acompanantes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/acompanantes")
@RequiredArgsConstructor
@Tag(
        name = "Acompañantes",
        description = "Gestión de acompañantes asociados a las reservas"
)
public class AcompananteController {

    private final AcompananteService acompananteService;
    @Operation(
            summary = "Registrar acompañante",
            description = "Registra un nuevo acompañante asociado a una reserva"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Acompañante registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<AcompananteResponseDTO> create(
            @Valid @RequestBody AcompananteRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(acompananteService.create(request));
    }
    @Operation(
            summary = "Listar acompañantes",
            description = "Obtiene todos los acompañantes registrados en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<AcompananteResponseDTO>> getAll() {
        return ResponseEntity.ok(acompananteService.getAll());
    }
    @Operation(
            summary = "Buscar acompañante por ID",
            description = "Obtiene un acompañante mediante su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Acompañante encontrado"),
            @ApiResponse(responseCode = "404", description = "Acompañante no encontrado")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<AcompananteResponseDTO> getById(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(acompananteService.getById(idExterno));
    }
    @Operation(
            summary = "Listar acompañantes por reserva",
            description = "Obtiene todos los acompañantes asociados a una reserva"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/reserva/{idExterno}")
    public ResponseEntity<List<AcompananteResponseDTO>> getByReserva(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(acompananteService.getByReserva(idExterno));
    }
    @Operation(
            summary = "Actualizar acompañante",
            description = "Modifica la información de un acompañante existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Acompañante actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Acompañante no encontrado")
    })
    @PutMapping("/{idExterno}")
    public ResponseEntity<AcompananteResponseDTO> update(
            @PathVariable UUID idExterno,
            @Valid @RequestBody AcompananteRequestDTO request
    ) {
        return ResponseEntity.ok(acompananteService.update(idExterno, request));
    }
    @Operation(
            summary = "Eliminar acompañante",
            description = "Elimina un acompañante utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Acompañante eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Acompañante no encontrado")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(@PathVariable UUID idExterno) {
        acompananteService.delete(idExterno);
        return ResponseEntity.noContent().build();
    }
}