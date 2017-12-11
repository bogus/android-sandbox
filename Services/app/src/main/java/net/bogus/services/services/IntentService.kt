package net.bogus.services.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.JsonReader
import com.google.gson.reflect.TypeToken
import net.bogus.services.helper.Constants
import net.bogus.services.model.Color
import com.google.gson.Gson




/**
 * Created by burak on 12/10/17.
 */
class IntentService : IntentService {

    override fun onHandleIntent(p0: Intent?) {
        /*
         * Creates a new Intent containing a Uri object
         * BROADCAST_ACTION is a custom Intent action
         */
        val colorType = object : TypeToken<Array<Color>>() {}.type
        val colors = Gson().fromJson<Array<Color>>(assets.open("test.json").reader(), colorType)
        val localIntent = Intent(Constants.BROADCAST_ACTION)
                // Puts the status into the Intent
                .putExtra(Constants.EXTENDED_DATA_STATUS, colors)
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)
    }

    constructor() : super("test")
    constructor(name: String?) : super(name)

}