package co.edu.uniquindio.proyectotriagesolicitud.dto.response;

import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SugerenciaIAResponse {

    private TipoSolicitud tipoSugerido;
    private Prioridad prioridadSugerida;
    private Double confianza;
    private String explicacion;
}