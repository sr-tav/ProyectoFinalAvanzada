package co.edu.uniquindio.proyectotriagesolicitud.service.impl;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AsignarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AtenderRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.CerrarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.ClasificarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.PriorizarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SolicitudRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.VersionRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.HistorialItemResponse;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.SolicitudResponse;
import co.edu.uniquindio.proyectotriagesolicitud.exception.BusinessException;
import co.edu.uniquindio.proyectotriagesolicitud.exception.ResourceNotFoundException;
import co.edu.uniquindio.proyectotriagesolicitud.model.EstadoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.HistorialSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import co.edu.uniquindio.proyectotriagesolicitud.model.RolUsuario;
import co.edu.uniquindio.proyectotriagesolicitud.model.SolicitudAcademica;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.Usuario;
import co.edu.uniquindio.proyectotriagesolicitud.repository.HistorialSolicitudRepository;
import co.edu.uniquindio.proyectotriagesolicitud.repository.SolicitudRepository;
import co.edu.uniquindio.proyectotriagesolicitud.repository.UsuarioRepository;
import co.edu.uniquindio.proyectotriagesolicitud.service.SolicitudService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final HistorialSolicitudRepository historialSolicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitudServiceImpl(SolicitudRepository solicitudRepository,
                                HistorialSolicitudRepository historialSolicitudRepository,
                                UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.historialSolicitudRepository = historialSolicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public SolicitudResponse registrarSolicitud(SolicitudRequest request) {
        Usuario solicitante = obtenerUsuarioActivo(request.getSolicitanteId());
        validarRol(solicitante,
                RolUsuario.ESTUDIANTE,
                RolUsuario.FUNCIONARIO,
                RolUsuario.COORDINADOR,
                RolUsuario.ADMIN);

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

        return toSolicitudResponse(guardada);
    }

    @Override
    public SolicitudResponse clasificarSolicitud(Long id, ClasificarRequest request) {
        Usuario actor = obtenerUsuarioActivo(request.getUsuarioResponsableId());
        validarRol(actor, RolUsuario.FUNCIONARIO, RolUsuario.COORDINADOR, RolUsuario.ADMIN);

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

        return toSolicitudResponse(actualizada);
    }

    @Override
    public SolicitudResponse priorizarSolicitud(Long id, PriorizarRequest request) {
        Usuario actor = obtenerUsuarioActivo(request.getUsuarioResponsableId());
        validarRol(actor, RolUsuario.FUNCIONARIO, RolUsuario.COORDINADOR, RolUsuario.ADMIN);

        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarNoCerrada(solicitud);
        validarVersion(solicitud, request.getVersion());

        if (solicitud.getEstado() != EstadoSolicitud.CLASIFICADA
                && solicitud.getEstado() != EstadoSolicitud.EN_ATENCION) {
            throw new BusinessException("Solo se puede priorizar una solicitud clasificada o en atención");
        }

        validarReglasPrioridad(solicitud, request.getPrioridad(), request.getJustificacion());

        solicitud.setPrioridad(request.getPrioridad());
        solicitud.setObservaciones(request.getJustificacion());

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "PRIORIZACION",
                request.getUsuarioResponsableId(),
                request.getJustificacion()
        );

        return toSolicitudResponse(actualizada);
    }

    @Override
    public SolicitudResponse asignarSolicitud(Long id, AsignarRequest request) {
        Usuario actor = obtenerUsuarioActivo(request.getUsuarioResponsableId());
        validarRol(actor, RolUsuario.FUNCIONARIO, RolUsuario.COORDINADOR, RolUsuario.ADMIN);

        SolicitudAcademica solicitud = obtenerSolicitudExistente(id);
        validarNoCerrada(solicitud);
        validarVersion(solicitud, request.getVersion());

        if (request.getResponsableId() == null) {
            throw new BusinessException("El responsableId es obligatorio");
        }

        Usuario responsable = obtenerUsuarioActivo(request.getResponsableId());

        if (responsable.getRolUsuario() == RolUsuario.ESTUDIANTE) {
            throw new BusinessException("No se puede asignar una solicitud a un estudiante");
        }

        solicitud.setResponsableId(request.getResponsableId());

        SolicitudAcademica actualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                actualizada,
                "ASIGNACION",
                request.getUsuarioResponsableId(),
                request.getObservacion()
        );

        return toSolicitudResponse(actualizada);
    }

    @Override
    public SolicitudResponse iniciarAtencion(Long id, VersionRequest request) {
        Usuario actor = obtenerUsuarioActivo(request.getUsuarioResponsableId());
        validarRol(actor, RolUsuario.FUNCIONARIO, RolUsuario.COORDINADOR, RolUsuario.ADMIN);

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

        return toSolicitudResponse(actualizada);
    }

    @Override
    public SolicitudResponse atenderSolicitud(Long id, AtenderRequest request) {
        Usuario actor = obtenerUsuarioActivo(request.getUsuarioResponsableId());
        validarRol(actor, RolUsuario.FUNCIONARIO, RolUsuario.COORDINADOR, RolUsuario.ADMIN);

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

        return toSolicitudResponse(actualizada);
    }

    @Override
    public SolicitudResponse cerrarSolicitud(Long id, CerrarRequest request) {
        Usuario actor = obtenerUsuarioActivo(request.getUsuarioResponsableId());
        validarRol(actor, RolUsuario.COORDINADOR, RolUsuario.ADMIN);

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

        return toSolicitudResponse(actualizada);
    }

    @Override
    public SolicitudResponse obtenerSolicitudPorId(Long id) {
        return toSolicitudResponse(obtenerSolicitudExistente(id));
    }

    @Override
    public List<SolicitudResponse> listarSolicitudes() {
        return solicitudRepository.findAll()
                .stream()
                .map(this::toSolicitudResponse)
                .toList();
    }

    @Override
    public List<SolicitudResponse> listarSolicitudes(
            EstadoSolicitud estado,
            TipoSolicitud tipo,
            Long responsableId,
            Prioridad prioridad) {

        List<SolicitudAcademica> lista = solicitudRepository.findAll();

        return lista.stream()
                .filter(s -> estado == null || s.getEstado() == estado)
                .filter(s -> tipo == null || s.getTipo() == tipo)
                .filter(s -> responsableId == null ||
                        (s.getResponsableId() != null && s.getResponsableId().equals(responsableId)))
                .filter(s -> prioridad == null || s.getPrioridad() == prioridad)
                .map(this::toSolicitudResponse)
                .toList();
    }

    @Override
    public List<HistorialItemResponse> obtenerHistorial(Long solicitudId) {
        obtenerSolicitudExistente(solicitudId);

        return historialSolicitudRepository.findBySolicitudId(solicitudId)
                .stream()
                .map(this::toHistorialItemResponse)
                .toList();
    }

    private SolicitudAcademica obtenerSolicitudExistente(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la solicitud con id: " + id));
    }

    private Usuario obtenerUsuarioActivo(Long usuarioId) {
        if (usuarioId == null) {
            throw new BusinessException("El id del usuario es obligatorio");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("El usuario no existe"));

        if (!usuario.isActivo()) {
            throw new BusinessException("El usuario no está activo");
        }

        return usuario;
    }

    private void validarRol(Usuario usuario, RolUsuario... rolesPermitidos) {
        for (RolUsuario rol : rolesPermitidos) {
            if (usuario.getRolUsuario() == rol) {
                return;
            }
        }
        throw new BusinessException("El usuario no tiene permisos para realizar esta operación");
    }

    private void validarReglasPrioridad(SolicitudAcademica solicitud,
                                        Prioridad prioridad,
                                        String justificacion) {
        if (prioridad == null) {
            throw new BusinessException("La prioridad es obligatoria");
        }

        if (justificacion == null || justificacion.isBlank()) {
            throw new BusinessException("La justificación de la prioridad es obligatoria");
        }

        if (solicitud.getTipo() == TipoSolicitud.CONSULTA_ACADEMICA
                && prioridad == Prioridad.CRITICA) {
            throw new BusinessException("Una consulta académica no puede asignarse con prioridad CRITICA");
        }

        if (solicitud.getTipo() == TipoSolicitud.SOLICITUD_CUPOS
                && prioridad == Prioridad.BAJA) {
            throw new BusinessException("Una solicitud de cupos no puede asignarse con prioridad BAJA");
        }
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

    private SolicitudResponse toSolicitudResponse(SolicitudAcademica solicitud) {
        SolicitudResponse response = new SolicitudResponse();
        response.setId(solicitud.getId());
        response.setTipo(solicitud.getTipo());
        response.setDescripcion(solicitud.getDescripcion());
        response.setCanalOrigen(solicitud.getCanalOrigen());
        response.setFechaRegistro(solicitud.getFechaRegistro());
        response.setSolicitanteId(solicitud.getSolicitanteId());
        response.setEstado(solicitud.getEstado());
        response.setPrioridad(solicitud.getPrioridad());
        response.setResponsableId(solicitud.getResponsableId());
        response.setObservaciones(solicitud.getObservaciones());
        response.setVersion(solicitud.getVersion());
        return response;
    }

    private HistorialItemResponse toHistorialItemResponse(HistorialSolicitud historial) {
        HistorialItemResponse response = new HistorialItemResponse();
        response.setFecha(historial.getFechaHora());
        response.setAccion(historial.getAccion());
        response.setUsuario(historial.getUsuarioResponsableId() != null
                ? historial.getUsuarioResponsableId().toString()
                : null);
        response.setObservaciones(historial.getObservacion());
        return response;
    }
}