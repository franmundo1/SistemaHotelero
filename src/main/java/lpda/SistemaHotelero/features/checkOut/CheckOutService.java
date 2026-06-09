package lpda.SistemaHotelero.features.checkOut;

import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.checkIn.CheckInRepository;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutRequestDTO;
import lpda.SistemaHotelero.features.checkOut.Dto.CheckOutResponseDTO;
import lpda.SistemaHotelero.features.habitaciones.HabitacionEntity;
import lpda.SistemaHotelero.features.habitaciones.HabitacionRepository;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.pagos.PagoEntity;
import lpda.SistemaHotelero.features.pagos.PagoRepository;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CheckOutService {

    private final CheckOutRepository checkOutRepository;
    private final CheckOutMapper checkOutMapper;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final CheckInRepository checkInRepository;
    private final PagoRepository pagoRepository;

    public CheckOutService(
            CheckOutRepository checkOutRepository,
            CheckOutMapper checkOutMapper,
            ReservaRepository reservaRepository,
            HabitacionRepository habitacionRepository,
            CheckInRepository checkInRepository,
            PagoRepository pagoRepository
    ) {
        this.checkOutRepository = checkOutRepository;
        this.checkOutMapper = checkOutMapper;
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.checkInRepository = checkInRepository;
        this.pagoRepository=pagoRepository;
    }

    @Transactional(readOnly = true)
    public CheckOutResponseDTO getById(UUID idExterno) {
        CheckOutEntity checkOut = checkOutRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Check-out no encontrado con ID: " + idExterno));

        return checkOutMapper.toResponseDTO(checkOut);
    }

    @Transactional
    public CheckOutResponseDTO registrar(CheckOutRequestDTO dto) {
        ReservaEntity reserva = reservaRepository.findByIdExterno(dto.getIdReservaExterno())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID externo: " + dto.getIdReservaExterno()));

        if (!"EN_CURSO".equalsIgnoreCase(reserva.getEstado())) {
            throw new BadRequestException("Solo se puede hacer check-out de reservas EN_CURSO");
        }

        if (!checkInRepository.existsByReserva_IdExterno(reserva.getIdExterno())) {
            throw new BadRequestException("No se puede hacer check-out sin check-in previo");
        }

        if (checkOutRepository.existsByReserva_IdExterno(reserva.getIdExterno())) {
            throw new BadRequestException("La reserva ya tiene un check-out registrado");
        }

        UsuarioEntity usuarioLogueado = (UsuarioEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        BigDecimal totalEstadia = reserva.getTotalEstadia();
        BigDecimal anticipo = reserva.getAnticipo();
        BigDecimal montoFinal = totalEstadia.subtract(anticipo);

        if (dto.getMontoAbonado().compareTo(montoFinal) != 0) {
            throw new BadRequestException("El monto abonado no coincide con el saldo pendiente de la reserva");
        }

        HabitacionEntity habitacion = reserva.getHabitacion();

        CheckOutEntity checkOut = new CheckOutEntity();
        checkOut.setReserva(reserva);
        checkOut.setUsuario(usuarioLogueado);
        checkOut.setFechaHora(LocalDateTime.now());
        checkOut.setTotalEstadia(totalEstadia);
        checkOut.setAnticipo(anticipo);
        checkOut.setMontoFinal(montoFinal);
        checkOut.setObservaciones(dto.getObservaciones());

        PagoEntity pagoFinal = new PagoEntity();
        pagoFinal.setReserva(reserva);
        pagoFinal.setUsuario(usuarioLogueado);
        pagoFinal.setMonto(dto.getMontoAbonado());
        pagoFinal.setMetodoPago(dto.getMetodoPago());
        pagoFinal.setTipoPago("FINAL");
        pagoFinal.setObservaciones("Pago final registrado durante el check-out. " +
                (dto.getObservaciones() != null ? dto.getObservaciones() : ""));

        reserva.setEstado("FINALIZADA");
        habitacion.setEstadoOcupacion(EstadoOcupacion.DISPONIBLE);
        habitacion.setEstadoLimpieza(EstadoLimpieza.SUCIA);

        CheckOutEntity checkOutGuardado = checkOutRepository.save(checkOut);
        habitacionRepository.save(habitacion);
        reservaRepository.save(reserva);
        pagoRepository.save(pagoFinal);

        return checkOutMapper.toResponseDTO(checkOutGuardado);
    }

    @Transactional
    public void delete(UUID idExterno) {
        CheckOutEntity checkOut = checkOutRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Check-out no encontrado con ID: " + idExterno));

        checkOutRepository.delete(checkOut);
    }
}