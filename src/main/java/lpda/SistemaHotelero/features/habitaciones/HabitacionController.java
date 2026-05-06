package lpda.SistemaHotelero.features.habitaciones;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionResponseDTO>> findAll() {
        return ResponseEntity.ok(habitacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.findById(id));
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
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(habitacionService.update(id, requestDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> patch(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionPatchDTO patchDTO
    ) {
        return ResponseEntity.ok(habitacionService.patch(id, patchDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        habitacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
