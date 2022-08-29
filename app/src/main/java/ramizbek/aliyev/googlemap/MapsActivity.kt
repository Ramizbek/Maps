package ramizbek.aliyev.googlemap

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ramizbek.aliyev.googlemap.databinding.ActivityMapsBinding
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        binding.imgLoc.setOnClickListener {
            if (mMap.mapType == GoogleMap.MAP_TYPE_NORMAL){

                //sputnik orqali olib keladi
//                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

                //Asosiy yoki kerakli narsalardi olib beradi (asosan taksistlar uchun)
//                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

                //Zdaniyalardi nomini qoymaydi
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

            }else{
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }

        }

           // Add a marker in Sydney and move the camera
//        val sydney = LatLng(40.382466, 71.781916)
//        mMap.addMarker(MarkerOptions().position(sydney).title("My Location"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        //Zoom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20.0f))

          //Camera joylashuvi
//        val cameraPosition =
//            CameraPosition.Builder().target(sydney).tilt(60f).zoom(20f).bearing(30f)
//                .build()
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                if (it != null){
                    val myLatLng = LatLng(it.latitude, it.longitude)

                    mMap.addMarker(MarkerOptions().position(myLatLng).title("Current Position"))

                    val camera = CameraPosition.Builder()
                        .target(myLatLng)
                        .zoom(15f)
                        .bearing(it.bearing)
                        .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera))
                }
            }

        mMap.setOnMapClickListener {

            //Qoyilgan markerni addressini aniqlab beradi
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                it.latitude,
                it.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            val address: String =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

//            val city: String = addresses[0].getLocality()
//            val state: String = addresses[0].getAdminArea()
//            val country: String = addresses[0].getCountryName()
//            val postalCode: String = addresses[0].getPostalCode()
//            val knownName: String = addresses[0].getFeatureName() // Only if available else return NULL


            mMap.addMarker(MarkerOptions().position(it).title(address))
        }

    }
}