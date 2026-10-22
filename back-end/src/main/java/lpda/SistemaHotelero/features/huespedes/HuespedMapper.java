package lpda.SistemaHotelero.features.huespedes;

import lpda.SistemaHotelero.features.huespedes.DTO.HuespedRequestDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class HuespedMapper {
    public HuespedEntity toEntity(HuespedRequestDTO dto){
        if (dto == null) return null;

        HuespedEntity entity = new HuespedEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDni(dto.getDni());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());

        return entity;
    }

    public HuespedResponseDTO toResponseDTO(HuespedEntity entity){
        if(entity == null) return null;

        HuespedResponseDTO dto = new HuespedResponseDTO();

        dto.setIdExterno(entity.getIdExterno());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setDni(entity.getDni());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());

        return dto;
    }

}
