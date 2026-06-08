package lpda.SistemaHotelero.features.canalesReservas;

import lpda.SistemaHotelero.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class CanalReservaMapper {

    public CanalReservaEntity toEntity(CanalReservaRequestDTO dto) {
        CanalReservaEntity canal = new CanalReservaEntity();

        try {
            canal.setTipo(TipoCanal.valueOf(dto.getTipo().toUpperCase()));
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new BadRequestException("Tipo de canal invalido");
        }

        return canal;
    }

    public CanalReservaResponseDTO toDTO(CanalReservaEntity canal) {
        return new CanalReservaResponseDTO(
                canal.getIdExterno(),
                canal.getTipo().name()
        );
    }
}
