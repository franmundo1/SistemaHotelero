package lpda.SistemaHotelero.features.checkOut;

import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CheckOutMapper {

    public CheckOutResponseDTO toResponseDTO(CheckOutEntity entity) {
        if (entity == null) {
            return null;
        }

        CheckOutResponseDTO dto = new CheckOutResponseDTO();

        dto.setIdExterno(entity.getIdExterno());
        dto.setFechaHoraSalida(entity.getFechaHora());
        dto.setObservaciones(entity.getObservaciones());
        dto.setTotalEstadia(entity.getTotalEstadia());
        dto.setAnticipo(entity.getAnticipo());
        dto.setMontoFinal(entity.getMontoFinal());

        if (entity.getReserva() != null) {
            dto.setIdReservaExterno(entity.getReserva().getIdExterno());
            dto.setEstadoReserva(entity.getReserva().getEstado());

            if (entity.getReserva().getHuesped() != null) {
                String nombreCompleto = entity.getReserva().getHuesped().getNombre() + " " +
                        entity.getReserva().getHuesped().getApellido();

                dto.setNombreHuesped(nombreCompleto);
            }

            if (entity.getReserva().getHabitacion() != null) {
                dto.setNumeroHabitacion(entity.getReserva().getHabitacion().getNumero());
                dto.setEstadoOcupacionHabitacion(
                        entity.getReserva().getHabitacion().getEstadoOcupacion().name()
                );
                dto.setEstadoLimpiezaHabitacion(
                        entity.getReserva().getHabitacion().getEstadoLimpieza().name()
                );
            }
        }

        return dto;
    }
}