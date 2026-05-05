package lpda.SistemaHotelero.features.huespedes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/huespedes")
public class HuespedController {

    private final HuespedService huespedService;

    public HuespedController(HuespedService huespedService) {
        this.huespedService = huespedService;
    }

    @GetMapping
public ResponseEntity<List<HuespedEntity>> listar (
        @RequestParam (required = false) String nombre,
        @RequestParam (required = false) String dni){

        List <HuespedEntity> resultado = huespedService.buscarConFiltros(nombre, dni);
         return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuespedEntity> obteenrPorId(@PathVariable Long id){
        return huespedService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
     public ResponseEntity<HuespedEntity> registrar (@RequestBody HuespedEntity huesped){
        HuespedEntity guardado = huespedService.guardarHuesped(huesped);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}
