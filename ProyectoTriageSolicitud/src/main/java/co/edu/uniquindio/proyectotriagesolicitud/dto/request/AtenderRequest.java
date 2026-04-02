package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtenderRequest {

    private String observacion;
    private Long usuarioResponsableId;
    private Long version;
}