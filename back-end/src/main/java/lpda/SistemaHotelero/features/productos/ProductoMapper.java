package lpda.SistemaHotelero.features.productos;

import lpda.SistemaHotelero.features.productos.DTO.ProductoRequestDTO;
import lpda.SistemaHotelero.features.productos.DTO.ProductoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoEntity toEntity(ProductoRequestDTO dto) {
        return ProductoEntity.builder()
                .nombre(dto.getNombre())
                .categoria(dto.getCategoria())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public ProductoResponseDTO toResponseDTO(ProductoEntity entity) {
        return ProductoResponseDTO.builder()
                .idExterno(entity.getIdExterno())
                .nombre(entity.getNombre())
                .categoria(entity.getCategoria())
                .precio(entity.getPrecio())
                .stock(entity.getStock())
                .activo(entity.getActivo())
                .build();
    }
}