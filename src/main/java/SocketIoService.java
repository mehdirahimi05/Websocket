import com.socketio4j.socketio.Configuration;
import com.socketio4j.socketio.SocketIOServer;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SocketIoService {
    @Inject
    SocketIOServer socketIOServer;

    /**
     * Constructor for SocketIoService.
     */
    @Inject
    public SocketIoService(
            @ConfigProperty(name = "netty-socketio.hostname") String hostname,
            @ConfigProperty(name = "netty-socketio.port") int port) {
        Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        socketIOServer = new SocketIOServer(config);

    }

    void registerListeners(@Observes @Priority(1000) StartupEvent event) {

        // Events die connection un d disconnection verwalten
        socketIOServer.addConnectListener(client -> {
            // client.joinRoom("klasseB");
            System.out.println("Client connected");
        });

        socketIOServer.addDisconnectListener(client -> System.out.println("Client disconnected"));

        // verwaltung von Chatnachrichten in einem bestimmten Room
        socketIOServer.addEventListener("chat", ChatMessage.class, (client, data, ackRequest) -> {
            System.out.println("Received chat message: " + data);
            // nachrichten nur im Raum senden
            if (!client.getAllRooms().contains(data.getRoomName())) {
                return;
            }
            socketIOServer.getRoomOperations(data.getRoomName()).sendEvent("chat", data);
        });

        // event um in ein Room zu joinen
        socketIOServer.addEventListener("joinRoom", String.class, (client, roomName, ackRequest) -> {
            client.joinRoom(roomName);
            System.out.println("Received join room request: " + roomName);
        });
    }

}
