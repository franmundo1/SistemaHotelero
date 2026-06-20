package lpda.SistemaHotelero.features.productos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.productos.DTO.ProductoRequestDTO;
import lpda.SistemaHotelero.features.productos.DTO.ProductoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(
        name = "Productos",
        description = "Gestión de productos y consumos disponibles en el hotel"
)
public class ProductoController {

    private final ProductoService productoService;
    @Operation(
            summary = "Crear producto",
            description = "Registra un nuevo producto disponible para consumos dentro del hotel"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoService.crear(dto));
    }
    @Operation(
            summary = "Listar productos",
            description = "Obtiene todos los productos registrados en el sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }
    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene un producto mediante su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{idExterno}")
    public ResponseEntity<ProductoResponseDTO> buscarPorIdExterno(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(productoService.buscarPorIdExterno(idExterno));
    }
    @Operation(
            summary = "Actualizar producto",
            description = "Modifica completamente la información de un producto"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{idExterno}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable UUID idExterno,
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        return ResponseEntity.ok(productoService.actualizar(idExterno, dto));
    }
    @Operation(
            summary = "Cambiar estado de producto",
            description = "Permite activar o desactivar un producto"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{idExterno}/activo")
    public ResponseEntity<ProductoResponseDTO> cambiarActivo(
            @PathVariable UUID idExterno,
            @RequestParam Boolean activo
    ) {
        return ResponseEntity.ok(productoService.cambiarActivo(idExterno, activo));
    }
    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto utilizando su identificador externo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idExterno
    ) {
        productoService.eliminar(idExterno);
        return ResponseEntity.noContent().build();
    }
}