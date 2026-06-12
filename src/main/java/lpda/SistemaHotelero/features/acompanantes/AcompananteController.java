package lpda.SistemaHotelero.features.acompanantes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/acompanantes")
@RequiredArgsConstructor
public class AcompananteController {

    private final AcompananteService acompananteService;

    @PostMapping
    public ResponseEntity<AcompananteResponseDTO> create(
            @Valid @RequestBody AcompananteRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(acompananteService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AcompananteResponseDTO>> getAll() {
        return ResponseEntity.ok(acompananteService.getAll());
    }

    @GetMapping("/{idExterno}")
    public ResponseEntity<AcompananteResponseDTO> getById(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(acompananteService.getById(idExterno));
    }

    @GetMapping("/reserva/{idExterno}")
    public ResponseEntity<List<AcompananteResponseDTO>> getByReserva(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(acompananteService.getByReserva(idExterno));
    }

    @PutMapping("/{idExterno}")
    public ResponseEntity<AcompananteResponseDTO> update(
            @PathVariable UUID idExterno,
            @Valid @RequestBody AcompananteRequestDTO request
    ) {
        return ResponseEntity.ok(acompananteService.update(idExterno, request));
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(@PathVariable UUID idExterno) {
        acompananteService.delete(idExterno);
        return ResponseEntity.noContent().build();
    }
}