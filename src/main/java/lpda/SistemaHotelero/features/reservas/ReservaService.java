package lpda.SistemaHotelero.features.reservas;

import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.canalesReservas.CanalReservaEntity;
import lpda.SistemaHotelero.features.canalesReservas.CanalReservaRepository;
import lpda.SistemaHotelero.features.habitaciones.HabitacionEntity;
import lpda.SistemaHotelero.features.habitaciones.HabitacionRepository;
import lpda.SistemaHotelero.features.huespedes.HuespedEntity;
import lpda.SistemaHotelero.features.huespedes.HuespedRepository;
import lpda.SistemaHotelero.features.pagos.PagoEntity;
import lpda.SistemaHotelero.features.pagos.PagoRepository;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaRequestDTO;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaResponseDTO;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;

    private final HuespedRepository huespedRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CanalReservaRepository canalReservaRepository;
    private final PagoRepository pagoRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            ReservaMapper reservaMapper,
            HuespedRepository huespedRepository,
            HabitacionRepository habitacionRepository,
            UsuarioRepository usuarioRepository,
            CanalReservaRepository canalReservaRepository,
            PagoRepository pagoRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.reservaMapper = reservaMapper;
        this.huespedRepository = huespedRepository;
        this.habitacionRepository = habitacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.canalReservaRepository = canalReservaRepository;
        this.pagoRepository=pagoRepository;
    }

    public ReservaResponseDTO crearReserva(ReservaRequestDTO dto) {
        HuespedEntity huesped = huespedRepository.findByIdExterno(dto.getIdHuespedExterno())
                .orElseThrow(() -> new ResourceNotFoundException("Huesped no encontrado con ID externo: " + dto.getIdHuespedExterno()));

        HabitacionEntity habitacion = habitacionRepository.findByNumero(dto.getNumeroHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con número: " + dto.getNumeroHabitacion()));

        if (dto.getCantidadPersonas() == null) {
            throw new BadRequestException("La cantidad de personas es obligatoria");
        }

        if (dto.getCantidadPersonas() < 1) {
            throw new BadRequestException("La cantidad de personas debe ser al menos 1");
        }

        if (dto.getCantidadPersonas() > habitacion.getCapacidad()) {
            throw new BadRequestException(
                    "La cantidad de personas supera la capacidad de la habitación. Capacidad máxima: "
                            + habitacion.getCapacidad()
            );
        }

        UsuarioEntity usuario = usuarioRepository.findByEmail(dto.getEmailUsuarioCreador())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + dto.getEmailUsuarioCreador()));

        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(dto.getIdCanalReservaExterno())
                .orElseThrow(() -> new ResourceNotFoundException("Canal no encontrado con ID externo: " + dto.getIdCanalReservaExterno()));

        LocalDate hoy = LocalDate.now();

        if (dto.getFechaEntrada() == null) {
            throw new BadRequestException("La fecha de entrada es obligatoria");
        }

        if (dto.getFechaSalida() == null) {
            throw new BadRequestException("La fecha de salida es obligatoria");
        }

        if (dto.getFechaEntrada().isBefore(hoy)) {
            throw new BadRequestException("No se puede crear una reserva con fecha de entrada pasada");
        }

        if (!dto.getFechaSalida().isAfter(dto.getFechaEntrada())) {
            throw new BadRequestException("La fecha de salida debe ser posterior a la fecha de entrada");
        }

        boolean existeSolapamiento = reservaRepository.existsReservaSolapada(
                habitacion.getIdHabitacion(),
                dto.getFechaEntrada(),
                dto.getFechaSalida()
        );

        if (existeSolapamiento) {
            throw new BadRequestException("La habitación ya tiene una reserva activa en ese rango de fechas");
        }
        long cantidadNoches = ChronoUnit.DAYS.between(
                dto.getFechaEntrada(),
                dto.getFechaSalida()
        );

        if (cantidadNoches <= 0) {
            throw new BadRequestException("La reserva debe tener al menos una noche");
        }

        BigDecimal precioPorNoche = habitacion.getPrecioPorNoche();

        BigDecimal totalEstadia = precioPorNoche.multiply(
                BigDecimal.valueOf(cantidadNoches)
        );

        BigDecimal anticipo = dto.getAnticipo() != null
                ? dto.getAnticipo()
                : BigDecimal.ZERO;

        if (anticipo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El anticipo debe ser mayor a 0");
        }

        if (anticipo.compareTo(totalEstadia) > 0) {
            throw new BadRequestException("El anticipo no puede ser mayor al total de la estadía");
        }

        if (anticipo.compareTo(BigDecimal.ZERO) > 0 &&
                (dto.getMetodoPagoAnticipo() == null || dto.getMetodoPagoAnticipo().isBlank())) {
            throw new BadRequestException("Debe indicar el método de pago del anticipo");
        }

        ReservaEntity reserva = reservaMapper.toEntity(
                dto,
                huesped,
                habitacion,
                usuario,
                canal
        );

        reserva.setTotalEstadia(totalEstadia);
        reserva.setAnticipo(anticipo);

        if (reserva.getEstado() == null || reserva.getEstado().isBlank()) {
            reserva.setEstado("CONFIRMADA");
        }

        ReservaEntity guardada = reservaRepository.save(reserva);

        if (anticipo.compareTo(BigDecimal.ZERO) > 0) {
            PagoEntity pagoAnticipo = new PagoEntity();
            pagoAnticipo.setReserva(guardada);
            pagoAnticipo.setUsuario(usuario);
            pagoAnticipo.setMonto(anticipo);
            pagoAnticipo.setMetodoPago(dto.getMetodoPagoAnticipo());
            pagoAnticipo.setTipoPago("ANTICIPO");
            pagoAnticipo.setObservaciones("Anticipo registrado al crear la reserva");

            pagoRepository.save(pagoAnticipo);
        }

        return reservaMapper.toDTO(guardada);
    }

    public List<ReservaResponseDTO> listarReservas() {
        return reservaRepository.findAll()
                .stream()
                .map(reservaMapper::toDTO)
                .toList();
    }

    public ReservaResponseDTO buscarPorId(UUID idExterno) {
        ReservaEntity reserva = reservaRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID externo: " + idExterno));

        return reservaMapper.toDTO(reserva);
    }

    public ReservaResponseDTO actualizarReserva(
            UUID idExterno,
            ReservaRequestDTO dto
    ) {
        ReservaEntity reserva = reservaRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID externo: " + idExterno));

        HabitacionEntity habitacion = habitacionRepository.findByNumero(dto.getNumeroHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con número: " + dto.getNumeroHabitacion()));

        if (dto.getCantidadPersonas() == null) {
            throw new BadRequestException("La cantidad de personas es obligatoria");
        }

        if (dto.getCantidadPersonas() < 1) {
            throw new BadRequestException("La cantidad de personas debe ser al menos 1");
        }

        if (dto.getCantidadPersonas() > habitacion.getCapacidad()) {
            throw new BadRequestException(
                    "La cantidad de personas supera la capacidad de la habitación. Capacidad máxima: "
                            + habitacion.getCapacidad()
            );
        }

        HuespedEntity huesped = huespedRepository.findByIdExterno(dto.getIdHuespedExterno())
                .orElseThrow(() -> new ResourceNotFoundException("Huesped no encontrado con ID externo: " + dto.getIdHuespedExterno()));

        UsuarioEntity usuario = usuarioRepository.findByEmail(dto.getEmailUsuarioCreador())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + dto.getEmailUsuarioCreador()));

        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(dto.getIdCanalReservaExterno())
                .orElseThrow(() -> new ResourceNotFoundException("Canal no encontrado con ID externo: " + dto.getIdCanalReservaExterno()));



        LocalDate hoy = LocalDate.now();

        if (dto.getFechaEntrada() == null) {
            throw new BadRequestException("La fecha de entrada es obligatoria");
        }

        if (dto.getFechaSalida() == null) {
            throw new BadRequestException("La fecha de salida es obligatoria");
        }

        if (dto.getFechaEntrada().isBefore(hoy)) {
            throw new BadRequestException("No se puede actualizar una reserva con fecha de entrada pasada");
        }

        if (!dto.getFechaSalida().isAfter(dto.getFechaEntrada())) {
            throw new BadRequestException("La fecha de salida debe ser posterior a la fecha de entrada");
        }

        boolean existeSolapamiento = reservaRepository.existsReservaSolapadaExcluyendoReservaActual(
                habitacion.getIdHabitacion(),
                reserva.getIdReserva(),
                dto.getFechaEntrada(),
                dto.getFechaSalida()
        );

        if (existeSolapamiento) {
            throw new BadRequestException("La habitación ya tiene una reserva activa en ese rango de fechas");
        }

        reserva.setHuesped(huesped);
        reserva.setHabitacion(habitacion);
        reserva.setUsuarioCreador(usuario);
        reserva.setCanalReserva(canal);

        long cantidadNoches = ChronoUnit.DAYS.between(
                dto.getFechaEntrada(),
                dto.getFechaSalida()
        );
        if (cantidadNoches <= 0) {
            throw new BadRequestException("La reserva debe tener al menos una noche");
        }

        BigDecimal totalEstadia = habitacion.getPrecioPorNoche()
                .multiply(BigDecimal.valueOf(cantidadNoches));

        BigDecimal anticipo = dto.getAnticipo() != null
                ? dto.getAnticipo()
                : BigDecimal.ZERO;

        if (anticipo.compareTo(totalEstadia) > 0) {
            throw new BadRequestException("El anticipo no puede ser mayor al total de la estadía");
        }

        reserva.setTotalEstadia(totalEstadia);
        reserva.setAnticipo(anticipo);


        reserva.setFechaEntrada(dto.getFechaEntrada());
        reserva.setFechaSalida(dto.getFechaSalida());
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setEstado(dto.getEstado());
        reserva.setObservaciones(dto.getObservaciones());

        ReservaEntity actualizada = reservaRepository.save(reserva);

        return reservaMapper.toDTO(actualizada);
    }

    public void eliminarReserva(UUID idExterno) {
        ReservaEntity reserva = reservaRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID externo: " + idExterno));

        reservaRepository.delete(reserva);
    }
    public ReservaResponseDTO cancelarReserva(UUID idExterno, String motivoCancelacion) {
        ReservaEntity reserva = reservaRepository.findByIdExterno(idExterno)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID externo: " + idExterno));

        if ("FINALIZADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new BadRequestException("No se puede cancelar una reserva finalizada");
        }

        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new BadRequestException("La reserva ya se encuentra cancelada");
        }

        if ("EN_CURSO".equalsIgnoreCase(reserva.getEstado())) {
            throw new BadRequestException("No se puede cancelar una reserva con check-in realizado");
        }

        reserva.setEstado("CANCELADA");
        reserva.setMotivoCancelacion(motivoCancelacion);

        ReservaEntity cancelada = reservaRepository.save(reserva);

        return reservaMapper.toDTO(cancelada);
    }

    public List<ReservaResponseDTO> listarPorEstado(String estado) {
        return reservaRepository.findByEstadoIgnoreCase(estado)
                .stream()
                .map(reservaMapper::toDTO)
                .toList();
    }
}
