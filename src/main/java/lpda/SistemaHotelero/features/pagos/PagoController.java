package lpda.SistemaHotelero.features.pagos;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(
            summary = "Obtener todos los pagos",
            description = "Retorna el listado completo de pagos registrados en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos correctamente")
    })
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }
    @Operation(
            summary = "Obtener pagos por reserva",
            description = "Obtiene todos los pagos asociados a una reserva específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/reserva/{idReservaExterno}")

    public ResponseEntity<List<PagoResponseDTO>> obtenerPorReserva(
            @PathVariable UUID idReservaExterno
    ) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorReserva(idReservaExterno));
    }


    @Operation(
            summary = "Obtener pagos por usuario",
            description = "Obtiene todos los pagos registrados por un usuario específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPorUsuario(@PathVariable UUID idUsuario) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorUsuario(idUsuario));
    }

    @Operation(
            summary = "Obtener pago por ID",
            description = "Obtiene la información de un pago utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<PagoResponseDTO> obtenerPorIdExterno(@PathVariable UUID idExterno) {
        return ResponseEntity.ok(pagoService.obtenerPorIdExterno(idExterno));
    }
}