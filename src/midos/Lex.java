/*
 * Clase que contiene toda la lógica para validar léxicamente los tokens
 */
package midos;


/**
 *
 * @author Jeffrey
 */
public class Lex {
    
    //instancia la clase de comandos
    Comandos cmd = new Comandos();
    
    //método que divide las cadenas en tokens
    public void toTokens(String cadena)
    {
        //separa la cadena ingresada en tokens, eliminando los espacios
        String [] tokens = cadena.split(" ");
        int i=0;
        int espacios = 0;
        //pasa todos los tokens a minpuscula y quita espacios almacenados por la clase split
        for(String comando: tokens)
        {
            if(!comando.equals(""))
            {
                tokens[i] = comando.toLowerCase();
                i++;
                espacios += 1;
            }
        }
         //switch que analiza el primer token
         switch(tokens[0])
         {
             case "ver": if(tokens.length > 1)
                            System.out.println("    ERROR 003: Comando no reconocido");
                         else
                            cmd.ver();
                break;
             case "cls": if(tokens.length > 1)
                            System.out.println("    ERROR 003: Comando no reconocido");
                         else
                            cmd.cls();
                break;
             case "date": if(tokens.length > 1)
                            System.out.println("    ERROR 003: Comando no reconocido");
                         else
                            cmd.fecha();
                break;   
             case "time": if(tokens.length > 1)
                            System.out.println("    ERROR 003: Comando no reconocido");
                         else
                            cmd.hora();
                break;
             case "md": if(tokens.length != 2)
                        {
                           System.out.println("    ERROR 010: Se esperaba un identificador");
                        }
                        else
                        {
                            cmd.md(tokens);
                        }  
                break;
             case "exit": if(tokens.length > 1)
                            System.out.println("    ERROR 003: Comando no reconocido");
                         else
                            cmd.salir();
                break;
             case "cd":
                         if(tokens.length == 1 && !(tokens[0].equals("cd..")))
                         {
                            System.out.println("    ERROR 010: Se esperaba un identificador");
                         }
                         else if(tokens.length > 2 && espacios > 2)
                         {
                             System.out.println("    ERROR 009: se esperaba solo un identificador");
                         }
                         else
                         {
                             cmd.cd(tokens);
                         }
                 break;
             case "cd..": if(tokens.length > 1)
                         {
                            System.out.println("    ERROR 003: Comando no reconocido");
                         }
                         else
                         {
                             cmd.cdAnterior(tokens[0]);
                         }
                 break;
                 
             case "rd": if(tokens.length != 2)
                        {
                            System.out.println("    ERROR 009: Se espera solo un identificador");
                        }
                        else
                        {
                            cmd.rd(tokens);
                        }
                 break;
                 
             case "dir": if(tokens.length > 1)
                        {
                            System.out.println("    ERROR 003: Comando no reconocido");
                        }
                        else
                        {
                            cmd.dir();
                        }
                break;
                
             case "prompt": cmd.prompt(tokens);
                 break;
                 
             case "copy" : if(tokens.length != 3)
                           {
                               System.out.println("     ERROR 003: Comando no reconocido");
                           }
                           else
                           {
                               cmd.copy(tokens);
                           }
                break;
                
             case "del": if(tokens.length != 2)
                            System.out.println("    ERROR 010: Se esperaba un identificador");
                         else
                            cmd.del(tokens);
                break;
             
             case "type": if(tokens.length != 2)
                          {
                             System.out.println("    ERROR 010: Se esperaba un identificador");
                          }
                          else
                             cmd.type(tokens);
                 break;
                 
             case "ren": if(tokens.length != 3)
                           {
                               System.out.println("     ERROR 003: Comando no reconocido");
                           }
                           else
                           {
                               cmd.ren(tokens);
                           }
                 break;
                 
             case "tree": if(tokens.length > 1)
                            System.out.println("     ERROR 003: Comando no reconocido");
                          else
                            cmd.tree();
                 break;  
                 
             default: System.out.println("   ERROR 003: Comando no reconocido");
                break;
         }
    }
    
}
