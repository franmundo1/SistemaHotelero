package lpda.SistemaHotelero.features.canalesReservas;

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

    public CanalReservaResponseDTO crearCanal(CanalReservaRequestDTO dto){
        TipoCanal tipo = TipoCanal.valueOf(dto.getTipo().toUpperCase());

        if(canalReservaRepository.existsByTipo(tipo)){
            throw new RuntimeException("El canal ya existe");
        }

        CanalReservaEntity canal = canalReservaMapper.toEntity(dto);

        CanalReservaEntity guardado = canalReservaRepository.save(canal);

        return canalReservaMapper.toDTO(guardado);
    }

    public List<CanalReservaResponseDTO> listarCanales(){

        return canalReservaRepository.findAll()
                .stream()
                .map(canalReservaMapper::toDTO)
                .toList();
    }

    public CanalReservaResponseDTO buscarPorIdExterno(UUID id){

        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(id)
                .orElseThrow(() -> new RuntimeException("Canal no encontrado"));

        return canalReservaMapper.toDTO(canal);
    }

    public CanalReservaResponseDTO actualizarCanal(UUID id, CanalReservaRequestDTO dto){

        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(id)
                .orElseThrow(() -> new RuntimeException("Canal no encontrado"));

        try {
            canal.setTipo(TipoCanal.valueOf(dto.getTipo().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de canal invalido");
        }

        CanalReservaEntity actualizado = canalReservaRepository.save(canal);

        return canalReservaMapper.toDTO(actualizado);
    }

    public void eliminarCanal(UUID id){

        CanalReservaEntity canal = canalReservaRepository.findByIdExterno(id)
                .orElseThrow(() -> new RuntimeException("Canal no encontrado"));

        canalReservaRepository.delete(canal);
    }
}
