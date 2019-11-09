package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    var permissionToRecordAccepted = false
    var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, permissions,
            REQUEST_RECORD_AUDIO_PERMISSION
        )
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            navController = findNavController(R.id.fragment_container)
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
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if(!permissionToRecordAccepted) finish()
    }
}
