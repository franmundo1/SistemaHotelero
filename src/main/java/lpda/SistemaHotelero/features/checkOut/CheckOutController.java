package lpda.SistemaHotelero.features.checkOut;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutRequestDTO;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutResponseDTO;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutResumenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/checkOut")
public class CheckOutController {

    private final CheckOutService checkOutService;

    public CheckOutController(CheckOutService checkOutService) {
        this.checkOutService = checkOutService;
    }

    @GetMapping("/resumen/{idReservaExterno}")
    public ResponseEntity<CheckOutResumenDTO> obtenerResumen(
            @PathVariable UUID idReservaExterno
    ) {
        return ResponseEntity.ok(checkOutService.obtenerResumen(idReservaExterno));
    }

    @GetMapping("/{idExterno}")
    public ResponseEntity<CheckOutResponseDTO> obtenerPorId(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(checkOutService.getById(idExterno));
    }

    @PostMapping
    public ResponseEntity<CheckOutResponseDTO> registrar(
            @Valid @RequestBody CheckOutRequestDTO request
    ) {
        CheckOutResponseDTO response = checkOutService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID idExterno
    ) {
        checkOutService.delete(idExterno);
        return ResponseEntity.noContent().build();
    }
}