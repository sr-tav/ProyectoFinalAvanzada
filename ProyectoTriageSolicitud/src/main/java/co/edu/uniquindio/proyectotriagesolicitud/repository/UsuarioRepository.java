package co.edu.uniquindio.proyectotriagesolicitud.repository;

import co.edu.uniquindio.proyectotriagesolicitud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Long, Usuario> {
}
