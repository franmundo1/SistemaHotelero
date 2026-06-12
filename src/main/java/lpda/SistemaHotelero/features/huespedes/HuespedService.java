package lpda.SistemaHotelero.features.huespedes;

import jakarta.persistence.EntityNotFoundException;
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

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;


    public HuespedService(HuespedRepository huespedRepository, HuespedMapper huespedMapper) {
        this.huespedRepository = huespedRepository;
        this.huespedMapper = huespedMapper;
    }

    @Transactional(readOnly = true)
    public List<HuespedResponseDTO> buscarConFiltros (String nombre, String dni){
        Specification<HuespedEntity> spec = Specification.where(HuespedSpecification.nombreLike(nombre))
                .and(HuespedSpecification.dniEquals(dni));

        return huespedRepository.findAll(spec).stream()
                .map(huespedMapper::toResponseDTO)
                .toList();
    }


    @Transactional
    public HuespedResponseDTO guardarHuesped(HuespedRequestDTO dto) {
        HuespedEntity entity = huespedMapper.toEntity(dto);
        if(huespedRepository.existsByDni(dto.getDni())){
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
    public void delete (UUID idExterno){
        HuespedEntity entity = huespedRepository.findByIdExterno(idExterno)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "Huesped no encontrado con ID: " +  idExterno
                        )
                );
      huespedRepository.delete(entity);
    }
}

