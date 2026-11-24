import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class SocketIoService {
    private final SocketIOServer socketIOServer;

    public SocketIoService() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8081);

        socketIOServer = new SocketIOServer(config);

        socketIOServer.addConnectListener(client -> System.out.println("Client connected"));
        socketIOServer.addDisconnectListener(client -> System.out.println("Client disconnected"));

        socketIOServer.addEventListener("chat", ChatMessage.class, (client, data, ackRequest) -> {
            System.out.println("Received chat message: " + data);
            socketIOServer.getBroadcastOperations().sendEvent("chat", data);
        });

    }


    public void start(@Observes StartupEvent event) {
        socketIOServer.start();
    }

}
