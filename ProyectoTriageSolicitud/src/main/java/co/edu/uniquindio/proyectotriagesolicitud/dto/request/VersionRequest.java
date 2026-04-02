package co.edu.uniquindio.proyectotriagesolicitud.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VersionRequest {

    private Long usuarioResponsableId;
    private Long version;
}