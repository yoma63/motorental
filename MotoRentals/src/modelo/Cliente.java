package modelo;

import java.util.Date;

/**
 * Clase cliente.
 *
 * @author Bogdan Marcut, Ivan Toro, Marc Valdivia
 */
public class Cliente extends Usuario {
    
    private int faltas;
    
    /**
     * Constructor vacio de la clase.
     */
    public Cliente() {
        
    }

    /**
     * Constructor de la clase sin tener en cuenta la herencia.
     * 
     * @param faltas 
     */
    public Cliente(int faltas) {
        this.faltas = faltas;
    }

    /**
     * Constructor con todos los parametros de la clase y de la herencia.
     * 
     * @param id
     * @param nombre
     * @param apellidos
     * @param CC
     * @param fechaCreacion
     * @param usuario
     * @param contrasenya 
     * @param faltas
     */
    public Cliente(String id, String nombre, String apellidos, String CC, Date fechaCreacion, String usuario, String contrasenya, int faltas) {
        super(id, nombre, apellidos, CC, fechaCreacion, usuario, contrasenya);
        this.faltas = faltas;
    }

    /**
     * @return the faltas
     */
    public int getFaltas() {
        return faltas;
    }

    /**
     * @param faltas the faltas to set
     */
    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

}
