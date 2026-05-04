package co.edu.uniquindio.proyectotriagesolicitud.controller;

import co.edu.uniquindio.proyectotriagesolicitud.config.JwtUtils;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.LoginRequest;
import co.edu.uniquindio.proyectotriagesolicitud.model.Usuario;
import co.edu.uniquindio.proyectotriagesolicitud.repository.UsuarioRepository;
import co.edu.uniquindio.proyectotriagesolicitud.service.impl.SolicitudServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final SolicitudServiceImpl  solicitudService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        String token = jwtUtils.generarToken(usuario);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "tipo", usuario.getRolUsuario().name(),
                "usuarioId", usuario.getId(),
                "rol", usuario.getRolUsuario().name()
        ));
    }
}
