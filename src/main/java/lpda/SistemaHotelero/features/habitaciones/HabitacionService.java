package lpda.SistemaHotelero.features.habitaciones;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.habitaciones.enums.TipoHabitacion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public HabitacionResponseDTO findById(UUID idExterno) {
        HabitacionEntity habitacion = habitacionRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idExterno));

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

    public HabitacionResponseDTO update(UUID idExterno, HabitacionRequestDTO requestDTO) {
        HabitacionEntity habitacionExistente = habitacionRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idExterno));

        habitacionRepository.findByNumero(requestDTO.getNumero())
                .ifPresent(habitacionConMismoNumero -> {
                    if (!habitacionConMismoNumero.getIdHabitacion().equals(idExterno)) {
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

    public void deleteById(UUID idExterno) {
        HabitacionEntity habitacion = habitacionRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idExterno));

        habitacionRepository.delete(habitacion);
    }

    public HabitacionResponseDTO patch (UUID idExterno, HabitacionPatchDTO patchDTO) {
        HabitacionEntity habitacion = habitacionRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idExterno));

        if (patchDTO.getNumero() != null) {
            habitacionRepository.findByNumero(patchDTO.getNumero())
                    .ifPresent(habitacionConMismoNumero -> {
                        if (!habitacionConMismoNumero.getIdHabitacion().equals(idExterno)) {
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
    public List<HabitacionResponseDTO> filtrarHabitaciones(
            String numero,
            TipoHabitacion tipo,
            Integer capacidad,
            EstadoOcupacion estadoOcupacion,
            EstadoLimpieza estadoLimpieza,
            Boolean activa
    ) {
        if (capacidad != null && (capacidad < 1 || capacidad > 3)) {
            throw new BadRequestException("La capacidad debe estar entre 1 y 3");
        }

        return habitacionRepository.filtrarHabitaciones(
                        numero,
                        tipo,
                        capacidad,
                        estadoOcupacion,
                        estadoLimpieza,
                        activa
                )
                .stream()
                .map(habitacionMapper::toResponseDTO)
                .toList();
    }
    public HabitacionLimpiezaResponseDTO cambiarEstadoLimpiezaPorNumero(
            String numero,
            EstadoLimpieza nuevoEstado
    ) {
        HabitacionEntity habitacion = habitacionRepository.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con número: " + numero));

        EstadoLimpieza estadoActual = habitacion.getEstadoLimpieza();

        boolean transicionValida =
                (EstadoLimpieza.SUCIA.equals(estadoActual) && EstadoLimpieza.EN_LIMPIEZA.equals(nuevoEstado))
                        || (EstadoLimpieza.EN_LIMPIEZA.equals(estadoActual) && EstadoLimpieza.LIMPIA.equals(nuevoEstado));

        if (!transicionValida) {
            throw new BadRequestException(
                    "Transición de limpieza no permitida. Solo se permite SUCIA -> EN_LIMPIEZA y EN_LIMPIEZA -> LIMPIA"
            );
        }

        habitacion.setEstadoLimpieza(nuevoEstado);

        HabitacionEntity habitacionActualizada = habitacionRepository.save(habitacion);

        return habitacionMapper.toLimpiezaResponseDTO(habitacionActualizada);
    }

    public HabitacionResponseDTO cambiarEstadoOcupacion(UUID idExterno, EstadoOcupacion estadoOcupacion) {
        HabitacionEntity habitacion = habitacionRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idExterno));

        habitacion.setEstadoOcupacion(estadoOcupacion);

        return habitacionMapper.toResponseDTO(habitacionRepository.save(habitacion));
    }

    public HabitacionResponseDTO cambiarActiva(UUID idExterno, Boolean activa) {
        HabitacionEntity habitacion = habitacionRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + idExterno));

        habitacion.setActiva(activa);

        return habitacionMapper.toResponseDTO(habitacionRepository.save(habitacion));
    }
    public List<HabitacionLimpiezaResponseDTO> findAllParaLimpieza() {
        return habitacionRepository.findByEstadoLimpieza(EstadoLimpieza.SUCIA)
                .stream()
                .map(habitacionMapper::toLimpiezaResponseDTO)
                .toList();
    }


}
