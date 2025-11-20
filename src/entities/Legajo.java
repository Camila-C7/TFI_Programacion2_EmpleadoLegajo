package entities;

import java.time.LocalDate;

public class Legajo {
    
    private Long id;
    private Boolean eliminado;
    private String nroLegajo;
    private String categoria;
    private java.time.LocalDate fechaAlta;
    private String observaciones;
    private EstadoLegajo estado;

    // Constructores

    public Legajo(String nroLegajo, String categoria, LocalDate fechaAlta, String observaciones, EstadoLegajo estado) {
        this.nroLegajo = nroLegajo;
        this.categoria = categoria;
        this.fechaAlta = fechaAlta;
        this.observaciones = observaciones;
        this.estado = estado;
    }
    
    
    public Legajo(){}

    // Getters
    public Long getId() {return id;}

    public Boolean getEliminado() {return eliminado;}

    public String getNroLegajo() {return nroLegajo;}

    public String getCategoria() {return categoria;}

    public LocalDate getFechaAlta() {return fechaAlta;}

    public String getObservaciones() {return observaciones;}

    public EstadoLegajo getEstado() {return estado;}


    
    // Setters
    public void setId(Long id) {this.id = id;}

    public void setEliminado(Boolean eliminado) {this.eliminado = eliminado;}

    public void setNroLegajo(String nroLegajo) {this.nroLegajo = nroLegajo;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public void setFechaAlta(LocalDate fechaAlta) {this.fechaAlta = fechaAlta;}

    public void setObservaciones(String observaciones) {this.observaciones = observaciones;}

    public void setEstado(EstadoLegajo estado) {this.estado = estado;}

    
    // toString

    @Override
    public String toString() {
        return "Legajo{" + "id=" + id + ", eliminado=" + eliminado + ", nroLegajo=" + nroLegajo + ", categoria=" + categoria + ", fechaAlta=" + fechaAlta + ", observaciones=" + observaciones + ", estado=" + estado + '}';
    }

    
    
    
    
}
