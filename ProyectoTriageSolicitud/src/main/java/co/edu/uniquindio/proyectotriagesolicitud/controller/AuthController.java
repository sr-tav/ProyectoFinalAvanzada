package co.edu.uniquindio.proyectotriagesolicitud.controller;

import co.edu.uniquindio.proyectotriagesolicitud.config.JwtUtils;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.LoginRequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.request.RegisterRequest;
import co.edu.uniquindio.proyectotriagesolicitud.model.RolUsuario;
import co.edu.uniquindio.proyectotriagesolicitud.model.Usuario;
import co.edu.uniquindio.proyectotriagesolicitud.repository.UsuarioRepository;
import co.edu.uniquindio.proyectotriagesolicitud.service.impl.SolicitudServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(request.getCorreo());
        if (usuario.isEmpty()) {

            Usuario usuario1 = new Usuario();
            usuario1.setCorreo(request.getCorreo());
            usuario1.setNombre(request.getNombre());
            usuario1.setRolUsuario(RolUsuario.ESTUDIANTE);
            usuario1.setActivo(true);

            usuarioRepository.save(usuario1);
            String token = jwtUtils.generarToken(usuario1);
            String descrip = "registro exitoso";

            return ResponseEntity.ok(Map.of(
                    "descripcion", descrip,
                    "token", token,
                    "tipo", usuario1.getRolUsuario().name(),
                    "usuarioId", usuario1.getId(),
                    "rol", usuario1.getRolUsuario().name()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "descripcion", null,
                "token", null,
                "tipo", null,
                "usuarioId", null,
                "rol", null
        ));
    }
}
