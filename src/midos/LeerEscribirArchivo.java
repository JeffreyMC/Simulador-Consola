/*
 * Esta clase lee y escribe en los archivos txt
 * a saber: Memoria y árbol de directorios
 */
package midos;

import java.io.BufferedReader; //para leer los archivos
import java.io.BufferedWriter; //para escribir en los archivos
import java.io.File; //para verificar que el archivo existe
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author Jeffrey
 */
public class LeerEscribirArchivo {
   
    private BufferedWriter bw; //escritor de archivo
    private BufferedReader br; //lector de archivo
    private final  File rutaM = new File("MIDOSFRE.txt"); //ruta del archivo de memoria
    private final File rutaA = new File("MIDOSTRE.txt"); //ruta del archivo del árbol
    
    ArbolEstructura arbol;
    //este método inicia la memoria del SO (256k POR DEFECTO)
    //crear el archivo MIDOSFREE.txt
    public void iniciarMemoria()
    {
        try {
            if(rutaM.exists())
            {
                //si el archivo existe no hace nada.
                //System.out.println("El archivo existe");
            }
            else
            {
               //crea el archivo y le asigna 256K de memoria
               bw = new BufferedWriter(new FileWriter(rutaM));
               bw.write("256");
               bw.close(); 
            }

        } catch (IOException ex) {
            System.out.println("        ERROR AL CREAR O INICIAR ARCHIVO ");
        }
        
        
    }
    
    //este método sobreescribe la memoria del SO
    //se suma o se resta según sea el caso
    public void modificarMemoria(String num)
    {
        try
        {
            bw = new BufferedWriter(new FileWriter(rutaM));
            bw.write(num);
            bw.close();
        }
        catch(IOException ex)
        {
            System.out.println("    ERROR 005: No se pudo escribir en la memoria");
        }
    }
    
    //leer el archivo de memoria
    public String leerMemoria()
    {
        String cadena =""; //obtendrá el numero entero de la memoria disponible
        try
        {
            br = new BufferedReader(new FileReader(rutaM));
            
            cadena = br.readLine();
            br.close();
        }
        catch(IOException ex)
        {
            System.out.println("        ERROR AL LEER MEMORIA");
        }
        
        //retorna el valor de la memoria
        return cadena;
    }
    
    //inicia o crear el archivo MIDOSTREE.txt
    public void iniciarArbol()
    {
        try {
            if(rutaA.exists())
            {
                //si el archivo existe no hace nada.
                //System.out.println("El archivo existe");
            }
            else
            {
               //crea el archivo vacío
               bw = new BufferedWriter(new FileWriter(rutaA));
               //escribe la ruta de la raíz
               bw.write("/#/#ROOT#N/A\r\n");
               bw.close(); 
            }

        } catch (IOException ex) {
            System.out.println("        ERROR AL CREAR O INICIAR ARCHIVO ");
        }
    }
    public void escribirArbol(ArbolEstructura cadena)
    {
        try
        {
            //escribe en el archivo (true es para no sobreescribir el contenido
            bw = new BufferedWriter(new FileWriter(rutaA, true));
            bw.write(cadena.getPadre() +"#" + cadena.getId() +"#"+ cadena.getTipo() +"#"+ cadena.getInfo()+ "\r\n");
            bw.close();
        }
        catch(IOException ex)
        {
           System.out.println("     ERROR 006: Error al crear o iniciar los directorios");
        }
    }
    
    //busca en el archivo coincidencias con el nombre del identificador
    public boolean buscarEnArbol(String directorioActual, String token)
    {
        String cadena =" "; 
        try
        {
            br = new BufferedReader(new FileReader(rutaA));
            //lee la primera línea que es la raíz
            String root = br.readLine();
            
            //lee el archivo, línea por línea
            while((cadena = br.readLine()) != null)
            {
                //obtiene los tokens que están divididos por el caracter '#'.
                String [] tk = cadena.split("#");
                //si el nodo padre y el id son el mismo, lo indica
                if((tk[0].equals(directorioActual) == true) && (tk[1].equals(token)==true))
                {
                    br.close();
                    return true;
                }

            }
            
            br.close(); //cierra el archivo en caso de no encontrar coincidencias
        }
        catch(IOException ex)
        {
            System.out.println("        ERROR AL LEER MEMORIA");
        }
        
        //si no encontró coincidencias
        return false;
    }
    
