package controlador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import modelo.Cliente;
import modelo.Datos;
import modelo.Direccion;
import modelo.Local;
import modelo.Moto;
import modelo.Reserva;
import modelo.Usuario;

/**
 * Clase controladora.
 *
 * @author Bogdan Marcut, Ivan Toro, Marc Valdivia
 */
public class MotoRental {
    
    private Datos datos;
    
    /**
     * Constructor por defecto de la clase.
     */
    public MotoRental(){
        datos = new Datos();
    }

    /**
     * @return the datos
     */
    public Datos getDatos() {
        return datos;
    }

    /**
     * @param datos the datos to set
     */
    public void setDatos(Datos datos) {
        this.datos = datos;
    }
    
    /**
     * Devuelve el tipo de usuario que pasamos por parametro.
     * Los usuarios pueden ser: administrador, gerente, cliente o anonimo.
     * 
     * @param usuario
     * @return String
     */
    public String tipoUsuario(Usuario usuario){
        if(usuario.isAdministrador()){
            return "administrador";
        }
        if(usuario.isCliente()){
            return "cliente";
        }
        if(usuario.isGerente()){
            return "gerente";
        }
        return "anonimo";
    }

    /**
     * Carga los datos contenidos del xml en el sistema.
     */
    public void cargarDatos() {
        MotoRentDataManager dataManager = new MotoRentDataManager();
        dataManager.obtenirDades("data/MotoRent.xml");
        datos.setListaLocales(dataManager.getListaLocales());
        datos.setListaUsuarios(dataManager.getListaUsuarios());
        datos.setListaReservas(dataManager.getListaReservas());
    }

    /**
     * Devuelve un booleano indicando si un nombre de usuario esta disponible.
     * 
     * @param usuario
     * @return boolean
     */
    public boolean comprobarCliente(String usuario) {
        boolean existe = false;
        Iterator<Usuario> iterador = datos.getListaUsuarios().iterator();
        while(iterador.hasNext()){
            Usuario u = iterador.next();
            if(u.getUsuario().equals(usuario)){
                existe = true;
            }
        }
        return existe;
    }

    /**
     * Añade un nuevo usuario al sistema.
     * 
     * @param usuario
     * @param contrasenya
     * @param nombre
     * @param apellidos
     * @param CC
     * @param calle
     * @param numero
     * @param codigoPostal
     * @param poblacion
     * @return Usuario
     */
    public Usuario añadirCliente(String usuario, String contrasenya, String nombre, String apellidos, String CC, String calle, String numero, String codigoPostal, String poblacion) {
        Cliente ci = new Cliente("c" + this.getNumeroClientes(), nombre, apellidos, CC, new Date(), usuario, contrasenya, 0, new Direccion(calle, numero, codigoPostal, poblacion));
        datos.getListaUsuarios().add(ci);
        return ci;
    }
    
    /**
     * Devuelve la cantidad de clientes dentro del conjunto de usuarios guardados
     * en el sistema.
     * 
     * @return int
     */
    public int getNumeroClientes(){
        int numeroClientes = 0;
        Iterator<Usuario> iterador = datos.getListaUsuarios().iterator();
        while(iterador.hasNext()){
            Usuario u = iterador.next();
            if(u.isCliente()){
                numeroClientes += 1;
            }
        }
        return numeroClientes;
    }

    /**
     * Devuelve un usuario segun su nombre de usuario y contraseña. Si el usuario
     * no existe devuelve null.
     * 
     * @param usuario
     * @param contrasenya
     * @return Usuario
     */
    public Usuario getUsuario(String usuario, String contrasenya) {
        Usuario us = null;
        Iterator<Usuario> iterador = datos.getListaUsuarios().iterator();
        while(iterador.hasNext()){
            Usuario u = iterador.next();
            if(u.getUsuario().equals(usuario) && u.getContrasenya().equals(contrasenya)){
                us = u;
            }
        }  
        return us;
    }
    
    /**
     * Devuelve un booleano indicando si el usuario ya dispone de una reserva 
     * posterior a la fecha introducida.
     * 
     * @param idCliente
     * @param fechaEntrega
     * @return boolean
     */
    public boolean comprobarReserva(String idCliente, Date fechaEntrega) {
        boolean noTieneReserva = true;
        for (Reserva ri : datos.getListaReservas()) {
            if (ri.getId().equals(idCliente) && ri.getFechaRecogida().before(fechaEntrega)) {
                noTieneReserva = false;
            }
        }
        return noTieneReserva;
    }
    
    
    /**
     * Devuelve un String con todos los locales.
     * 
     * @return String
     */
    public String mostrarLocales() {
        String s = "--- Locales ---\n";
        for (Local li : datos.getListaLocales()) {
            s += li + "\n";
        }
        return s;
    }
    
    
    /**
     * Devuelve un String con los locales que tienen motos disponibles.
     * 
     * @return String
     */
    public String mostrarLocalesConMotos() {
        String s = "--- Locales con Motos Disponibles ---\n";
        for (Local li : datos.getListaLocales()) {
            if (li.getNumMotosDisponibles() > 0) {
                s += li + "\n";
            }
        }
        return s;
    }
    
    /**
     * Llama al metodo entregarMoto del local pasado por parametro
     * y le pasa el id de la reserva.
     * 
     * @param idReserva
     * @param local 
     */
    public void entregarMoto(String idReserva, Local local){
        local.entregarMoto(idReserva);
    }
    
    /**
     * Llama al metodo devolverMoto del local pasado por parametro y
     * le pasa el estado de la moto, el coste de reparacion y la fecha.
     * 
     * @param idReserva
     * @param local
     * @param estadoMoto
     * @param costReparacio
     * @param fecha 
     */
    public void devolverMoto(String idReserva, Local local, char estadoMoto, double costReparacio, Date fecha){
        local.devolverMoto(idReserva, estadoMoto, costReparacio, fecha);
    }
    
