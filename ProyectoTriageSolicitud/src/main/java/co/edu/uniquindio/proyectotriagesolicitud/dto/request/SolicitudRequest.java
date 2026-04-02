package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import co.edu.uniquindio.proyectotriagesolicitud.model.CanalOrigen;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudRequest {
    private TipoSolicitud tipo;
    private String descripcion;
    private CanalOrigen canalOrigen;
    private Long solicitanteId;
}