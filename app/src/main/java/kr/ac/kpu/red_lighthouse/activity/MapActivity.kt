package kr.ac.kpu.red_lighthouse.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.ac.kpu.red_lighthouse.R
import kr.ac.kpu.red_lighthouse.placeReview.PlaceReviewDao
import kr.ac.kpu.red_lighthouse.placeReview.review
import kr.ac.kpu.red_lighthouse.user.UserDao
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class MapActivity : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var mView: MapView
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    lateinit var button: Button
    //private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    //private val REQUEST_PERMISSION_LOCATION = 10
    lateinit var mMap : GoogleMap
    var mapList: ArrayList<ArrayList<String>?> = arrayListOf()// map에 전달할 값 저장
    var mapList2: ArrayList<ArrayList<String>?> = arrayListOf()
    var map = HashMap<String,ArrayList<String>>() // 초기 화면 마커 띄우기

    lateinit var card_view: LinearLayout
    lateinit var info:TextView
    lateinit var address:TextView
    lateinit var name:TextView
    lateinit var tvAdd:TextView
    lateinit var cntReview: TextView
    lateinit var tvDetails:TextView
    lateinit var indutype_num:TextView
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10
    var cntMyLoc = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.activity_map, container, false).apply {

            tvAdd = findViewById(R.id.tv_add);
            mView = findViewById(R.id.mapView)
            button = findViewById(R.id.mapBtn)
            card_view = findViewById(R.id.card_view)
            info = findViewById(R.id.info)
            address = findViewById(R.id.address)
            name = findViewById(R.id.name)
            cntReview = findViewById(R.id.cnt_review)
            tvDetails = findViewById(R.id.tv_details)
            indutype_num = findViewById(R.id.indutype_num)
        }
        mView.onCreate(savedInstanceState)
        mLocationRequest =  LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }
        card_view.visibility = View.GONE

        mView.getMapAsync(this)

        tvAdd.setOnClickListener{
            var intent = Intent(
                context,
                AddReviewActivity::class.java
            )
            intent.putExtra("name",name.text.toString())
            intent.putExtra("address",address.text.toString())
            startActivity(intent)
        }

        tvDetails.setOnClickListener{
            var intent = Intent(
                context,
                LocationDetailsActivity::class.java
            )
            intent.putExtra("address",address.text)
            intent.putExtra("name",name.text)
            intent.putExtra("info",info.text)
            intent.putExtra("indutype_num",indutype_num.text)
            startActivity(intent)
        }

        button.setOnClickListener{
            if(checkPermissionForLocation(requireContext())){
                startLocationUpdates()
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            locationResult.lastLocation?.let { onLocationChanged(it) }
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        mMap.addMarker(MarkerOptions().position(LatLng(mLastLocation.latitude,mLastLocation.longitude)).title("내위치"))
        if(cntMyLoc == 0){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastLocation.latitude,mLastLocation.longitude)))
        }
        mMap.setOnMarkerClickListener(markerClickListener);
    }

    private fun checkPermissionForLocation(context: android.content.Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    //내위치 추적 권한 허용코드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(
                    requireContext(),
                    "권한이 없어 해당 기능을 실행할 수 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //최초로는 오이도 빨간등대가 나오도록
    override fun onMapReady(googleMap: GoogleMap) {
        if(checkPermissionForLocation(requireContext())){
            startLocationUpdates()
        }
        mMap = googleMap
        //val marker1 = LatLng(37.3452397,126.6879337)
        //mMap.addMarker(MarkerOptions().position(marker1).title("빨간등대"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(marker1))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        val count = arguments?.getInt("count")
        if(count != null){
            for(i in 0..count!!-1){
                mapList.add(arguments?.getStringArrayList("resultKey${i}"))
                if(arguments?.getString("click").equals(mapList[i]?.get(1))){
                    val marker = LatLng(mapList[i]?.get(2)!!.toDouble(),mapList[i]?.get(3)!!.toDouble())
                    val mInfo = mMap.addMarker(MarkerOptions().position(marker).title(mapList[i]?.get(1)))
                    mInfo?.tag = mInfo?.title +"/"+mapList[i]?.get(4)+"/"+mapList[i]?.get(0)+"/"+mapList[i]?.get(5)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
                }
                else{
                    val marker = LatLng(mapList[i]?.get(2)!!.toDouble(),mapList[i]?.get(3)!!.toDouble())
                    val mInfo = mMap.addMarker(MarkerOptions().position(marker).title(mapList[i]?.get(1)))
                    mInfo?.tag = mInfo?.title +"/"+mapList[i]?.get(4)+"/"+mapList[i]?.get(0)+"/"+mapList[i]?.get(5)
                }
            }
            cntMyLoc++
        }
        else{
            val thread = NetworkThread()
            thread.start()
            thread.join()
            val count2 = mapList2.count()
            for(i in 0..count2!!-1){
                val marker = LatLng(mapList2[i]?.get(2)!!.toDouble(),mapList2[i]?.get(3)!!.toDouble())
                val mInfo = mMap.addMarker(MarkerOptions().position(marker).title(mapList2[i]?.get(1)))
                mInfo?.tag = mapList2[i]?.get(1)+"/"+mapList2[i]?.get(4)+"/"+mapList2[i]?.get(0)+"/"+mapList2[i]?.get(5)
            }
        }

        //if(cntMyLoc==0){
            if(checkPermissionForLocation(requireContext())){
                startLocationUpdates()
            }
        //}


        //맵 클릭 리스너-맵 클릭하면 카드뷰 없어짐
        googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                card_view.visibility = View.GONE
            }
        })

        mMap.setOnInfoWindowClickListener(this)

    }

    //마커 클릭 리스너
    var markerClickListener =
        OnMarkerClickListener { marker ->
            card_view.visibility = View.VISIBLE
            var placeReviewDao = PlaceReviewDao()
            Log.e("마커", marker.toString())


            var arr = marker.tag.toString().split("/") //마커에 붙인 태그
            Log.i(arr.toString(),"success")
            if(arr.size > 2) {
                name.text = arr[0]
                info.text = arr[1]
                address.text = arr[2]
                indutype_num.text = arr[3]
                CoroutineScope(Dispatchers.IO).launch {
                    var documents = placeReviewDao.getDataWithAddress(arr[2])
                    var cnt :String = documents.size.toString()
                    Log.e("파이어베이스",cnt)
                    launch(Dispatchers.Main) {
                        cntReview.text = cnt
                    }
                }
            }
            else{
                name.text=""
                info.text = "현재위치"
                address.text = ""
                cntReview.text = "0"

            }
            false
        }

    override fun onInfoWindowClick(marker: Marker) {
        val marker1 = LatLng(mLastLocation.latitude,mLastLocation.longitude)
        mMap.addMarker(MarkerOptions().position(marker1).title("현재위치"))
        var slat = marker1.latitude
        var slng = marker1.longitude
        var sname = "현재위치"
        var dlat = marker.position.latitude
        var dlng = marker.position.longitude
        var dname = marker.title
        val url =
            "nmap://route/public?slat=" + slat + "&slng=" + slng + "&sname=" + sname + "&dlat=" + dlat + "&dlng=" + dlng + "&dname=" + dname + "&appname=kr.ac.kpu.red_lighthouse"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        //val list: List<ResolveInfo> =
        //   requireActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        //if (list == null || list.isEmpty()) {
        //    requireContext().startActivity(
        //        Intent(
        //            Intent.ACTION_VIEW,
        //            Uri.parse("market://details?id=com.nhn.android.nmap")
        //        )
        //    )
        //} else {
            requireContext().startActivity(intent)
        //}
    }

    override fun onStart() {
        super.onStart()
        mView.onStart()
    }
    override fun onStop() {
        super.onStop()
        mView.onStop()
    }
    override fun onResume() {
        super.onResume()
        mView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }
    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }

    var cnt = 0

    inner class NetworkThread: Thread(){
        // 키 값
        val key = "ad8bb95db5e54be5a4592904f8d3412a"

        // 한 페이지 결과 수
        val numOfRows ="&pSize=1000"

        // 시흥시만 뜨도록
        val sigun_nm = "&SIGUN_NM=시흥시"

        override fun run() {

            // 지역화폐 api
            // 페이지 수를 구하기 위해 사용
            val site1 = "https://openapi.gg.go.kr/RegionMnyFacltStus?KEY="+key+"&Type=json&pIndex=1"+numOfRows+"&SIGUN_NM=시흥시"

            val url1 = URL(site1)
            val conn1 = url1.openConnection()
            val input1 = conn1.getInputStream()
            val isr1 = InputStreamReader(input1)
            // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
            val br1 = BufferedReader(isr1)

            // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
            var str1: String? = null
            val buf1 = StringBuffer()

            do{
                str1 = br1.readLine()

                if(str1!=null){
                    buf1.append(str1)
                }
            }while (str1!=null)

            // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
            var root1 = JSONObject(buf1.toString())
            var response1 = root1.getJSONArray("RegionMnyFacltStus").getJSONObject(0)
            var result1 = response1.getJSONArray("head")
            var pageNumber1 = (result1.getJSONObject(0).getString("list_total_count").toString().toInt()/1000).toInt()

            for(num in 1..pageNumber1+1){
                // 페이지번호
                var pageNo = "&pIndex=" + num

                // API 정보를 가지고 있는 주소
                val site = "https://openapi.gg.go.kr/RegionMnyFacltStus?KEY="+key+"&Type=json"+pageNo+numOfRows+sigun_nm

                val url = URL(site)
                val conn = url.openConnection()
                val input = conn.getInputStream()
                val isr = InputStreamReader(input)
                // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
                val br = BufferedReader(isr)

                // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
                var str: String? = null
                val buf = StringBuffer()

                do{
                    str = br.readLine()

                    if(str!=null){
                        buf.append(str)
                    }
                }while (str!=null)

                // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
                var root = JSONObject(buf.toString())
                var response = root.getJSONArray("RegionMnyFacltStus").getJSONObject(1)
                var result = response.getJSONArray("row")


                // 페이지 수만큼 반복하여 데이터를 불러온다.
                for(i in 0 until  result.length()){
                    // 쪽수 별로 데이터를 읽는다.
                    var obj = result.getJSONObject(i)
                    if(obj.getString("LEAD_TAX_MAN_STATE").toString().equals("계속사업자")){
                        if(cnt<21){
                            var REFINE_ROADNM_ADDR = obj.getString("REFINE_ROADNM_ADDR").toString()
                            var CMPNM_NM = obj.getString("CMPNM_NM").toString()
                            var REFINE_WGS84_LAT = obj.getString("REFINE_WGS84_LAT").toString() // 위도
                            var REFINE_WGS84_LOGT = obj.getString("REFINE_WGS84_LOGT").toString() // 경도
                            var regionMny = "경기도 지역화폐 가맹점"
                            var INDUTYPE_NM = obj.getString("INDUTYPE_NM").toString()
                            if((REFINE_ROADNM_ADDR != "null")&&(CMPNM_NM != "null")
                                &&(REFINE_WGS84_LAT != "null")&&(REFINE_WGS84_LOGT != "null")){
                                var searchMap = arrayListOf <String>() // hashMap에 저장할 값
                                searchMap.add(REFINE_ROADNM_ADDR)
                                searchMap.add(CMPNM_NM)
                                searchMap.add(REFINE_WGS84_LAT)
                                searchMap.add(REFINE_WGS84_LOGT)
                                searchMap.add(regionMny)
                                searchMap.add(INDUTYPE_NM)
                                map.put("REFINE_ROADNM_ADDR",searchMap)
                                mapList2.add(searchMap)
                            }
                            cnt++
                        }
                        else{
                            break
                        }
                    }
                }
            }
        }

        // 함수를 통해 데이터를 불러온다.
        fun JSON_Parse(obj: JSONObject, data : String): String {

            // 원하는 정보를 불러와 리턴받고 없는 정보는 캐치하여 "없습니다."로 리턴받는다.
            return try {

                obj.getString(data)

            } catch (e: Exception) {
                "없습니다."
            }
        }
    }
}


