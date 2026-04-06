package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriorizarRequest {

    @NotNull(message = "La prioridad es obligatoria")
    private Prioridad prioridad;

    @NotBlank(message = "La justificación es obligatoria")
    @Size(min = 5, max = 500, message = "La justificación debe tener entre 5 y 500 caracteres")
    private String justificacion;

    @NotNull(message = "El ID del usuario responsable es obligatorio")
    private Long usuarioResponsableId;

    @NotNull(message = "La versión es obligatoria")
    private Long version;
}