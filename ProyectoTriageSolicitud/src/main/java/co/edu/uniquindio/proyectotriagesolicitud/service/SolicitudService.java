package co.edu.uniquindio.proyectotriagesolicitud.service;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AsignarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AtenderRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.CerrarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.ClasificarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.PriorizarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SolicitudRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.VersionRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.HistorialItemResponse;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.SolicitudResponse;
import co.edu.uniquindio.proyectotriagesolicitud.model.EstadoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;

import java.util.List;

public interface SolicitudService {

    SolicitudResponse registrarSolicitud(SolicitudRequest request);

    SolicitudResponse clasificarSolicitud(Long id, ClasificarRequest request);

    SolicitudResponse priorizarSolicitud(Long id, PriorizarRequest request);

    SolicitudResponse asignarSolicitud(Long id, AsignarRequest request);

    SolicitudResponse iniciarAtencion(Long id, VersionRequest request);

    SolicitudResponse atenderSolicitud(Long id, AtenderRequest request);

    SolicitudResponse cerrarSolicitud(Long id, CerrarRequest request);

    SolicitudResponse obtenerSolicitudPorId(Long id);

    List<SolicitudResponse> listarSolicitudes();

    List<SolicitudResponse> listarSolicitudes(
            EstadoSolicitud estado,
            TipoSolicitud tipo,
            Long responsableId,
            Prioridad prioridad
    );

    List<HistorialItemResponse> obtenerHistorial(Long solicitudId);
}