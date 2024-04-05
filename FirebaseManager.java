import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseManager {

    private DatabaseReference databaseReference;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FirebaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("regions");
    }

    public void checkRegionExistence(LatLng newRegion, RegionManager regionManager, ArrayBlockingQueue<LatLng> regionQueue) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Query query = databaseReference.orderByChild("latitude").equalTo(newRegion.latitude);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            // Se não houver regiões no banco de dados com a mesma latitude, verificar na fila
                            checkRegionQueue(newRegion, regionManager, regionQueue);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Tratamento de erro
                    }
                });
            }
        });
    }

    private void checkRegionQueue(LatLng newRegion, RegionManager regionManager, ArrayBlockingQueue<LatLng> regionQueue) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (LatLng region : regionQueue) {
                    if (isRegionWithinRadius(region, newRegion)) {
                        return; // Região já existe dentro do raio
                    }
                }
                // Se não houver regiões na fila dentro do raio, adicionar ao banco de dados
                addRegionToDatabase(newRegion);
            }
        });
    }

    private boolean isRegionWithinRadius(LatLng existingRegion, LatLng newRegion) {
        // Verificar se a nova região está dentro de 30 metros da região existente
        float[] results = new float[1];
        android.location.Location.distanceBetween(existingRegion.latitude, existingRegion.longitude, newRegion.latitude, newRegion.longitude, results);
        return results[0] < 30;
    }

    private void addRegionToDatabase(LatLng newRegion) {
        databaseReference.push().setValue(newRegion); // Adicionar nova região ao banco de dados
    }
}

