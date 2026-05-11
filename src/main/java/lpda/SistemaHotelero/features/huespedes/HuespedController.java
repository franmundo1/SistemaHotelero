package lpda.SistemaHotelero.features.huespedes;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.acompanantes.AcompananteResponseDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedRequestDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RestController
@RequestMapping("/api/huespedes")
public class HuespedController {

    private final HuespedService huespedService;

    public HuespedController(HuespedService huespedService) {
        this.huespedService = huespedService;
    }

    @GetMapping
    public ResponseEntity<List<HuespedResponseDTO>> obtenerHuesped(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String dni) {


        return ResponseEntity.ok(huespedService.buscarConFiltros(nombre,dni));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuespedEntity> obtenerPorId(@PathVariable Long id) {
        return huespedService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping("/{idExterno}")
    public ResponseEntity<HuespedResponseDTO> getById(
            @PathVariable UUID idExterno){
        return ResponseEntity.ok(huespedService.getById(idExterno)
        );
    }

    @PostMapping
    public ResponseEntity<HuespedResponseDTO> registrar(@Valid @RequestBody HuespedRequestDTO request) {
       HuespedResponseDTO respons = huespedService.guardarHuesped(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(respons);
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID idExterno)
    {
        huespedService.delete(idExterno);
        return  ResponseEntity.noContent().build();
    }

}