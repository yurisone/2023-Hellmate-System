package com.example.monitorapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monitorapp.databinding.ActivitySafetyruleBinding

var num : Int  = 0

@Suppress("DEPRECATION")
class Safetyrule_activity : AppCompatActivity() {

    private var mBinding: ActivitySafetyruleBinding? = null
    private val binding get() = mBinding!!
    val set_new : String = "수칙안내 확인이 완료되었습니다.\n              (화면 이동 가능)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_safetyrule)
        mBinding = ActivitySafetyruleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)
        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastImage = layout.findViewById<ImageView>(R.id.toast_image)

        if(num==1)
        {
            binding.checktext.setText(set_new)
        }

        binding.nextBtn.setOnClickListener{
            if(binding.notion1.isChecked&&binding.notion2.isChecked&&binding.notion3.isChecked&&binding.notion4.isChecked
                &&binding.notion5.isChecked&&binding.notion6.isChecked&&num==0) {
                num=1
                val intent_next = Intent(this, Blth_activity::class.java)
                startActivity(intent_next)
            }
            else if(num==1)
            {
//                binding.checktext.text = set_new
                val intent_next = Intent(this, Blth_activity::class.java)
                startActivity(intent_next)
            }
            else
            {
                toastText.text = "모든 수칙 확인바랍니다."
                toastImage.setImageResource(R.drawable.xx)
                val toast = Toast(applicationContext)
                toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
                toast.view = layout // 커스텀 레이아웃 설정
                toast.show()
                //Toast.makeText(this@Safetyrule_activity,"모든 수칙 확인바랍니다.",Toast.LENGTH_SHORT).show()
            }
//            val intent_next = Intent(this, Status_activity::class.java)
//            startActivity(intent_next)
        }

        binding.backBtn.setOnClickListener{
            val intent_back = Intent(this, MainActivity::class.java)
            startActivity(intent_back)
        }
    }
}
