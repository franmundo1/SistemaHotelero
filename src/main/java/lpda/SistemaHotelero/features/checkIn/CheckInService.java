package lpda.SistemaHotelero.features.checkIn;


import jakarta.persistence.EntityNotFoundException;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInRequestDTO;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInResponseDTO;
import lpda.SistemaHotelero.features.huespedes.HuespedSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final CheckInMapper checkInMapper;

    public CheckInService(CheckInRepository checkInRepository, CheckInMapper checkInMapper) {
        this.checkInRepository = checkInRepository;
        this.checkInMapper = checkInMapper;
    }

    @Transactional(readOnly = true)

    public CheckInResponseDTO getById (UUID idExterno) {
        return checkInRepository.findByIdExterno(idExterno)
                .map(checkInMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Check no encontrado con ID: " + idExterno));
    }

    @Transactional

    public void delete (UUID idExterno) {
        CheckInEntity entity = checkInRepository.findByIdExterno(idExterno)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "Check no encontrado con ID: " + idExterno
                        )
                );

        checkInRepository.delete(entity);

    }

    @Transactional
    public CheckInResponseDTO guardarCheck (CheckInRequestDTO dto) {
        CheckInEntity entity = checkInMapper.toEntity (dto);
        CheckInEntity guardado = checkInRepository.save(entity);
        return  checkInMapper.toResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public Optional<CheckInEntity> buscarPorId(long Id){
        return checkInRepository.findById(Id);
    }

    @Transactional (readOnly = true)
    public List<CheckInResponseDTO> buscarConFiltros(String nombre, UUID idExterno) {
        Specification<CheckInEntity> spec = Specification
                .where(CheckInSpecification.nombreHuespedLike(nombre))
                .and(CheckInSpecification.idExternoEquals(idExterno));

        return checkInRepository.findAll(spec).stream()
                .map(checkInMapper::toResponseDTO)
                .toList();
    }


}

