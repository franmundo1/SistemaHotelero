package lpda.SistemaHotelero.features.productos.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lpda.SistemaHotelero.features.productos.CategoriaProducto;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String nombre;

    @NotNull(message = "La categoría es obligatoria")
    private CategoriaProducto categoria;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private Boolean activo;
}