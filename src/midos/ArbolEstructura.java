package midos;

/*
 * Esta clase crea una estructura para los archivos y directorios creados
/**
 *
 * @author Jeffrey
 */


public class ArbolEstructura {
    
    private String padre;
    private String id;
    private String tipo; //archivo o directorio (en mayúscula por defecto)
    private String info;
   
    //constructor de la clase
    public ArbolEstructura(String padre, String id, String tipo, String info)
    {
        this.padre = padre;
        this.id = id;
        this.tipo = tipo;
        this.info = info;
    }
    public ArbolEstructura()
    {
        //constructor vacío
    }
    //GET Y SET DE LAS VARIABLES***
    public String getPadre() {
        return padre;
    }

    /**
     * @param padre the padre to set
     */
    public void setPadre(String padre) {
        this.padre = padre;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getInfo()
    {
        return info;
    }
    
    public void setInfo(String info)
    {
        this.info = info;
    }

}
