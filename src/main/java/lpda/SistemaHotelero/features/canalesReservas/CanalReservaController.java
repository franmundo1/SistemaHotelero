package lpda.SistemaHotelero.features.canalesReservas;

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
@RequestMapping("/canalesReserva")
@Tag(
        name = "Canales de Reserva",
        description = "Gestión de canales de reserva utilizados por el hotel"
)
public class CanalReservaController {

    private final CanalReservaService canalReservaService;

    public CanalReservaController(CanalReservaService canalReservaService) {
        this.canalReservaService = canalReservaService;
    }
    @Operation(
            summary = "Crear canal de reserva",
            description = "Registra un nuevo canal de reserva en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Canal creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<CanalReservaResponseDTO> crearCanal(
            @RequestBody CanalReservaRequestDTO dto) {

        CanalReservaResponseDTO response =
                canalReservaService.crearCanal(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(
            summary = "Listar canales de reserva",
            description = "Obtiene todos los canales de reserva registrados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<CanalReservaResponseDTO>> listarCanales() {
        return ResponseEntity.ok(canalReservaService.listarCanales());
    }
    @Operation(
            summary = "Buscar canal por ID",
            description = "Obtiene un canal de reserva utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canal encontrado"),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CanalReservaResponseDTO> buscarPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(canalReservaService.buscarPorIdExterno(id));
    }
    @Operation(
            summary = "Actualizar canal de reserva",
            description = "Modifica la información de un canal de reserva existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canal actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CanalReservaResponseDTO> actualizarCanal(
            @PathVariable UUID id,
            @RequestBody CanalReservaRequestDTO dto) {

        return ResponseEntity.ok(
                canalReservaService.actualizarCanal(id, dto)
        );
    }
    @Operation(
            summary = "Eliminar canal de reserva",
            description = "Elimina un canal de reserva del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canal eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Canal no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCanal(@PathVariable UUID id) {

        canalReservaService.eliminarCanal(id);

        return ResponseEntity.noContent().build();
    }
}
