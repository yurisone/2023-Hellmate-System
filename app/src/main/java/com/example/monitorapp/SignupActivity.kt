package com.example.monitorapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monitorapp.databinding.ActivitySignupBinding

@Suppress("DEPRECATION")
class SignupActivity : AppCompatActivity() {

    private var mBinding: ActivitySignupBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signup)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)
        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastImage = layout.findViewById<ImageView>(R.id.toast_image)

        binding.comSign.setOnClickListener{

            val name = binding.nameSg.text.toString()
            val id = binding.idSg.text.toString()
            val pw = binding.pwSg.text.toString()

            if(binding.checka.isChecked&&binding.checkw.isChecked)
            {
                toastText.text = "체크박스는 한 개의 항목만 체크 가능합니다."
                toastImage.setImageResource(R.drawable.xx)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
                //Toast.makeText(this@SignupActivity,"체크박스는 한 개의 항목만 체크 가능합니다.", Toast.LENGTH_SHORT).show()
            }
            else if(id.isNotBlank()&&pw.isNotBlank()&&name.isNotBlank()&&binding.checka.isChecked){
                toastText.text = "[관리자] 회원가입 성공"
                toastImage.setImageResource(R.drawable.check)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
               //Toast.makeText(this@SignupActivity,"[관리자] 회원가입 성공", Toast.LENGTH_SHORT).show()
                val pref = getSharedPreferences("signup", MODE_PRIVATE)
                val editor = pref.edit()
                editor.putString("ida", id)
                editor.putString("pwa", pw)
                editor.putString("namea", name)
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else if(id.isNotBlank()&&pw.isNotBlank()&&name.isNotBlank()&&binding.checkw.isChecked){
                toastText.text = "[작업자] 회원가입 성공"
                toastImage.setImageResource(R.drawable.check)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
                //Toast.makeText(this@SignupActivity,"[작업자] 회원가입 성공", Toast.LENGTH_SHORT).show()
                val pref = getSharedPreferences("signup", MODE_PRIVATE)
                val editor = pref.edit()
                editor.putString("idw", id)
                editor.putString("pww", pw)
                editor.putString("namew", name)
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else
            {
                toastText.text = "모든 항목을 채워주시길 바랍니다."
                toastImage.setImageResource(R.drawable.xx)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
                //Toast.makeText(this@SignupActivity,"모든 항목을 채워주시길 바랍니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener{
            val intent_back = Intent(this, MainActivity::class.java)
            startActivity(intent_back)
        }
    }
}