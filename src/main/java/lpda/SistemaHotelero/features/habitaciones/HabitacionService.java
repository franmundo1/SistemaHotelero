package lpda.SistemaHotelero.features.habitaciones;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    public List<HabitacionResponseDTO> findAll() {
        return habitacionRepository.findAll()
                .stream()
                .map(habitacionMapper::toResponseDTO)
                .toList();
    }

    public HabitacionResponseDTO findById(Long id) {
        HabitacionEntity habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + id));

        return habitacionMapper.toResponseDTO(habitacion);
    }

    public HabitacionResponseDTO save(HabitacionRequestDTO requestDTO) {
        if (habitacionRepository.existsByNumero(requestDTO.getNumero())) {
            throw new BadRequestException("Ya existe una habitación con el número: " + requestDTO.getNumero());
        }

        HabitacionEntity habitacion = habitacionMapper.toEntity(requestDTO);
        HabitacionEntity habitacionGuardada = habitacionRepository.save(habitacion);

        return habitacionMapper.toResponseDTO(habitacionGuardada);
    }

    public HabitacionResponseDTO update(Long id, HabitacionRequestDTO requestDTO) {
        HabitacionEntity habitacionExistente = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + id));

        habitacionRepository.findByNumero(requestDTO.getNumero())
                .ifPresent(habitacionConMismoNumero -> {
                    if (!habitacionConMismoNumero.getIdHabitacion().equals(id)) {
                        throw new BadRequestException("Ya existe otra habitación con el número: " + requestDTO.getNumero());
                    }
                });

        habitacionExistente.setNumero(requestDTO.getNumero());
        habitacionExistente.setTipo(requestDTO.getTipo());
        habitacionExistente.setCapacidad(requestDTO.getCapacidad());
        habitacionExistente.setPrecioPorNoche(requestDTO.getPrecioPorNoche());
        habitacionExistente.setEstadoOcupacion(requestDTO.getEstadoOcupacion());
        habitacionExistente.setEstadoLimpieza(requestDTO.getEstadoLimpieza());
        habitacionExistente.setActiva(requestDTO.getActiva() != null ? requestDTO.getActiva() : true);

        HabitacionEntity habitacionActualizada = habitacionRepository.save(habitacionExistente);

        return habitacionMapper.toResponseDTO(habitacionActualizada);
    }

    public void deleteById(Long id) {
        HabitacionEntity habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + id));

        habitacionRepository.delete(habitacion);
    }

    public HabitacionResponseDTO patch (Long id, HabitacionPatchDTO patchDTO) {
        HabitacionEntity habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + id));

        if (patchDTO.getNumero() != null) {
            habitacionRepository.findByNumero(patchDTO.getNumero())
                    .ifPresent(habitacionConMismoNumero -> {
                        if (!habitacionConMismoNumero.getIdHabitacion().equals(id)) {
                            throw new BadRequestException("Ya existe otra habitación con el número: " + patchDTO.getNumero());
                        }
                    });

            habitacion.setNumero(patchDTO.getNumero());
        }

        if (patchDTO.getTipo() != null) {
            habitacion.setTipo(patchDTO.getTipo());
        }

        if (patchDTO.getCapacidad() != null) {
            habitacion.setCapacidad(patchDTO.getCapacidad());
        }

        if (patchDTO.getPrecioPorNoche() != null) {
            habitacion.setPrecioPorNoche(patchDTO.getPrecioPorNoche());
        }

        if (patchDTO.getEstadoOcupacion() != null) {
            habitacion.setEstadoOcupacion(patchDTO.getEstadoOcupacion());
        }

        if (patchDTO.getEstadoLimpieza() != null) {
            habitacion.setEstadoLimpieza(patchDTO.getEstadoLimpieza());
        }

        if (patchDTO.getActiva() != null) {
            habitacion.setActiva(patchDTO.getActiva());
        }

        HabitacionEntity habitacionActualizada = habitacionRepository.save(habitacion);

        return habitacionMapper.toResponseDTO(habitacionActualizada);
    }
}