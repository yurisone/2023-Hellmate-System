package com.example.monitorapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.monitorapp.databinding.ActivityWktestBinding
import java.util.UUID

@Suppress("DEPRECATION")
class WktestActivity : AppCompatActivity() {

    private var mBinding: ActivityWktestBinding? = null
    private val binding get() = mBinding!!
    private val TAG = "WktestActivity"
    private val BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    //SOS
    private lateinit var temp_txt: TextView
    private lateinit var humid_txt: TextView
    private lateinit var body_txt: TextView
    private lateinit var body_img: ImageView
    private lateinit var uvl_txt: TextView
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

    //    private lateinit var btnSend: Button
//    private lateinit var btnReceive: Button
    private lateinit var listView: ListView

    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private lateinit var btArrayAdapter: ArrayAdapter<String>
    private val deviceAddressArray = ArrayList<String>()

    private val YOUR_REQUEST_CODE = 123

    private val REQUEST_ENABLE_BT = 1
    //private var btSocket: BluetoothSocket? = null
    //private var connectedThread: WktestActivity.ConnectedThread? = null

    // 추가: 권한 요청 및 검사 상수
    private val PERMISSION_REQUEST_CODE = 1
    private val MESSAGE_READ = 2
    private val BLUETOOTH_REQUEST_CODE = 1

    var temp: Double = 0.0
    var humid: Double = 0.0
    var body: Double = 0.0
    var fire_list = arrayListOf<Int>(0, 0, 0)
    var fire: Int = fire_list.count { it == 1 }
    var uvl: Int = 0

    //    var o2 : Int = 0
//    var htrt : Int = 0
//    var h2s : Int = 0
//    var co2 : Int = 0
//    var co : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWktestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("signup", MODE_PRIVATE)
        val savedName = pref.getString("namew", "")

        binding.nameStatus.setText("${savedName} 작업 상태")

        binding.cam.setOnClickListener {
            val intent_cam =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://172.20.10.10:8080/?action=stream"))
            startActivity(intent_cam)
        }

        binding.gps.setOnClickListener {
            val intent_map = Intent(this, MapsActivity::class.java)
            startActivity(intent_map)
        }

