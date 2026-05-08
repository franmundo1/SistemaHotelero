package lpda.SistemaHotelero.features.acompanantes;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcompananteService {

    private final AcompananteRepository acompananteRepository;
    private final AcompananteMapper acompananteMapper;

    public List<AcompananteResponseDTO> getAll() {

        return acompananteRepository.findAll()
                .stream()
                .map(acompananteMapper::toResponse)
                .toList();
    }

    public AcompananteResponseDTO getById(UUID idExterno) {

        AcompananteEntity entity = acompananteRepository.findByIdExterno(idExterno)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Acompañante no encontrado con ID: " + idExterno
                        )
                );

        return acompananteMapper.toResponse(entity);
    }

    public List<AcompananteResponseDTO> getByReserva(Long idReserva) {

        return acompananteRepository.findByReserva_IdReserva(idReserva)
                .stream()
                .map(acompananteMapper::toResponse)
                .toList();
    }

    public void delete(UUID idExterno) {

        AcompananteEntity entity = acompananteRepository.findByIdExterno(idExterno)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Acompañante no encontrado con ID: " + idExterno
                        )
                );

        acompananteRepository.delete(entity);
    }
}