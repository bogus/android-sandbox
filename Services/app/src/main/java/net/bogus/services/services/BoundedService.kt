package net.bogus.services.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*

/**
 * Created by burak on 12/10/17.
 */
class BoundedService() : Service() {

    // Binder given to clients
    private val mBinder = LocalBinder()
    // Random number generator
    private val mGenerator = Random()

    override fun onBind(p0: Intent?): IBinder {
        return mBinder
    }

    inner class LocalBinder : Binder() {

        fun getService() : BoundedService? {
            // Return this instance of LocalService so clients can call public methods
            return this@BoundedService
        }

    }

    /** method for clients  */
    open fun getRandomNumber(): Int {
        return mGenerator.nextInt(100)
    }



}