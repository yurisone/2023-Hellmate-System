package com.example.monitorapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monitorapp.databinding.ActivityWklistBinding

@Suppress("DEPRECATION")
class Wklist : AppCompatActivity() {

    var fire1 = 0
    var fire2 = 0
    var fire3 = 0
    var body = 0
    var fire = fire1 + fire2 + fire3

    var h2s = 0
    var c2o = 0
    var co = 0
    var o2 = 98
    var htrt = 70

    private var mBinding: ActivityWklistBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)
        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastImage = layout.findViewById<ImageView>(R.id.toast_image)

        val pref = getSharedPreferences("signup", MODE_PRIVATE)
        val savedName = pref.getString("namew", "")

        val UserList2 = arrayListOf<User2>(
            if (fire >= 2 || h2s >= 20 || c2o >= 5000 || co >= 100 || o2 <= 90 || body >= 38.5 || !(htrt <= 100 && htrt >= 60 )) {
                User2("$savedName", R.drawable.red)
            } else if (h2s >= 10 && c2o >= 2000 && co >= 30) {
                User2("$savedName", R.drawable.red)
            } else if (fire == 1 ||  h2s >= 10 || c2o >= 2000 || co >= 30 || o2 <= 95 || body >= 37.5) {
                User2("$savedName", R.drawable.yellow)
            } else {
                User2("$savedName", R.drawable.green)
            }
        )

        val UserList3 = arrayListOf<User2>(
            User2("김감자", R.drawable.gray),
            User2("박구마", R.drawable.gray),
            User2("오수수", R.drawable.gray)
        )

        val Adapter = UserAdapter2(this, UserList2)
        binding.listWorkersActive.adapter = Adapter

        val Adapter2 = UserAdapter2(this, UserList3)
        binding.listWorkersNonactive.adapter = Adapter2

        binding.listWorkersActive.setOnItemClickListener { adapterView, view, i, l ->
            val clickedname = UserList2[i]
            val myIntent = Intent(this, WktestActivity::class.java)
            startActivity(myIntent)
        }

        binding.listWorkersNonactive.setOnItemClickListener { adapterView, view, i, l ->
            val clickedname1 = UserList3[i]
            toastText.text = "비활성화 되어있는 작업자입니다."
            toastImage.setImageResource(R.drawable.xx)
            val toast = Toast(applicationContext)
            toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
            toast.view = layout // 커스텀 레이아웃 설정
            toast.show()
            //Toast.makeText(this@Wklist, "비활성화 되어있는 작업자입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.backBtn.setOnClickListener {
            val intent_back = Intent(this, MainActivity::class.java)
            startActivity(intent_back)
        }
    }
}

