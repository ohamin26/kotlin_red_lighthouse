package kr.ac.kpu.red_lighthouse.activity

import android.R.string
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.ac.kpu.red_lighthouse.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class MenuSelectActivity : AppCompatActivity(){
    lateinit var btn_search : ImageButton
    lateinit var search_loc : EditText
    lateinit var list: ListView
    lateinit var listItem: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    var map = HashMap<String,ArrayList<String>>() // 검색한 장소 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        listItem = ArrayList()

        adapter = ArrayAdapter<String>(this,R.layout.list_item,listItem)

        btn_search = findViewById<ImageButton>(R.id.btn_search)
        search_loc = findViewById<EditText>(R.id.search_loc)
        list = findViewById<ListView>(R.id.list)

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        val intent = intent;
        var user_info_change = intent.getIntExtra("user_info_changed",1)

        list.adapter = adapter

        // 버튼을 누르면 쓰레드 동작
        btn_search.setOnClickListener {
            // 쓰레드 생성
            val thread = NetworkThread()
            thread.start()
            thread.join()
        }


        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run { setOnNavigationItemSelectedListener {

            if(user_info_change === 0){
                // 회원 정보 수정 시에 유저정보 프레그먼트 화면(UserInfoActivity)으로 이동한다.
                val fragment = UserInfoActivity()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                user_info_change = 1
            }else{
                when(it.itemId) {
                    R.id.first -> {
                        //  프래그먼트 화면(MapActivity)으로 이동하는 기능
                        val fragment = MapActivity()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                    }
                    R.id.second -> {
                        // 프래그먼트 화면(공유명소)으로 이동하는 기능
                        val fragment = LocationAddActivity() //임시 값 / 나중에 공유명소 화면으로 전환하도록 변경 예정
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                    }
                    R.id.third -> {
                        // 프래그먼트 화면(유저정보)으로 이동하는 기능
                        val fragment = UserInfoActivity()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit()
                    }
                }
            }
            true
        }
        }
    }

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

                // 화면에 출력
                runOnUiThread {

                    // 페이지 수만큼 반복하여 데이터를 불러온다.
                    for(i in 0 until  result.length()){

                        // 쪽수 별로 데이터를 읽는다.
                        var obj = result.getJSONObject(i)
                        if(obj.getString("CMPNM_NM").toString().contains(search_loc.getText().toString()))
                            if(obj.getString("LEAD_TAX_MAN_STATE").toString().equals("계속사업자")){

                                var REFINE_ROADNM_ADDR = obj.getString("REFINE_ROADNM_ADDR").toString()
                                var CMPNM_NM = obj.getString("CMPNM_NM").toString()
                                var REFINE_WGS84_LAT = obj.getString("REFINE_WGS84_LAT").toString() // 위도
                                var REFINE_WGS84_LOGT = obj.getString("REFINE_WGS84_LOGT").toString() // 경도
                                var regionMny = "경기도 지역화폐 가맹점"

                                if((REFINE_ROADNM_ADDR != "null")&&(CMPNM_NM != "null")
                                    &&(REFINE_WGS84_LAT != "null")&&(REFINE_WGS84_LOGT != "null")){
                                    var searchMap = arrayListOf <String>() // hashMap에 저장할 값
                                    searchMap.add(REFINE_ROADNM_ADDR)
                                    searchMap.add(CMPNM_NM)
                                    searchMap.add(REFINE_WGS84_LAT)
                                    searchMap.add(REFINE_WGS84_LOGT)
                                    searchMap.add(regionMny)
                                    map.put("REFINE_ROADNM_ADDR",searchMap)

                                    listItem.add(CMPNM_NM)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                    }
                }
            }

            // 경기도 일반 음식점 api
            // 페이지 수를 구하기 위해 사용
            val site2 = "https://openapi.gg.go.kr/GENRESTRT?KEY="+key+"&Type=json"+numOfRows+"&pSize=1000&SIGUN_NM=%EC%8B%9C%ED%9D%A5%EC%8B%9C"+sigun_nm

            val url2 = URL(site2)
            val conn2 = url2.openConnection()
            val input2 = conn2.getInputStream()
            val isr2 = InputStreamReader(input2)
            // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
            val br2 = BufferedReader(isr2)

            // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
            var str2: String? = null
            val buf2 = StringBuffer()

            do{
                str2 = br2.readLine()

                if(str2!=null){
                    buf2.append(str2)
                }
            }while (str2!=null)

            // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
            var root2 = JSONObject(buf2.toString())
            var response2 = root2.getJSONArray("GENRESTRT").getJSONObject(0)
            var result2 = response2.getJSONArray("head")
            var pageNumber2 = (result2.getJSONObject(0).getString("list_total_count").toString().toInt()/1000).toInt()

            for(num in 1..pageNumber2+1){
                // 페이지번호
                var pageNo = "&pIndex=" + num

                // API 정보를 가지고 있는 주소
                val site = "https://openapi.gg.go.kr/GENRESTRT?KEY="+key+"&Type=json"+pageNo+numOfRows+sigun_nm

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
                var response = root.getJSONArray("GENRESTRT").getJSONObject(1)
                var result = response.getJSONArray("row")

                // 화면에 출력
                runOnUiThread {

                    // 페이지 수만큼 반복하여 데이터를 불러온다.
                    for(i in 0 until  result.length()){

                        // 쪽수 별로 데이터를 읽는다.
                        var obj = result.getJSONObject(i)
                        if(obj.getString("BIZPLC_NM").toString().contains(search_loc.getText().toString()))
                            if(obj.getString("BSN_STATE_NM").toString().equals("영업")){

                                var REFINE_ROADNM_ADDR = obj.getString("REFINE_ROADNM_ADDR").toString()
                                var BIZPLC_NM = obj.getString("BIZPLC_NM").toString()
                                var X_CRDNT_VL = obj.getString("X_CRDNT_VL").toString() // 위도
                                var Y_CRDNT_VL = obj.getString("Y_CRDNT_VL").toString() // 경도

                                // 경기 지역 화폐 매장인지 아닌지 확인
                                if(!map.containsKey("REFINE_ROADNM_ADDR")){
                                    if((REFINE_ROADNM_ADDR != "null")&&(BIZPLC_NM != "null")
                                        &&(X_CRDNT_VL != "null")&&(Y_CRDNT_VL != "null")){
                                        var searchMap = arrayListOf <String>()
                                        searchMap.add(REFINE_ROADNM_ADDR)
                                        searchMap.add(BIZPLC_NM)
                                        searchMap.add(X_CRDNT_VL)
                                        searchMap.add(Y_CRDNT_VL)
                                        map.put("REFINE_ROADNM_ADDR",searchMap)

                                        listItem.add(BIZPLC_NM)
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            }
                    }
                }
            }

        }

        // 함수를 통해 데이터를 불러온다.
        fun JSON_Parse(obj:JSONObject, data : String): String {

            // 원하는 정보를 불러와 리턴받고 없는 정보는 캐치하여 "없습니다."로 리턴받는다.
            return try {

                obj.getString(data)

            } catch (e: Exception) {
                "없습니다."
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}