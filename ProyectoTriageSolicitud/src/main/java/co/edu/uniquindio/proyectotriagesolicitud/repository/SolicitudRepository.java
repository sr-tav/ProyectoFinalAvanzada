package co.edu.uniquindio.proyectotriagesolicitud.repository;

import co.edu.uniquindio.proyectotriagesolicitud.model.EstadoSolicitud;
import co.edu.uniquindio.proyectotriagesolicitud.model.Prioridad;
import co.edu.uniquindio.proyectotriagesolicitud.model.SolicitudAcademica;
import co.edu.uniquindio.proyectotriagesolicitud.model.TipoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudAcademica, Long> {

    List<SolicitudAcademica> findByEstado(EstadoSolicitud estado);

    List<SolicitudAcademica> findByTipo(TipoSolicitud tipo);

    List<SolicitudAcademica> findByResponsableId(Long responsableId);

    List<SolicitudAcademica> findByPrioridad(Prioridad prioridad);
}