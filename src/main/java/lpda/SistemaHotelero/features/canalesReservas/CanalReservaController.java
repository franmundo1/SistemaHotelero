package lpda.SistemaHotelero.features.canalesReservas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canalesReserva")
public class CanalReservaController {

    @Autowired
    private CanalReservaService canalReservaService;

    @PostMapping
    public CanalReservaResponseDTO crearCanal(@RequestBody CanalReservaRequestDTO dto){
        return canalReservaService.crearCanal(dto);
    }

    @GetMapping
    public List<CanalReservaResponseDTO> listarCanales(){
        return canalReservaService.listarCanales();
    }

    @GetMapping("/{id}")
    public CanalReservaResponseDTO buscarPorId(@PathVariable Long id){
        return canalReservaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CanalReservaResponseDTO actualizarCanal(@PathVariable Long id,
                                                   @RequestBody CanalReservaRequestDTO dto){
        return canalReservaService.actualizarCanal(id, dto);
    }

    @DeleteMapping("/{id}")
    public String eliminarCanal(@PathVariable Long id){
        canalReservaService.eliminarCanal(id);
        return "Canal eliminado correctamente";
    }
}
