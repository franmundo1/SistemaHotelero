package lpda.SistemaHotelero.features.productos.DTO;

import lombok.*;
import lpda.SistemaHotelero.features.productos.CategoriaProducto;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private UUID idExterno;
    private String nombre;
    private CategoriaProducto categoria;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
}