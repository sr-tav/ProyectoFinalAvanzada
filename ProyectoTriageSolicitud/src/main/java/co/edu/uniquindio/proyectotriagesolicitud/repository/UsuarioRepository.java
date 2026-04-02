package co.edu.uniquindio.proyectotriagesolicitud.repository;

import co.edu.uniquindio.proyectotriagesolicitud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}