    //método que verifica el tipo de un objeto en el txt (directorio o archivo)
    public String buscarTipo(String directorioActual, String token)
    {
        String cadena =" "; 
        String tipo = "";
        try
        {
            br = new BufferedReader(new FileReader(rutaA));
            //lee la primera línea que es la raíz
            String root = br.readLine();
            
            
            //lee el archivo, línea por línea
            while((cadena = br.readLine()) != null)
            {
                //obtiene los tokens que están divididos por el caracter '#'.
                String [] tk = cadena.split("#");
                //si el nodo padre y el id son el mismo, devuelve el tipo encontrado
                if((tk[0].equals(directorioActual) == true) && (tk[1].equals(token)==true))
                {
                    br.close();
                    return tk[2];
                }

            }
            
            br.close(); //cierra el archivo en caso de no encontrar coincidencias
        }
        catch(IOException ex)
        {
            System.out.println("        ERROR AL LEER MEMORIA");
        }
        
        //si no encontró coincidencias devuelve el string vacío
        return tipo;
    }
    
    //método que cuenta los hijos de un nodo padre
    public int contarHijos(String padre)
    {
        String cadena="";
        int contador = 0;
        
        try
        {
            br = new BufferedReader(new FileReader(rutaA));
            //lee la primera línea que es la raíz
            String root = br.readLine();
            
             while((cadena = br.readLine()) != null)
            {
                //obtiene los tokens que están divididos por el caracter '#'.
                String [] tk = cadena.split("#");
                //cuenta los nodos del padre
                if(tk[0].equals(padre))
                    contador++;
            }
            br.close();
        }
        catch(IOException ex)
        {
            System.out.println("    ERROR 007: Error al leer el árbol de directorios");
        }
        
        //devuelve la cantidad de nodos hijos encontrados
        return contador;
    }
    
    //busca al padre de un directorio
    public String buscarPadre(String directorioActual)
    {
         String cadena="";
         String padre="";
        
        try
        {
            br = new BufferedReader(new FileReader(rutaA));
            //busca en el archivo txt las coincidencias
             while((cadena = br.readLine()) != null)
            {
                //obtiene los tokens que están divididos por el caracter '#'.
                String [] tk = cadena.split("#");
                //devuelve el padre
                if(tk[1].equals(directorioActual))
                {
                    br.close();
                    return tk[0];
                }    
            }    
             
            br.close();
        }
        catch(IOException ex)
        {
            System.out.println("    ERROR 007: Error al leer el árbol de directorios");
        }
        

        //devuelve el padre
        return padre;
    
    }
    
    public void borrarDirectorio(String padre, String id, String tipo)
    {
        try
        {
            //ruta del archivo temporal
            File temp = new File("temp.txt");
            //lector del archivo de la ruta MIDOSTREE
            br = new BufferedReader(new FileReader(rutaA));
            //crea un archivo temporal
            bw = new BufferedWriter(new FileWriter(temp));
            String token = "";
            //se pasa el contenido del archivo MIDOSTRE al archivo temporal
            //menos el directorio que se quiere eliminar
            while((token = br.readLine()) != null)
            {
                String[] cadena = token.split("#");
                //si encuentra el archivo, lo obvia
                if(padre.equals(cadena[0]) && id.equals(cadena[1]) && tipo.equals(cadena[2]))
                    ;
                //escribe los demás archivos en el archivo temporal
                else
                {
                    bw.write(cadena[0] + "#" + cadena[1] + "#" + cadena[2] + "#" + cadena[3] + "\r\n");
                }
            }
            //cierra los archivos
            br.close();
            bw.close();
            //borra el archivo MIDOSTREE
            rutaA.delete();
            //renombra el archivo temporal con el nombre de MIDOSTREE.txt
            temp.renameTo(rutaA);
            
        }
        catch(IOException ex)
        {
            System.out.println("    ERROR 014: No se pudo eliminar el directorio");
        }
    }
   
    
    public ArrayList buscarHijos(String directorioActual)
    {
       //list que contendrá los hijos encontrados
        ArrayList<ArbolEstructura> lista = new ArrayList();
        try
        {
            
            //lector del archivo de la ruta MIDOSTREE
            br = new BufferedReader(new FileReader(rutaA));
            String token = "";

            //si el directorio actual es la raiz, lee la primera línea
            if(directorioActual.equals("/"));
                br.readLine();
            
            //se navega por MIDOSTRE
            while((token = br.readLine()) != null)
            {
                //se crea un arreglo para obtener los tokens del archivo
                String[] cadena = token.split("#");

                //se buscan los nodos en los que el directorio actual es el padre
                if(cadena[0].equals(directorioActual))
                {
                   arbol = new ArbolEstructura();
                   arbol.setPadre(cadena[0]);
                   arbol.setId(cadena[1]);
                   arbol.setTipo(cadena[2]);
                   //agrega los directorios encontrados a un arraylist
                   lista.add(arbol);  
                }
                
            }
            //cierra los archivos
            br.close();
              
        }
        catch(IOException ex)
        {
            System.out.println("ERROR con el archivo" + ex);
        }  
        
        //retorna la lista
        return lista;
    
    }
    
