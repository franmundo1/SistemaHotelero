package lpda.SistemaHotelero.features.habitaciones;

import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper {

    public HabitacionEntity toEntity(HabitacionRequestDTO dto) {
        return HabitacionEntity.builder()
                .numero(dto.getNumero())
                .tipo(dto.getTipo())
                .capacidad(dto.getCapacidad())
                .precioPorNoche(dto.getPrecioPorNoche())
                .estadoOcupacion(dto.getEstadoOcupacion())
                .estadoLimpieza(dto.getEstadoLimpieza())
                .activa(dto.getActiva() != null ? dto.getActiva() : true)
                .build();
    }
    public HabitacionResponseDTO toResponseDTO(HabitacionEntity entity) {
        return HabitacionResponseDTO.builder()
                .idHabitacion(entity.getIdHabitacion())
                .numero(entity.getNumero())
                .tipo(entity.getTipo())
                .capacidad(entity.getCapacidad())
                .precioPorNoche(entity.getPrecioPorNoche())
                .estadoOcupacion(entity.getEstadoOcupacion())
                .estadoLimpieza(entity.getEstadoLimpieza())
                .activa(entity.getActiva())
                .build();
    }
}
