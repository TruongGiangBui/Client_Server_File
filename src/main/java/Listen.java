import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Listen implements Runnable{
    private Socket socket;

    public Listen(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true)
        {
            try {
                BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String res=br.readLine();
                System.out.println("response: "+res);
                String[] responses=res.split(";");
                Client.addcommand(res.split(";")[3]);
//                System.out.println("resonse " +responses[1]+" "+responses[2]);
                FileServices.process(res,Client.client_path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
