package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import co.edu.uniquindio.proyectotriagesolicitud.model.CanalOrigen;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudRequest {
    @NotNull(message = "el tipo de solicitud es obligatorio")
    private TipoSolicitud tipo;

    @NotBlank(message = "la descripcion es obligatoria")
    @Size(min = 1, max = 100, message = "La descripcion debe tener entre 1 y 100 caracteres")
    private String descripcion;

    @NotNull(message = "el canal de origen es obligatorio")
    private CanalOrigen canalOrigen;

    @NotBlank(message = "el id del solicitante es obligatorio")
    private Long solicitanteId;
}