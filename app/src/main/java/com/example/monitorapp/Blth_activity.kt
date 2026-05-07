@file:Suppress("DEPRECATION")

package com.example.monitorapp

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.monitorapp.databinding.ActivityBlthBinding
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Method
import java.util.UUID

class Blth_activity : AppCompatActivity() {

    private var mBinding: ActivityBlthBinding? = null
    private val binding get() = mBinding!!

    private val TAG = "Blth_activity"
    private val BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    //SOS
    private lateinit var temp_txt: TextView
    private lateinit var humid_txt: TextView
    private lateinit var body_txt: TextView
    private lateinit var body_img: ImageView
    private lateinit var uvl_txt: TextView
    private lateinit var uvl_img: ImageView
    private lateinit var fire_txt: TextView
    private lateinit var fire_img: ImageView
    private lateinit var o2_txt: TextView
    private lateinit var o2_img: ImageView
    private lateinit var htrt_txt: TextView
    private lateinit var htrt_img: ImageView
    private lateinit var h2s_txt: TextView
    private lateinit var h2s_img: ImageView
    private lateinit var co2_txt: TextView
    private lateinit var co2_img: ImageView
    private lateinit var co_txt: TextView
    private lateinit var co_img: ImageView

    private lateinit var textStatus: TextView
    private lateinit var arduinoText: TextView
    private lateinit var btnPaired: Button
    private lateinit var btnSearch: Button
    private lateinit var listView: ListView

    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private lateinit var btArrayAdapter: ArrayAdapter<String>
    private val deviceAddressArray = ArrayList<String>()

    private val YOUR_REQUEST_CODE = 123

    private val REQUEST_ENABLE_BT = 1
    private var btSocket: BluetoothSocket? = null
    private var connectedThread: ConnectedThread? = null

    // 추가: 권한 요청 및 검사 상수
    private val PERMISSION_REQUEST_CODE = 1
    private val MESSAGE_READ = 2
    private val BLUETOOTH_REQUEST_CODE = 1

    private var fire1: Int = 0
    private var fire2: Int = 0
    private var fire3: Int = 0

    private var sec: Int = 0
    private var min: Int = 0
    private var milli: Int = 0

