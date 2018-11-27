package com.jgchk.hotelhavoc.ui.menu

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.jgchk.hotelhavoc.R
import com.jgchk.hotelhavoc.core.platform.BaseFragment
import com.jgchk.hotelhavoc.ui.game.GameActivity
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isSignedIn()) {
            showPlay()
        } else {
            showLogin()
        }
    }

    fun isSignedIn(): Boolean = GoogleSignIn.getLastSignedInAccount(context) != null

    fun showPlay() {
        main_btn.text = "Play"
        main_btn.setOnClickListener { startGame() }
    }

    fun startGame() {
        startActivity(GameActivity.callingIntent(context!!))
    }

    fun showLogin() {
        main_btn.text = "Login"
        main_btn.setOnClickListener { startSignInIntent(this) }
    }

    fun startSignInIntent(fragment: Fragment) {
        val client = GoogleSignIn.getClient(fragment.context!!, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        val intent = client.signInIntent
        fragment.startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                showPlay()
            } else {
                showSignInErrorDialog(result)
            }
        }
    }

    fun showSignInErrorDialog(result: GoogleSignInResult) {
        var message = result.status.statusMessage
        if (message.isNullOrEmpty()) {
            message = "Unknown Google Play Games error"
        }
        AlertDialog.Builder(context!!).setMessage(message)
            .setNeutralButton(android.R.string.ok, null).show()
    }

    companion object {
        private const val RC_SIGN_IN = 43219
    }

}