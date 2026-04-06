package co.edu.uniquindio.proyectotriagesolicitud.controller;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AsignarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AtenderRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.CerrarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.ClasificarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.PriorizarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SolicitudRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SugerenciaIARequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.VersionRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.HistorialItemResponse;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.SolicitudResponse;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.SugerenciaIAResponse;
import co.edu.uniquindio.proyectotriagesolicitud.model.EstadoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.service.AsistenteIAService;
import co.edu.uniquindio.proyectotriagesolicitud.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final AsistenteIAService asistenteIAService;

    public SolicitudController(SolicitudService solicitudService,
                               AsistenteIAService asistenteIAService) {
        this.solicitudService = solicitudService;
        this.asistenteIAService = asistenteIAService;
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> registrarSolicitud(@Valid @RequestBody SolicitudRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitudService.registrarSolicitud(request));
    }

    @PatchMapping("/{id}/clasificar")
    public ResponseEntity<SolicitudResponse> clasificarSolicitud(@PathVariable Long id,
                                                                 @Valid @RequestBody ClasificarRequest request) {
        return ResponseEntity.ok(
                solicitudService.clasificarSolicitud(id, request)
        );
    }

    @PatchMapping("/{id}/priorizar")
    public ResponseEntity<SolicitudResponse> priorizarSolicitud(@PathVariable Long id,
                                                                @Valid @RequestBody PriorizarRequest request) {
        return ResponseEntity.ok(
                solicitudService.priorizarSolicitud(id, request)
        );
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<SolicitudResponse> asignarSolicitud(@PathVariable Long id,
                                                              @Valid @RequestBody AsignarRequest request) {
        return ResponseEntity.ok(
                solicitudService.asignarSolicitud(id, request)
        );
    }

    @PatchMapping("/{id}/iniciar-atencion")
    public ResponseEntity<SolicitudResponse> iniciarAtencion(@PathVariable Long id,
                                                             @Valid @RequestBody VersionRequest request) {
        return ResponseEntity.ok(
                solicitudService.iniciarAtencion(id, request)
        );
    }

    @PatchMapping("/{id}/atender")
    public ResponseEntity<SolicitudResponse> atenderSolicitud(@PathVariable Long id,
                                                              @Valid @RequestBody AtenderRequest request) {
        return ResponseEntity.ok(
                solicitudService.atenderSolicitud(id, request)
        );
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<SolicitudResponse> cerrarSolicitud(@PathVariable Long id,
                                                             @Valid @RequestBody CerrarRequest request) {
        return ResponseEntity.ok(
                solicitudService.cerrarSolicitud(id, request)
        );
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> listarSolicitudes(
            @RequestParam(required = false) EstadoSolicitud estado,
            @RequestParam(required = false) TipoSolicitud tipo,
            @RequestParam(required = false) Long responsableId,
            @RequestParam(required = false) Prioridad prioridad) {

        return ResponseEntity.ok(
                solicitudService.listarSolicitudes(estado, tipo, responsableId, prioridad)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtenerSolicitudPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                solicitudService.obtenerSolicitudPorId(id)
        );
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialItemResponse>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(
                solicitudService.obtenerHistorial(id)
        );
    }

    @PostMapping("/sugerir")
    public ResponseEntity<SugerenciaIAResponse> sugerir(@Valid @RequestBody SugerenciaIARequest request) {
        return ResponseEntity.ok(
                asistenteIAService.sugerir(request)
        );
    }
}