package djamelfel.communitycargps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Maps extends Activity implements LocationListener, View.OnClickListener {

    private LocationManager lManager;
    private Location location;
    private IMapController mapController;
    private String provider;
    private ToggleButton gpsTButton;
    private ToggleButton providerTButton;
    private Button settingsMenu;
    final String EXTRA_DISTANCE = "distance_voulue";
    private String distance_settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(7);
        mapView.setMaxZoomLevel(30);

        //limiter la carte uniquement a un seul modele
        //BoundingBoxE6 bbox = new BoundingBoxE6(0.0, 100.0, 0.0, 100.0);
        //mapview.setScrollableAreaLimit(bbox);

        mapController = mapView.getController();
        mapController.setZoom(9);

        gpsTButton = (ToggleButton) findViewById(R.id.gps);
        findViewById(R.id.gps).setOnClickListener(this);

        providerTButton = (ToggleButton) findViewById(R.id.provider);
        providerTButton.setEnabled(false);
        provider = "network";
        findViewById(R.id.provider).setOnClickListener(this);

        settingsMenu = (Button) findViewById(R.id.settingsMenu);
        findViewById(R.id.settingsMenu).setOnClickListener(this);

        Intent intent = getIntent();
        distance_settings = intent.getStringExtra(EXTRA_DISTANCE);
        //System.out.println(distance_settings);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        printLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gps:
                if(gpsTButton.isChecked()) {
                    providerTButton.setEnabled(true);
                    enablePosition();
                } else {
                    providerTButton.setEnabled(false);
                    disablePosition();
                }
                break;
            case R.id.provider:
                if(providerTButton.getText().equals("Network")) {
                    provider = "network";
                } else {
                    provider = "gps";
                }
                enablePosition();
                break;
            case R.id.settingsMenu:
                Intent intent = new Intent(Maps.this, DisplaySettings.class);
                intent.putExtra(EXTRA_DISTANCE, distance_settings);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void enablePosition() {
        lManager.requestLocationUpdates(provider, 30000, 0, this);
    }

    private void disablePosition() {
        lManager.removeUpdates(this);
    }

    private void printLocation() {
        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapController.setCenter(startPoint);
        mapController.setZoom(14);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }
        Toast.makeText(this, newStatus, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
