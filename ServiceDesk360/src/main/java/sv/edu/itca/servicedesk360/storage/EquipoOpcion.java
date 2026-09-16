package sv.edu.itca.servicedesk360.storage;

public final class EquipoOpcion {
    private final long id;
    private final String etiqueta;

    public EquipoOpcion(long id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    public long getId() {
        return id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}