        binding.backBtn.setOnClickListener {
//            if (btSocket != null) {
//                try {
//                    btSocket?.close()
//                } catch (e: IOException) {
//                    // 연결을 닫을 때 오류가 발생할 수 있으므로 오류 처리를 수행합니다.
//                    e.printStackTrace()
//                }
//            }
            val intent_back = Intent(this, Wklist::class.java)
            startActivity(intent_back)
        }

//        val inflater: LayoutInflater = layoutInflater
//        val layout: View = inflater.inflate(R.layout.custom_toast, null)
//        val toastText = layout.findViewById<TextView>(R.id.toast_text)
//        val toastImage = layout.findViewById<ImageView>(R.id.toast_image)
//
//        // 위치 권한 체크
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
//            // 권한이 없을 경우 요청
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), BLUETOOTH_REQUEST_CODE)
//        }
//
//        // 추가: 권한 확인 및 요청
//        checkAndRequestPermissions()
//
//        // Enable Bluetooth
//        btAdapter = BluetoothAdapter.getDefaultAdapter()
//        if (btAdapter == null) {
//            // 기기가 Bluetooth를 지원하지 않는 경우에 대한 처리
//            Toast.makeText(this, "Bluetooth is not supported on this device.", Toast.LENGTH_SHORT).show()
//            finish()
//        } else if (!btAdapter.isEnabled) {
//            // Bluetooth가 비활성화된 경우, 활성화하도록 사용자에게 요청
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_ENABLE_BT)
//            }
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//        }
//
//        // Initialize variables
//        textStatus = findViewById(R.id.text_status)
//        arduinoText = findViewById(R.id.arduino)
//        btnPaired = findViewById(R.id.btn_paired)
//        btnSearch = findViewById(R.id.btn_search)
////        btnSend = findViewById(R.id.btn_send)
////        btnReceive = findViewById(R.id.btn_receive)
//        listView = findViewById(R.id.listview)
//
//        // Show paired devices
//        btArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
//        listView.adapter = btArrayAdapter
//        listView.setOnItemClickListener(MyOnItemClickListener())
//
//        //SOS
//        temp_txt = findViewById<TextView>(R.id.temp)
//        humid_txt = findViewById<TextView>(R.id.humid)
//        body_txt = findViewById<TextView>(R.id.body)
//        body_img = findViewById<ImageView>(R.id.body_img)
//        uvl_txt = findViewById<TextView>(R.id.uvl)
//        fire_txt = findViewById<TextView>(R.id.fire)
//        fire_img = findViewById<ImageView>(R.id.fire_img)
//        o2_txt = findViewById<TextView>(R.id.o2)
//        o2_img = findViewById<ImageView>(R.id.o2_img)
//        htrt_txt = findViewById<TextView>(R.id.htrt)
//        htrt_img = findViewById<ImageView>(R.id.htrt_img)
//        h2s_txt = findViewById<TextView>(R.id.h2s)
//        h2s_img = findViewById<ImageView>(R.id.h2s_img)
//        co2_txt = findViewById<TextView>(R.id.co2)
//        co2_img = findViewById<ImageView>(R.id.co2_img)
//        co_txt = findViewById<TextView>(R.id.co)
//        co_img = findViewById<ImageView>(R.id.co_img)
//    }
//
//    private fun checkAndRequestPermissions() {
//        val permissions = arrayOf(
//            Manifest.permission.BLUETOOTH,
//            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//
//        val permissionDeniedList = ArrayList<String>()
//
//        for (permission in permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
//                permissionDeniedList.add(permission)
//            }
//        }
//
//        if (permissionDeniedList.isNotEmpty()) {
//            // 권한이 거부된 경우 권한 요청
//            ActivityCompat.requestPermissions(this, permissionDeniedList.toTypedArray(), PERMISSION_REQUEST_CODE)
//        }
//    }
//
//    fun onClickButtonPaired(view: View) {
//        btArrayAdapter.clear()
//        if (deviceAddressArray.isNotEmpty()) {
//            deviceAddressArray.clear()
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_ENABLE_BT)
//        }
//        pairedDevices = btAdapter.bondedDevices
//        if (pairedDevices.isNotEmpty()) {
//            for (device in pairedDevices) {
//                val deviceName = device.name
//                val deviceHardwareAddress = device.address
//                btArrayAdapter.add(deviceName)
//                deviceAddressArray.add(deviceHardwareAddress)
//            }
//        }
//    }
//
//    fun onClickButtonSearch(view: View) {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), REQUEST_ENABLE_BT)
//        }
//        if (btAdapter.isDiscovering) {
//            btAdapter.cancelDiscovery()
//        } else {
//            if (btAdapter.isEnabled) {
//                btAdapter.startDiscovery()
//                btArrayAdapter.clear()
//                if (deviceAddressArray.isNotEmpty()) {
//                    deviceAddressArray.clear()
//                }
//                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//                registerReceiver(receiver, filter)
//            } else {
//                Toast.makeText(applicationContext, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // Handler를 사용하여 데이터 처리
//    private val mHandler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(msg: Message) {
//            when (msg.what) {
//                MESSAGE_READ -> {
//                    val data = msg.obj as String
//                    onDataReceived(data)
//                }
//            }
//        }
//    }
//
////    fun onClickButtonSend(view: View) {
////        sendToArduino("a")
////    }
//
////    fun onClickButtonSend2(view: View) {
////        sendToArduino("b")
////    }
//
//    private fun sendToArduino(message: String) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
//            // 권한이 허용된 경우에만 Bluetooth 관련 작업 수행
//            connectedThread?.write(message)
//        } else {
//            // 권한이 거부된 경우 또는 사용자가 권한을 거부한 경우, 권한 요청 로직을 추가해야 합니다.
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), YOUR_REQUEST_CODE)
//        }
//    }
//    // Create a BroadcastReceiver for ACTION_FOUND.
//    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            if (BluetoothDevice.ACTION_FOUND == action) {
//                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
//                // 1. Bluetooth 권한 확인
//                if (ActivityCompat.checkSelfPermission(this@WktestActivity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
//                    // 권한이 허용된 경우에만 Bluetooth 관련 작업 수행
//                    val deviceName = device?.name
//                    // 여기에 Bluetooth 작업 수행
//                } else {
//                    // 권한이 거부된 경우 또는 사용자가 권한을 거부한 경우, 권한 요청 로직을 추가해야 합니다.
//                    ActivityCompat.requestPermissions(this@WktestActivity, arrayOf(Manifest.permission.BLUETOOTH), YOUR_REQUEST_CODE)
//                }
//
//                val deviceName = device?.name
//                val deviceHardwareAddress = device?.address
//                btArrayAdapter.add(deviceName.toString())
//                deviceHardwareAddress?.let { deviceAddressArray.add(it) }
//                btArrayAdapter.notifyDataSetChanged()
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // Unregister the ACTION_FOUND receiver.
//        unregisterReceiver(receiver)
//    }
//
//    inner class MyOnItemClickListener : AdapterView.OnItemClickListener {
//        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            Toast.makeText(applicationContext, btArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show()
//            textStatus.text = "Connecting..."
//            val name = btArrayAdapter.getItem(position) // Get name
//            val address = deviceAddressArray[position] // Get address
//            var flag = true
//            val device = btAdapter.getRemoteDevice(address)
//            try {
//                btSocket = createBluetoothSocket(device)
//                if (ContextCompat.checkSelfPermission(this@WktestActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this@WktestActivity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_ENABLE_BT)
//                }
//                btSocket?.connect()
//            } catch (e: IOException) {
//                flag = false
//                textStatus.text = "Connection failed!"
//                e.printStackTrace()
//            }
//            if (flag) {
//                textStatus.text = "Connected to $name"
//                connectedThread = btSocket?.let { ConnectedThread(it) }
//                connectedThread?.start()
//            }
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {
//        return try {
//            val m: Method = device.javaClass.getMethod("createInsecureRfcommSocketToServiceRecord", UUID::class.java)
//            m.invoke(device, BT_MODULE_UUID) as BluetoothSocket
//        } catch (e: Exception) {
//            Log.e(TAG, "Could not create Insecure RFComm Connection", e)
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//
//            }
//            device.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
//        }
//    }
//
//    // 추가: ConnectedThread 클래스
//    inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
//        private val mmInStream: InputStream = mmSocket.inputStream  // InputStream을 사용합니다
//        private val mmOutStream = mmSocket.outputStream
//        private val mBluetoothHandler: Handler = Handler(Looper.getMainLooper())
//
//        override fun run() {
//            val buffer = ByteArray(1024)
//            var bytes: Int
//
//            while (true) {
//                try {
//                    bytes = mmInStream.read(buffer)
//                    val incomingMessage = String(buffer, 0, bytes)
//                    Log.d(TAG, "InputStream: $incomingMessage")
//
//                    // 데이터를 Handler를 사용하여 MainActivity로 전달
//                    mBluetoothHandler.obtainMessage(MESSAGE_READ, incomingMessage).sendToTarget()
//
//                    // 여기서 데이터를 파싱하고 temp 값을 업데이트합니다.
//                    if (incomingMessage.startsWith("")) {
//                        var data = incomingMessage.substring(0)
//                        // ViewModel을 통해 UI 업데이트
//                        runOnUiThread {
//                            onDataReceived(data) // onDataReceived 호출
//                        }
//                    }
//                } catch (e: IOException) {
//                    Log.e(TAG, "Error reading from input stream", e)
//                    break
//                }
//            }
//        }
//
//        // 다음과 같이 데이터를 보낼 수 있는 함수를 추가할 수 있습니다.
//        fun write(message: String) {
//            val msgBuffer = message.toByteArray()
//            try {
//                mmOutStream.write(msgBuffer)
//            } catch (e: IOException) {
//                Log.e(TAG, "Error writing to output stream", e)
//            }
//        }
//    }
//
//    // 데이터를 수신하면 호출할 함수
//    private fun onDataReceived(data: String) {
//        val data1 = data
//        val value = data1.split(",")
//
//        if (data1.startsWith("")){
//            runOnUiThread {
//                arduinoText.text = "실시간 데이터 전송 중"
//                temp_txt.text = "${value[0]}°C"
//                humid_txt.text = "${value[1]}%"
//                uvl_txt.text = "${value[2]}"
//                body_txt.text = "${value[3]}°C"
//                fire_txt.text = "${value[4]},${value[5]},${value[6]}"
//                o2_txt.text = "${value[7]}%"
//                htrt_txt.text = "${value[8]}회"
//                h2s_txt.text = "${value[9]}ppm"
//                co2_txt.text = "${value[10]}ppm"
//                co_txt.text = "${value[11]}ppm"
//            }
//        }
//        // 데이터를 파싱하고 필요한 작업을 수행합니다.
//    }
//
//    private fun rshowFireAlert() {
//        //위험값요소 1개일때 팝업알림
//        val mmDialogView = LayoutInflater.from(this@WktestActivity).inflate(R.layout.dialog_bt2, null)
//        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
//        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)
//
//        val Builder = AlertDialog.Builder(this@WktestActivity).create() //위험값 팝업창 제작 변수
//        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
//        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드
//
//        textm1.setText("다량의 불꽃 감지")
//        var timer = object : CountDownTimer(5000, 1000) {
//            override fun onTick(millisUntilFinished: Long) { }
//            override fun onFinish() {
//                val myUri = Uri.parse("tel:119")
//                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
//                startActivity(myIntent)
//            }
//        }
//        timer.start()
//        val myUri = Uri.parse("tel:01012345678")
//        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
//        mbtn.setOnClickListener {
//            timer.cancel()
//            startActivity(myIntent)
//        }
//        Builder.setView(mmDialogView)
//        Builder.show()
//        Builder.window?.setLayout(850, 1300)
//    }
//    private fun rshowTemperatureAlert() {
//        //위험값요소 1개일때 팝업알림
//        val mmDialogView = LayoutInflater.from(this@WktestActivity).inflate(R.layout.dialog_bt2, null)
//        val textm1 = mmDialogView.findViewById<TextView>(R.id.w_comment1)
//        val mbtn = mmDialogView.findViewById<Button>(R.id.ok_btn)
//
//        val Builder = AlertDialog.Builder(this@WktestActivity).create() //위험값 팝업창 제작 변수
//        Builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //테두리 투명화 코드
//        Builder.window?.requestFeature(Window.FEATURE_NO_TITLE) //테두리 투명화 코드
//
//        textm1.setText("체온 위험")
//        var timer = object : CountDownTimer(5000, 1000) {
//            override fun onTick(millisUntilFinished: Long) { }
//            override fun onFinish() {
//                val myUri = Uri.parse("tel:119")
//                val myIntent = Intent(Intent.ACTION_DIAL, myUri)
//                startActivity(myIntent)
//            }
//        }
//        timer.start()
//        val myUri = Uri.parse("tel:01012345678")
//        val myIntent = Intent(Intent.ACTION_DIAL, myUri)
//        mbtn.setOnClickListener {
//            timer.cancel()
//            startActivity(myIntent)
//        }
//        Builder.setView(mmDialogView)
//        Builder.show()
//        Builder.window?.setLayout(850, 1300)
//    }
    }
}