    /**
     * Devuelve un objeto Date dado un formato y un String.
     * 
     * @param format
     * @param sDate
     * @return Date
     * @throws ParseException 
     */
    public Date stringToDate(String format, String sDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Date date = formatter.parse(sDate);
        return date;
    }

    /**
     * Devuelve un booleano indicando si los locales seleccionados son aptos.
     * 
     * @param idOrigen
     * @param idDestino
     * @return boolean
     */
    public boolean comprobarOrigenDestino(String idOrigen, String idDestino) {
        boolean esLocalOrigen = false;
        boolean esLocalDestino = false;
        for (Local li : datos.getListaLocales()) {
            if (li.checkLocalOrigen(idOrigen)) esLocalOrigen = true;
            if (li.checkLocalDestino(idDestino)) esLocalDestino = true;
        }
        return esLocalOrigen && esLocalDestino;
    }

    /**
     * Devuelve un String con todas las motos disponibles en el local de origen
     * enviado por parametro.
     * 
     * @param idOrigen
     * @return 
     */
    public String mostrarMotosDisponibles(String idOrigen) {
       String s = "--- Motos Disponibles ---\n";
       for (Local li : datos.getListaLocales()) {
           if (li.checkLocal(idOrigen)) {
               s += li.mostrarMotosDisponibles();
           }
       }
       return s;
    }

    /**
     * Solicita una moto y en caso de que todo sea correcto realiza una reserva.
     * 
     * @param cliente
     * @param fechaRegogida
     * @param fechaDevolucion
     * @param idOrigen
     * @param idDestino
     * @param idMoto 
     * @return  String
     * @throws java.lang.Exception 
     */
    public String solicitarMoto(Cliente cliente, Date fechaRegogida, Date fechaDevolucion, String idOrigen, String idDestino, String idMoto) throws Exception {
        if (fechaDevolucion.before(fechaRegogida)) {
            for (Local li : datos.getListaLocales()) {
                if (li.checkLocal(idOrigen)) {
                    Local origen = obtenerLocal(idOrigen);
                    Local destino = obtenerLocal(idDestino);
                    if (origen != null && destino != null) {
                        Reserva reserva = li.solicitarMotoLocal(fechaRegogida, fechaDevolucion, destino, idMoto, cliente);
                        if (reserva != null) {
                            datos.getListaReservas().add(reserva);
                            return reserva.getId();
                        } else {
                            throw new Exception("La reserva no se ha podido crear");
                        }
                    } else {
                        throw new Exception("Los locales introducidos no son validos");
                    }
                }
            }
        } else {
            throw new Exception("La fecha de devolucion debe ser posterior a la de recogida");
        }
        return null;
    }
    
    /**
     * Devuelve el objeto Local correspondiente al id enviado por parametro.
     * 
     * @param idLocal
     * @return Local
     */
    public Local obtenerLocal(String idLocal) {
        for (Local li : datos.getListaLocales()) {
            if (li.checkLocal(idLocal)) return li;
        }
        return null;
    }
    
    /**
     * Devuelve un string con todas las motos por cada local.
     * 
     * @return String
     */
    public String verMotos(){
        String s = "--- Locales ---\n";
        for(Local li:this.datos.getListaLocales()){
            s += li + "\n";
            s += li.mostrarMotos();
        }
        return s;
    }

    /**
     * Dado un mes por parametro, genera un detallado con todas las reservas
     * por cada cliente.
     * 
     * @param mes
     * @return String
     */
    public String verInformeDelMes(Date mes) {
        String informe = "--- Informe del mes ---\n";
        for (Usuario ci : datos.getListaUsuarios()) {
            if (ci.isCliente()) {
                String reservas = "--- Cliente: " + ci.getId() + " ---\n";
                double costeMes = 0;
                for (Reserva ri : datos.getListaReservas()) {
                    if (ri.getCliente().equals(ci)) {
                        if (ri.mostrarReservasClienteDelMes(mes)) {
                            Local origen = ri.getLocalOrigen();
                            Local destino = ri.getLocalDestino();
                            String locales = "Origen:\n";
                            locales += origen + "\n";
                            locales += "Destino:\n";
                            locales += destino + "\n";
                            reservas += locales;
                            if (ri.haTenidoPenalizacion()) {
                                reservas += "Penalizacion: SI\n";
                            } else {
                                reservas += "Penalizacion: NO\n";
                            }
                            costeMes += ri.getCosteTotal();
                        }
                        reservas += "Coste Total del mes: " + costeMes + "\n";
                        informe += reservas + "\n";
                    }
                }
            }
        }
        return informe;
    }

    /**
     * Devuelve las motos de un local
     * 
     * @param org
     * @return 
     */
    public String mostrarMotos(Local org) {
        return org.mostrarMotos();
    }

    /**
     * Borra una moto del local origen y la añade al local destino
     * 
     * @param org
     * @param dest
     * @param idMoto 
     */
    public void moverMoto(Local org, Local dest, String idMoto) {
        Moto moto;
        moto = this.datos.moverMoto(org, idMoto);
        dest.añadirMoto(moto);
    }

    /**
     * Devuelve los locales que estan por encima del 75% de capacidad
     * 
     * @return 
     */
    public String mostrarLocalesSobre75() {
        String s = "--- Locales ---\n";
        for (Local li : datos.getListaLocales()) {
            if(li.getNumMotosDisponibles() > (li.getCapacidad()*0.75)){
                s += li;
            }
        }
        return s;
    }
}
