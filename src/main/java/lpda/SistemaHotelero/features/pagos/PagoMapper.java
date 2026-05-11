package lpda.SistemaHotelero.features.pagos;

import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoEntity toEntity(PagoRequestDTO dto, ReservaEntity reserva, UsuarioEntity usuario) {
        PagoEntity pago = new PagoEntity();
        pago.setReserva(reserva);
        pago.setUsuario(usuario);
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setTipoPago(dto.getTipoPago());
        pago.setObservaciones(dto.getObservaciones());
        return pago;
    }

    public PagoResponseDTO toResponse(PagoEntity pago) {
        return PagoResponseDTO.builder()
                .idPago(pago.getIdPago())
                .idExterno(pago.getIdExterno())
                .idReserva(pago.getReserva().getIdReserva())
                .idUsuario(pago.getUsuario().getIdUsuario())
                .nombreUsuario(pago.getUsuario().getNombre() + " " + pago.getUsuario().getApellido())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .tipoPago(pago.getTipoPago())
                .fechaPago(pago.getFechaPago())
                .observaciones(pago.getObservaciones())
                .build();
    }
}
