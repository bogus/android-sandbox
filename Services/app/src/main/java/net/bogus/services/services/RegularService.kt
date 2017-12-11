package net.bogus.services.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.bogus.services.helper.Constants
import net.bogus.services.model.Color

/**
 * Created by burak on 12/10/17.
 */
class RegularService() : Service() {

    private var serviceLooper:Looper? = null
    private var serviceHandler:ServiceHandler? = null

    override fun onBind(p0: Intent?): IBinder {
        return Binder()
    }

    override fun onCreate() {
        super.onCreate()
        val thread = HandlerThread("parserservice")
        thread.start()
        serviceLooper = thread.looper
        serviceHandler = ServiceHandler(serviceLooper!!)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val msg = serviceHandler?.obtainMessage()
        msg?.arg1 = startId
        msg?.obj = intent
        serviceHandler?.sendMessage(msg)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceLooper?.quit()
    }

    private inner class ServiceHandler(looper:Looper) : Handler(looper) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            parseFile(msg?.obj as Intent)
        }

        private fun parseFile(intent:Intent?) {
            val colorType = object : TypeToken<Array<Color>>() {}.type
            val colors = Gson().fromJson<Array<Color>>(this@RegularService.assets.open("test.json").reader(),
                    colorType)
            val msg = Message.obtain()
            msg.arg1 = 0
            val bundle = Bundle()
            bundle.putParcelableArray("colors", colors)
            msg.obj = bundle
            val messenger = intent?.extras?.get("MESSENGER") as Messenger
            messenger?.send(msg)
        }
    }


}