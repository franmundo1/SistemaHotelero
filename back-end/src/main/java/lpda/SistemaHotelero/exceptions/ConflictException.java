package lpda.SistemaHotelero.exceptions;

// 409: la operacion choca con el estado actual de los datos
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
