package lpda.SistemaHotelero.features.acompanantes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acompanantes")
@RequiredArgsConstructor
public class AcompananteController {

    private final AcompananteService acompananteService;

    @GetMapping
    public ResponseEntity<List<AcompananteResponseDTO>> getAll() {
        return ResponseEntity.ok(acompananteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcompananteResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(acompananteService.getById(id));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<AcompananteResponseDTO>> getByReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(acompananteService.getByReserva(idReserva));
    }

    /*
    @PostMapping
    public ResponseEntity<AcompananteResponseDTO> create(@Valid @RequestBody AcompananteRequestDTO dto) {}
    */

    /*
    @PutMapping("/{id}")
    public ResponseEntity<AcompananteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AcompananteRequestDTO dto) {}
    */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        acompananteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
