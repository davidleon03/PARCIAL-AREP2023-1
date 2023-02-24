package org.escuelaing.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpServer2 {
    public static void main(String[] args) throws IOException {
        Service service = new Service();
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        int cont = 0;
        while(running) {
            try {
                System.out.println("RECIBIENDO PETICIONES");
                clientSocket = serverSocket.accept();
                if(cont == 0){
                    front(clientSocket);
                    cont +=1;
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
                String[] peticion = in.readLine().split(" ");
                if(peticion[0].equals("POST")){
                    String busqueda = peticion[1].substring(1);
                    System.out.println("ESTO LLEGO POST ---> "+busqueda);
                    busqueda(busqueda, clientSocket);
                }

                in.close();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void front(Socket clientSocket) throws IOException {
        String outputLine;
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:  text/html\r\n" + "\r\n" + htmlForm();
        out.println(outputLine);
    }

    public static void busqueda(String entrada, Socket clientSocket) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Service prueba = new Service();
        System.out.println(entrada.substring(0, 2));
        if (entrada.substring(0, 2).equals("Cl")){
            System.out.println(entrada.substring(6, entrada.length()-1));
            ArrayList<String> res = prueba.getCl(entrada.substring(6, entrada.length()-1));
            String respuesta = null;
            for (String r: res){
                respuesta += r + "      ";
                System.out.println(r);
            }
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:  text/html\r\n" + "\r\n" + respuesta;
            out.println(outputLine);
        }
        if (entrada.substring(0, 2).equals("un")){
            // java.net.URLDecoder.decode(path, StandardCharsets.UTF_8);
            String part2 = entrada.split(",")[1];
            System.out.println(part2);
            String decode = java.net.URLDecoder.decode(part2, StandardCharsets.UTF_8);
            System.out.println(decode.substring(1, decode.length()-1));
            String res = prueba.getIV(entrada.split(",")[0].substring(12), decode.substring(1, decode.length()-1));
            String outputLine;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n" + "Content-Type:  text/html\r\n" + "\r\n" + res;
            out.println(outputLine);
        }
    }
    public static String htmlForm(){
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "\n" +
                "        <h1>Reflective ChatGPT</h1>\n" +
                "        <form action=\"/hellopost\">\n" +
                "            <label for=\"postname\">Name:</label><br>\n" +
                "            <input type=\"text\" id=\"postname\" name=\"Funcion\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n" +
                "        </form>\n" +
                "        \n" +
                "        <div id=\"postrespmsg\"></div>\n" +
                "        \n" +
                "        <script>\n" +
                "            function loadPostMsg(name){\n" +
                "                let url = \"\" + name.value;\n" +
                "\n" +
                "                fetch (url, {method: 'POST'})\n" +
                "                    .then(x => x.text())\n" +
                "                    .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n" +
                "            }\n" +
                "        </script>\n" +
                "    </body>\n" +
                "</html>";
    }
}
