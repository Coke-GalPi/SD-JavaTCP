import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteTCP {
    public static void mostrarMenuApp() {
        System.out.println("\n--Menu--");
        System.out.println("1.- Diccionario");
        System.out.println("2.- PDF");
        System.out.println("0.- Salir App");
    }

    public static void mostrarMenuAppDic() {
        System.out.println("--Menu Diccionario--");
        System.out.println("1.- Buscar");
        System.out.println("2.- Agregar");
        System.out.println("0.- Salir Diccionario");
    }

    public static void mostrarMenuAppPD() {
        System.out.println("--Menu Documentos--");
        System.out.println("1.- Buscar");
        System.out.println("2.- Agregar");
        System.out.println("0.- Salir PDF");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nIngrese la IP a la que se va a conectar: ");
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
                            String palabra, significado, msj, datos;
                            switch (opcionDIC) {
                                case 1:
                                    System.out.println("Buscar palabra");
                                    System.out.print("Ingrese la palagra que quiere buscar: ");
                                    palabra = scanner.nextLine();
                                    msj = "buscarPalabra:" + palabra;
                                    salida.writeUTF(msj);
                                    datos = entrada.readUTF();
                                    System.out.println(datos);
                                    break;
                                case 2:
                                    System.out.println("Agregar palabra con significado");
                                    System.out.print("Ingrese la palagra: ");
                                    palabra = scanner.nextLine();
                                    System.out.print("Ingrese el significado: ");
                                    significado = scanner.nextLine();
                                    msj = "agregarPalabra:" + palabra + "," + significado;
                                    salida.writeUTF(msj);
                                    datos = entrada.readUTF();
                                    System.out.println(datos);
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
                        int opcionPDF;
                        do {
                            mostrarMenuAppPD();
                            System.out.print("Ingrese una opcion: ");
                            opcionPDF = scanner.nextInt();
                            scanner.nextLine();
                            String nombrePDF, msj, datos;
                            switch (opcionPDF) {
                                case 1:
                                    System.out.println("Buscar PDF");
                                    System.out.print("Ingrese el nombre del PDF que quiere buscar: ");
                                    nombrePDF = scanner.nextLine();
                                    msj = "buscarPDF:" + nombrePDF;
                                    salida.writeUTF(msj);
                                    datos = entrada.readUTF();
                                    System.out.println(datos);
                                    break;
                                case 2:
                                    System.out.println("Agregar PDF");
                                    System.out.print("Ingrese el nombre del archivo: ");
                                    nombrePDF = scanner.nextLine();
                                    System.out.print("Ingrese la ruta del archivo: ");
                                    String rutaPDF = scanner.nextLine();
                                    File archivoPDF = new File(rutaPDF);
                                    if (archivoPDF.exists() && !archivoPDF.isDirectory()) {
                                        try {
                                            File file = new File(rutaPDF);
                                            FileInputStream fis = new FileInputStream(file);
                                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                                            // Enviar el nombre y tamaño del archivo
                                            dos.writeUTF(file.getName());
                                            dos.writeLong(file.length());
                                            byte[] buffer = new byte[4096];
                                            int bytesRead;
                                            while ((bytesRead = fis.read(buffer)) > 0) {
                                                dos.write(buffer, 0, bytesRead);
                                            }
                                            System.out.println("Archivo enviado");
                                        } catch (IOException ex) {
                                            System.out.println("Error al enviar el archivo: " + ex.getMessage());
                                        }
                                        salida.writeUTF("agregarPDF:" + nombrePDF + "," + rutaPDF);
                                    } else {
                                        System.out.println("El archivo no existe o es un directorio.");
                                    }
                                    break;
                                case 0:
                                    System.out.println("\n\nSaliendo del PDF...\n");
                                    break;
                                default:
                                    System.out.println("Opcion no valida");
                            }
                        } while (opcionPDF != 0);
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