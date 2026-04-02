package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignarRequest {

    private Long responsableId;
    private Long usuarioResponsableId;
    private String observacion;
    private Long version;
}