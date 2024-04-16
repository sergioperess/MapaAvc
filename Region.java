import com.google.android.gms.maps.model.LatLng;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class RegionManager {

    private BlockingQueue<LatLng> regionQueue;
    private Semaphore semaphore;

    public RegionManager(BlockingQueue<LatLng> regionQueue, Semaphore semaphore) {
        this.regionQueue = regionQueue;
        this.semaphore = semaphore;
    }

    public boolean canAddRegion(LatLng newRegion, LatLng currentLocation) {
        for (LatLng region : regionQueue) {
            float[] results = new float[1];
            android.location.Location.distanceBetween(region.latitude, region.longitude, newRegion.latitude, newRegion.longitude, results);
            if (results[0] < 30) {
                return false;
            }
        }
        return true;
    }

    public void addRegion(LatLng region) throws InterruptedException {
        semaphore.acquire();
        regionQueue.put(region);
        semaphore.release();
    }
}

