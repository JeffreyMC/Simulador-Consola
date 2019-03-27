/*
 * 
 */
package midos;

//importa la clase Scanner para leer desde el teclado
import java.util.Scanner;

/**
 *
 * @author Jeffrey
 */
public class MIDOS {

     
     //método main donde se manejará el ingreso de caracteres desde el teclado
    public static void main(String[] args) {
        //Crear el objeto de la clase que lee y escribe archivos
        LeerEscribirArchivo archivo = new LeerEscribirArchivo();
        //Crea el objeto de la clase de comandos
        Comandos cmd = new Comandos();
        //crear un objeto Scanner para leer la entrada
        Scanner sc = new Scanner(System.in);
        
        //crea el archivo de memoria (en caso de que no exista)
        archivo.iniciarMemoria();
        //crea el archivo del árbol de directorios (en caso de que no exista)
        archivo.iniciarArbol();
        //lee la cantidad de memoria disponible
        archivo.leerMemoria();
        
        //se inicializa el el directorio en root
        cadenaDirectorios = "/";
        //inicializa el prompt por defecto
        promptP = "M:";
        //inicializa el promptG vacío
        promptG = "";
        
        //instancia la clase del analizador léxico
        Lex lex = new Lex();
        //llama el objeto de Comandos
        cmd.ver();
        String cadena; //caracteres que se recibirán desde el teclado
        while(true)
        {  
            String posicionActual = cadenaDirectorios;
            System.out.print(promptP + posicionActual + promptG + " ");
            cadena = sc.nextLine();
            lex.toTokens(cadena);
        }
        
    }
    
    //variable que contiene la cadena de directorios
    private static String cadenaDirectorios;
    
     //métodos set y get de la cadena de directorios
     public void setCadenaDirectorios(String cadena)
     {
         cadenaDirectorios = cadena;
     }
     
     public String getCadenaDirectorios()
     {
         return cadenaDirectorios;
     }
     
     
    //vartiables del prompt
    private static String promptP;
    private static String promptG;
    
     //métodos set y get de las variables promptP y promptG
     public void setPromptP(String p)
     {
         promptP = p;
     }
     
     public String getPromptP()
     {
         return promptP;
     }
     
     public void setPromptG(String g)
     {
         promptG = g;
     }
     
     public String getPromptG()
     {
         return promptG;
     }
}
