package com.example.crowdsoursingambiental
/*
* Módulo de localização
* linguagem: kotlin
* data: 12 de outubro de 2021
* Adaptado de curso de Daniel Richter/Digital Innovation One
*/
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.crowdsoursingambiental.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
class MapsActivity : AppCompatActivity(),
OnMapReadyCallback,
GoogleMap.OnMarkerClickListener {
private lateinit var map: GoogleMap
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private lateinit var lastLocation: Location
private lateinit var binding: ActivityMapsBinding
/*para atualizar localização do usuário*/
private lateinit var locationCallback: LocationCallback
private lateinit var locationRequest: LocationRequestprivate var locationUpdateState = false
companion object {
private const val LOCATION_PERMISSION_REQUEST_CODE = 1
private const val REQUEST_CHECK_SETTINGS = 2
}
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
binding = ActivityMapsBinding.inflate(layoutInflater)
setContentView(binding.root)
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
val mapFragment = supportFragmentManager
.findFragmentById(R.id.map) as SupportMapFragment
mapFragment.getMapAsync(this)
fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
locationCallback = object : LocationCallback (){
override fun onLocationResult(p0: LocationResult) {
super.onLocationResult(p0)
lastLocation = p0.lastLocation
placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
}
}
createLocationRequest()
}
override fun onMapReady(googleMap: GoogleMap) {
map = googleMap
//a linha abaixo (em true) habilita os controles de zoom no mapa
map.getUiSettings().setZoomControlsEnabled(true)
// a linha abaixo habilita a "escuta" do clique,
// tornando-o funcional no Google Mapas para traçar rotas e localizar no Mapa!
map.setOnMarkerClickListener (this)
// tornando-o funcional no Google Mapas para traçar rotas e localizar no Mapa!
setUpMap()
}
private fun setUpMap() {if (ActivityCompat.checkSelfPermission(this,
android.Manifest.permission.ACCESS_FINE_LOCATION) !=
PackageManager.PERMISSION_GRANTED) {
ActivityCompat.requestPermissions(this,
arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
LOCATION_PERMISSION_REQUEST_CODE)
return
}
map.isMyLocationEnabled = true
map.mapType = GoogleMap.MAP_TYPE_HYBRID
fusedLocationProviderClient.lastLocation.addOnSuccessListener (this) { location ->
// Got last known location. In some rare situations this can be null.
if (location != null) {
lastLocation = location
val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
placeMarkerOnMap(currentLatLng)
map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
}
}
}
private fun placeMarkerOnMap(location: LatLng){
val markerOptions = MarkerOptions().position(location)
markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources
, R.mipmap.ic_user_location)))
val titleStr = getAddress(location)
markerOptions.title(titleStr)
map.addMarker(markerOptions)
}
//capta o endereço do local
private fun getAddress(latLng: LatLng): String{
val geocoder:Geocoder
val addresses: List<Address>
geocoder = Geocoder(this, Locale.getDefault())
addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
val address = addresses[0].getAddressLine(0)
val city = addresses[0].locality
val state = addresses[0].adminArea
val country = addresses[0].countryNameval postalCode = addresses[0].postalCode
return address
}
//função dialoga com usuário para ligar o GPS e o wi-fi juntos
private fun startLocationUpdates(){
if (ActivityCompat.checkSelfPermission(this,
android.Manifest.permission.ACCESS_FINE_LOCATION) !=
PackageManager.PERMISSION_GRANTED) {
ActivityCompat.requestPermissions(this,
arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
LOCATION_PERMISSION_REQUEST_CODE)
return
}
fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null
/* Looper */)
}
private fun createLocationRequest() {
locationRequest = LocationRequest()
/*
* Usar estas linhas quando precisar localizar pontos em série (rodovia, trilha)
*locationRequest.interval = 10000
*locationRequest.fastestInterval = 5000
locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
*/
val builder = LocationSettingsRequest.Builder()
.addLocationRequest(locationRequest)
val client = LocationServices.getSettingsClient(this)
val task = client.checkLocationSettings(builder.build())
task.addOnSuccessListener {
locationUpdateState = true
startLocationUpdates()
}
task.addOnFailureListener { e ->
if (e is ResolvableApiException) {
try {
e.startResolutionForResult(
this@MapsActivity,
REQUEST_CHECK_SETTINGS)
} catch (sendEx: IntentSender.SendIntentException) {
//Ignore the error
}
}
}
}
override fun onPause() {
super.onPause()
fusedLocationProviderClient.removeLocationUpdates(locationCallback)
}
public override fun onResume() {
super.onResume()
if (!locationUpdateState) {
startLocationUpdates()
}
}
override fun onMarkerClick(p0: Marker?) = false
}



