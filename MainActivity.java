import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView latitudeTextView, longitudeTextView;
    private EditText newRegionEditText;
    private Button addRegionButton;

    private LocationManagerService locationManagerService;
    private RegionManager regionManager;
    private MapManager mapManager;
    private FirebaseManager firebaseManager;

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Semaphore semaphore = new Semaphore(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        newRegionEditText = findViewById(R.id.newRegionEditText);
        addRegionButton = findViewById(R.id.addRegionButton);

        locationManagerService = new LocationManagerService(this);
        locationManagerService.startLocationUpdates();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapManager = new MapManager();
        firebaseManager = new FirebaseManager();
        regionManager = new RegionManager();

        addRegionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRegion();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager.setMap(googleMap);
    }

    private void addRegion() {
        String newRegion = newRegionEditText.getText().toString();
        if (newRegion.isEmpty()) {
            Toast.makeText(MainActivity.this, "Insira um nome para a nova região.", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng currentLatLng = locationManagerService.getCurrentLocation();
        if (currentLatLng != null) {
            try {
                semaphore.acquire();
                if (regionManager.canAddRegion(currentLatLng)) {
                    regionManager.addRegion(currentLatLng);
                    mapManager.drawCircle(currentLatLng);
                    firebaseManager.addRegion(currentLatLng);
                    Toast.makeText(MainActivity.this, "Nova região adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Não é possível adicionar uma nova região tão perto da localização atual.", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        } else {
            Toast.makeText(MainActivity.this, "Falha ao obter localização atual. Verifique se a permissão de localização está concedida.", Toast.LENGTH_SHORT).show();
        }
    }
}