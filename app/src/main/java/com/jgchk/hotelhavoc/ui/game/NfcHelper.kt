package com.jgchk.hotelhavoc.ui.game

import android.content.Intent
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import kotlin.experimental.and

object NfcHelper {
    fun readNfcIntent(intent: Intent): String? {
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            return readTag(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
        }
        return null
    }

    private fun readTag(tag: Tag): String {
        val ndef = Ndef.get(tag)
        val ndefMessage = ndef.cachedNdefMessage
        val records = ndefMessage.records
        return readText(records[0])
    }

    private fun readText(record: NdefRecord): String {
        val languageCodeLength = record.payload[0] and 51
        return String(record.payload, languageCodeLength + 1, record.payload.size - languageCodeLength - 1)
    }
}