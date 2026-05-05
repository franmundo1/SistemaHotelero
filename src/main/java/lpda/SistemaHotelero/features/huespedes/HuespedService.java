package lpda.SistemaHotelero.features.huespedes;

import lpda.SistemaHotelero.features.huespedes.DTO.HuespedRequestDTO;
import lpda.SistemaHotelero.features.huespedes.DTO.HuespedResponseDTO;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HuespedService {

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;


    public HuespedService(HuespedRepository huespedRepository, HuespedMapper huespedMapper) {
        this.huespedRepository = huespedRepository;
        this.huespedMapper = huespedMapper;
    }

    @Transactional

    public HuespedEntity guardarHuesped (HuespedEntity huesped){
        return huespedRepository.save(huesped);

    }

    @Transactional(readOnly = true)
    public List<HuespedEntity> buscarConFiltros (String nombre, String dni){
        PredicateSpecification<HuespedEntity> spec = PredicateSpecification.allOf(
                HuespedSpecification.nombreLike(nombre),
                HuespedSpecification.dniEquals(dni)
        );

        return huespedRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Optional<HuespedEntity> buscarPorId (Long id){
        return huespedRepository.findById(id);
    }

    @Transactional

    public HuespedResponseDTO guardarHuesped(HuespedRequestDTO dto){
        HuespedEntity entity = huespedMapper.toEntity(dto);

        HuespedEntity guardado = huespedRepository.save(entity);

        return huespedMapper.toResponseDTO(guardado);

    }
}
