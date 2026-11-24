import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class SocketIoService {
    private final SocketIOServer socketIOServer;


   public SocketIoService() {
       Configuration config = new Configuration();
       config.setHostname("localhost");
       config.setPort(8081);

       socketIOServer = new SocketIOServer(config);
    }

    public void start() {
        socketIOServer.start();
    }
}
