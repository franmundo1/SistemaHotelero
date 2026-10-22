package lpda.SistemaHotelero.seguridad.dto;

public record AuthDtoRequest(
        String username,
        String password
) {
}