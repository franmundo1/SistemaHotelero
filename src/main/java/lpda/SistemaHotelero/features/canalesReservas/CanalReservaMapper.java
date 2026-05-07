package lpda.SistemaHotelero.features.canalesReservas;

import org.springframework.stereotype.Component;

@Component
public class CanalReservaMapper {

    public CanalReservaEntity toEntity(CanalReservaRequestDTO dto){
        CanalReservaEntity canal = new CanalReservaEntity();
        canal.setNombrePersona(dto.getNombrePersona());
        return canal;
    }

    public CanalReservaResponseDTO toDTO(CanalReservaEntity canal){
        return new CanalReservaResponseDTO(
                canal.getIdCanal(),
                canal.getNombrePersona()
        );
    }
}
