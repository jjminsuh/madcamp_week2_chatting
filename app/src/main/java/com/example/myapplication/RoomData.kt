package com.example.myapplication

class RoomData {
    private lateinit var username: String
    private lateinit var num: String

    fun setRoomData(username: String, num: String){
        this.username = username
        this.num = num
    }

    fun getUsername(): String{
        return username
    }

    fun setUsername(username: String){
        this.username = username
    }

    fun getRoomNumber(): String{
        return num
    }

    public fun setRoomNumber(num: String){
        this.num = num
    }
}
