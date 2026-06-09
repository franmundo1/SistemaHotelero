package lpda.SistemaHotelero.features.consumos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.consumos.DTO.ConsumoRequestDTO;
import lpda.SistemaHotelero.features.consumos.DTO.ConsumoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consumos")
@RequiredArgsConstructor
public class ConsumoController {

    private final ConsumoService consumoService;

    @PostMapping
    public ResponseEntity<ConsumoResponseDTO> crear(
            @Valid @RequestBody ConsumoRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(consumoService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ConsumoResponseDTO>> listar() {
        return ResponseEntity.ok(consumoService.listar());
    }

    @GetMapping("/{idExterno}")
    public ResponseEntity<ConsumoResponseDTO> buscarPorIdExterno(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(consumoService.buscarPorIdExterno(idExterno));
    }

    @GetMapping("/reserva/{idReservaExterno}")
    public ResponseEntity<List<ConsumoResponseDTO>> listarPorReserva(
            @PathVariable UUID idReservaExterno
    ) {
        return ResponseEntity.ok(consumoService.listarPorReserva(idReservaExterno));
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idExterno
    ) {
        consumoService.eliminar(idExterno);
        return ResponseEntity.noContent().build();
    }
}