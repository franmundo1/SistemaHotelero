package lpda.SistemaHotelero.features.checkIn;

import lpda.SistemaHotelero.features.checkIn.DTO.CheckInRequestDTO;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CheckInMapper {

    public CheckInEntity toEntity(CheckInRequestDTO dto) {
        if (dto == null) return null;

        CheckInEntity entity = new CheckInEntity();
        entity.setObservaciones(dto.getObservaciones());

        return entity;
    }

    public CheckInResponseDTO toResponseDTO(CheckInEntity entity) {
        if (entity == null) return null;

        CheckInResponseDTO dto = new CheckInResponseDTO();


        dto.setId(entity.getIdExterno());
        dto.setFechaHoraEntrada(entity.getFechaHora());
        dto.setObservaciones(entity.getObservaciones());


        if (entity.getReserva() != null) {
            dto.setReservaId(String.valueOf(entity.getReserva().getIdReserva()));

            // Si la reserva tiene un huésped, armamos el nombre completo
            if (entity.getReserva().getHuesped() != null) {
                String nombreCompleto = entity.getReserva().getHuesped().getNombre() + " " +
                        entity.getReserva().getHuesped().getApellido();
                dto.setNombreHuesped(nombreCompleto);
            }
        }

        return dto;
    }
}