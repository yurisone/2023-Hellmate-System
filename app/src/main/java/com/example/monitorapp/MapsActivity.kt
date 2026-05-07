package com.example.monitorapp

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.monitorapp.databinding.ActivityMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
internal class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    lateinit var locationPermission: ActivityResultLauncher<Array<String>>
    //위치 서비스가 gps를 사용해서 위치를 확인
    lateinit var fusedLocationClient: FusedLocationProviderClient
    //위치 값 요청에 대한 갱신 정보를 받는 변수
    lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //etContentView(R.layout.activity_map)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){ results ->
            if(results.all{it.value}){
                startProcess()
            }else{ //문제가 발생했을 때
                Toast.makeText(this,"권한 승인이 필요합니다.",Toast.LENGTH_LONG).show()
            }
        }
//권한 요청
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    fun startProcess(){
        //구글 맵을 준비하는 작업을 진행한다
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync (this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()

        // 마커 클릭 시 InfoWindow를 표시하기 위한 Adapter 설정
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
    }
    @SuppressLint("MissingPermission")
    fun updateLocation(){
        val locationRequest = LocationRequest.create()
        locationRequest.run{
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object :LocationCallback(){
            //1초에 한번씩 변경된 위치 정보가 onLocationResult 으로 전달된다.
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let{
                    for (location in it.locations){
                        Log.d("위치정보",  "위도: ${location.latitude} 경도: ${location.longitude}")
                        setLastLocation(location) //계속 실시간으로 위치를 받아오고 있기 때문에 맵을 확대해도 다시 줄어든다.
                    }
                }
            }
        }
        //권한 처리
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

    }
    fun setLastLocation(lastLocation: Location){
        val LATLNG = LatLng(lastLocation.latitude,lastLocation.longitude)

        val markerOptions = MarkerOptions()
            .position(LATLNG)
            .title("[작업자의 위치]")
            .snippet("위도: ${lastLocation.latitude}, 경도: ${lastLocation.longitude}")

        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(18.0f).build()

        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {
        private val contentsView = layoutInflater.inflate(R.layout.custom_info_window, null)

        override fun getInfoContents(marker: Marker): View? {
            // 마커가 클릭된 경우 호출됨 (커스텀 InfoWindow 내용 설정)
            val title = marker.title
            val snippet = marker.snippet
            val titleTextView = contentsView.findViewById<TextView>(R.id.title)
            val snippetTextView = contentsView.findViewById<TextView>(R.id.snippet)

            titleTextView.text = title
            snippetTextView.text = snippet

            return contentsView
        }

        override fun getInfoWindow(marker: Marker): View? {
            // getInfoContents가 null을 반환할 때 호출됨 (기본 InfoWindow 사용)
            return null
        }
    }

}



