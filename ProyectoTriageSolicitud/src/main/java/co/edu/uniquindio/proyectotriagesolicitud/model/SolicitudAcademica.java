package co.edu.uniquindio.proyectotriagesolicitud.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "solicitudes")
public class SolicitudAcademica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSolicitud tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalOrigen canalOrigen;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private Long solicitanteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolictud estado;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    private Long responsableId;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Version
    private Long version;

    public SolicitudAcademica() {}

    @PrePersist
    public void prePersist() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
        if(estado == null) {
            estado = EstadoSolictud.REGISTRADA;
        }
    }
}
