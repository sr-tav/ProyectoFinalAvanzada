package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtenderRequest {

    @NotBlank(message = "La observación es obligatoria")
    @Size(min = 5, max = 500, message = "La observación debe tener entre 5 y 500 caracteres")
    private String observacion;

    @NotNull(message = "El ID del usuario responsable es obligatorio")
    private Long usuarioResponsableId;

    @NotNull(message = "La versión es obligatoria")
    private Long version;
}