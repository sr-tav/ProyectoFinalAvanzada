package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriorizarRequest {

    private Prioridad prioridad;
    private String justificacion;
    private Long usuarioResponsableId;
    private Long version;
}