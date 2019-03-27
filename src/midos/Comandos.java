/*
 * Esta clase es la encargada de ejecutar los métodos para cada comando
 */
package midos;

import java.text.DateFormat; //librería para el formato de la fecha
import java.text.SimpleDateFormat; //librería para modificar los formatos de fecha
import java.util.Date; //librería para mostrar fechas
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.ArrayList; 
import java.util.Collections;


/**
 *
 * @author Jeffrey
 */
public class Comandos {
    //crea e instancia el objeto para leer y escribir archivos
    LeerEscribirArchivo archivo = new LeerEscribirArchivo();
    //instancia un arrayList de tipo ArbolEstructura para almacenar los datos
    //obtenidos de una forma estructurada y así leerlos del txt de mejor manera
    ArrayList<ArbolEstructura> datos = new ArrayList();
    ArbolEstructura arbol;
    //contiene el directorio actual
    public String directorioActual = "/"; //valor por defecto. Luego irá cambiando
    private String cadenaDirectorios="/"; // tiene las cadenas de directorios para mostrar en el terminal
    //variable global que contiene los niveles del directorio para el arbol tree
    int nivel = 1;
    
    //crea un objeto del main para luego pasarle la cadena de directorios
    MIDOS directorios = new MIDOS();
    
  
    //método para ver versión del sistema y memoria disponible
    public void ver()
    {
        String memoria = archivo.leerMemoria(); //obtiene la cantidad de memoria disponible
        
        System.out.println("MINGOSOFT® MIDOS");
        System.out.println("© Copyright MINGOSOFT CORPORATION 2018");
        System.out.println("Versión 1.0 Memoria libre: " + memoria + "K");
        System.out.println("Autor: Jeffrey Muñoz Castro - Cédula: 1-1370-0324\n");
    }
    
    //método que simula limpiar la pantalla
    public void cls()
    {
        for(int i=0; i<10; i++)
            System.out.println();
    }
    
    //método que muestra la fecha actual
    public void fecha()
    {
        Date date = new Date();
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        System.out.println("La fecha actual es: " + formatoFecha.format(date));
    }
    
    //método que muestra la hora actual
    public void hora()
    {
        Date date = new Date();
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        
        System.out.println("La hora es: " + formatoHora.format(date));
    }
    
