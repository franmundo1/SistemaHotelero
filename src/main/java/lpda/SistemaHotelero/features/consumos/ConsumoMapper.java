package lpda.SistemaHotelero.features.consumos;

import lpda.SistemaHotelero.features.consumos.DTO.ConsumoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsumoMapper {

    public ConsumoResponseDTO toResponseDTO(ConsumoEntity entity) {
        return ConsumoResponseDTO.builder()
                .idExterno(entity.getIdExterno())
                .idReservaExterno(entity.getReserva().getIdExterno())
                .numeroHabitacion(entity.getReserva().getHabitacion().getNumero())
                .idProductoExterno(entity.getProducto().getIdExterno())
                .producto(entity.getProducto().getNombre())
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotal(entity.getSubtotal())
                .fechaConsumo(entity.getFechaConsumo())
                .observaciones(entity.getObservaciones())
                .build();
    }
}