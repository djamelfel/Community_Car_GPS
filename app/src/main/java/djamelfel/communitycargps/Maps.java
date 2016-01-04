package djamelfel.communitycargps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.util.constants.MapViewConstants;

import java.util.List;

public class Maps extends Activity implements MapViewConstants, LocationListener, View
        .OnClickListener {

    private LocationManager lManager;
    private Location location;
    private MapView mapView;
    private IMapController mapController;
    private ToggleButton gpsTButton;
    private Button settingsMenu;
    final String EXTRA_DISTANCE = "distance_voulue";
    private String distance_settings;

    private MapOverlay mmapOverlay = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        /* Permet de zoomer à l'aide de deux doigts */
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(7);

        //limiter la carte uniquement a un seul modele
        //BoundingBoxE6 bbox = new BoundingBoxE6(0.0, 100.0, 0.0, 100.0);
        //mapview.setScrollableAreaLimit(bbox);

        mapController = mapView.getController();
        mapController.setZoom(9);

        mmapOverlay = new MapOverlay(this);
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.add(mmapOverlay);

        gpsTButton = (ToggleButton) findViewById(R.id.gps);
        findViewById(R.id.gps).setOnClickListener(this);

        settingsMenu = (Button) findViewById(R.id.settingsMenu);
        findViewById(R.id.settingsMenu).setOnClickListener(this);

        Intent intent = getIntent();
        distance_settings = intent.getStringExtra(EXTRA_DISTANCE);
        //System.out.println(distance_settings);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapController.setCenter(startPoint);
        mapController.setZoom(14);

        mapView.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gps:
                if(gpsTButton.isChecked()) {
                    enablePosition();
                } else {
                    disablePosition();
                }
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
        lManager.requestLocationUpdates("network", 30000, 0, this);
    }

    private void disablePosition() {
        lManager.removeUpdates(this);
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

    public class MapOverlay extends org.osmdroid.views.overlay.Overlay {
        Paint lp3;
        float pisteX;
        float pisteY;
        Projection projection;
        Point pt;
        GeoPoint gie;
        Rect rec;

        public MapOverlay(Context ctx) {
            super(ctx);

            lp3 = new Paint();
            lp3.setColor(Color.RED);
            lp3.setAntiAlias(true);
            lp3.setStyle(Paint.Style.FILL);
            lp3.setStrokeWidth(1);
            pt = new Point();
        }

        @Override
        protected void draw(Canvas pC, MapView pOsmv, boolean shadow) {
            if (shadow)
                return;

            if(location != null) {
                projection = pOsmv.getProjection();
                gie = new GeoPoint(location.getLatitude(),location.getLongitude());
                rec = mapView.getScreenRect(new Rect());
                projection.toPixels(gie, pt);
                pisteX = pt.x-rec.left;
                pisteY = pt.y - rec.top;
                pC.drawCircle(pisteX, pisteY, 15, lp3);
            }
        }
    }
}
