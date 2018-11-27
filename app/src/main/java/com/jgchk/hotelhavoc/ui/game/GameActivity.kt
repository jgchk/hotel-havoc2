package com.jgchk.hotelhavoc.ui.game

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.core.platform.BaseActivity

class GameActivity : BaseActivity() {

    override fun fragment() = GameFragment()

    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.setOnNdefPushCompleteCallback(NfcAdapter.OnNdefPushCompleteCallback { onBeamIngredient() }, this)
    }

    private fun onBeamIngredient() = (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as GameFragment).onBeamIngredient()

    fun setBeamMessage(message: String) {
        nfcAdapter.setNdefPushMessage(NdefMessage(NdefRecord.createTextRecord("en", "b$message")), this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onNfcScan(intent)
    }

    private fun onNfcScan(intent: Intent?) {
        val nfcString = intent?.let { NfcHelper.readNfcIntent(it) }
        nfcString?.let { (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as GameFragment).onNfcScan(it) }
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch()
    }

    private fun enableForegroundDispatch() {
        val intent = Intent(applicationContext, javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    private fun disableForegroundDispatch() {
        nfcAdapter.disableForegroundDispatch(this)
    }

    companion object {
        private val TAG = GameActivity::class.simpleName
        fun callingIntent(context: Context) = Intent(context, GameActivity::class.java)
    }
}