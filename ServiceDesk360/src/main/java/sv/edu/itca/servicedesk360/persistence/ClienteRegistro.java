package sv.edu.itca.servicedesk360.persistence;

public final class ClienteRegistro {
    private final long id;
    private final String nombre;
    private final String correo;
    private final boolean activo;

    public ClienteRegistro(long id, String nombre, String correo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public boolean isActivo() { return activo; }
}