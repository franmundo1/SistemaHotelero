package lpda.SistemaHotelero.features.huespedes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/huespedes")
public class HuespedController {

    private final HuespedRepository huespedRepository;

    public HuespedController(HuespedRepository huespesRepository) {
        this.huespedRepository = huespesRepository;
    }

    // MAPEAMOS TODOS LOS HUESPEDES
    @GetMapping
    public ResponseEntity<List<HuespedEntity>> obtenerTodos() {
        List<HuespedEntity> huespedes = huespedRepository.findAll();
        return ResponseEntity.ok(huespedes);
    }

    //MAPEAMOS HUESPEDES POR ID
    @GetMapping("/{id}")
    public ResponseEntity<HuespedEntity> obtenerPorId(@PathVariable Long id) {
        return huespedRepository.findById(id)
                .map(ResponseEntity::ok) // Si existe, retorna 200 con el objeto[cite: 16]
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Si no, 404[cite: 14]
    }

    //CREAMOS UN NUEVO HUESPED
    @PostMapping
    public ResponseEntity <HuespedEntity> crear(@RequestBody HuespedEntity huesped){
        HuespedEntity nuevoHuesped = huespedRepository.save(huesped);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHuesped);
    }

    //ELIMINAMOS UN HESPED

    @DeleteMapping ("/{di}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if (huespedRepository.existsById(id)){
            huespedRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}