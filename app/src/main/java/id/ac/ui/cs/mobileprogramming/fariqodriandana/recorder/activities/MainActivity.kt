package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.UserDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.UserName
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models.UserViewModel
import kotlinx.coroutines.runBlocking

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val PICK_ACCOUNT = 1

class MainActivity : AppCompatActivity() {

    var permissionToRecordAccepted = false
    var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            ActivityCompat.requestPermissions(this, permissions,
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        }
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            navController = findNavController(R.id.fragment_container)
        }
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        if (!userViewModel.promptedForAccount) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            val intent = AccountManager.newChooseAccountIntent(null, null, arrayOf("com.google"), null, null, null, null)
            startActivityForResult(intent, PICK_ACCOUNT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_ACCOUNT -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    val accountName = data?.extras?.get(AccountManager.KEY_ACCOUNT_NAME)
                    if (accountName == null) finish()
                    else {
                        runBlocking {
                            userViewModel.insert(UserName(userName = accountName as String), this@MainActivity)
                            userViewModel.promptedForAccount = true
                        }
                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) return false
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.recorderFragment) {
                menu?.findItem(R.id.list_menu)?.isVisible = true
//                listMenu.visibility = View.VISIBLE
            } else if (destination.id == R.id.playerFragment) {
                menu?.findItem(R.id.list_menu)?.isVisible = false
//                listMenu.visibility = View.GONE
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.list_menu -> {
                findNavController(R.id.fragment_container).navigate(R.id.action_recorderFragment_to_playerFragment)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
//
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.isNotEmpty()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if(!permissionToRecordAccepted) finish()
    }
}
