package lpda.SistemaHotelero.features.checkIn;

import lpda.SistemaHotelero.features.checkIn.DTO.CheckInResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CheckInMapper {

    public CheckInResponseDTO toResponseDTO(CheckInEntity entity) {
        if (entity == null) {
            return null;
        }

        CheckInResponseDTO dto = new CheckInResponseDTO();

        dto.setIdExterno(entity.getIdExterno());
        dto.setFechaHoraEntrada(entity.getFechaHora());
        dto.setObservaciones(entity.getObservaciones());

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
            }
        }

        return dto;
    }
}