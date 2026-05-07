//package com.example.monitorapp
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.monitorapp.databinding.ActivityStadminBinding
//
//class StadminActivity : AppCompatActivity() {
//
//    var temp : Int = Status_activity().temp
//    var hum : Int = Status_activity().hum
//    var uvl : Int = Status_activity().uvl
//    var h2s : Int = Status_activity().h2s
//    var c2o : Int = Status_activity().c2o
//    var co : Int = Status_activity().co
//    var o2 : Int = Status_activity().o2
//    var body : Double = Status_activity().body
//    var htrt : Int = Status_activity().htrt
//    var fire : Int = Status_activity().fire
//
//    val UserList = arrayListOf<User>(
//        //온도값 표시
//        User("온도", "${temp}°C",R.drawable.gray),
//        //습도값 표시
//        User("습도", "${hum}%", R.drawable.gray),
//        //체온값 표시
//        if (body >= 38.5) {
//            User("체온", "${body}°C", R.drawable.red)
//        } else if (body >= 37.5) {
//            User("체온", "${body}°C", R.drawable.yellow)
//        } else {
//            User("체온", "${body}°C", R.drawable.green)
//        },
//        //자외선값 표시
//        if(uvl >= 90) {
//            User("자외선", "${uvl}nm", R.drawable.red)
//        } else if(uvl >= 60){
//            User("자외선", "${uvl}nm", R.drawable.yellow)
//        }else{
//            User("자외선", "${uvl}nm", R.drawable.green)
//        },
//        //산소포화도값 표시
//        if (o2 > 94) {
//            User("산소포화도", "${o2}%", R.drawable.green)
//        } else if (o2 > 90) {
//            User("산소포화도", "${o2}%", R.drawable.yellow)
//        } else {
//            User("산소포화도", "${o2}%", R.drawable.red)
//        },
//        //심박수표시
//        if (htrt <= 100 && htrt >= 60 ) {
//            User("심박수", "${htrt}", R.drawable.green)
//        } else {
//            User("심박수", "${htrt}ppm", R.drawable.red)
//        },
//        //황화수소값 표시
//        if (h2s >= 20) {
//            User("황화수소", "${h2s}ppm", R.drawable.red)
//        } else if (h2s >= 10) {
//            User("황화수소", "${h2s}ppm", R.drawable.yellow)
//        } else {
//            User("황화수소", "${h2s}ppm", R.drawable.green)
//        },
//        //이산화탄소값 표시
//        if (c2o >= 5000) {
//            User("이산화탄소", "${c2o}ppm", R.drawable.red)
//        } else if (c2o >= 2000) {
//            User("이산화탄소", "${c2o}ppm", R.drawable.yellow)
//        } else {
//            User("이산화탄소", "${c2o}ppm", R.drawable.green)
//        },
//        //일산화탄소값 표시
//        if (co >= 100) {
//            User("일산화탄소", "${co}ppm", R.drawable.red)
//        } else if (co >= 30) {
//            User("일산화탄소", "${co}ppm", R.drawable.yellow)
//        } else {
//            User("일산화탄소", "${co}ppm", R.drawable.green)
//        },
//        //불꽃감지
//        if (fire >= 2) {
//            User("불꽃 감지 여부", "${fire}/3", R.drawable.red)
//        } else if (fire >= 1) {
//            User("불꽃 감지 여부", "1/3", R.drawable.yellow)
//        } else {
//            User("불꽃 감지 여부", " 0/3", R.drawable.green)
//        }
//    )
//
//    private var mBinding: ActivityStadminBinding? = null
//    private val binding get() = mBinding!!
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //setContentView(R.layout.activity_status)
//        mBinding = ActivityStadminBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val pref = getSharedPreferences("signup", MODE_PRIVATE)
//        val savedName = pref.getString("namew", "")
//
//        binding.nameStatus.setText("${savedName} 작업 상태")
//
//        val Adapter = UserAdapter(this,UserList)
//        binding.listStatus.adapter = Adapter
//
//        binding.backBtn.setOnClickListener{
//            val intent_back = Intent(this,Wklist::class.java)
//            startActivity(intent_back)
//        }
//
//        binding.gpsBtn.setOnClickListener{
//            val intent_map = Intent(this,MapsActivity::class.java)
//            startActivity(intent_map)
//        }
//    }
//}