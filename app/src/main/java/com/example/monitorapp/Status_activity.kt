//package com.example.monitorapp
//
//import android.app.AlertDialog
//import android.content.Intent
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.net.Uri
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.view.LayoutInflater
//import android.view.Window
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.monitorapp.databinding.ActivityStatusBinding
//
//
//class Status_activity : AppCompatActivity() {
//
//    var temp : Int = 31 //조건문x
//    var hum : Int = 66 //조건문x
//    var uvl : Int = 35
//    var h2s : Int = 5
//    var c2o : Int = 1500
//    var co : Int = 20
//    var o2 : Int = 97
//    var body : Double = 38.7
//    var htrt : Int = 70
//    var fire : Int = 0
//
//    private var mBinding: ActivityStatusBinding? = null
//    private val binding get() = mBinding!!
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //setContentView(R.layout.activity_status)
//        mBinding = ActivityStatusBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val UserList = arrayListOf<User>(
//            //온도값 표시
//            User("온도", "${temp}°C",R.drawable.gray),
//            //습도값 표시
//            User("습도", "${hum}%", R.drawable.gray),
//            //체온값 표시
//            if (body >= 38.5) {
//                User("체온", "${body}°C", R.drawable.red)
//            } else if (body >= 37.5) {
//                User("체온", "${body}°C", R.drawable.yellow)
//            } else {
//                User("체온", "${body}°C", R.drawable.green)
//            },
//            //자외선값 표시
//            if(uvl >= 90) {
//                User("자외선", "${uvl}m", R.drawable.red)
//            } else if(uvl >= 60){
//                User("자외선", "${uvl}m", R.drawable.yellow)
//            }else{
//                User("자외선", "${uvl}m", R.drawable.green)
//            },
//            //산소포화도값 표시
//            if (o2 > 94) {
//                User("산소포화도", "${o2}%", R.drawable.green)
//            } else if (o2 > 90) {
//                User("산소포화도", "${o2}%", R.drawable.yellow)
//            } else {
//                User("산소포화도", "${o2}%", R.drawable.red)
//            },
//            //심박수표시
//            if (htrt <= 100 && htrt >= 60 ) {
//                User("심박수", "${htrt}", R.drawable.green)
//            } else {
//                User("심박수", "${htrt}", R.drawable.red)
//            },
//            //황화수소값 표시
//            if (h2s >= 20) {
//                User("황화수소", "${h2s}ppm", R.drawable.red)
//            } else if (h2s >= 10) {
//                User("황화수소", "${h2s}ppm", R.drawable.yellow)
//            } else {
//                User("황화수소", "${h2s}ppm", R.drawable.green)
//            },
//            //이산화탄소값 표시
//            if (c2o >= 5000) {
//                User("이산화탄소", "${c2o}ppm", R.drawable.red)
//            } else if (c2o >= 2000) {
//                User("이산화탄소", "${c2o}ppm", R.drawable.yellow)
//            } else {
//                User("이산화탄소", "${c2o}ppm", R.drawable.green)
//            },
//            //일산화탄소값 표시
//            if (co >= 100) {
//                User("일산화탄소", "${co}ppm", R.drawable.red)
//            } else if (co >= 30) {
//                User("일산화탄소", "${co}ppm", R.drawable.yellow)
//            } else {
//                User("일산화탄소", "${co}ppm", R.drawable.green)
//            },
//            //불꽃감지
//            if (fire >= 2) {
//                User("불꽃 감지 여부", "${fire}/3", R.drawable.red)
//            } else if (fire >= 1) {
//                User("불꽃 감지 여부", "1/3", R.drawable.yellow)
//            } else {
//                User("불꽃 감지 여부", " 0/3", R.drawable.green)
//            }
//        )
//
//        //위험,경계값요소 1개일때 팝업알림
//        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_bt1, null)
//        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
//        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
//        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)
//        val img = mDialogView.findViewById<ImageView>(R.id.warn_img)
//        //경계 2개
//        val nDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_yellow, null)
//        val textlist = arrayListOf<TextView>(nDialogView.findViewById<TextView>(R.id.text1),
//            nDialogView.findViewById<TextView>(R.id.text1_1),
//            nDialogView.findViewById<TextView>(R.id.text2),
//            nDialogView.findViewById<TextView>(R.id.text2_1),
//            nDialogView.findViewById<TextView>(R.id.text3),
//            nDialogView.findViewById<TextView>(R.id.text3_1),
//            nDialogView.findViewById<TextView>(R.id.text4),
//            nDialogView.findViewById<TextView>(R.id.text4_1),
//            nDialogView.findViewById<TextView>(R.id.text5),
//            nDialogView.findViewById<TextView>(R.id.text5_1),
//            nDialogView.findViewById<TextView>(R.id.text6),
//            nDialogView.findViewById<TextView>(R.id.text6_1),)
//        val nbtn = nDialogView.findViewById<Button>(R.id.ok_btn)
//        //위험 2개
//        val rDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_red, null)
//        val textlist1 = arrayListOf<TextView>(rDialogView.findViewById<TextView>(R.id.text1),
//            rDialogView.findViewById<TextView>(R.id.text2),
//            rDialogView.findViewById<TextView>(R.id.text3),
//            rDialogView.findViewById<TextView>(R.id.text4),
//            rDialogView.findViewById<TextView>(R.id.text5),
//            rDialogView.findViewById<TextView>(R.id.text6),
//            rDialogView.findViewById<TextView>(R.id.text7))
//        val admin = rDialogView.findViewById<Button>(R.id.admin)
//        val ambul = rDialogView.findViewById<Button>(R.id.ambul)
//
//        val Builder = AlertDialog.Builder(this).create() //위험값 팝업창 제작 변수
//        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
//        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드
//
//        val nBuilder = AlertDialog.Builder(this).create() //경계값 팝업창 제작 변수
//        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
//        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드
//
//        val Adapter = UserAdapter(this,UserList)
//        binding.listStatus.adapter = Adapter
//
//        binding.backBtn.setOnClickListener{
//            val intent_back = Intent(this,Safetyrule_activity::class.java)
//            startActivity(intent_back)
//        }
//
//        binding.cam.setOnClickListener{
//            val intent_cam = Intent(Intent.ACTION_VIEW, Uri.parse("http://172.20.10.10:8080/?action=stream"))
//            startActivity(intent_cam)
//        }
//
//        var Numlist = arrayListOf<Int>(0,0,0,0,0,0,0,0)
//        //체온
//        if(body>=38.5){ Numlist[0] = 2}
//        else if(body>=37.5){ Numlist[0] = 1}
//        else{Numlist[0]=0}
//        //자외선
//        if(uvl>=90){ Numlist[1] = 2}
//        else if(uvl>=60){ Numlist[1] = 1}
//        else{Numlist[1]=0}
//        //산소포화도
//        if(o2<=90){ Numlist[2] = 2}
//        else if(o2<=94){ Numlist[2] = 1}
//        else{Numlist[2]=0}
//        //심박수
//        if(!(htrt <= 100 && htrt >= 60)){ Numlist[3] = 2}
//        else{Numlist[3]=0}
//        //황화수소
//        if(h2s>=20){ Numlist[4] = 2}
//        else if(h2s>=10){ Numlist[4] = 1}
//        else{Numlist[4]=0}
//        //이산화탄소
//        if(c2o>=5000){ Numlist[5] = 2}
//        else if(c2o>=2000){ Numlist[5] = 1}
//        else{Numlist[5]=0}
//        //일산화탄소
//        if(co>=100){ Numlist[6] = 2}
//        else if(co>=30){ Numlist[6] = 1}
//        else{Numlist[6]=0}
//        //불꽃
//        if(fire>=2){ Numlist[7] = 2}
//        else if(fire==1){ Numlist[7] = 1}
//        else{Numlist[7]=0}
//
//        var one = Numlist.count { it == 1 } //경계값 카운트
//        var two = Numlist.count { it == 2 } //위험값 카운트
//
//        //경계값 조건문
//        if((o2>90&&o2<=94)||(body<38.5&&body>=37.5)||(h2s<20&&h2s>=10)||(c2o<5000&&c2o>=2000)||(co<100&&co>=30)||(uvl<90&&uvl>=60)||fire==1) {
//            if((o2<=94||body>=37.5||h2s>=10||c2o>=2000||co>=30||fire==1)&&one==1){ //1개의 경계값 조건문
//                if (o2 <= 94) {
//                    text1.setText("산소포화도 저하")
//                    text2.setText("산소를 공급하고\n휴식을 취하세요.")
//                }else if(uvl >= 60) {
//                    text1.setText("자외선 노출 경고")
//                    text2.setText("60분이 경과했습니다.\n주의하십시오.")
//                }else if (body >= 37.5) {
//                    text1.setText("체온 상승")
//                    text2.setText("쿨링 기능을 가동하고\n휴식을 취하세요.")
//                }else if (h2s >= 10) {
//                    text1.setText("황화수소 농도 상승")
//                    text2.setText("작업장을 환기하고\n휴식을 취하세요.")
//                }else if (c2o >= 2000) {
//                    text1.setText("이산화탄소 농도 상승")
//                    text2.setText("작업장을 환기하고\n휴식을 취하세요.")
//                }else if (co >= 30) {
//                    text1.setText("일산화탄소 농도 상승")
//                    text2.setText("작업장을 환기하고\n휴식을 취하세요.")
//                }else if (fire == 1) {
//                    text1.setText("미약한 불꽃 감지")
//                    text2.setText("화재를 주의하고\n현장을 살피세요.")
//                }
//                btn.setOnClickListener {nBuilder.dismiss()}
//                nBuilder.setView(mDialogView)
//            }//1개의경계값닫는괄호
//
//            if(one>=2){ //2개의경계값 조건문
//                var numm = 0
//                if (body >= 37.5){
//                    textlist[numm].setText("체온")
//                    textlist[numm+1].setText("수치 상승 | 쿨링 가동 후 휴식을 권장합니다.")
//                    numm+=2 }
//                if (o2 <= 94){
//                    textlist[numm].setText("산소포화도")
//                    textlist[numm+1].setText("수치 상승 | 산소 공급 후 휴식을 권장합니다.")
//                    numm+=2 }
//                if (uvl >= 60){
//                    textlist[numm].setText("자외선")
//                    textlist[numm+1].setText("노출 시간 경고 | 휴식을 권장합니다.")
//                    numm+=2 }
//                if (h2s >= 10){
//                    textlist[numm].setText("황화수소")
//                    textlist[numm+1].setText("수치 상승 | 현장 환기 후 휴식을 권장합니다.")
//                    numm+=2 }
//                if (c2o >= 2000){
//                    textlist[numm].setText("이산화탄소")
//                    textlist[numm+1].setText("수치 상승 | 현장 환기 후 휴식을 권장합니다.")
//                    numm+=2 }
//                if (co >= 30){
//                    textlist[numm].setText("일산화탄소")
//                    textlist[numm+1].setText("수치 상승 | 현장 환기 후 휴식을 권장합니다.")
//                    numm+=2 }
//                if (fire == 1){
//                    textlist[numm].setText("미약한 불꽃 감지")
//                    textlist[numm+1].setText("화재를 주의하고 현장을 살펴보세요.")
//                    numm+=2 }
//                nbtn.setOnClickListener {nBuilder.dismiss()}
//                nBuilder.setView(nDialogView)
//            }//2개경계값 닫는괄호
//            nBuilder.show()
//            nBuilder.window?.setLayout(850, 1300)
//        }//경계조건문닫는괄호
//
//        //위험값 조건문
//        if(o2<=90||body>=38.5||!(htrt <= 100 && htrt >= 60)||uvl>=90||h2s >= 20||c2o >= 5000||co >= 100||fire>=2) {
//            //1개의 값 위험 나타남
//            if ((o2 <= 90 || body >= 38.5||uvl>=90 || !(htrt <= 100 && htrt >= 60)||h2s >= 20||c2o >= 5000||co >= 100||fire>=2)&&two==1) {
//                if (o2 <= 90) {
//                    text1.setText("산소포화도 위험")
//                } else if (body >= 38.5) {
//                    text1.setText("체온 위험")
//                }else if (uvl >= 90) {
//                    text1.setText("자외선 장시간 노출")
//                } else if (!(htrt <= 100 && htrt >= 60)) {
//                    text1.setText("심박수 위험")
//                }else if (h2s >= 20) {
//                    text1.setText("황화수소 위험")
//                }else if (c2o >= 5000) {
//                    text1.setText("이산화탄소 위험")
//                }else if (co >= 100) {
//                    text1.setText("일산화탄소 위험")
//                }else if (fire >= 2) {
//                    text1.setText("화재 위험(과반수 이상 센서 감지)")
//                }
//                text2.setText("관리자에게 전달하세요\n(미 응답시, 5초 뒤\n119에 연결됩니다)")
//                img.setImageResource(R.drawable.warn_red)
//                btn.setText("관리자 연결")
//                var timer = object : CountDownTimer(5000, 1000) {
//                    override fun onTick(millisUntilFinished: Long) { }
//                    override fun onFinish() {
//                        val myUri = Uri.parse("tel:119")
//                        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
//                        startActivity(myIntent)
//                    }
//                }
//                timer.start()
//                val myUri = Uri.parse("tel:01012345678")
//                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
//                btn.setOnClickListener {
//                    timer.cancel()
//                    startActivity(myIntent)
//                }
//                Builder.setView(mDialogView)
//            }//1개의위험값닫는괄호
//            if(two>=2){ //2개의위험값 조건문
//                var numm = 0
//                if (body >= 38.5){
//                    textlist1[numm].setText("체온 상승")
//                    numm+=1 }
//                if (o2 <= 90){
//                    textlist1[numm].setText("산소포화도 저하")
//                    numm+=1 }
//                if (uvl >= 90){
//                    textlist1[numm].setText("자외선 장시간 노출")
//                    numm+=1 }
//                if (!(htrt <= 100 && htrt >= 60)){
//                    textlist1[numm].setText("심박수 이상 발생")
//                    numm+=1 }
//                if (h2s >= 20){
//                    textlist1[numm].setText("황화수소 농도 증가")
//                    numm+=1 }
//                if (c2o >= 5000){
//                    textlist1[numm].setText("이산화탄소 농도 증가")
//                    numm+=1 }
//                if (co >= 100){
//                    textlist1[numm].setText("일산화탄소 농도 증가")
//                    numm+=1 }
//                if (fire >= 2){
//                    textlist1[numm].setText("과반수 이상 불꽃 센서 감지")
//                    numm+=1 }
//                var timer1 = object : CountDownTimer(5000, 1000) {
//                    override fun onTick(millisUntilFinished: Long) { }
//                    override fun onFinish() {
//                        val myUri = Uri.parse("tel:119")
//                        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
//                        startActivity(myIntent)
//                    }
//                }
//                timer1.start()
//
//                val ambul_num = Uri.parse("tel:01012345678")
//                val yIntent = Intent(Intent.ACTION_DIAL, ambul_num)
//
//                admin.setOnClickListener {
//                    timer1.cancel()
//                    Builder.dismiss()
//                }
//                ambul.setOnClickListener {
//                    timer1.cancel()
//                    startActivity(yIntent)
//                }
//                Builder.setView(rDialogView)
//            }//2개의위험값닫는괄호
//            Builder.show()
//            Builder.window?.setLayout(850, 1300)
//        }//위험조건문닫는괄호
//    }//함수
//}//클래스
