package com.example.monitorapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monitorapp.databinding.ActivityMainBinding


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)
        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastImage = layout.findViewById<ImageView>(R.id.toast_image)

//        val id = binding.lgTxt.text.toString()
//        val pw = binding.pwTxt.text.toString()

        binding.wrkBtn.setOnClickListener {
            val id = binding.lgTxt.text.toString()
            val pw = binding.pwTxt.text.toString()

            val pref = getSharedPreferences("signup", MODE_PRIVATE)
            val savedId = pref.getString("idw", "")
            val savedPw = pref.getString("pww", "")
            val savedName = pref.getString("namew", "")

            if (id.isBlank() || pw.isBlank()) {
                toastText.text = "아이디 또는 비밀번호를 입력해주세요."
                //toastImage.setImageResource(R.drawable.xx)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
                //oast.makeText(this@MainActivity,"아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (id == savedId && pw == savedPw) {
//                Toast.makeText(this@MainActivity,"[ 작업자 : $savedName ] 로그인 성공\n기기와 연결하세요", Toast.LENGTH_SHORT).show()
                toastText.text = "[ 작업자 : $savedName ] 로그인 성공\n안전 수칙을 확인하세요."
                toastImage.setImageResource(R.drawable.check)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
                val intent_next = Intent(this, Safetyrule_activity::class.java)
                startActivity(intent_next)

            } else {
                toastText.text = "아이디 또는 비밀번호가 틀렸습니다."
                //toastImage.setImageResource(R.drawable.xx)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
                //Toast.makeText(this@MainActivity,"아이디 또는 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show() }
            }
        }
            binding.admBtn.setOnClickListener {
                val id = binding.lgTxt.text.toString()
                val pw = binding.pwTxt.text.toString()

                val pref = getSharedPreferences("signup", MODE_PRIVATE)
                val savedId = pref.getString("ida", "")
                val savedPw = pref.getString("pwa", "")
                val savedName = pref.getString("namea", "")

                if (id.isBlank() || pw.isBlank()) {
                    toastText.text = "아이디 또는 비밀번호를 입력해주세요."
                    //toastImage.setImageResource(R.drawable.xx)
                    val toast = Toast(applicationContext)
                    toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                    toast.view = layout // 커스텀 레이아웃 설정
                    toast.show()
                } else if (id == savedId && pw == savedPw) {
                    toastText.text = "[ 관리자 : $savedName ] 로그인 성공"
                    toastImage.setImageResource(R.drawable.check)
                    val toast = Toast(applicationContext)
                    toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                    toast.view = layout // 커스텀 레이아웃 설정
                    toast.show()
//                    Toast.makeText(
//                        this@MainActivity,
//                        "[ 관리자 : $savedName ] 로그인 성공",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    val intent_wklist = Intent(this, Wklist::class.java)
                    startActivity(intent_wklist)
                } else {
                    toastText.text = "아이디 또는 비밀번호가 틀렸습니다."
                    //toastImage.setImageResource(R.drawable.xx)
                    val toast = Toast(applicationContext)
                    toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                    toast.view = layout // 커스텀 레이아웃 설정
                    toast.show()
//                    Toast.makeText(this@MainActivity, "아이디 또는 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT)
//                        .show()
                }
            }

            binding.regBtn.setOnClickListener {
                val intent_sign = Intent(this, SignupActivity::class.java)
                startActivity(intent_sign)
            }
     }
}