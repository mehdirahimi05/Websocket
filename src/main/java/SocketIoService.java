import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class SocketIoService {
    private final SocketIOServer socketIOServer;

    /**
     * Constructor for SocketIoService.
     */
    public SocketIoService() {
        // Initialize Socket.IO server configuration
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8081);

        socketIOServer = new SocketIOServer(config);

        // Events die connection un d disconnection verwalten
        socketIOServer.addConnectListener(client -> System.out.println("Client connected"));
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


    public void start(@Observes StartupEvent event) {
        socketIOServer.start();
    }

}
