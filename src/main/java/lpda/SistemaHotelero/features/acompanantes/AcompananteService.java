package lpda.SistemaHotelero.features.acompanantes;

import jakarta.persistence.EntityNotFoundException;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcompananteService {

    private final AcompananteRepository acompananteRepository;
    //private final ReservaRepository reservaRepository;
    private final AcompananteMapper acompananteMapper;

    public List<AcompananteResponseDTO> getAll() {
        return acompananteRepository.findAll()
                .stream()
                .map(acompananteMapper::toResponse)
                .toList();
    }

    public AcompananteResponseDTO getById(Long id) {
        AcompananteEntity entity = acompananteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Acompañante no encontrado con ID: " + id));
        return acompananteMapper.toResponse(entity);
    }

    public List<AcompananteResponseDTO> getByReserva(Long idReserva) {
        return acompananteRepository.findByReserva_IdReserva(idReserva)
                .stream()
                .map(acompananteMapper::toResponse)
                .toList();
    }

    // Falta reserva para que pueda hacerlo: public AcompananteResponseDTO create(AcompananteRequestDTO dto) {}

    // Falta reserva para que pueda hacerlo: public AcompananteResponseDTO update(Long id, AcompananteRequestDTO dto) {}

    public void delete(Long id) {
        if (!acompananteRepository.existsById(id)) {
            throw new EntityNotFoundException("Acompañante no encontrado con ID: " + id);
        }
        acompananteRepository.deleteById(id);
    }
}
