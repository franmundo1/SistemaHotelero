package lpda.SistemaHotelero.features.acompanantes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/acompanantes")
@RequiredArgsConstructor
public class AcompananteController {

    private final AcompananteService acompananteService;

    @GetMapping
    public ResponseEntity<List<AcompananteResponseDTO>> getAll() {

        return ResponseEntity.ok(acompananteService.getAll());
    }

    @GetMapping("/{idExterno}")
    public ResponseEntity<AcompananteResponseDTO> getById(
            @PathVariable UUID idExterno
    ) {

        return ResponseEntity.ok(
                acompananteService.getById(idExterno)
        );
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<AcompananteResponseDTO>> getByReserva(
            @PathVariable Long idReserva
    ) {

        return ResponseEntity.ok(
                acompananteService.getByReserva(idReserva)
        );
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID idExterno
    ) {

        acompananteService.delete(idExterno);

        return ResponseEntity.noContent().build();
    }
}