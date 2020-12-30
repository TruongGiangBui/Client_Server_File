import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class Client {
    public final static String SERVER_IP = "127.0.0.1";
    public final static int SERVER_PORT = 2000;
    public static String client_path="/home/truonggiang/IdeaProjects/client1";
    public static String client_name;
    public static HashSet<String> cm=new HashSet<>();
    public static synchronized boolean checkcommand(String command)
    {
        if(cm.contains(command)){
            cm.remove(command);
            return false;
        }
        return true;
    }
    public static synchronized void addcommand(String command)
    {
        cm.add(command);
    }
    public static void main(String[] args) throws IOException {
        client_path=args[1];
        client_name=args[0];
        System.out.println(client_path);
        System.out.println(client_name);
        Socket socket = null;
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT); // Connect to server
            System.out.println("Connected: " + socket);
            Path dir = Paths.get(client_path);
            new Thread(new Listen(socket)).start();
            new WatchFileServices(dir,socket).processEvents();
        } catch (IOException ie) {
            System.out.println("Can't connect to server");
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

    }
}