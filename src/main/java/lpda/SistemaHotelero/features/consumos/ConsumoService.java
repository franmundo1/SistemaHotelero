package lpda.SistemaHotelero.features.consumos;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.consumos.DTO.ConsumoRequestDTO;
import lpda.SistemaHotelero.features.consumos.DTO.ConsumoResponseDTO;
import lpda.SistemaHotelero.features.productos.ProductoEntity;
import lpda.SistemaHotelero.features.productos.ProductoService;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumoService {

    private final ConsumoRepository consumoRepository;
    private final ReservaRepository reservaRepository;
    private final ProductoService productoService;
    private final ConsumoMapper consumoMapper;

    @Transactional
    public ConsumoResponseDTO crear(ConsumoRequestDTO dto) {
        ReservaEntity reserva = reservaRepository.findByIdExterno(dto.getIdReservaExterno())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con ID externo: " + dto.getIdReservaExterno()
                ));

        if (!"EN_CURSO".equalsIgnoreCase(reserva.getEstado())) {
            throw new BadRequestException("Solo se pueden cargar consumos a reservas EN_CURSO");
        }

        ProductoEntity producto = productoService.buscarEntityPorIdExterno(dto.getIdProductoExterno());

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new BadRequestException("El producto no está activo");
        }

        if (producto.getStock() < dto.getCantidad()) {
            throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(dto.getCantidad()));

        producto.setStock(producto.getStock() - dto.getCantidad());

        ConsumoEntity consumo = ConsumoEntity.builder()
                .reserva(reserva)
                .producto(producto)
                .cantidad(dto.getCantidad())
                .precioUnitario(precioUnitario)
                .subtotal(subtotal)
                .observaciones(dto.getObservaciones())
                .build();

        ConsumoEntity guardado = consumoRepository.save(consumo);

        return consumoMapper.toResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<ConsumoResponseDTO> listar() {
        return consumoRepository.findAll()
                .stream()
                .map(consumoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConsumoResponseDTO buscarPorIdExterno(UUID idExterno) {
        return consumoMapper.toResponseDTO(buscarEntityPorIdExterno(idExterno));
    }

    @Transactional(readOnly = true)
    public List<ConsumoResponseDTO> listarPorReserva(UUID idReservaExterno) {
        if (!reservaRepository.existsByIdExterno(idReservaExterno)) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID externo: " + idReservaExterno);
        }

        return consumoRepository.findByReserva_IdExterno(idReservaExterno)
                .stream()
                .map(consumoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void eliminar(UUID idExterno) {
        ConsumoEntity consumo = buscarEntityPorIdExterno(idExterno);

        String estadoReserva = consumo.getReserva().getEstado();

        if ("FINALIZADA".equalsIgnoreCase(estadoReserva) || "CANCELADA".equalsIgnoreCase(estadoReserva)) {
            throw new BadRequestException("No se pueden eliminar consumos de una reserva " + estadoReserva);
        }

        ProductoEntity producto = consumo.getProducto();
        producto.setStock(producto.getStock() + consumo.getCantidad());

        consumoRepository.delete(consumo);
    }

    public ConsumoEntity buscarEntityPorIdExterno(UUID idExterno) {
        return consumoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Consumo no encontrado con ID externo: " + idExterno));
    }

    public BigDecimal calcularTotalConsumosPorReserva(UUID idReservaExterno) {
        return consumoRepository.findByReserva_IdExterno(idReservaExterno)
                .stream()
                .map(ConsumoEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}