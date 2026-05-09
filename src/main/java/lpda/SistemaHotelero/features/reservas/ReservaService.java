package lpda.SistemaHotelero.features.reservas;

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

import java.util.List;

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

        HuespedEntity huesped = huespedRepository.findById(dto.getIdHuesped())
                .orElseThrow(() -> new RuntimeException("Huésped no encontrado"));

        HabitacionEntity habitacion = habitacionRepository.findById(dto.getIdHabitacion())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        UsuarioEntity usuario = usuarioRepository.findById(dto.getIdUsuarioCreador())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CanalReservaEntity canal = canalReservaRepository.findById(dto.getIdCanalReserva())
                .orElseThrow(() -> new RuntimeException("Canal no encontrado"));

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
}
