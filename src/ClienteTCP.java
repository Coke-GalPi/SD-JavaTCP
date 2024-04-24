import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteTCP {
    public static void mostrarMenuApp() {
        System.out.println("\n\n--Menu--");
        System.out.println("1.- Diccionario");
        System.out.println("2.- Traspaso de PDF");
        System.out.println("0.- Salir App");
    }

    public static void mostrarMenuAppDic() {
        System.out.println("\n\n--Menu Diccionario--");
        System.out.println("1.- Buscar");
        System.out.println("2.- Agregar");
        System.out.println("0.- Salir Diccionario");
    }

    public static void mostrarMenuAppPD() {
        System.out.println("\n\n--Menu Diccionario--");
        System.out.println("1.- Buscar");
        System.out.println("2.- Decargar");
        System.out.println("0.- Salir PDF");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nIngrese la ip a la que se va a conectar: ");
        String ipService = scanner.nextLine();
        int opcionApp;
        try {
            int puertoServicio = 7896;
            Socket s = new Socket(ipService, puertoServicio);
            DataInputStream entrada = new DataInputStream(s.getInputStream());
            DataOutputStream salida = new DataOutputStream(s.getOutputStream());
            do {
                mostrarMenuApp();
                System.out.print("Ingrese una opcion: ");
                opcionApp = scanner.nextInt();
                scanner.nextLine();
                switch (opcionApp) {
                    case 1:
                        System.out.println("\nDiccionario.");
                        int opcionDIC;
                        do {
                            mostrarMenuAppDic();
                            System.out.print("Ingrese una opcion: ");
                            opcionDIC = scanner.nextInt();
                            scanner.nextLine();
                            switch (opcionDIC) {
                                case 1:
                                    System.out.println("\nBuscar palabra");

                                    break;
                                case 2:
                                    System.out.println("\nAgregar palabra con significado");

                                    break;
                                case 0:
                                    System.out.println("\n\nSaliendo del Diccionario...\n");
                                    break;
                                default:
                                    System.out.println("Opcion no valida");
                            }
                        } while (opcionDIC != 0);
                        break;
                    case 2:
                        System.out.println("\nTraspaso de PDF.");

                        System.out.print("Ingrese el nombre del archivo: ");
                        String nombreArchivo = scanner.nextLine();

                        System.out.print("Ingrese el tema del archivo: ");
                        String temaArchivo = scanner.nextLine();

                        System.out.print("Ingrese la ruta del archivo: ");
                        String rutaArchivo = scanner.nextLine();

                        System.out.println("Datos a enviar al servidor:");
                        System.out.println("Nombre del archivo: " + nombreArchivo);
                        System.out.println("Tema del archivo: " + temaArchivo);
                        System.out.println("Ruta del archivo: " + rutaArchivo);

                        // Enviar los datos al servidor
                        String mensaje = nombreArchivo + "|" + temaArchivo + "|" + rutaArchivo;
                        salida.writeUTF(mensaje);
                        System.out.println("Datos enviados al servidor.");

                        // Leer la respuesta del servidor si es necesario
                        String datos = entrada.readUTF();
                        System.out.println("\nRecibido del servidor: " + datos);
                        break;
                    case 0:
                        System.out.println("\n\nSaliendo de la APP...\n");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }
            } while (opcionApp != 0);
            s.close();
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        scanner.close();
    }
}