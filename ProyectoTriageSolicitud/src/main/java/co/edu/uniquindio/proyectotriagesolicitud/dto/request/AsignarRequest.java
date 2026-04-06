package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignarRequest {

    @NotNull(message = "El ID del responsable es obligatorio")
    private Long responsableId;

    @NotNull(message = "El ID del usuario responsable es obligatorio")
    private Long usuarioResponsableId;

    @Size(max = 500, message = "La observación no puede superar los 500 caracteres")
    private String observacion;

    @NotNull(message = "La versión es obligatoria")
    private Long version;
}