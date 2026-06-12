package lpda.SistemaHotelero.features.habitaciones;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.habitaciones.enums.TipoHabitacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> findAll() {
        return ResponseEntity.ok(habitacionService.findAll());
    }
    @GetMapping("/disponibles-por-fecha")
    public ResponseEntity<List<HabitacionResponseDTO>> disponiblesPorFecha(
            @RequestParam LocalDate fechaEntrada,
            @RequestParam LocalDate fechaSalida
    ) {
        return ResponseEntity.ok(
                habitacionService.findDisponiblesPorRangoDeFechas(fechaEntrada, fechaSalida)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> findById(@PathVariable UUID idExterno) {
        return ResponseEntity.ok(habitacionService.findById(idExterno));
    }
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
    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> save(
            @Valid @RequestBody HabitacionRequestDTO requestDTO
    ) {
        HabitacionResponseDTO responseDTO = habitacionService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> update(
            @PathVariable UUID idExterno,
            @Valid @RequestBody HabitacionRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(habitacionService.update(idExterno, requestDTO));
    }



    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID idExterno) {
        habitacionService.deleteById(idExterno);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> patch(
            @PathVariable UUID idExterno,
            @Valid @RequestBody HabitacionPatchDTO patchDTO
    ) {
        return ResponseEntity.ok(habitacionService.patch(idExterno, patchDTO));
    }

    @PatchMapping("/limpieza/{numero}/estado-limpieza")
    public ResponseEntity<HabitacionLimpiezaResponseDTO> cambiarEstadoLimpiezaPorNumero(
            @PathVariable String numero,
            @RequestParam EstadoLimpieza estadoLimpieza
    ) {
        return ResponseEntity.ok(
                habitacionService.cambiarEstadoLimpiezaPorNumero(numero, estadoLimpieza)
        );
    }

    @PatchMapping("/{id}/estado-ocupacion")
    public ResponseEntity<HabitacionResponseDTO> cambiarEstadoOcupacion(
            @PathVariable UUID idExterno,
            @RequestParam EstadoOcupacion estadoOcupacion
    ) {
        return ResponseEntity.ok(habitacionService.cambiarEstadoOcupacion(idExterno, estadoOcupacion));
    }

    @PatchMapping("/{id}/activa")
    public ResponseEntity<HabitacionResponseDTO> cambiarActiva(
            @PathVariable UUID idExterno,
            @RequestParam Boolean activa
    ) {
        return ResponseEntity.ok(habitacionService.cambiarActiva(idExterno, activa));
    }
    @GetMapping("/limpieza")
    public ResponseEntity<List<HabitacionLimpiezaResponseDTO>> findAllParaLimpieza() {
        return ResponseEntity.ok(habitacionService.findAllParaLimpieza());
    }
}
