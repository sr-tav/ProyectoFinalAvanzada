package co.edu.uniquindio.proyectotriagesolicitud.dto.response;

import co.edu.uniquindio.proyectotriagesolicitud.model.CanalOrigen;
import co.edu.uniquindio.proyectotriagesolicitud.model.EstadoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudResponse {

    private Long id;
    private TipoSolicitud tipo;
    private String descripcion;
    private CanalOrigen canalOrigen;
    private LocalDateTime fechaRegistro;
    private Long solicitanteId;
    private EstadoSolicitud estado;
    private Prioridad prioridad;
    private Long responsableId;
    private String observaciones;
    private Long version;
}