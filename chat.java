import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class chat{
    static void envia_mensaje_multicast(byte[] buffer,String ip,int puerto) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer, buffer.length,InetAddress.getByName(ip),puerto));
        socket.close();
    }
    static byte[] recibe_mensaje_multicast(MulticastSocket socket,int longitud_mensaje)throws IOException{
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
    static class Worker extends Thread{
        static Object obj = new Object();
        public void run(){
            try{
                InetAddress ip_grupo = InetAddress.getByName("230.0.0.0");
                MulticastSocket socket = new MulticastSocket(40000);
                socket.joinGroup(ip_grupo);
                byte[] a;
                for(;;){
                    synchronized(obj){
                        a = recibe_mensaje_multicast(socket,80);
                        System.out.println(new String(a,"ISO-8859-1"));
 
                    }
               }
            }
            catch(Exception e){
                System.out.println("Error:"+e.getMessage());
            }
            

        }
    }
        public static void main(String[] args) throws Exception{
            System.setProperty("java.net.preferIPV4Stack", "true");
            Scanner teclado = new Scanner(System.in,"ISO-8859-1");
            String nombre = args[0];
            String mensaje=new String("¿holá?".getBytes(),"ISO-8859-1");
            new Worker().start();
            for(;;){
                System.out.println("Ingrese el mensaje a enviar:");
                mensaje = teclado.nextLine();
                envia_mensaje_multicast((nombre+":"+mensaje).getBytes(), "230.0.0.0", 40000);
            }
            
            
        }
    
}