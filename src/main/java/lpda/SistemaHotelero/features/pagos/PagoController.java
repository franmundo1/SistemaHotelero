package lpda.SistemaHotelero.features.pagos;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Pagos", description = "Gestión de pagos de reservas")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo pago para una reserva")
    public ResponseEntity<PagoResponseDTO> registrarPago(@Valid @RequestBody PagoRequestDTO dto) {
        PagoResponseDTO response = pagoService.registrarPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pagos")
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @GetMapping("/reserva/{idReserva}")
    @Operation(summary = "Obtener todos los pagos de una reserva")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorReserva(idReserva));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtener todos los pagos registrados por un usuario")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorUsuario(idUsuario));
    }

    @GetMapping("/{idExterno}")
    @Operation(summary = "Obtener un pago por su ID externo (UUID)")
    public ResponseEntity<PagoResponseDTO> obtenerPorIdExterno(@PathVariable UUID idExterno) {
        return ResponseEntity.ok(pagoService.obtenerPorIdExterno(idExterno));
    }
}