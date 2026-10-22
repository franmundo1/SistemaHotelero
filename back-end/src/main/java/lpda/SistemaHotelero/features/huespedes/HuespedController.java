package lpda.SistemaHotelero.features.huespedes;

import jakarta.validation.Valid;
import lpda.SistemaHotelero.features.acompanantes.AcompananteResponseDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedRequestDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@Controller
@RestController
@RequestMapping("/api/huespedes")
@Tag(
        name = "Huéspedes",
        description = "Gestión de huéspedes registrados en el sistema hotelero"
)
public class HuespedController {

    private final HuespedService huespedService;

    public HuespedController(HuespedService huespedService) {
        this.huespedService = huespedService;
    }
    @Operation(
            summary = "Buscar huéspedes",
            description = "Obtiene huéspedes registrados permitiendo filtrar por nombre y DNI"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<HuespedResponseDTO>> obtenerHuesped(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String dni) {


        return ResponseEntity.ok(huespedService.buscarConFiltros(nombre,dni));
    }
    @Operation(
            summary = "Buscar huésped por ID",
            description = "Obtiene la información de un huésped utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Huésped encontrado"),
            @ApiResponse(responseCode = "404", description = "Huésped no encontrado")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<HuespedResponseDTO> getById(
            @PathVariable UUID idExterno){
        return ResponseEntity.ok(huespedService.getById(idExterno)
        );
    }
    @Operation(
            summary = "Registrar huésped",
            description = "Crea un nuevo huésped en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Huésped registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<HuespedResponseDTO> registrar(@Valid @RequestBody HuespedRequestDTO request) {
       HuespedResponseDTO respons = huespedService.guardarHuesped(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(respons);
    }
    @Operation(
            summary = "Eliminar huésped",
            description = "Elimina un huésped utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Huésped eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Huésped no encontrado")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID idExterno)
    {
        huespedService.delete(idExterno);
        return  ResponseEntity.noContent().build();
    }

}