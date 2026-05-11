package lpda.SistemaHotelero.features.pagos;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoMapper pagoMapper;

    @Transactional
    public PagoResponseDTO registrarPago(PagoRequestDTO dto) {
        ReservaEntity reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con ID: " + dto.getIdReserva()));

        UsuarioEntity usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + dto.getIdUsuario()));

        if ("FINAL".equals(dto.getTipoPago()) &&
                pagoRepository.existsByReserva_IdReservaAndTipoPago(dto.getIdReserva(), "FINAL")) {
            throw new BadRequestException(
                    "Ya existe un pago FINAL registrado para la reserva con ID: " + dto.getIdReserva());
        }

        String estadoReserva = reserva.getEstado();
        if ("CANCELADA".equals(estadoReserva) || "FINALIZADA".equals(estadoReserva)) {
            throw new BadRequestException(
                    "No se puede registrar un pago para una reserva en estado: " + estadoReserva);
        }

        PagoEntity pago = pagoMapper.toEntity(dto, reserva, usuario);
        PagoEntity guardado = pagoRepository.save(pago);
        return pagoMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerTodos() {
        return pagoRepository.findAll()
                .stream()
                .map(pagoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerPagosPorReserva(Long idReserva) {
        if (!reservaRepository.existsById(idReserva)) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + idReserva);
        }
        return pagoRepository.findByReserva_IdReserva(idReserva)
                .stream()
                .map(pagoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerPagosPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario);
        }
        return pagoRepository.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(pagoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerPorIdExterno(UUID idExterno) {
        PagoEntity pago = pagoRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con ID externo: " + idExterno));
        return pagoMapper.toResponse(pago);
    }
}
