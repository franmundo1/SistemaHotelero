package lpda.SistemaHotelero.features.checkIn;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInRequestDTO;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;

@RestController
@RequestMapping("/api/checkIn")
@Tag(
        name = "Check-In",
        description = "Gestión del ingreso de huéspedes al hotel"
)
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }
    @Operation(
            summary = "Buscar check-in por ID",
            description = "Obtiene la información de un check-in mediante su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Check-in encontrado"),
            @ApiResponse(responseCode = "404", description = "Check-in no encontrado")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<CheckInResponseDTO> obtenerPorId(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(checkInService.getById(idExterno));
    }
    @Operation(
            summary = "Registrar check-in",
            description = "Registra el ingreso de un huésped asociado a una reserva"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Check-in registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PostMapping
    public ResponseEntity<CheckInResponseDTO> registrar(
            @Valid @RequestBody CheckInRequestDTO request
    ) {
        CheckInResponseDTO response = checkInService.guardarCheck(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "Eliminar check-in",
            description = "Elimina un registro de check-in utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Check-in eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Check-in no encontrado")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID idExterno
    ) {
        checkInService.delete(idExterno);
        return ResponseEntity.noContent().build();
    }
}