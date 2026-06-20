package lpda.SistemaHotelero.features.habitaciones;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.habitaciones.enums.TipoHabitacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
@Tag(
        name = "Habitaciones",
        description = "Gestión de habitaciones del sistema hotelero"
)
public class HabitacionController {

    private final HabitacionService habitacionService;
    @Operation(
            summary = "Listar habitaciones",
            description = "Obtiene el listado completo de habitaciones"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> findAll() {
        return ResponseEntity.ok(habitacionService.findAll());
    }
    @Operation(
            summary = "Consultar disponibilidad por fechas",
            description = "Obtiene las habitaciones disponibles dentro de un rango de fechas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    })
    @GetMapping("/disponibles-por-fecha")
    public ResponseEntity<List<HabitacionResponseDTO>> disponiblesPorFecha(
            @RequestParam LocalDate fechaEntrada,
            @RequestParam LocalDate fechaSalida
    ) {
        return ResponseEntity.ok(
                habitacionService.findDisponiblesPorRangoDeFechas(fechaEntrada, fechaSalida)
        );
    }
    @Operation(
            summary = "Buscar habitación",
            description = "Obtiene una habitación mediante su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación encontrada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<HabitacionResponseDTO> findById(@PathVariable UUID idExterno) {
        return ResponseEntity.ok(habitacionService.findById(idExterno));
    }
    @Operation(
            summary = "Filtrar habitaciones",
            description = "Permite filtrar habitaciones por número, tipo, capacidad y estados"
    )
    @GetMapping("/filtrar")
    public ResponseEntity<List<HabitacionResponseDTO>> filtrarHabitaciones(
            @RequestParam(required = false)String numero,
            @RequestParam(required = false) TipoHabitacion tipo,
            @RequestParam(required = false) Integer capacidad,
            @RequestParam(required = false) EstadoOcupacion estadoOcupacion,
            @RequestParam(required = false) EstadoLimpieza estadoLimpieza,
            @RequestParam(required = false) Boolean activa
    ) {
        return ResponseEntity.ok(
                habitacionService.filtrarHabitaciones(
                        numero,
                        tipo,
                        capacidad,
                        estadoOcupacion,
                        estadoLimpieza,
                        activa
                )
        );
    }
    @Operation(
            summary = "Crear habitación",
            description = "Registra una nueva habitación en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> save(
            @Valid @RequestBody HabitacionRequestDTO requestDTO
    ) {
        HabitacionResponseDTO responseDTO = habitacionService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    @Operation(
            summary = "Actualizar habitación",
            description = "Actualiza completamente una habitación existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @PutMapping("/{idExterno}")
    public ResponseEntity<HabitacionResponseDTO> update(
            @PathVariable UUID idExterno,
            @Valid @RequestBody HabitacionRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(habitacionService.update(idExterno, requestDTO));
    }


    @Operation(
            summary = "Eliminar habitación",
            description = "Elimina una habitación del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habitación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID idExterno) {
        habitacionService.deleteById(idExterno);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Actualizar parcialmente habitación",
            description = "Permite modificar parcialmente los datos de una habitación"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @PatchMapping("/{idExterno}")
    public ResponseEntity<HabitacionResponseDTO> patch(
            @PathVariable UUID idExterno,
            @Valid @RequestBody HabitacionPatchDTO patchDTO
    ) {
        return ResponseEntity.ok(habitacionService.patch(idExterno, patchDTO));
    }
    @Operation(
            summary = "Cambiar estado de limpieza",
            description = "Actualiza el estado de limpieza de una habitación mediante su número"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @PatchMapping("/limpieza/{numero}/estado-limpieza")
    public ResponseEntity<HabitacionLimpiezaResponseDTO> cambiarEstadoLimpiezaPorNumero(
            @PathVariable String numero,
            @RequestParam EstadoLimpieza estadoLimpieza
    ) {
        return ResponseEntity.ok(
                habitacionService.cambiarEstadoLimpiezaPorNumero(numero, estadoLimpieza)
        );
    }
    @Operation(
            summary = "Cambiar estado de ocupación",
            description = "Actualiza el estado de ocupación de una habitación"
    )
    @PatchMapping("/{idExterno}/estado-ocupacion")
    public ResponseEntity<HabitacionResponseDTO> cambiarEstadoOcupacion(
            @PathVariable UUID idExterno,
            @RequestParam EstadoOcupacion estadoOcupacion
    ) {
        return ResponseEntity.ok(habitacionService.cambiarEstadoOcupacion(idExterno, estadoOcupacion));
    }
    @Operation(
            summary = "Activar o desactivar habitación",
            description = "Permite cambiar el estado activo de una habitación"
    )
    @PatchMapping("/{idExterno}/activa")
    public ResponseEntity<HabitacionResponseDTO> cambiarActiva(
            @PathVariable UUID idExterno,
            @RequestParam Boolean activa
    ) {
        return ResponseEntity.ok(habitacionService.cambiarActiva(idExterno, activa));
    }
    @Operation(
            summary = "Listado para limpieza",
            description = "Obtiene las habitaciones visibles para el personal de limpieza"
    )
    @GetMapping("/limpieza")
    public ResponseEntity<List<HabitacionLimpiezaResponseDTO>> findAllParaLimpieza() {
        return ResponseEntity.ok(habitacionService.findAllParaLimpieza());
    }
}
