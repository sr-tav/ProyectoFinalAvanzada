package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClasificarRequest {

    @NotNull(message = "El tipo de solicitud es obligatorio")
    private TipoSolicitud tipo;

    @NotNull(message = "El ID del usuario responsable es obligatorio")
    private Long usuarioResponsableId;

    @Size(max = 500, message = "La observación no puede superar los 500 caracteres")
    private String observacion;

    @NotNull(message = "La versión es obligatoria")
    private Long version;
}