package lpda.SistemaHotelero.features.canalesReservas;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/canalesReserva")
public class CanalReservaController {

    private final CanalReservaService canalReservaService;

    public CanalReservaController(CanalReservaService canalReservaService) {
        this.canalReservaService = canalReservaService;
    }

    @PostMapping
    public ResponseEntity<CanalReservaResponseDTO> crearCanal(
            @RequestBody CanalReservaRequestDTO dto) {

        CanalReservaResponseDTO response =
                canalReservaService.crearCanal(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CanalReservaResponseDTO>> listarCanales() {
        return ResponseEntity.ok(canalReservaService.listarCanales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanalReservaResponseDTO> buscarPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(canalReservaService.buscarPorIdExterno(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanalReservaResponseDTO> actualizarCanal(
            @PathVariable Long id,
            @RequestBody CanalReservaRequestDTO dto) {

        return ResponseEntity.ok(
                canalReservaService.actualizarCanal(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCanal(@PathVariable UUID id) {

        canalReservaService.eliminarCanal(id);

        return ResponseEntity.noContent().build();
    }
}
