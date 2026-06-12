package lpda.SistemaHotelero.features.reservas;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.reservas.DTO.CancelarReservaRequestDTO;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaRequestDTO;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @Valid @RequestBody ReservaRequestDTO dto
    ) {
        ReservaResponseDTO response = reservaService.crearReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarReservas() {

        return ResponseEntity.ok(
                reservaService.listarReservas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                reservaService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizarReserva(
            @PathVariable UUID id,
            @Valid @RequestBody ReservaRequestDTO dto
    ) {
        return ResponseEntity.ok(reservaService.actualizarReserva(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(
            @PathVariable UUID id) {

        reservaService.eliminarReserva(id);

        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(
            @PathVariable UUID id,
            @Valid @RequestBody CancelarReservaRequestDTO dto
    ) {
        return ResponseEntity.ok(
                reservaService.cancelarReserva(id, dto.getMotivoCancelacion())
        );
    }
    @GetMapping("/confirmadas")
    public ResponseEntity<List<ReservaResponseDTO>> listarConfirmadas() {
        return ResponseEntity.ok(reservaService.listarPorEstado("CONFIRMADA"));
    }

    @GetMapping("/en-curso")
    public ResponseEntity<List<ReservaResponseDTO>> listarEnCurso() {
        return ResponseEntity.ok(reservaService.listarPorEstado("EN_CURSO"));
    }

    @GetMapping("/finalizadas")
    public ResponseEntity<List<ReservaResponseDTO>> listarFinalizadas() {
        return ResponseEntity.ok(reservaService.listarPorEstado("FINALIZADA"));
    }

    @GetMapping("/canceladas")
    public ResponseEntity<List<ReservaResponseDTO>> listarCanceladas() {
        return ResponseEntity.ok(reservaService.listarPorEstado("CANCELADA"));
    }
}