    //método que maneja la salida de la consola
    public void salir()
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("¿Seguro/a que desea salir del programa? (S/n): ");
        String ans = sc.nextLine();
        ans = ans.toUpperCase();
        switch (ans) {
            case "S":
            case "":
                System.out.println("¡Qué tengas un lindo día!\n");
                System.exit(0);
            case "N":
                ;
                break;
            default:
                System.out.println("    ERROR 020: Caracter inválido");
                break;
        }
        
    }
    //método para crear archivos o carpetas
    public void md(String[] tokens)
    {
        //si el arreglo tiene mas de dos tokens es un error
        if(tokens.length > 2)
            System.out.println("    ERROR 009: se esperaba solo un identificador");
        //verifica que el directorio no tenga más de 8 hijos
        if(archivo.contarHijos(directorioActual)>= 8)
            System.out.println("    Error 008: El directorio está lleno");
        else
        {
            
            //se analiza que el identificador cumpla con lo requerido
            //un máximo 8 caracteres
            //no debe iniciar con números
            Pattern pat = Pattern.compile("^[^\\d]" + "[a-zA-Z0-9]{0,7}$");
            Matcher mat = pat.matcher(tokens[1]);
            
            if(mat.find())
            {
                //lee los identificadores que estan en el arbol
                boolean encontrado = archivo.buscarEnArbol(directorioActual, tokens[1]);
                
                //si el nombre del directorio no existe, lo crea
                if(encontrado == false)
                {
                    //se lee el archivo con la memoria disponible
                    String memoriaDisponible = archivo.leerMemoria();
                    //se hace un parse para obtener la memoria en un int
                    int memoriaActual = Integer.parseInt(memoriaDisponible);
                    //verificar si hay espacio disponible en la memoria
                    if(memoriaActual < 8)
                    {
                        System.out.println("    ERROR 004: Memoria insuficiente. No se pudo crear el directorio");
                    }
                    else
                    {
                        //ingresa los datos de nodo padre + id + tipo en un arraylist
                        arbol = new ArbolEstructura(directorioActual, tokens[1], "dir", "N/A");
                        datos.add(arbol);
                        //se ingresa el directorio al archivo de texto
                        archivo.escribirArbol(arbol);
                        //se le restan 8K a la memoria y luego esta se actualiza
                        memoriaActual -= 8;
                        archivo.modificarMemoria(String.valueOf(memoriaActual));
                    }  
                    
                }
                 //si encuentra un archivo o directorio con el mismo nombre lo indica   
                else
                {
                    String tipo = archivo.buscarTipo(directorioActual, tokens[1]);
                    if(tipo.equals("arch"))
                        System.out.println("    ERROR 001: Ya existe un archivo con ese nombre: " + tokens[1]);
                    else
                        System.out.println("    ERROR 015: Ya existe un directorio con ese nombre: " + tokens[1]);
                }
                    
            }
            else
            {
                System.out.println("    ERROR 002: Identificador incorrecto");
                System.out.println("    *No debe comenzar con dígitos");
                System.out.println("    *No debe contener caracteres especiales");
                System.out.println("    *Debe tener un máximo de 8 caracteres");
            }
        }
    }
    
    //método que se encarga de ejecutar el comando CD para cambiar de directorio
    public void cd(String[] tokens)
    {
        //si los 2 primeros tokens forman la palabra 'cd..' se llama al método
        switch (tokens[0] + tokens[1]) {
            case "cd..":
                //lo envía al método correspondiente
                cdAnterior(tokens[0]);
                break;
            //si los 2 primeros tokens forman la palabra 'cd/' se posiciona en la raíz
            case "cd/":
                //se asignan las variables globales para posicionarse en la raíz
                directorioActual = "/";
                cadenaDirectorios = "/";
                //se setea la cadena para posicionarse en la raíz
                directorios.setCadenaDirectorios("/");
                break;
            default:
                //verifica que el directorio exista
                boolean encontrado  = archivo.buscarEnArbol(directorioActual, tokens[1]);
                //busca el tipo del identificador ingresado
                String tipo = archivo.buscarTipo(directorioActual, tokens[1]);
                //si el directorio no existe
                if(encontrado == false)
                {
                    System.out.println("    ERROR 011: El directorio al que desea acceder no existe");
                }
                //si se desea acceder a un archivo muestra un error
                else if(tipo.equals("arch"))
                {
                    System.out.println("     ERROR 016: Está intentando acceder a un archivo");
                }
                //si el directorio existe
                else
                {
                    //si se encuentra en la raiz, solo se concatena el directorio
                    if(directorioActual.equals("/"))
                    {
                        directorioActual = tokens[1];
                        cadenaDirectorios += tokens[1];
                        //setea la cadena de directorios en el main
                        directorios.setCadenaDirectorios(cadenaDirectorios);
                    }
                    //sino se concatena un '/' más el directorio
                    else
                    {
                        directorioActual = tokens[1];
                        cadenaDirectorios += ("/" + tokens[1]);
                        //setea la cadena de directorios en el main
                        directorios.setCadenaDirectorios(cadenaDirectorios);
                    }
                }   break; //fin del else
        }//fin del switch
   
    }
    
    
    //m´todo que realiza la función de ir al directorio anterior (padre)
    public void cdAnterior(String comando)
    {
        //obtengo los tokens de la cadena de directorios que se muestra en consola
        String [] cadena = cadenaDirectorios.split("/");
        if(cadena.length == 0)
           System.out.println("    ERROR 015: El directorio raíz no tiene nodo padre"); 
        else
        {
            //se le agrega al índice 0 el símbolo de root
            cadena[0] = "/";
            String directorioAnterior="";
            //se recorre el árbol de directorios hasta el padre de la variable actual
            for(int i=0; i < cadena.length-1; i++)
            {         
                //si el token leído es la raíz se salta la asignación
                if(cadena[i].equals("/"));
                // se va concatenando los directorios en la variable
                else
                    directorioAnterior += ("/" + cadena[i]);
            }
            
            //si solo queda un token, se sabe que es la raíz
            if(cadena.length-1 == 1)
            {
                //se asignan las variables globales con el directorio de la raíz
                directorioActual = "/";
                cadenaDirectorios = "/";
                //se setea la cadena de directorios con el directorio principal
                directorios.setCadenaDirectorios("/");
            }
            //si quedan más de un token entonces se sabe que hay más directorios padre
            else
            {
                //se pasa el valor del directorio actual al anterior
                directorioActual = cadena[cadena.length-2];
                //se setea la cadena de directorios para mostrar en consola
                directorios.setCadenaDirectorios(directorioAnterior);
                //se pasan los directorios concatenados
                cadenaDirectorios = directorioAnterior;
            }
            
        }
    
    }
    
    //comando que se encarga de borrar un directorio
    public void rd(String[] tokens)
    {
        //verfica que el identificador exista
        //lee los identificadores que estan en el arbol
        boolean encontrado = archivo.buscarEnArbol(directorioActual, tokens[1]);
        //si existe
        if(encontrado)
        {
            //se asegura que el archivo a borrar sea un directorio
            String tipo = archivo.buscarTipo(directorioActual, tokens[1]);
            if(tipo.equals("dir"))
            {
                    //se asegura que el directorio esté vacío
                if(archivo.contarHijos(tokens[1]) > 0)
                {
                    System.out.println("    ERROR 012: El directorio no está vacío");
                }
                else
                {
                    //llama al método para borrar el directorio
                    archivo.borrarDirectorio(directorioActual, tokens[1], "dir");

                    //le suma la memoria liberada al archivo MIDOSFREE
                    //se lee el archivo con la memoria disponible
                    String memoriaDisponible = archivo.leerMemoria();
                    //se hace un parse para obtener la memoria en un int
                    int memoriaActual = Integer.parseInt(memoriaDisponible);
                    //se le suman 8K a la memoria y luego esta se actualiza
                    memoriaActual += 8;
                    archivo.modificarMemoria(String.valueOf(memoriaActual));
                }
            }
            //si intenta borrar un archivo, lo indica.
            else
            {
                System.out.println("    ERROR: 020: El archivo a eliminar corresponde a un archivo");
            }
            
        }
        //si no existe
        else
        {
            System.out.println("    ERROR 013: No existe un directorio con ese nombre");
        }
    }
    
    //método que muestra los directorios y archivos (DIR)
    public void dir()
    {
       //busca los hijos del directorio actual
       ArrayList<ArbolEstructura> lista = archivo.buscarHijos(directorioActual);
        

         //ordena la lista por nombre
        Collections.sort(lista, (ArbolEstructura id, ArbolEstructura id2) -> id.getId().compareTo(id2.getId()));
        //ordena la lista por tipo
        Collections.sort(lista, (ArbolEstructura id, ArbolEstructura id2) -> id.getTipo().compareTo(id2.getTipo()));
        
        //contadores de los directorios y archivos
        int contDir = 0, contArch = 0;
        //imprime la lista ordenada
        for(ArbolEstructura temp: lista)
        {
           if(temp.getTipo().equals("arch"))
           {
               System.out.printf("%-10s %s", temp.getId(), temp.getTipo() + "\n");
               contArch++;
           }   
           else
           {
               System.out.printf("%-10s %5s", temp.getId(), "<" +temp.getTipo().toUpperCase()+ ">" +"\n");
               contDir++;
           }
               
        }
        
        if(contDir != 0 || contArch !=0)
        {
            //System.out.println();
            //se imprimen los contadores
            if(contArch > 0)
                System.out.printf("%4s %3s", contArch, "Archivos\n");
            if(contDir > 0)
                System.out.printf("%4s %3s", contDir, "Directorios\n");
            
            //se imprime la memoria disponible
            System.out.println(archivo.leerMemoria() + "K Libres");
        }
        else
            System.out.println("El directorio se encuentra vacío");
    }
    
    //método encargado de cambiar la apariencia del prompt
    public void prompt(String [] tokens)
    {
        //se verifica que la cadena no tenga más de 3 tokens
        if(tokens.length > 3)
        {
            System.out.println("    ERROR 003: Comando no reconocido");
        }
        //si existen 3 tokens, se verifican sus distintas combinaciones
        else if(tokens.length == 3)
        {
            if(tokens[1].equals("$p") && tokens[2].equals("$g"))
            {
                directorios.setPromptP("M:");
                directorios.setPromptG(">");
            }
            else if(tokens[1].equals("$g") && tokens[2].equals("$p"))
            {
                {
                    directorios.setPromptP(">M:");
                    directorios.setPromptG("");
                }
            }
            //si el token 2 o 3 es erróneo, se muestra el error
            else
                System.out.println("    ERROR 003: Comando no reconocido");
            }
        //si existen 2 tokens, se verifican las posibles combinaciones
        else if(tokens.length == 2)
        {
            switch (tokens[1]) {
                case "$p":
                    //se mantiene el prompt por defecto
                    directorios.setPromptP("M:");
                    directorios.setPromptG("");
                    break;
                case "$g":
                    //se mantiene el prompt por defecto
                    directorios.setPromptP("");
                    directorios.setPromptG(">");
                    break;
                default:
                    //el token 1 NO es una palabra reservada se muestra el error
                    System.out.println("    ERROR 003: Comando no reconocido");
                    break;
            }
        }
        //si solo se encontró un token, se muestra el prompt por defecto
        else
        { 
           directorios.setPromptP("M:");
           directorios.setPromptG(">"); 

        }     
    }
    
    //método que se encarga de crear un archivo
    public void copy(String [] tokens)
    {
        if(tokens[1].equals("con"))
        {
            //se analiza que el identificador cumpla con lo requerido
            //un máximo de 8 caracteres
            //no debe iniciar con números
            Pattern pat = Pattern.compile("^[^\\d]" + "[a-zA-Z0-9]{0,7}$");
            Matcher mat = pat.matcher(tokens[2]);
            
            if(mat.find())
            {
                //lee los identificadores que estan en el arbol
                boolean encontrado = archivo.buscarEnArbol(directorioActual, tokens[2]);
                
                //si el nombre de la carpeta/archivo no existe, lo crea
                if(encontrado == false)
                {
                    //se lee el archivo con la memoria disponible
                    String memoriaDisponible = archivo.leerMemoria();
                    //se hace un parse para obtener la memoria en un int
                    int memoriaActual = Integer.parseInt(memoriaDisponible);
                    //verificar si hay espacio disponible en la memoria
                    if(memoriaActual < 4)
                    {
                        System.out.println("    ERROR 004: Memoria insuficiente. No se pudo crear el archivo");
                    }
                    else
                    {
                        Scanner sc = new Scanner(System.in);
                        String info = "";
                        boolean continuar = true;
                        System.out.println("Ingrese el texto a guardar en el archivo (para terminar digite ^z)");
                        System.out.print("---> ");
                        
                        do
                        {
                           
                           String word = sc.nextLine();
                           String [] palabras = word.split(" ");
                           int cont = palabras.length;
                           
                           //si el contador de tokens es menor que 2. No se ingresó ^z
                           if(cont < 2)
                           {
                               System.out.println("Olvidó escribir ^z para finalizar. Intente de nuevo");
                               System.out.print("---> ");
                           }
                               
                           else
                           {
                               //si el segundo token corresponde a ^z sale del ciclo
                               if((palabras[cont - 1].toLowerCase()).equals("^z"))
                               {
                                       for(int i = 0; i <= palabras.length -2 ; i++)
                                       {
                                           info += palabras[i] + " ";
                                           continuar = false;
                                       }
                               }
                               else
                               {
                                   System.out.println("Olvidó escribir ^z para finalizar. Intente de nuevo.");
                                   System.out.print("---> ");
                               }
                                   
                           }
                           
                        }while(continuar);

                        //ingresa los datos de nodo padre + id + tipo en un arraylist
                        arbol = new ArbolEstructura(directorioActual, tokens[2], "arch", info);
                        datos.add(arbol);
                        //se ingresa el directorio al archivo de texto
                        archivo.escribirArbol(arbol);
                        //se le restan 8K a la memoria y luego esta se actualiza
                        memoriaActual -= 4;
                        archivo.modificarMemoria(String.valueOf(memoriaActual));
                    }  
                    
                }
                 //si encuentra un archivo o directorio con el mismo nombre lo indica   
                else
                {
                    String tipo = archivo.buscarTipo(directorioActual, tokens[2]);
                    if(tipo.equals("arch"))
                        System.out.println("    ERROR 001: Ya existe un archivo con ese nombre: " + tokens[2]);
                    else
                        System.out.println("    ERROR 015: Ya existe un directorio con ese nombre: " + tokens[2]);
                }
                    
            }
            else
            {
                System.out.println("    ERROR 002: Identificador incorrecto");
                System.out.println("    *No debe comenzar con dígitos");
                System.out.println("    *No debe contener caracteres especiales");
                System.out.println("    *Debe tener un máximo de 8 caracteres");
            }
        }
    }
    
    
    //método que borra un archivo
    public void del(String[] tokens)
    {
        //busca que el archivo exista en el directorio actual
        boolean encontrado = archivo.buscarEnArbol(directorioActual, tokens[1]);
        
        //si encuentra el archivo, hace las operaciones respectivas
        if(encontrado)
        {
            //se verifica que el nombre del archivo no correcponda a un directorio
            String tipo = archivo.buscarTipo(directorioActual, tokens[1]);
            
            //se adegura que el objeto a eliminar sea un archivo
            if(tipo.equals("arch"))
            {
                //busca el padre del directorio actual
                String padre = archivo.buscarPadre(directorioActual);
                //borra el archivo
                archivo.borrarDirectorio(padre, tokens[1], tipo);
                
                //se lee el archivo con la memoria disponible
                String memoriaDisponible = archivo.leerMemoria();
                //se hace un parse para obtener la memoria en un int
                int memoriaActual = Integer.parseInt(memoriaDisponible);
                //se libera la memoria desocupada
                memoriaActual += 4;
                archivo.modificarMemoria(String.valueOf(memoriaActual));
            }
            else
            {
                System.out.println("    ERROR 017: El objeto a eliminar corresponde a un directorio");
            }
        }
        //si no lo encuentra muestra el error
        else
        {
            System.out.println("    ERROR 018: No existe un archivo con ese nombre --> " + tokens[1]);
        }
    }
    
    //método para ver el contenido de un archivo
    public void type(String[] tokens)
    {
        //busca que el archivo exista en el directorio actual
        boolean encontrado = archivo.buscarEnArbol(directorioActual, tokens[1]);
        
        //si encuentra el archivo, hace las operaciones respectivas
        if(encontrado)
        {
            //se verifica que el nombre del archivo no correcponda a un directorio
            String tipo = archivo.buscarTipo(directorioActual, tokens[1]);
            
            //se adegura que el objeto a eliminar sea un archivo
            if(tipo.equals("arch"))
            {
                //muestra el contenido del archivo
                archivo.verContenido(directorioActual, tokens[1], tipo);
            }
            else
            {
                System.out.println("    ERROR 019: El nombre ingresado corresponde a un directorio");
            }
        }
        //si no lo encuentra muestra el error
        else
        {
            System.out.println("    ERROR 018: No existe un archivo con ese nombre --> " + tokens[1]);
        }
    
    }
    
    //método para renombrar archivos y directorios
    public void ren(String[] tokens)
    {
        //se analiza que el identificador cumpla con lo requerido
        //un máximo de 8 caracteres
        //no debe iniciar con números
        Pattern pat = Pattern.compile("^[^\\d]" + "[a-zA-Z0-9]{0,7}$");
        Matcher mat = pat.matcher(tokens[2]);
            
        if(mat.find())
        {
            //busca que el archivo o directorio exista en el directorio actual
            boolean encontrado = archivo.buscarEnArbol(directorioActual, tokens[1]);

            //si encuentra el archivo o directorio, hace las operaciones respectivas
            if(encontrado)
            {
                //verifica que el nuevo nombre no exista
                boolean encontrado2 = archivo.buscarEnArbol(directorioActual, tokens[2]);
                //si existe, muestra los errores correspondientes
                if(encontrado2)
                {
                    String tipo = archivo.buscarTipo(directorioActual, tokens[2]);
                    if(tipo.equals("arch"))
                        System.out.println("    ERROR 015: Ya existe un archivo con ese nombre: " +tokens[2]);
                    else
                       System.out.println("    ERROR 015: Ya existe un directorio con ese nombre: " +tokens[2]); 
                }
                //si no encuentra el nombre del archivo nuevo, lo renombra.
                else
                {
                    //busca el tipo de archivo a renombrar
                    String tipo = archivo.buscarTipo(directorioActual, tokens[1]);
                    //llama al método que renombra
                    archivo.renombrar(directorioActual, tokens[1], tipo, tokens[2]);
                } 

            }
            //si no lo encuentra muestra el error
            else
            {
                System.out.println("    ERROR 018: No existe un objeto con ese nombre --> " + tokens[1]);
            }
        }
        else
        {
            System.out.println("    ERROR 002: Identificador incorrecto");
            System.out.println("    *No debe comenzar con dígitos");
            System.out.println("    *No debe contener caracteres especiales");
            System.out.println("    *Debe tener un máximo de 8 caracteres");
        }
          
    }
    
    //método para mostrar el árbol de directorios
    public void tree()
    {
        //SE REINICIA LA VARIABLE GLOBAL 'NIVEL'.
        nivel = 1;
        System.out.println("Listado de rutas de directorios para el volumen MIDOS");
        System.out.println("El número de serie del volumen es: JeffreyMunoz-113700324");
        //llama al método recursivo para mostrar los directorios
        arbolRecursivo("/", "/");
    }
    
    //método recursivo
    public void arbolRecursivo(String padre, String id)
    {
        ArrayList<ArbolEstructura> hijos;
              
        if(padre.equals("/")&& id.equals("/"))
        {
           System.out.println("M:/");
           hijos = archivo.buscarHijos("/");
         
           if(hijos.size() > 0)
           {
               //aumenta el nivel de los saltos de línea 
               //nivel += 1;
               //ordena albabéticamente los hijos encontrados
               Collections.sort(hijos, (ArbolEstructura child, ArbolEstructura child2) -> child.getId().compareTo(child2.getId()));

               for(ArbolEstructura names: hijos)
               {
                  if(!(names.getTipo().equals("arch")))
                  {
                      System.out.println("|_____" + names.getId());
                      //busca los hijos de cada hijo de root
                      arbolRecursivo(names.getPadre(), names.getId());
                  } 
               }
           } 
       }
        else
        {
            hijos = archivo.buscarHijos(id);
  
            if(hijos.size() > 0)
            {
                //Ordena alfabéticamente los hijos encontrados
                Collections.sort(hijos, (ArbolEstructura child, ArbolEstructura child2) -> child.getId().compareTo(child2.getId()));
                //aumenta el nivel del tab
                nivel += 1;
                
                for(ArbolEstructura names: hijos)
                {
                   if(!(names.getTipo().equals("arch")))
                   {
                      //de acuerdo al nivel de tabulaciones, se muestra el directorio
                      switch(nivel)
                      {
                          case 2: System.out.println("|     |___" + names.getId());
                          break;
                          case 3: System.out.println("|     |   |__" + names.getId());
                          break;
                          case 4: System.out.println("|     |      |__" + names.getId());
                          break;
                          case 5: System.out.println("|     |         |__" + names.getId());
                          break;
                          case 6: System.out.println("|     |            |__" + names.getId());
                          break;
                          case 7: System.out.println("|     |               |__" + names.getId());
                          break;
                          case 8: System.out.println("|     |                  |__" + names.getId());
                          break;
                          default:
                              System.out.println("\t" + names.getId());
                              break;
                      }
                      
                      arbolRecursivo(names.getPadre(), names.getId());
                   }
                }
                //disminuye el nivel de salto de línea
                nivel -= 1;
            }
             
        }//fin del else
        
    }//fin del método recursivo
    
}
