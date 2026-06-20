package lpda.SistemaHotelero.features.checkOut;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutRequestDTO;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutResponseDTO;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutResumenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;

@RestController
@RequestMapping("/api/checkOut")
@Tag(
        name = "Check-Out",
        description = "Gestión del egreso de huéspedes y cierre de estadías"
)
public class CheckOutController {

    private final CheckOutService checkOutService;

    public CheckOutController(CheckOutService checkOutService) {
        this.checkOutService = checkOutService;
    }
    @Operation(
            summary = "Obtener resumen de check-out",
            description = "Genera un resumen de la reserva incluyendo consumos, pagos y monto final a abonar"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumen generado correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/resumen/{idReservaExterno}")
    public ResponseEntity<CheckOutResumenDTO> obtenerResumen(
            @PathVariable UUID idReservaExterno
    ) {
        return ResponseEntity.ok(checkOutService.obtenerResumen(idReservaExterno));
    }
    @Operation(
            summary = "Buscar check-out por ID",
            description = "Obtiene la información de un check-out mediante su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Check-out encontrado"),
            @ApiResponse(responseCode = "404", description = "Check-out no encontrado")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<CheckOutResponseDTO> obtenerPorId(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(checkOutService.getById(idExterno));
    }
    @Operation(
            summary = "Registrar check-out",
            description = "Finaliza una estadía, libera la habitación y registra el egreso del huésped"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Check-out registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PostMapping
    public ResponseEntity<CheckOutResponseDTO> registrar(
            @Valid @RequestBody CheckOutRequestDTO request
    ) {
        CheckOutResponseDTO response = checkOutService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "Eliminar check-out",
            description = "Elimina un registro de check-out utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Check-out eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Check-out no encontrado")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID idExterno
    ) {
        checkOutService.delete(idExterno);
        return ResponseEntity.noContent().build();
    }
}