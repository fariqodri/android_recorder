package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import android.app.ActivityManager
import android.content.Context
import android.widget.Toast
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.opengl.MyGlSurfaceView


class EasterEggActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (detectOpenGLES30()) {
            setContentView(MyGlSurfaceView(this))
        } else {
            Toast.makeText(this, R.string.opengl_error, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun detectOpenGLES30(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = am.deviceConfigurationInfo
        return info.reqGlEsVersion >= 0x30000
    }
}
