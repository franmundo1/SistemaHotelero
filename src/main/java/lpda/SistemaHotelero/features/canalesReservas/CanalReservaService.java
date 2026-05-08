package lpda.SistemaHotelero.features.canalesReservas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanalReservaService {

    @Autowired
    private CanalReservaRepository canalReservaRepository;

    @Autowired
    private CanalReservaMapper canalReservaMapper;

    public CanalReservaResponseDTO crearCanal(CanalReservaRequestDTO dto){

        if(canalReservaRepository.existsByNombrePersona(dto.getNombrePersona())){
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

    public CanalReservaResponseDTO buscarPorId(Long id){

        CanalReservaEntity canal = canalReservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canal no encontrado"));

        return canalReservaMapper.toDTO(canal);
    }

    public CanalReservaResponseDTO actualizarCanal(Long id, CanalReservaRequestDTO dto){

        CanalReservaEntity canal = canalReservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canal no encontrado"));

        canal.setNombrePersona(dto.getNombrePersona());

        CanalReservaEntity actualizado = canalReservaRepository.save(canal);

        return canalReservaMapper.toDTO(actualizado);
    }

    public void eliminarCanal(Long id){

        CanalReservaEntity canal = canalReservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canal no encontrado"));

        canalReservaRepository.delete(canal);
    }
}
