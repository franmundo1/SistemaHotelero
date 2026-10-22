package lpda.SistemaHotelero.features.reservas;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.reservas.DTO.CancelarReservaRequestDTO;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaRequestDTO;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaResponseDTO;
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
@RequestMapping("/api/reservas")
@Tag(
        name = "Reservas",
        description = "Gestión de reservas del sistema hotelero"
)
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(
            summary = "Crear reserva",
            description = "Registra una nueva reserva asociada a un huésped y una habitación"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @Valid @RequestBody ReservaRequestDTO dto
    ) {
        ReservaResponseDTO response = reservaService.crearReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Listar reservas",
            description = "Obtiene todas las reservas registradas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarReservas() {

        return ResponseEntity.ok(
                reservaService.listarReservas()
        );
    }
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene una reserva utilizando su identificador UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                reservaService.buscarPorId(id)
        );
    }
    @Operation(
            summary = "Actualizar reserva",
            description = "Modifica los datos de una reserva existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(
            @PathVariable UUID id,
            @Valid @RequestBody ReservaRequestDTO dto
    ) {
        return ResponseEntity.ok(reservaService.actualizarReserva(id, dto));
    }
    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(
            @PathVariable UUID id) {

        reservaService.eliminarReserva(id);

        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Cancelar reserva",
            description = "Cancela una reserva indicando el motivo de cancelación"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(
            @PathVariable UUID id,
            @Valid @RequestBody CancelarReservaRequestDTO dto
    ) {
        return ResponseEntity.ok(
                reservaService.cancelarReserva(id, dto.getMotivoCancelacion())
        );
    }

    @Operation(
            summary = "Reservas confirmadas",
            description = "Obtiene todas las reservas con estado CONFIRMADA"
    )
    @GetMapping("/confirmadas")
    public ResponseEntity<List<ReservaResponseDTO>> listarConfirmadas() {
        return ResponseEntity.ok(reservaService.listarPorEstado("CONFIRMADA"));
    }
    @Operation(
            summary = "Reservas en curso",
            description = "Obtiene todas las reservas con estado EN_CURSO"
    )
    @GetMapping("/en-curso")
    public ResponseEntity<List<ReservaResponseDTO>> listarEnCurso() {
        return ResponseEntity.ok(reservaService.listarPorEstado("EN_CURSO"));
    }
    @Operation(
            summary = "Reservas finalizadas",
            description = "Obtiene todas las reservas con estado FINALIZADA"
    )
    @GetMapping("/finalizadas")
    public ResponseEntity<List<ReservaResponseDTO>> listarFinalizadas() {
        return ResponseEntity.ok(reservaService.listarPorEstado("FINALIZADA"));
    }
    @Operation(
            summary = "Reservas canceladas",
            description = "Obtiene todas las reservas con estado CANCELADA"
    )
    @GetMapping("/canceladas")
    public ResponseEntity<List<ReservaResponseDTO>> listarCanceladas() {
        return ResponseEntity.ok(reservaService.listarPorEstado("CANCELADA"));
    }
}
