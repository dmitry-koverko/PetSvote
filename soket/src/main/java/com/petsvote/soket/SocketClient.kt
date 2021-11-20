package com.petsvote.soket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SocketClient: ISocketClient {

    private val TAG = SocketClient::class.java.canonicalName

    private var mSocketListener: SocketListener? = null
    private var mSocket: Socket? = null
    private var mConnect: Boolean = false

    companion object{
        fun newInstance() = SocketClient()
    }


    fun setListener(socketListener: SocketListener){
        mSocketListener = socketListener
    }

    override fun connect() {
        if(mConnect) return
        try {
            //This address is the way you can connect to localhost with AVD(Android Virtual Device)
            mSocket = IO.socket("https://devr.pvapi.site/")
            mSocket?.id()?.let { Log.d("success", it) }
            mSocket?.connect()

            mConnect = true

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
            mConnect = false
        }
    }

    private var onDetailsPet = Emitter.Listener {
        Log.d(TAG, it[0].toString())
        val details = Json.decodeFromString<Any>(it[0].toString())
    }

    override fun getDatails() {
        if(!mConnect) connect()


        var user = UserId(1772448)
        val data = Json.encodeToString(user)
        mSocket?.emit("user_data", data)

        var filter = Filter("cat", "0:30", -2, "ANY", "global")
        //var filterJS = Json.encodeToString(fileter)

        mSocket?.on("get_details", onDetailsPet)

        var pet = Pet(16727638,1772448, filter)
        val dataPet = Json.encodeToString(pet)
        mSocket?.emit("subscribe_details", dataPet)


    }

    fun isConnect(): Boolean{
        return mConnect
    }

}

interface ISocketClient {
    fun connect()
    fun getDatails()
}

interface SocketListener{

}

@Serializable
open class UserId(
    @SerialName("user_id")
    val user_id: Int
)

@Serializable
open class Pet(
    @SerialName("pet_id")
    val pet_id: Int,

    @SerialName("user_id")
    val user_id: Int,

    @SerialName("filter")
    val filter: Filter,
)

@Serializable
open class Filter(
    @SerialName("type")
    val type: String,

    @SerialName("age_between")
    val age_between: String,

    @SerialName("breed_id")
    val breed_id: Int,

    @SerialName("gender")
    val gender: String,

    @SerialName("rating_type")
    val rating_type: String,

)
