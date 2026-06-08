package lpda.SistemaHotelero.features.canalesReservas;

import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CanalReservaService {

    @Autowired
    private CanalReservaRepository canalReservaRepository;

    @Autowired
    private CanalReservaMapper canalReservaMapper;

    public CanalReservaResponseDTO crearCanal(CanalReservaRequestDTO dto) {
        TipoCanal tipo = parseTipoCanal(dto.getTipo());

        if (canalReservaRepository.existsByTipo(tipo)) {
            throw new BadRequestException("El canal ya existe");
        }

        CanalReservaEntity canal = canalReservaMapper.toEntity(dto);
        CanalReservaEntity guardado = canalReservaRepository.save(canal);

        return canalReservaMapper.toDTO(guardado);
    }

    public List<CanalReservaResponseDTO> listarCanales() {
        return canalReservaRepository.findAll()
                .stream()
                .map(canalReservaMapper::toDTO)
                .toList();
    }

    public CanalReservaResponseDTO buscarPorIdExterno(UUID id) {
        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canal no encontrado"));

        return canalReservaMapper.toDTO(canal);
    }

    public CanalReservaResponseDTO actualizarCanal(UUID id, CanalReservaRequestDTO dto) {
        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canal no encontrado"));

        canal.setTipo(parseTipoCanal(dto.getTipo()));

        CanalReservaEntity actualizado = canalReservaRepository.save(canal);

        return canalReservaMapper.toDTO(actualizado);
    }

    public void eliminarCanal(UUID id) {
        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canal no encontrado"));

        canalReservaRepository.delete(canal);
    }

    private TipoCanal parseTipoCanal(String tipo) {
        try {
            return TipoCanal.valueOf(tipo.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new BadRequestException("Tipo de canal invalido");
        }
    }
}
