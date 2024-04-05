package com.example.calculationlibrary;

import android.graphics.Color;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class MapManager {

    private GoogleMap mMap;
    private Semaphore semaphore;

    public MapManager(GoogleMap mMap, Semaphore semaphore) {
        this.mMap = mMap;
        this.semaphore = semaphore;
    }

    public void updateMap(LatLng currentLocation) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Localização Atual"));
        mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

    public void drawCircle(LatLng latLng) throws InterruptedException {
        semaphore.acquire();
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(30); // Raio de 30m
        circleOptions.strokeWidth(2);
        circleOptions.strokeColor(Color.RED);
        mMap.addCircle(circleOptions);
        semaphore.release();
    }
}

