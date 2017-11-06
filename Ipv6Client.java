
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Ipv6Client {
    public static void main(String[] args)throws IOException{
        byte[] packet;
        try (Socket socket = new Socket("18.221.102.182", 38004)) {
            System.out.println("Connected to server.");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            
            for (int i = 0 ; i < 12 ; i++){
                int dataLength = (int)(Math.pow(2, i+1));
                short length = (short)(40 + dataLength);
                packet = new byte[length];
                packet[0]= 0b01100000;
                packet[1]= 0;
                packet[2]= 0;
                packet[3]= 0;
                packet[4]= (byte)((dataLength >>8)& 0xFF);
                packet[5]= (byte)(dataLength & 0xFF);
                packet[6] = (byte)17;
                packet[7]= (byte)20;
                for(int j=8;j<18;j++){
                    packet[j]=0;
                }
                packet[18]=(byte)0xFF;
                packet[19]=(byte)0xFF;
                packet[20]=127;
                packet[21]=0;
                packet[22]=0;
                packet[23]=1;
                for(int j = 24; j<34; j++){
                    packet[j]=0; 
                }
                packet[34]=(byte)0xFF;
                packet[35]=(byte)0xFF;
                byte[] sendTo=socket.getInetAddress().getAddress();
                for(int j=0;j<=3;j++){
                    packet[j+36]=sendTo[j];
                }
                System.out.println("data length: " + dataLength);
                out.write(packet);
                System.out.print("Response: 0x");
                byte[] code = new byte[4];
                in.read(code);
                for(int j = 0; j < 4; j++){
                    System.out.printf("%X", code[j]);
                }
                System.out.println();
            }
        }
    }

}