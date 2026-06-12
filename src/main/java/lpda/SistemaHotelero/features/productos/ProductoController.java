package lpda.SistemaHotelero.features.productos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.productos.DTO.ProductoRequestDTO;
import lpda.SistemaHotelero.features.productos.DTO.ProductoResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{idExterno}")
    public ResponseEntity<ProductoResponseDTO> buscarPorIdExterno(
            @PathVariable UUID idExterno
    ) {
        return ResponseEntity.ok(productoService.buscarPorIdExterno(idExterno));
    }

    @PutMapping("/{idExterno}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable UUID idExterno,
            @Valid @RequestBody ProductoRequestDTO dto
    ) {
        return ResponseEntity.ok(productoService.actualizar(idExterno, dto));
    }

    @PatchMapping("/{idExterno}/activo")
    public ResponseEntity<ProductoResponseDTO> cambiarActivo(
            @PathVariable UUID idExterno,
            @RequestParam Boolean activo
    ) {
        return ResponseEntity.ok(productoService.cambiarActivo(idExterno, activo));
    }

    @DeleteMapping("/{idExterno}")
    public ResponseEntity<Void> eliminar(
            @PathVariable UUID idExterno
    ) {
        productoService.eliminar(idExterno);
        return ResponseEntity.noContent().build();
    }
}