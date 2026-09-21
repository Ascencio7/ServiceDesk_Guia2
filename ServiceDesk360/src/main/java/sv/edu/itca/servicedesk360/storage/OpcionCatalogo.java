package sv.edu.itca.servicedesk360.storage;

public final class OpcionCatalogo {
    private final long id;
    private final String nombre;

    public OpcionCatalogo(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
}