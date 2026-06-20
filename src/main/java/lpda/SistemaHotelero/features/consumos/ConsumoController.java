package lpda.SistemaHotelero.features.consumos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.consumos.DTO.ConsumoRequestDTO;
import lpda.SistemaHotelero.features.consumos.DTO.ConsumoResponseDTO;
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
@RequestMapping("/api/consumos")
@RequiredArgsConstructor
@Tag(
        name = "Consumos",
        description = "Gestión de consumos realizados durante la estadía de los huéspedes"
)
public class ConsumoController {

    private final ConsumoService consumoService;
    @Operation(
            summary = "Registrar consumo",
            description = "Registra un nuevo consumo asociado a una reserva"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consumo registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ConsumoResponseDTO> crear(
            @Valid @RequestBody ConsumoRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(consumoService.crear(dto));
    }
    @Operation(
            summary = "Listar consumos",
            description = "Obtiene todos los consumos registrados en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ConsumoResponseDTO>> listar() {
        return ResponseEntity.ok(consumoService.listar());

    }
    @Operation(
            summary = "Listar consumos del día",
            description = "Obtiene todos los consumos registrados en la fecha actual"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    })
    @GetMapping("/hoy")
    public ResponseEntity<List<ConsumoResponseDTO>> listarConsumosDelDia() {
        return ResponseEntity.ok(consumoService.listarConsumosDelDia());
    }
    @Operation(
            summary = "Buscar consumo por ID",
            description = "Obtiene un consumo mediante su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumo encontrado"),
            @ApiResponse(responseCode = "404", description = "Consumo no encontrado")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<ConsumoResponseDTO> buscarPorIdExterno(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(consumoService.buscarPorIdExterno(idExterno));
    }
    @Operation(
            summary = "Listar consumos por reserva",
            description = "Obtiene todos los consumos asociados a una reserva específica"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/reserva/{idReservaExterno}")
    public ResponseEntity<List<ConsumoResponseDTO>> listarPorReserva(
            @PathVariable UUID idReservaExterno
    ) {
        return ResponseEntity.ok(consumoService.listarPorReserva(idReservaExterno));
    }
    @Operation(
            summary = "Eliminar consumo",
            description = "Elimina un consumo utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consumo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Consumo no encontrado")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idExterno
    ) {
        consumoService.eliminar(idExterno);
        return ResponseEntity.noContent().build();
    }
}