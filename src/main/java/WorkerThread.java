import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class WorkerThread extends Thread {
    private Socket socket;
    private HashSet<Socket> clients;
    public WorkerThread(Socket socket) {
        this.socket = socket;
    }
    private static Logger logger
            = LoggerFactory.getLogger(WorkerThread.class);
    public void run() {
        System.out.println("Processing: " + socket);
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ArrayList<Integer> arr=new ArrayList<>();
            while (true) {
                String line=br.readLine();
//                System.out.println(line);
                logger.info(line);
                for(Socket s: Server.clients) {
                    if(!s.equals(socket)) {
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        bw.write(line);
                        bw.newLine();
                        bw.flush();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Request Processing Error: " + e);
        }
        System.out.println("Complete processing: " + socket);
    }
}