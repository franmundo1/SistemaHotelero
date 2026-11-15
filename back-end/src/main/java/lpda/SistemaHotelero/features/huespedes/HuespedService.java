package lpda.SistemaHotelero.features.huespedes;

import jakarta.persistence.EntityNotFoundException;
import lpda.SistemaHotelero.exceptions.ConflictException;
import lpda.SistemaHotelero.features.huespedes.DTO.BajaHuespedResponseDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedRequestDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedResponseDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HuespedService {

    private static final List<String> ESTADOS_ACTIVOS = List.of("CONFIRMADA", "EN_CURSO");

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;


    public HuespedService(HuespedRepository huespedRepository, HuespedMapper huespedMapper) {
        this.huespedRepository = huespedRepository;
        this.huespedMapper = huespedMapper;
    }

    @Transactional(readOnly = true)
    public List<HuespedResponseDTO> buscarConFiltros (String nombre, String dni){
        Specification<HuespedEntity> spec = Specification.where(HuespedSpecification.nombreLike(nombre))
                .and(HuespedSpecification.dniEquals(dni))
                .and(HuespedSpecification.activos());

        return huespedRepository.findAll(spec).stream()
                .map(huespedMapper::toResponseDTO)
                .toList();
    }


    @Transactional
    public HuespedResponseDTO guardarHuesped(HuespedRequestDTO dto) {
        // Si el DNI es de un huesped dado de baja, se reactiva con los datos nuevos
        Optional<HuespedEntity> existente = huespedRepository.findByDni(dto.getDni());
        if (existente.isPresent() && !existente.get().getActivo()) {
            HuespedEntity reactivado = existente.get();
            copiarDatos(reactivado, dto);
            reactivado.setActivo(true);
            return huespedMapper.toResponseDTO(huespedRepository.save(reactivado));
        }

        HuespedEntity entity = huespedMapper.toEntity(dto);
        if(existente.isPresent()){
            throw new IllegalArgumentException(
                    "Ya existe un huesped con DNI " + dto.getDni()
            );
        }
        HuespedEntity guardado = huespedRepository.save(entity);

        return huespedMapper.toResponseDTO(guardado);

    }

    @Transactional
    public HuespedResponseDTO getById(UUID idExterno){

        HuespedEntity entity = huespedRepository.findByIdExterno(idExterno)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "Huesped no encontrado con ID: " + idExterno
                        ));
        return huespedMapper.toResponseDTO(entity);
    }

    @Transactional
    public HuespedResponseDTO actualizarHuesped(UUID idExterno, HuespedRequestDTO dto) {
        HuespedEntity entity = huespedRepository.findByIdExterno(idExterno)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "Huesped no encontrado con ID: " + idExterno
                        ));
        if(huespedRepository.existsByDniAndIdExternoNot(dto.getDni(), idExterno)){
            throw new IllegalArgumentException(
                    "Ya existe un huesped con DNI " + dto.getDni()
            );
        }
        copiarDatos(entity, dto);

        HuespedEntity actualizado = huespedRepository.save(entity);

        return huespedMapper.toResponseDTO(actualizado);
    }

    // Sin reservas: se borra. Con reservas cerradas: baja logica. Con una reserva activa: no se puede.
    @Transactional
    public BajaHuespedResponseDTO delete (UUID idExterno){
        HuespedEntity entity = huespedRepository.findByIdExterno(idExterno)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "Huesped no encontrado con ID: " +  idExterno
                        )
                );

        if (entity.getReservas() == null || entity.getReservas().isEmpty()) {
            huespedRepository.delete(entity);
            return new BajaHuespedResponseDTO("FISICA", "Huesped eliminado");
        }

        boolean tieneReservaActiva = entity.getReservas().stream()
                .anyMatch(reserva -> ESTADOS_ACTIVOS.contains(reserva.getEstado().toUpperCase()));
        if (tieneReservaActiva) {
            throw new ConflictException(
                    "El huesped tiene una reserva confirmada o en curso. Cancelala o cerrala primero"
            );
        }

        entity.setActivo(false);
        huespedRepository.save(entity);
        return new BajaHuespedResponseDTO("LOGICA", "Huesped dado de baja. Se conserva su historial de reservas");
    }

    private void copiarDatos(HuespedEntity entity, HuespedRequestDTO dto) {
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDni(dto.getDni());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
    }
}

