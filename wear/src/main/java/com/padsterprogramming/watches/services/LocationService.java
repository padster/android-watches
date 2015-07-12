package com.padsterprogramming.watches.services;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Exposes the current location and update events.
 *
 * NOTE: DO NOT USE. Still WIP, it doesn't yet work properly.
 */
public class LocationService {
  // private static final int UPDATE_INTERVAL_MS = 5 * 60 * 1000; /* Every five minutes should be fine... */
  // HACK - for testing. Also make coarse updates.
  private static final int UPDATE_INTERVAL_MS = 5 * 1000; // 5s

  private final WatchContext context;
  private GoogleApiClient googleApiClient;

  private Location lastLocation;

  public LocationService(WatchContext context) {
    this.context = context;
  }

  public void start() {
    Log.w("LS", "Starting!");

    // TODO - share with other services, and just add to its builder.
    if (googleApiClient == null) {
      Log.w("LS", "Connecting...");
      ConnectionListener listener = new ConnectionListener();
      googleApiClient = new GoogleApiClient.Builder(context.androidContext())
          .addApi(LocationServices.API)
          .addConnectionCallbacks(listener)
          .addOnConnectionFailedListener(listener)
          .build();
      // TODO - connect/disconnect with proper lifecycle.
      googleApiClient.connect();
      updateLocation(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));
    }
  }

  /** @return The last known location for the watch. */
  public Location getLocation() {
    return lastLocation;
  }

  /** Update internally. TODO - also fire events?. */
  private void updateLocation(Location newLocation) {
    Log.w("LS", "Updating location to " + (newLocation == null ? "null" : newLocation.toString()));
    this.lastLocation = newLocation;
  }

  // Eek, weird android callback chaining. TODO - split out into common library.
  public final class ConnectionListener
      implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @Override public void onConnected(Bundle bundle) {
      Log.w("LS", "Connected! Sending location request...");
      LocationRequest locationRequest = LocationRequest.create()
//          .setPriority(LocationRequest.PRIORITY_LOW_POWER)
          .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
          .setInterval(UPDATE_INTERVAL_MS)
          .setFastestInterval(UPDATE_INTERVAL_MS);

      Log.w("LS", "Starting the updates...");
      LocationServices.FusedLocationApi
          .requestLocationUpdates(googleApiClient, locationRequest, new LocationInnerListener())
          .setResultCallback(new ResultCallback<Status>() {
            @Override public void onResult(Status status) {
              Log.d("LS", "Location result: " + status.getStatusMessage());
            }
          });
    }
    @Override public void onConnectionSuspended(int i) {
      Log.d("gAPI", "Connection suspended: " + i);
    }
    @Override public void onConnectionFailed(ConnectionResult connectionResult) {
      Log.d("LS", "Connection failed: " + connectionResult.toString());
    }
  }

  public class LocationInnerListener implements LocationListener {
    @Override public void onLocationChanged(Location location) {
      Log.w("LS", "UPDATED!");
      updateLocation(location);
    }
  }
}
