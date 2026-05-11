package lpda.SistemaHotelero.features.checkIn;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInRequestDTO;

import lpda.SistemaHotelero.features.checkIn.DTO.CheckInResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/checkIn")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping
    public ResponseEntity<CheckInResponseDTO> obtenerCheckIn (
            @RequestParam UUID idExterno) {
        return ResponseEntity.ok(checkInService.getById(idExterno));
    }

    @GetMapping("/{idExterno}")
    public ResponseEntity<CheckInResponseDTO> obtenerPorId(
            @PathVariable UUID idExterno) {
        return ResponseEntity.ok(checkInService.getById(idExterno));
    }

    @PostMapping
    public ResponseEntity<CheckInResponseDTO> registrar(@Valid @RequestBody CheckInRequestDTO request) {
        CheckInResponseDTO response = checkInService.guardarCheck(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(@PathVariable UUID idExterno) {
        checkInService.delete(idExterno);
        return ResponseEntity.noContent().build();
    }
}