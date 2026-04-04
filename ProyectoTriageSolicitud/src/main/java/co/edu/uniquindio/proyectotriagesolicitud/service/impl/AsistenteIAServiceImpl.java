package co.edu.uniquindio.proyectotriagesolicitud.service.impl;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SugerenciaIARequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.SugerenciaIAResponse;
import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.service.AsistenteIAService;
import org.springframework.stereotype.Service;

@Service
public class AsistenteIAServiceImpl implements AsistenteIAService {

    @Override
    public SugerenciaIAResponse sugerir(SugerenciaIARequest request) {

        if (request == null || request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria para generar la sugerencia");
        }

        String texto = request.getDescripcion().toLowerCase();

        SugerenciaIAResponse response = new SugerenciaIAResponse();

        // Sugerencia de tipo
        if (texto.contains("cancelar") || texto.contains("cancelación")) {
            response.setTipoSugerido(TipoSolicitud.CANCELACION_ASIGNATURAS);
        } else if (texto.contains("homolog") || texto.contains("equivalencia")) {
            response.setTipoSugerido(TipoSolicitud.HOMOLOGACION);
        } else if (texto.contains("cupo") || texto.contains("grupo lleno")) {
            response.setTipoSugerido(TipoSolicitud.SOLICITUD_CUPOS);
        } else if (texto.contains("registro") || texto.contains("matricular") || texto.contains("inscribir")) {
            response.setTipoSugerido(TipoSolicitud.REGISTRO_ASIGNATURAS);
        } else {
            response.setTipoSugerido(TipoSolicitud.CONSULTA_ACADEMICA);
        }

        // Sugerencia de prioridad
        if (texto.contains("urgente") || texto.contains("inmediato") || texto.contains("ya")
                || texto.contains("hoy") || texto.contains("vence")) {
            response.setPrioridadSugerida(Prioridad.CRITICA);
            response.setConfianza(0.90);
        } else if (texto.contains("pronto") || texto.contains("rápido") || texto.contains("rapido")) {
            response.setPrioridadSugerida(Prioridad.ALTA);
            response.setConfianza(0.80);
        } else if (texto.contains("consulta") || texto.contains("información") || texto.contains("informacion")) {
            response.setPrioridadSugerida(Prioridad.BAJA);
            response.setConfianza(0.70);
        } else {
            response.setPrioridadSugerida(Prioridad.MEDIA);
            response.setConfianza(0.60);
        }

        response.setExplicacion("Sugerencia generada con base en palabras clave encontradas en la descripción.");

        return response;
    }
}