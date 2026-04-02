package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClasificarRequest {

    private TipoSolicitud tipo;
    private Long usuarioResponsableId;
    private String observacion;
    private Long version;
}