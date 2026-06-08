package lpda.SistemaHotelero.features.acompanantes;

import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import org.springframework.stereotype.Component;

@Component
public class AcompananteMapper {

    public AcompananteEntity toEntity(AcompananteRequestDTO dto, ReservaEntity reserva) {
        AcompananteEntity entity = new AcompananteEntity();

        entity.setReserva(reserva);
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDni(dto.getDni());

        return entity;
    }

    public void updateEntity(AcompananteEntity entity, AcompananteRequestDTO dto) {
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDni(dto.getDni());
    }

    public AcompananteResponseDTO toResponse(AcompananteEntity entity) {
        return AcompananteResponseDTO.builder()
                .idExterno(entity.getIdExterno())
                .idReserva(entity.getReserva().getIdReserva())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .dni(entity.getDni())
                .build();
    }
}