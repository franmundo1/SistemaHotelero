package lpda.SistemaHotelero.features.reservas;

import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import lpda.SistemaHotelero.features.canalesReservas.CanalReservaEntity;
import lpda.SistemaHotelero.features.canalesReservas.CanalReservaRepository;
import lpda.SistemaHotelero.features.habitaciones.HabitacionEntity;
import lpda.SistemaHotelero.features.habitaciones.HabitacionRepository;
import lpda.SistemaHotelero.features.huespedes.HuespedEntity;
import lpda.SistemaHotelero.features.huespedes.HuespedRepository;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaRequestDTO;
import lpda.SistemaHotelero.features.reservas.DTO.ReservaResponseDTO;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public ReservaService(
            ReservaRepository reservaRepository,
            ReservaMapper reservaMapper,
            HuespedRepository huespedRepository,
            HabitacionRepository habitacionRepository,
            UsuarioRepository usuarioRepository,
            CanalReservaRepository canalReservaRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.reservaMapper = reservaMapper;
        this.huespedRepository = huespedRepository;
        this.habitacionRepository = habitacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.canalReservaRepository = canalReservaRepository;
    }

    public ReservaResponseDTO crearReserva(ReservaRequestDTO dto) {
        HuespedEntity huesped = huespedRepository.findByIdExterno(dto.getIdHuespedExterno())
                .orElseThrow(() -> new ResourceNotFoundException("Huesped no encontrado con ID externo: " + dto.getIdHuespedExterno()));

        HabitacionEntity habitacion = habitacionRepository.findByNumero(dto.getNumeroHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con número: " + dto.getNumeroHabitacion()));

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

        ReservaEntity reserva = reservaMapper.toEntity(
                dto,
                huesped,
                habitacion,
                usuario,
                canal
        );

        ReservaEntity guardada = reservaRepository.save(reserva);

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


        reserva.setFechaEntrada(dto.getFechaEntrada());
        reserva.setFechaSalida(dto.getFechaSalida());
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setPrecioPorNoche(dto.getPrecioPorNoche());
        reserva.setTotalEstadia(dto.getTotalEstadia());
        reserva.setAnticipo(dto.getAnticipo());
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
}
