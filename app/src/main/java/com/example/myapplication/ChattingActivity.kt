package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*

class ChattingActivity : AppCompatActivity() {
    private lateinit var mSocket: Socket
    private lateinit var username : String
    private lateinit var num : String
    private lateinit var chattingView : RecyclerView

    var adapter = ChattingAdapter(this)

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatting)

        init();
    }

    private fun init(){
        try{
            mSocket = IO.socket("http://172.10.5.98:80")
            Log.d("SOCKET", "connection success: " + mSocket.id())
        }catch (e: URISyntaxException){
            e.printStackTrace()
        }

        val intent = intent
        username = intent.getStringExtra("username").toString()
        num = intent.getStringExtra("num").toString()

        mSocket.connect()

        val obj = RoomData()
        obj.setRoomData(username, num)

        mSocket.on(Socket.EVENT_CONNECT){ args ->
            mSocket.emit("enter", gson.toJson(obj))
        }

        Log.d("SOCKET", "entered" + obj.getUsername())


        val contentView = findViewById<EditText>(R.id.inputMessage)
        val sendButton = findViewById<Button>(R.id.sendButton)

        chattingView = findViewById(R.id.chattingRV)
        chattingView.layoutManager = LinearLayoutManager(applicationContext)

        chattingView.adapter = adapter

        sendButton.setOnClickListener() {

            Log.d("SOCKET", "click send")

            val message = MessageData()
            message.setMessageData("Message", username, num, contentView.text.toString(), System.currentTimeMillis())

            fun sendMessage(){
                mSocket.emit("newMessage", gson.toJson(message))
                adapter.addItem(ChatItem(username, contentView.text.toString(), toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
                contentView.setText("")
            }

            sendMessage()
        }

        mSocket.on("update") { args ->
            val data: MessageData = gson.fromJson(args[0].toString(), MessageData::class.java)
            addChat(data)
        }
    }

    private fun addChat(data: MessageData) {
        runOnUiThread {
            val from = data.getFrom()
            val content = data.getContent()
            val time = toDate(data.getSendTime()!!)


            //adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
            //chattingView.scrollToPosition(adapter.itemCount - 1)

            if(username != data.getFrom()){
                adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
            }
            /*else{
                adapter.addItem(ChatItem(from, content, time, ChatType.LEFT_MESSAGE))
                chattingView.scrollToPosition(adapter.itemCount - 1)
            }*/
        }
    }

    private fun toDate(currentMiliis: Long) : String{
        return SimpleDateFormat("hh:mm a").format(Date(currentMiliis))
    }


    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }

}