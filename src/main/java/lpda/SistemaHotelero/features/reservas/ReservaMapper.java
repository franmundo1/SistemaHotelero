package lpda.SistemaHotelero.features.reservas;

import lpda.SistemaHotelero.features.canalesReservas.CanalReservaEntity;
import lpda.SistemaHotelero.features.habitaciones.HabitacionEntity;
import lpda.SistemaHotelero.features.huespedes.HuespedEntity;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaRequestDTO;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaResponseDTO;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaEntity toEntity(
            ReservaRequestDTO dto,
            HuespedEntity huesped,
            HabitacionEntity habitacion,
            UsuarioEntity usuario,
            CanalReservaEntity canal
    ) {

        ReservaEntity reserva = new ReservaEntity();

        reserva.setHuesped(huesped);
        reserva.setHabitacion(habitacion);
        reserva.setUsuarioCreador(usuario);
        reserva.setCanalReserva(canal);

        reserva.setCodigoReservaExterna(dto.getCodigoReservaExterna());
        reserva.setFechaEntrada(dto.getFechaEntrada());
        reserva.setFechaSalida(dto.getFechaSalida());
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setPrecioPorNoche(dto.getPrecioPorNoche());
        reserva.setTotalEstadia(dto.getTotalEstadia());
        reserva.setAnticipo(dto.getAnticipo());
        reserva.setEstado(dto.getEstado());
        reserva.setObservaciones(dto.getObservaciones());

        return reserva;
    }

    public ReservaResponseDTO toDTO(ReservaEntity reserva) {

        return new ReservaResponseDTO(
                reserva.getIdReserva(),
                reserva.getHuesped().getNombre(),
                reserva.getHabitacion().getNumero(),
                reserva.getUsuarioCreador().getNombre() + " " +
                        reserva.getUsuarioCreador().getApellido(),
                reserva.getCanalReserva().getNombrePersona(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getTotalEstadia(),
                reserva.getEstado()
        );
    }
}
