package co.edu.uniquindio.proyectotriagesolicitud.repository;

import co.edu.uniquindio.proyectotriagesolicitud.model.HistorialSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.SolicitudAcademica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
@Repository
public interface SolicitudRepository extends JpaRepository<Long, SolicitudAcademica> {
}
