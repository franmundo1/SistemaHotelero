package lpda.SistemaHotelero.features.acompanantes;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcompananteService {

    private final AcompananteRepository acompananteRepository;
    private final ReservaRepository reservaRepository;
    private final AcompananteMapper acompananteMapper;

    @Transactional
    public AcompananteResponseDTO create(AcompananteRequestDTO dto) {
        ReservaEntity reserva = buscarReserva(dto.getIdReserva());

        validarReservaHabilitada(reserva);
        validarDniDuplicadoEnReserva(dto.getIdReserva(), dto.getDni());
        validarCapacidadReserva(reserva);

        AcompananteEntity entity = acompananteMapper.toEntity(dto, reserva);
        AcompananteEntity guardado = acompananteRepository.save(entity);

        return acompananteMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<AcompananteResponseDTO> getAll() {
        return acompananteRepository.findAll()
                .stream()
                .map(acompananteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AcompananteResponseDTO getById(UUID idExterno) {
        return acompananteMapper.toResponse(buscarAcompanante(idExterno));
    }

    @Transactional(readOnly = true)
    public List<AcompananteResponseDTO> getByReserva(Long idReserva) {
        if (!reservaRepository.existsById(idReserva)) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + idReserva);
        }

        return acompananteRepository.findByReserva_IdReserva(idReserva)
                .stream()
                .map(acompananteMapper::toResponse)
                .toList();
    }

    @Transactional
    public AcompananteResponseDTO update(UUID idExterno, AcompananteRequestDTO dto) {
        AcompananteEntity entity = buscarAcompanante(idExterno);
        ReservaEntity reserva = buscarReserva(dto.getIdReserva());

        validarReservaHabilitada(reserva);

        if (dto.getDni() != null && !dto.getDni().isBlank()) {
            boolean dniDuplicado = acompananteRepository
                    .existsByReserva_IdReservaAndDniAndIdExternoNot(
                            dto.getIdReserva(),
                            dto.getDni(),
                            idExterno
                    );

            if (dniDuplicado) {
                throw new BadRequestException("Ya existe un acompañante con ese DNI en la reserva");
            }
        }

        entity.setReserva(reserva);
        acompananteMapper.updateEntity(entity, dto);

        return acompananteMapper.toResponse(acompananteRepository.save(entity));
    }

    @Transactional
    public void delete(UUID idExterno) {
        AcompananteEntity entity = buscarAcompanante(idExterno);
        acompananteRepository.delete(entity);
    }

    private AcompananteEntity buscarAcompanante(UUID idExterno) {
        return acompananteRepository.findByIdExterno(idExterno)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Acompañante no encontrado con ID: " + idExterno)
                );
    }

    private ReservaEntity buscarReserva(Long idReserva) {
        return reservaRepository.findById(idReserva)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reserva no encontrada con ID: " + idReserva)
                );
    }

    private void validarDniDuplicadoEnReserva(Long idReserva, String dni) {
        if (dni == null || dni.isBlank()) {
            return;
        }

        if (acompananteRepository.existsByReserva_IdReservaAndDni(idReserva, dni)) {
            throw new BadRequestException("Ya existe un acompañante con ese DNI en la reserva");
        }
    }

    private void validarCapacidadReserva(ReservaEntity reserva) {
        long acompanantesActuales = acompananteRepository.countByReserva_IdReserva(reserva.getIdReserva());

        int cantidadPersonas = reserva.getCantidadPersonas();
        int maximoAcompanantes = cantidadPersonas - 1;

        if (acompanantesActuales >= maximoAcompanantes) {
            throw new BadRequestException("La reserva ya alcanzó la cantidad máxima de acompañantes");
        }
    }

    private void validarReservaHabilitada(ReservaEntity reserva) {
        String estado = reserva.getEstado();

        if ("CANCELADA".equalsIgnoreCase(estado) || "FINALIZADA".equalsIgnoreCase(estado)) {
            throw new BadRequestException("No se pueden modificar acompañantes de una reserva " + estado);
        }
    }
}