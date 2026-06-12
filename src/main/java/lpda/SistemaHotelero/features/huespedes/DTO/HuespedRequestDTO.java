package lpda.SistemaHotelero.features.huespedes.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuespedRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min=2, max= 50, message = "el nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{7,10}$", message = "El DNI debe ser numerico y tener entre 7 y 10 digitos") //^ = inicia aqui, [0-9] dijitos del 1 al 9 sin letras, {7,10} minimo 7 numeros, max 10
    private  String dni;

    @NotBlank (message = "El mail es obligatorio")
    @Email(message = "El email no es valido")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "El telefono debe tener un formato valido (ejemplo: 2235123456)")
    private  String telefono;
}
