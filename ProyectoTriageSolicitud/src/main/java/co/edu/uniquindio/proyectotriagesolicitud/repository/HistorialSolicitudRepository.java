package co.edu.uniquindio.proyectotriagesolicitud.repository;

import co.edu.uniquindio.proyectotriagesolicitud.model.HistorialSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialSolicitudRepository extends JpaRepository<HistorialSolicitud, Long> {

    List<HistorialSolicitud> findBySolicitudId(Long solicitudId);
}