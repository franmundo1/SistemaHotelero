package lpda.SistemaHotelero.features.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(
        name = "Dashboard",
        description = "Indicadores y métricas generales del sistema hotelero"
)
public class DashboardController {

    private final DashboardService dashboardService;
    @Operation(
            summary = "Obtener resumen del dashboard",
            description = "Retorna indicadores generales del hotel como reservas, ocupación, consumos y estadísticas operativas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumen obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> obtenerResumen() {
        return ResponseEntity.ok(dashboardService.obtenerResumen());
    }
}