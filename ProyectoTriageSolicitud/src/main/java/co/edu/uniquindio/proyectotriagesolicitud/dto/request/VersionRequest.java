package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VersionRequest {

    @NotNull(message = "El ID del usuario responsable es obligatorio")
    private Long usuarioResponsableId;

    @NotNull(message = "La versión es obligatoria")
    private Long version;
}