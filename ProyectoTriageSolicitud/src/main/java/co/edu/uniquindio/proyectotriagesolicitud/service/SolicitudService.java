package co.edu.uniquindio.proyectotriagesolicitud.service;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AsignarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AtenderRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.CerrarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.ClasificarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.PriorizarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SolicitudRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.VersionRequest;
import co.edu.uniquindio.proyectotriagesolicitud.model.HistorialSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.SolicitudAcademica;

import java.util.List;

public interface SolicitudService {

    SolicitudAcademica registrarSolicitud(SolicitudRequest request);

    SolicitudAcademica clasificarSolicitud(Long id, ClasificarRequest request);

    SolicitudAcademica priorizarSolicitud(Long id, PriorizarRequest request);

    SolicitudAcademica asignarSolicitud(Long id, AsignarRequest request);

    SolicitudAcademica iniciarAtencion(Long id, VersionRequest request);

    SolicitudAcademica atenderSolicitud(Long id, AtenderRequest request);

    SolicitudAcademica cerrarSolicitud(Long id, CerrarRequest request);

    SolicitudAcademica obtenerSolicitudPorId(Long id);

    List<SolicitudAcademica> listarSolicitudes();

    List<HistorialSolicitud> obtenerHistorial(Long solicitudId);
}