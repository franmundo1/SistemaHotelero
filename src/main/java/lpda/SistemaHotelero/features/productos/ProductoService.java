package lpda.SistemaHotelero.features.productos;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.productos.DTO.ProductoRequestDTO;
import lpda.SistemaHotelero.features.productos.DTO.ProductoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        if (productoRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new BadRequestException("Ya existe un producto con el nombre: " + dto.getNombre());
        }

        ProductoEntity producto = productoMapper.toEntity(dto);
        ProductoEntity guardado = productoRepository.save(producto);

        return productoMapper.toResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorIdExterno(UUID idExterno) {
        return productoMapper.toResponseDTO(buscarEntityPorIdExterno(idExterno));
    }

    @Transactional
    public ProductoResponseDTO actualizar(UUID idExterno, ProductoRequestDTO dto) {
        ProductoEntity producto = buscarEntityPorIdExterno(idExterno);

        productoRepository.findAll()
                .stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(dto.getNombre()))
                .filter(p -> !p.getIdProducto().equals(producto.getIdProducto()))
                .findFirst()
                .ifPresent(p -> {
                    throw new BadRequestException("Ya existe otro producto con el nombre: " + dto.getNombre());
                });

        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        ProductoEntity actualizado = productoRepository.save(producto);

        return productoMapper.toResponseDTO(actualizado);
    }

    @Transactional
    public ProductoResponseDTO cambiarActivo(UUID idExterno, Boolean activo) {
        ProductoEntity producto = buscarEntityPorIdExterno(idExterno);

        producto.setActivo(activo);

        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(UUID idExterno) {
        ProductoEntity producto = buscarEntityPorIdExterno(idExterno);
        productoRepository.delete(producto);
    }

    public ProductoEntity buscarEntityPorIdExterno(UUID idExterno) {
        return productoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID externo: " + idExterno));
    }
}