    //método que muestra el contenido de un archivo
    public void verContenido(String padre, String id, String tipo)
    {
        String cadena="";
        
        try
        {
            br = new BufferedReader(new FileReader(rutaA));
            //lee la primera línea que es la raíz
            String root = br.readLine();
            
             while((cadena = br.readLine()) != null)
            {
                //obtiene los tokens que están divididos por el caracter '#'.
                String [] tk = cadena.split("#");
                
                //si encuentra en el archivo coincidencias, muestra el contenido del archivo
                if(tk[0].equals(padre) && tk[1].equals(id) && tk[2].equals(tipo))
                    System.out.println(tk[3]);
            }
            br.close();
        }
        catch(IOException ex)
        {
            System.out.println("    ERROR 007: Error al leer el árbol de directorios");
        }
    
    }
    
    //método para renombrar archivos o directorios
    public void renombrar(String padre, String id, String tipo, String nuevoNombre)
    {
       try
        {
            //ruta del archivo temporal
            File temp = new File("temp.txt");
            //lector del archivo de la ruta MIDOSTREE
            br = new BufferedReader(new FileReader(rutaA));
            //crea un archivo temporal
            bw = new BufferedWriter(new FileWriter(temp));
            String token = "";
            //se pasa el contenido del archivo MIDOSTRE al archivo temporal
            //menos el directorio que se quiere eliminar
            while((token = br.readLine()) != null)
            {
                String[] cadena = token.split("#");
                //si encuentra el archivo, lo renombra y lo escribe en el txt temporal
                if(padre.equals(cadena[0]) && id.equals(cadena[1]) && tipo.equals(cadena[2]))
                {
                    bw.write(cadena[0] + "#" + nuevoNombre + "#" + cadena[2] + "#" + cadena[3] + "\r\n");
                }
                //si el directorio tiene hijos, renombra la referencia del padre
                else if(id.equals(cadena[0]))
                {
                    bw.write(nuevoNombre + "#" + cadena[1] + "#" + cadena[2] + "#" + cadena[3] + "\r\n");
                }
                //escribe los demás archivos en el archivo temporal
                else
                {
                    bw.write(cadena[0] + "#" + cadena[1] + "#" + cadena[2] + "#" + cadena[3] + "\r\n");
                }
            }
            //cierra los archivos
            br.close();
            bw.close();
            //borra el archivo MIDOSTREE
            rutaA.delete();
            //renombra el archivo temporal con el nombre de MIDOSTREE.txt
            temp.renameTo(rutaA);
            
        }
        catch(IOException ex)
        {
            System.out.println("    ERROR 014: No se pudo eliminar el directorio");
        }

    }
    
    //devuelve un arraylist con el contenido del archivo MIDOSTREE
    public ArrayList treeLista()
    {
        //lista que será devuelta con los elementos del txt
        ArrayList<ArbolEstructura> lista = new ArrayList<>();
        
        //lee el archivo .txt y pasa cada directorio y archivo al arraylist
        String cadena = "";
        try
        {
            br = new BufferedReader(new FileReader(rutaA));

            //lee el archivo, línea por línea
            while((cadena = br.readLine()) != null)
            {
                //obtiene los tokens que están divididos por el caracter '#'.
                String [] tk = cadena.split("#");
                
                //objeto utilizado para ir pasando los datos al arraylist
                arbol = new ArbolEstructura();
                
                //pasa los datos de cada línea al arraylist
                arbol.setPadre(tk[0]);
                arbol.setId(tk[1]);
                arbol.setTipo(tk[2]);
                arbol.setInfo(tk[3]);
                
                //se van añadiendo los datos al arrayList
                lista.add(arbol);
            }
            
            br.close(); //cierra el archivo en caso de no encontrar coincidencias
        }
        catch(IOException ex)
        {
            System.out.println("        ERROR AL LEER MEMORIA");
        }
        
        //retorna el ArrayList con los datos de MIDOSTREE
        return lista;
    }
}
