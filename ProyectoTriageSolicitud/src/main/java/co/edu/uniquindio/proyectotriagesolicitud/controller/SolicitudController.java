package co.edu.uniquindio.proyectotriagesolicitud.controller;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AsignarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.AtenderRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.CerrarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.ClasificarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.PriorizarRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SolicitudRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.VersionRequest;
import co.edu.uniquindio.proyectotriagesolicitud.model.HistorialSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.SolicitudAcademica;
import co.edu.uniquindio.proyectotriagesolicitud.service.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudAcademica> registrarSolicitud(@RequestBody SolicitudRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitudService.registrarSolicitud(request));
    }

    @PatchMapping("/{id}/clasificar")
    public ResponseEntity<SolicitudAcademica> clasificarSolicitud(@PathVariable Long id,
                                                                  @RequestBody ClasificarRequest request) {
        return ResponseEntity.ok(solicitudService.clasificarSolicitud(id, request));
    }

    @PatchMapping("/{id}/priorizar")
    public ResponseEntity<SolicitudAcademica> priorizarSolicitud(@PathVariable Long id,
                                                                 @RequestBody PriorizarRequest request) {
        return ResponseEntity.ok(solicitudService.priorizarSolicitud(id, request));
    }

    @PatchMapping("/{id}/asignar")
    public ResponseEntity<SolicitudAcademica> asignarSolicitud(@PathVariable Long id,
                                                               @RequestBody AsignarRequest request) {
        return ResponseEntity.ok(solicitudService.asignarSolicitud(id, request));
    }

    @PatchMapping("/{id}/iniciar-atencion")
    public ResponseEntity<SolicitudAcademica> iniciarAtencion(@PathVariable Long id,
                                                              @RequestBody VersionRequest request) {
        return ResponseEntity.ok(solicitudService.iniciarAtencion(id, request));
    }

    @PatchMapping("/{id}/atender")
    public ResponseEntity<SolicitudAcademica> atenderSolicitud(@PathVariable Long id,
                                                               @RequestBody AtenderRequest request) {
        return ResponseEntity.ok(solicitudService.atenderSolicitud(id, request));
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<SolicitudAcademica> cerrarSolicitud(@PathVariable Long id,
                                                              @RequestBody CerrarRequest request) {
        return ResponseEntity.ok(solicitudService.cerrarSolicitud(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudAcademica> obtenerSolicitudPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerSolicitudPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudAcademica>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudService.listarSolicitudes());
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialSolicitud>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerHistorial(id));
    }
}