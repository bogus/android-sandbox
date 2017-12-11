package net.bogus.services

import android.content.*
import android.os.*
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_scrolling.*
import net.bogus.services.adapter.RVAdapter
import net.bogus.services.services.IntentService
import android.support.v4.content.LocalBroadcastManager
import net.bogus.services.helper.Constants
import net.bogus.services.model.Color
import net.bogus.services.services.BoundedService
import net.bogus.services.services.RegularService


class ScrollingActivity : AppCompatActivity {

    var recyclerView:RecyclerView? = null
    var intentService:IntentService? = null
    var boundedService: BoundedService? = null
    var isServiceBound = false
    val serviceConnection = object:ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceBound = false
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            isServiceBound = true
            boundedService = (p1 as BoundedService.LocalBinder)?.getService()
        }
    }

    val handler = object:Handler() {
        override fun handleMessage(msg: Message?) {
            (msg?.obj as Bundle)?.let {
                (it.getParcelableArray("colors") as Array<Color>)?.let {
                    update(it)
                }
            }

        }
    }

    constructor() : super()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        assets.open("test.json")
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView);
        recyclerView?.layoutManager = LinearLayoutManager(this)

        intentService = IntentService("test")
        val statusIntentFilter = IntentFilter(Constants.BROADCAST_ACTION)
        val sampleBroadcastReceiver = SampleBroadcastReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(sampleBroadcastReceiver,
                statusIntentFilter)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings ->
                return true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, BoundedService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        boundedService?.let {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }

    fun parseWithIntentService(v:View) {
        val intent = Intent(this, IntentService::class.java)
        startService(intent)
    }

    fun parseWithRegularService(v:View) {
        val intent = Intent(this, RegularService::class.java)
        intent.putExtra("MESSENGER", Messenger(handler))
        startService(intent)
    }


    fun parseWithBoundedService(v:View) {
        var list = ArrayList<String>()
        while (list.size < 30) {
            list.add(boundedService?.getRandomNumber()?.toString() ?: "Empty")
        }

        recyclerView?.adapter = RVAdapter(list)
    }


    inner class SampleBroadcastReceiver() : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            val colors = p1?.extras?.getParcelableArray(Constants.EXTENDED_DATA_STATUS) as Array<Color>
            update(colors)
        }

    }

    private fun update(colors: Array<Color>) {
        val strings = colors.map { it.color }
        val adapter = RVAdapter(strings)
        recyclerView?.adapter = adapter

    }
}
