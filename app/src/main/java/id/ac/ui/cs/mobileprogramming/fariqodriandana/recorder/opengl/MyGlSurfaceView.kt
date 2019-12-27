package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

/*
 * simple extention of the GLsurfaceview.  basically setup to use opengl 3.0
 * and set some configs.  This would be where the touch listener is setup to do something.
 *
 * It also declares and sets the render.
 */

class MyGlSurfaceView(context: Context) : GLSurfaceView(context) {

    internal var myRender: myRenderer
    private var mPreviousX: Float = 0.toFloat()
    private var mPreviousY: Float = 0.toFloat()

    init {
        // Create an OpenGL ES 3.0 context.
        setEGLContextClientVersion(3)

        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        // Set the Renderer for drawing on the GLSurfaceView
        myRender = myRenderer(context)
        setRenderer(myRender)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x = e.x
        val y = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                val dx = x - mPreviousX
                //subtract, so the cube moves the same direction as your finger.
                //with plus it moves the opposite direction.
                myRender.x = myRender.x - dx * TOUCH_SCALE_FACTOR

                val dy = y - mPreviousY
                myRender.y = myRender.y - dy * TOUCH_SCALE_FACTOR
            }
        }

        mPreviousX = x
        mPreviousY = y
        return true
    }

    companion object {


        //private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
        private val TOUCH_SCALE_FACTOR = 0.005f
    }

}