    private var num1 : Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBlthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inflater: LayoutInflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)
        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastImage = layout.findViewById<ImageView>(R.id.toast_image)

        // 위치 권한 체크
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없을 경우 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                BLUETOOTH_REQUEST_CODE
            )
        }

        binding.backBtn.setOnClickListener {
            // Bluetooth 연결이 열려 있는지 확인하고 닫습니다.
            if (btSocket != null) {
                try {
                    btSocket?.close()
                } catch (e: IOException) {
                    // 연결을 닫을 때 오류가 발생할 수 있으므로 오류 처리를 수행합니다.
                    e.printStackTrace()
                }
            }
            toastText.text = "블루투스 연결이 해제되었습니다."
            toastImage.setImageResource(R.drawable.bluetooth)
            val toast = Toast(applicationContext)
            toast.duration = Toast.LENGTH_SHORT // 또는 Toast.LENGTH_LONG
            toast.view = layout // 커스텀 레이아웃 설정
            toast.show()
            val intent_back = Intent(this, Safetyrule_activity::class.java)
            startActivity(intent_back)
        }

        binding.btnManual.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_manual, null)
            val btn = mDialogView.findViewById<Button>(R.id.ok)
            val admin = mDialogView.findViewById<Button>(R.id.admin)

            val nBuilder = AlertDialog.Builder(this@Blth_activity).create() // 사용자 매뉴얼 팝업창 변수 생성
            nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 테두리 투명화 설정
            nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) // 테두리 투명화 설정
            // 사용자 매뉴얼 레이아웃을 보여줌. 'R.layout.dialog_manual' 파일은 필요한 디자인에 맞게 수정하세요.
            // 사용자 매뉴얼 내용은 이 레이아웃에 표시
            btn.setOnClickListener {
                nBuilder.dismiss()
            }
            admin.setOnClickListener {
                val myUri = Uri.parse("tel:01012345678")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
            nBuilder.setView(mDialogView)
            nBuilder.show()
            nBuilder.window?.setLayout(850, 1300)
        }
        // 추가: 권한 확인 및 요청
        checkAndRequestPermissions()

        // Enable Bluetooth
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (btAdapter == null) {
            // 기기가 Bluetooth를 지원하지 않는 경우에 대한 처리
            Toast.makeText(this, "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT)
                .show()
            finish()
        } else if (!btAdapter.isEnabled) {
            // Bluetooth가 비활성화된 경우, 활성화하도록 사용자에게 요청
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    REQUEST_ENABLE_BT
                )
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        // Initialize variables
        textStatus = findViewById(R.id.text_status)
        arduinoText = findViewById(R.id.arduino)
        btnPaired = findViewById(R.id.btn_paired)
        btnSearch = findViewById(R.id.btn_search)
//        btnSend = findViewById(R.id.btn_send)
//        btnReceive = findViewById(R.id.btn_receive)
        listView = findViewById(R.id.listview)

        // Show paired devices
        btArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = btArrayAdapter
        listView.setOnItemClickListener(MyOnItemClickListener())

        //SOS
        temp_txt = findViewById<TextView>(R.id.temp)
        humid_txt = findViewById<TextView>(R.id.humid)
        body_txt = findViewById<TextView>(R.id.body)
        body_img = findViewById<ImageView>(R.id.body_img)
        uvl_txt = findViewById<TextView>(R.id.uvl)
        uvl_img = findViewById<ImageView>(R.id.uvl_img)
        fire_txt = findViewById<TextView>(R.id.fire)
        fire_img = findViewById<ImageView>(R.id.fire_img)
        o2_txt = findViewById<TextView>(R.id.o2)
        o2_img = findViewById<ImageView>(R.id.o2_img)
        htrt_txt = findViewById<TextView>(R.id.htrt)
        htrt_img = findViewById<ImageView>(R.id.htrt_img)
        h2s_txt = findViewById<TextView>(R.id.h2s)
        h2s_img = findViewById<ImageView>(R.id.h2s_img)
        co2_txt = findViewById<TextView>(R.id.co2)
        co2_img = findViewById<ImageView>(R.id.co2_img)
        co_txt = findViewById<TextView>(R.id.co)
        co_img = findViewById<ImageView>(R.id.co_img)
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val permissionDeniedList = ArrayList<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                permissionDeniedList.add(permission)
            }
        }

        if (permissionDeniedList.isNotEmpty()) {
            // 권한이 거부된 경우 권한 요청
            ActivityCompat.requestPermissions(
                this,
                permissionDeniedList.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    fun onClickButtonPaired(view: View) {
        btArrayAdapter.clear()
        if (deviceAddressArray.isNotEmpty()) {
            deviceAddressArray.clear()
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_ENABLE_BT
            )
        }
        pairedDevices = btAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                val deviceName = device.name
                val deviceHardwareAddress = device.address
                btArrayAdapter.add(deviceName)
                deviceAddressArray.add(deviceHardwareAddress)
            }
        }
    }

    fun onClickButtonSearch(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_SCAN),
                REQUEST_ENABLE_BT
            )
        }
        if (btAdapter.isDiscovering) {
            btAdapter.cancelDiscovery()
        } else {
            if (btAdapter.isEnabled) {
                btAdapter.startDiscovery()
                btArrayAdapter.clear()
                if (deviceAddressArray.isNotEmpty()) {
                    deviceAddressArray.clear()
                }
                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                registerReceiver(receiver, filter)
            } else {
                Toast.makeText(applicationContext, "Bluetooth is not enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Handler를 사용하여 데이터 처리
    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_READ -> {
                    val data = msg.obj as String
                    onDataReceived(data)
                }
            }
        }
    }

    fun onClickButtonSend(view: View) {
        sendToArduino("a")
    }

    fun onClickButtonSend2(view: View) {
        if (btSocket != null) {
            try {
                btSocket?.close()
                textStatus.text = "Bluetooth 연결 해제"
                arduinoText.text = "데이터 수신 중단..."
                temp_txt.text = "0°C"
                humid_txt.text = "0%"
                uvl_txt.text = "0"
                body_txt.text = "0°C"
                fire_txt.text = "0,0,0"
                o2_txt.text = "0%"
                htrt_txt.text = "0bpm"
                h2s_txt.text = "0ppm"
                co2_txt.text = "0ppm"
                co_txt.text = "0ppm"

                // 여기에 연결이 끊어졌을 때의 추가 작업을 수행할 수 있습니다.

            } catch (e: IOException) {
                // 연결을 닫을 때 오류가 발생할 수 있으므로 오류 처리를 수행합니다.
                e.printStackTrace()
            }
        }
    }

    private fun sendToArduino(message: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 허용된 경우에만 Bluetooth 관련 작업 수행
            connectedThread?.write(message)
        } else {
            // 권한이 거부된 경우 또는 사용자가 권한을 거부한 경우, 권한 요청 로직을 추가해야 합니다.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                YOUR_REQUEST_CODE
            )
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // 1. Bluetooth 권한 확인
                if (ActivityCompat.checkSelfPermission(
                        this@Blth_activity,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // 권한이 허용된 경우에만 Bluetooth 관련 작업 수행
                    val deviceName = device?.name
                    // 여기에 Bluetooth 작업 수행
                } else {
                    // 권한이 거부된 경우 또는 사용자가 권한을 거부한 경우, 권한 요청 로직을 추가해야 합니다.
                    ActivityCompat.requestPermissions(
                        this@Blth_activity,
                        arrayOf(Manifest.permission.BLUETOOTH),
                        YOUR_REQUEST_CODE
                    )
                }

                val deviceName = device?.name
                val deviceHardwareAddress = device?.address
                btArrayAdapter.add(deviceName.toString())
                deviceHardwareAddress?.let { deviceAddressArray.add(it) }
                btArrayAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }

    inner class MyOnItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Toast.makeText(applicationContext, btArrayAdapter.getItem(position), Toast.LENGTH_SHORT)
                .show()
            textStatus.text = "연결중..."
            val name = btArrayAdapter.getItem(position) // Get name
            val address = deviceAddressArray[position] // Get address
            var flag = true
            val device = btAdapter.getRemoteDevice(address)
            try {
                btSocket = createBluetoothSocket(device)
                if (ContextCompat.checkSelfPermission(
                        this@Blth_activity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@Blth_activity,
                        arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                        REQUEST_ENABLE_BT
                    )
                }
                btSocket?.connect()
            } catch (e: IOException) {
                flag = false
                textStatus.text = "연결 실패"
                e.printStackTrace()
            }
            if (flag) {
                textStatus.text = "$name 연결 성공"
                connectedThread = btSocket?.let { ConnectedThread(it) }
                connectedThread?.start()
            }
        }
    }

    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {
        return try {
            val m: Method = device.javaClass.getMethod(
                "createInsecureRfcommSocketToServiceRecord",
                UUID::class.java
            )
            m.invoke(device, BT_MODULE_UUID) as BluetoothSocket
        } catch (e: Exception) {
            Log.e(TAG, "Could not create Insecure RFComm Connection", e)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            device.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
        }
    }

    // 추가: ConnectedThread 클래스
    inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream = mmSocket.inputStream  // InputStream을 사용합니다
        private val mmOutStream = mmSocket.outputStream
        private val mBluetoothHandler: Handler = Handler(Looper.getMainLooper())

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int

            while (true) {
                try {
                    bytes = mmInStream.read(buffer)
                    val incomingMessage = String(buffer, 0, bytes)
                    Log.d(TAG, "InputStream: $incomingMessage")

                    // 데이터를 Handler를 사용하여 MainActivity로 전달
                    mBluetoothHandler.obtainMessage(MESSAGE_READ, incomingMessage).sendToTarget()

                    // 여기서 데이터를 파싱하고 temp 값을 업데이트합니다.
                    if (incomingMessage.startsWith("")) {
                        var data = incomingMessage.substring(0)
                        // ViewModel을 통해 UI 업데이트
                        runOnUiThread {
                            onDataReceived(data) // onDataReceived 호출
                        }
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Error reading from input stream", e)
                    break
                }
            }
        }

        // 다음과 같이 데이터를 보낼 수 있는 함수를 추가할 수 있습니다.
        fun write(message: String) {
            val msgBuffer = message.toByteArray()
            try {
                mmOutStream.write(msgBuffer)
            } catch (e: IOException) {
                Log.e(TAG, "Error writing to output stream", e)
            }
        }
    }

    // 데이터를 수신하면 호출할 함수
    private fun onDataReceived(data: String) {
        val data1 = data
        val value = data1.split(",")

        if (data1.startsWith("")) {
            runOnUiThread {
                if (value[4].toDouble() >= 3.3) {
                    fire1 = 1
                }else{
                    fire1 = 0
                }
                if (value[5].toDouble() >= 3.3) {
                    fire2 = 1
                }else{
                    fire2 = 0
                }
                if (value[6].toDouble() >= 3.3) {
                    fire3 = 1
                }else{
                    fire3 = 0
                }
                arduinoText.text = "데이터 전송중..."
                temp_txt.text = "${value[0]}°C"
                humid_txt.text = "${value[1]}%"
                uvl_txt.text = "${value[2]}"
                body_txt.text = "${value[3]}°C"
                fire_txt.text = "${fire1 + fire2 + fire3}/3"
                //fire_txt.text = "${value[4]},${value[5]},${value[6]}"
                o2_txt.text = "${value[7]}%"
                htrt_txt.text = "${value[8]}bpm"
                h2s_txt.text = "${value[9]}ppm"
                co2_txt.text = "${value[10]}ppm"
                co_txt.text = "${value[11]}ppm"

                //자외선 o
                //실험요망
                if (value[2].toDouble() > 0.0) {
                    //uvl_img.setImageResource(R.drawable.red)
                    var num: Int = 0
                    var timer_uvl = object : CountDownTimer(90 * 60000, 1000) {
                        override fun onTick(p0: Long) {
                            milli = (90*60000-p0.toInt())/1000
                            if(milli>=60 && num1==1){
                                min = 1
                                num1++
                            }
                            if(milli>=120 && num1 == 2){
                                min = 2
                                num1++
                            }
                            if(milli>=180 && num1 == 3){
                                min = 3
                                num1++
                            }
                            if(milli>=240 && num1 == 4){
                                min = 4
                                num1++
                            }
                            if(milli>=300 && num1 == 5){
                                min = 5
                                num1++
                            }
                            if(milli>=360 && num1 == 6){
                                min = 6
                            }
                            if(milli>=300 && num == 5){
                                min = 5
                            }
                            if (milli>=3600 && num == 0) {
                                num = 1
                                uvl_img.setImageResource(R.drawable.yellow)
                                yshowUVLAlert()
                            }
                        }
                        override fun onFinish() {
                            rshowUVLAlert()
                            uvl_img.setImageResource(R.drawable.red)
                        }
                    }
                    timer_uvl.start()
                    uvl_txt.text = "${min}분 (${value[2]})"
                }
//                //체온 o
//                //실험요망
                if (value[3].toDouble() >= 38.5) {
                    body_img.setImageResource(R.drawable.red)
                    rshowTemperatureAlert()
                }else if(value[3].toDouble()>=37.5){
                    body_img.setImageResource(R.drawable.yellow)
                    yshowTemperatureAlert()
                }else{
                    body_img.setImageResource(R.drawable.green)
                }
//              //불꽃 감지 o, 제한시간x
                if (value[4].toDouble() >= 3.3 && value[5].toDouble() >= 3.3 && value[6].toDouble() >= 3.3) {
                    fire_img.setImageResource(R.drawable.red)
                    rshowFireAlert()
                } else if (value[4].toDouble() >= 3.3 && value[5].toDouble() >= 3.3) {
                    fire_img.setImageResource(R.drawable.red)
                    rshowFireAlert()
                } else if (value[5].toDouble() >= 3.3 && value[6].toDouble() >= 3.3) {
                    fire_img.setImageResource(R.drawable.red)
                    rshowFireAlert()
                } else if (value[4].toDouble() >= 3.3 && value[6].toDouble() >= 3.3) {
                    fire_img.setImageResource(R.drawable.red)
                    rshowFireAlert()
                } else if (value[4].toDouble() >= 3.3 || value[5].toDouble() >= 3.3 || value[6].toDouble() >= 3.3) {
                    fire_img.setImageResource(R.drawable.yellow)
                    yshowFireAlert()
                } else {
                    fire_img.setImageResource(R.drawable.green)
                }

                //산소 포화도
                if(value[7].toDouble()<=90.0){
                    o2_img.setImageResource(R.drawable.red)
                    rshowO2Alert()
                }else if(value[7].toDouble()<=94.0){
                    o2_img.setImageResource(R.drawable.yellow)
                    yshowO2Alert()
                }else{
                    o2_img.setImageResource(R.drawable.green)
                }
                //심박수
                if(value[8].toDouble() in 60.0..100.0){
                    htrt_img.setImageResource(R.drawable.green)
                }else{
                    htrt_img.setImageResource(R.drawable.red)
                    rshowhtrtAlert()
                }
//                //황화수소
                if(value[9].toDouble()>=20.0){
                    h2s_img.setImageResource(R.drawable.red)
                    rshowh2sAlert()
                }else if(value[9].toDouble()>=10.0){
                    h2s_img.setImageResource(R.drawable.yellow)
                    yshowh2sAlert()
                }else{
                    h2s_img.setImageResource(R.drawable.green)
                }
                //이산화탄소
                if(value[10].toDouble()>=5000.0){
                    co2_img.setImageResource(R.drawable.red)
                    rshowco2Alert()
                }else if(value[10].toDouble()>=2000.0){
                    co2_img.setImageResource(R.drawable.yellow)
                    yshowco2Alert()
                }else{
                    co2_img.setImageResource(R.drawable.green)
                }
                //일산화탄소
                if(value[11].toDouble()>=100.0){
                    co_img.setImageResource(R.drawable.red)
                    rshowcoAlert()
                }else if(value[11].toDouble()>=30.0){
                    co_img.setImageResource(R.drawable.yellow)
                    yshowcoAlert()
                }else{
                    co_img.setImageResource(R.drawable.green)
                }
                if(value[9].toDouble()>=20.0&&value[11].toDouble()<=100.0&&value[10].toDouble()>=5000.0){
                    rshowgasAlert()
                }
            }
        }
        // 데이터를 파싱하고 필요한 작업을 수행합니다.
    }
        private fun rshowUVLAlert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)
        val img = mDialogView.findViewById<ImageView>(R.id.warn_img)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        img.setImageResource(R.drawable.warn_red)
        text1.setText("자외선 노출 위험")
        text2.setText("노출 시간이 90분에 도달했습니다.\n현장에서 벗어나 휴식을 취하세요.")
        btn.setOnClickListener {nBuilder.dismiss()}
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }
    private fun yshowUVLAlert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        text1.setText("자외선 노출 경고")
        text2.setText("노출 시간이 60분에 도달했습니다.\n주의하세요.")
        btn.setOnClickListener {nBuilder.dismiss()}
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }
    private fun yshowTemperatureAlert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        text1.setText("체온 상승")
        text2.setText("쿨링 기능을 가동하고\n휴식을 취하세요.")
        btn.setOnClickListener { nBuilder.dismiss() }
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }

    //
    private fun yshowFireAlert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        text1.setText("미약한 불꽃 감지")
        text2.setText("현장을 살펴보고\n주의하세요.")
        btn.setOnClickListener { nBuilder.dismiss() }
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }
    private fun yshowO2Alert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        text1.setText("산소포화도 상승")
        text2.setText("산소를 공급하고\n휴식을 취하세요.")
        btn.setOnClickListener {nBuilder.dismiss()}
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }
    private fun yshowh2sAlert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        text1.setText("황화수소 농도 상승")
        text2.setText("현장을 환기하고\n휴식을 취하세요.")
        btn.setOnClickListener {nBuilder.dismiss()}
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }
    private fun yshowco2Alert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        text1.setText("이산화탄소 농도 상승")
        text2.setText("현장을 환기하고\n휴식을 취하세요.")
        btn.setOnClickListener {nBuilder.dismiss()}
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }
    private fun yshowcoAlert() {
        val mDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt1, null)
        val text1 = mDialogView.findViewById<TextView>(R.id.w_comment1)
        val text2 = mDialogView.findViewById<TextView>(R.id.w_comment2)
        val btn = mDialogView.findViewById<Button>(R.id.ok_btn)

        val nBuilder = AlertDialog.Builder(this@Blth_activity).create() //경계값 팝업창 제작 변수
        nBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        nBuilder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        text1.setText("일산화탄소 농도 상승")
        text2.setText("현장을 환기하고\n휴식을 취하세요.")
        btn.setOnClickListener {nBuilder.dismiss()}
        nBuilder.setView(mDialogView)
        nBuilder.show()
        nBuilder.window?.setLayout(850, 1300)
    }
    private fun rshowFireAlert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView =
            LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("다량의 불꽃 감지")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }

    private fun rshowTemperatureAlert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView =
            LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("체온 위험")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }
    private fun rshowO2Alert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("산소포화도 위험")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }
    private fun rshowh2sAlert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("황화수소 위험")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }
    private fun rshowhtrtAlert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("심박수 위험")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }
    private fun rshowco2Alert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("이산화탄소 위험")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }
    private fun rshowcoAlert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("일산화탄소 위험")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }
    private fun rshowgasAlert() {
        //위험값요소 1개일때 팝업알림
        val mmDialogView = LayoutInflater.from(this@Blth_activity).inflate(R.layout.dialog_bt2, null)
        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)

        val Builder = AlertDialog.Builder(this@Blth_activity).create() //위험값 팝업창 제작 변수
        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드

        textm1.setText("유해가스 위험")
        var timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                val myUri = Uri.parse("tel:119")
                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(myIntent)
            }
        }
        timer.start()
        val myUri = Uri.parse("tel:01012345678")
        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
        mbtn.setOnClickListener {
            timer.cancel()
            startActivity(myIntent)
        }
        Builder.setView(mmDialogView)
        Builder.show()
        Builder.window?.setLayout(850, 1300)
    }
    }








