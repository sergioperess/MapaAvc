import com.google.android.gms.maps.model.LatLng;
import java.util.concurrent.ArrayBlockingQueue;

public class RegionQueueManager {

    private ArrayBlockingQueue<LatLng> regionQueue;
    private FirebaseManager firebaseManager;

    public RegionQueueManager(ArrayBlockingQueue<LatLng> regionQueue) {
        this.regionQueue = regionQueue;
        firebaseManager = new FirebaseManager();
    }

    public void processRegionQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        LatLng region = regionQueue.take(); // Remover região da fila
                        firebaseManager.addRegionToDatabase(region); // Adicionar região ao banco de dados
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

