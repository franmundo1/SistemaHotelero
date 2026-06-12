package lpda.SistemaHotelero.features.checkIn;

import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInRequestDTO;
import lpda.SistemaHotelero.features.checkIn.DTO.CheckInResponseDTO;
import lpda.SistemaHotelero.features.habitaciones.HabitacionEntity;
import lpda.SistemaHotelero.features.habitaciones.HabitacionRepository;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final CheckInMapper checkInMapper;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;

    public CheckInService(
            CheckInRepository checkInRepository,
            CheckInMapper checkInMapper,
            ReservaRepository reservaRepository,
            HabitacionRepository habitacionRepository
    ) {
        this.checkInRepository = checkInRepository;
        this.checkInMapper = checkInMapper;
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional(readOnly = true)
    public CheckInResponseDTO getById(UUID idExterno) {
        CheckInEntity checkIn = checkInRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in no encontrado con ID: " + idExterno));

        return checkInMapper.toResponseDTO(checkIn);
    }

    @Transactional
    public CheckInResponseDTO guardarCheck(CheckInRequestDTO dto) {
        ReservaEntity reserva = reservaRepository.findByIdExterno(dto.getIdReservaExterno())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID externo: " + dto.getIdReservaExterno()));

        LocalDate hoy = LocalDate.now();

        if (!reserva.getFechaEntrada().equals(hoy)) {
            throw new BadRequestException("El check-in solo puede realizarse en la fecha de entrada de la reserva");
        }


        if (!"CONFIRMADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new BadRequestException("Solo se puede hacer check-in de reservas CONFIRMADAS");
        }

        if (checkInRepository.existsByReserva_IdExterno(reserva.getIdExterno())) {
            throw new BadRequestException("La reserva ya tiene un check-in registrado");
        }

        HabitacionEntity habitacion = reserva.getHabitacion();

        if (!EstadoOcupacion.DISPONIBLE.equals(habitacion.getEstadoOcupacion())) {
            throw new BadRequestException("La habitación no está disponible para check-in");
        }
        if (!EstadoLimpieza.LIMPIA.equals(habitacion.getEstadoLimpieza())) {
            throw new BadRequestException("No se puede hacer check-in porque la habitación no está limpia");
        }

        UsuarioEntity usuarioLogueado = (UsuarioEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        CheckInEntity checkIn = new CheckInEntity();
        checkIn.setReserva(reserva);
        checkIn.setUsuario(usuarioLogueado);
        checkIn.setFechaHora(LocalDateTime.now());
        checkIn.setObservaciones(dto.getObservaciones());

        reserva.setEstado("EN_CURSO");
        habitacion.setEstadoOcupacion(EstadoOcupacion.OCUPADA);

        CheckInEntity checkInGuardado = checkInRepository.save(checkIn);
        habitacionRepository.save(habitacion);
        reservaRepository.save(reserva);

        return checkInMapper.toResponseDTO(checkInGuardado);
    }

    @Transactional
    public void delete(UUID idExterno) {
        CheckInEntity checkIn = checkInRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in no encontrado con ID: " + idExterno));

        checkInRepository.delete(checkIn);
    }
}
