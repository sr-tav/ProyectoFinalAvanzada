package co.edu.uniquindio.proyectotriagesolicitud.service.impl;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AsignarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AtenderRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.CerrarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.ClasificarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.PriorizarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SolicitudRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.VersionRequest;
import co.edu.uniquindio.proyectotriagesolicitud.exception.BusinessException;
import co.edu.uniquindio.proyectotriagesolicitud.exception.ResourceNotFoundException;
import co.edu.uniquindio.proyectotriagesolicitud.model.EstadoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.HistorialSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.SolicitudAcademica;
import co.edu.uniquindio.proyectotriagesolicitud.repository.HistorialSolicitudRepository;
import co.edu.uniquindio.proyectotriagesolicitud.repository.SolicitudRepository;
import co.edu.uniquindio.proyectotriagesolicitud.service.SolicitudService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final HistorialSolicitudRepository historialSolicitudRepository;

    public SolicitudServiceImpl(SolicitudRepository solicitudRepository,
                                HistorialSolicitudRepository historialSolicitudRepository) {
        this.solicitudRepository = solicitudRepository;
        this.historialSolicitudRepository = historialSolicitudRepository;
    }

    @Override
    public SolicitudAcademica registrarSolicitud(SolicitudRequest request) {
        SolicitudAcademica solicitud = new SolicitudAcademica();
        solicitud.setTipo(request.getTipo());
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setCanalOrigen(request.getCanalOrigen());
        solicitud.setSolicitanteId(request.getSolicitanteId());
        solicitud.setEstado(EstadoSolicitud.REGISTRADA);

        SolicitudAcademica guardada = solicitudRepository.save(solicitud);

        registrarHistorial(
                guardada,
                "REGISTRO",
                request.getSolicitanteId(),
                "Solicitud registrada en el sistema"
        );

        return guardada;
    }

    @Override
    public SolicitudAcademica clasificarSolicitud(Long id, ClasificarRequest request) {
        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarNoCerrada(solicitud);
        validarVersion(solicitud, request.getVersion());

        if (solicitud.getEstado() != EstadoSolicitud.REGISTRADA) {
            throw new BusinessException("Solo se pueden clasificar solicitudes en estado REGISTRADA");
        }

        solicitud.setTipo(request.getTipo());
        solicitud.setEstado(EstadoSolicitud.CLASIFICADA);

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "CLASIFICACION",
                request.getUsuarioResponsableId(),
                request.getObservacion()
        );

        return actualizada;
    }

    @Override
    public SolicitudAcademica priorizarSolicitud(Long id, PriorizarRequest request) {
        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarNoCerrada(solicitud);
        validarVersion(solicitud, request.getVersion());

        if (solicitud.getEstado() != EstadoSolicitud.CLASIFICADA
                && solicitud.getEstado() != EstadoSolicitud.EN_ATENCION) {
            throw new BusinessException("Solo se puede priorizar una solicitud clasificada o en atención");
        }

        solicitud.setPrioridad(request.getPrioridad());
        solicitud.setObservaciones(request.getJustificacion());

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "PRIORIZACION",
                request.getUsuarioResponsableId(),
                request.getJustificacion()
        );

        return actualizada;
    }

    @Override
    public SolicitudAcademica asignarSolicitud(Long id, AsignarRequest request) {
        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarNoCerrada(solicitud);
        validarVersion(solicitud, request.getVersion());

        if (request.getResponsableId() == null) {
            throw new BusinessException("El responsableId es obligatorio");
        }

        solicitud.setResponsableId(request.getResponsableId());

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "ASIGNACION",
                request.getUsuarioResponsableId(),
                request.getObservacion()
        );

        return actualizada;
    }

    @Override
    public SolicitudAcademica iniciarAtencion(Long id, VersionRequest request) {
        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarNoCerrada(solicitud);
        validarVersion(solicitud, request.getVersion());

        if (solicitud.getEstado() != EstadoSolicitud.CLASIFICADA) {
            throw new BusinessException("Solo se puede iniciar atención desde el estado CLASIFICADA");
        }

        if (solicitud.getResponsableId() == null) {
            throw new BusinessException("No se puede iniciar la atención sin responsable asignado");
        }

        solicitud.setEstado(EstadoSolicitud.EN_ATENCION);

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "INICIO_ATENCION",
                request.getUsuarioResponsableId(),
                "La solicitud pasó a estado EN_ATENCION"
        );

        return actualizada;
    }

    @Override
    public SolicitudAcademica atenderSolicitud(Long id, AtenderRequest request) {
        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarNoCerrada(solicitud);
        validarVersion(solicitud, request.getVersion());

        if (solicitud.getEstado() != EstadoSolicitud.EN_ATENCION) {
            throw new BusinessException("Solo se puede atender una solicitud en estado EN_ATENCION");
        }

        solicitud.setEstado(EstadoSolicitud.ATENDIDA);
        solicitud.setObservaciones(request.getObservacion());

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "ATENCION",
                request.getUsuarioResponsableId(),
                request.getObservacion()
        );

        return actualizada;
    }

    @Override
    public SolicitudAcademica cerrarSolicitud(Long id, CerrarRequest request) {
        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarVersion(solicitud, request.getVersion());

        if (solicitud.getEstado() != EstadoSolicitud.ATENDIDA) {
            throw new BusinessException("Solo se puede cerrar una solicitud en estado ATENDIDA");
        }

        if (request.getObservacion() == null || request.getObservacion().isBlank()) {
            throw new BusinessException("La observación de cierre es obligatoria");
        }

        solicitud.setEstado(EstadoSolicitud.CERRADA);
        solicitud.setObservaciones(request.getObservacion());

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "CIERRE",
                request.getUsuarioResponsableId(),
                request.getObservacion()
        );

        return actualizada;
    }

    @Override
    public SolicitudAcademica obtenerSolicitudPorId(Long id) {
        return obtenerSolicitudExistente(id);
    }

    @Override
    public List<SolicitudAcademica> listarSolicitudes() {
        return solicitudRepository.findAll();
    }

    @Override
    public List<HistorialSolicitud> obtenerHistorial(Long solicitudId) {
        obtenerSolicitudExistente(solicitudId);
        return historialSolicitudRepository.findBySolicitudId(solicitudId);
    }

    private SolicitudAcademica obtenerSolicitudExistente(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la solicitud con id: " + id));
    }

    private void validarNoCerrada(SolicitudAcademica solicitud) {
        if (solicitud.getEstado() == EstadoSolicitud.CERRADA) {
            throw new BusinessException("La solicitud está cerrada y no puede modificarse");
        }
    }

    private void validarVersion(SolicitudAcademica solicitud, Long versionRequest) {
        if (versionRequest == null) {
            throw new BusinessException("La versión es obligatoria");
        }

        if (!solicitud.getVersion().equals(versionRequest)) {
            throw new BusinessException("Conflicto de versión: la solicitud fue modificada por otro proceso");
        }
    }

    private void registrarHistorial(SolicitudAcademica solicitud,
                                    String accion,
                                    Long usuarioResponsableId,
                                    String observacion) {
        HistorialSolicitud historial = new HistorialSolicitud();
        historial.setSolicitud(solicitud);
        historial.setAccion(accion);
        historial.setUsuarioResponsableId(usuarioResponsableId);
        historial.setObservacion(observacion);

        historialSolicitudRepository.save(historial);
    }
}