package co.edu.uniquindio.proyectotriagesolicitud.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HistorialItemResponse {

    private LocalDateTime fecha;
    private String accion;
    private String usuario;
    private String observaciones;
}