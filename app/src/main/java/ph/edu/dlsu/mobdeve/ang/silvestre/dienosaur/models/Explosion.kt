package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R

class Explosion(private val context: Context) {
    private val explosion: Array<Bitmap> = arrayOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.e_small_1),
        BitmapFactory.decodeResource(context.resources, R.drawable.e_small_2),
        BitmapFactory.decodeResource(context.resources, R.drawable.e_small_3),
        BitmapFactory.decodeResource(context.resources, R.drawable.e_small_4),
        BitmapFactory.decodeResource(context.resources, R.drawable.e_small_5),
        BitmapFactory.decodeResource(context.resources, R.drawable.e_small_6)
    )
    var explosionFrame: Int = 0
    var explosionX: Int = 0
    var explosionY: Int = 0

    fun getExplosion(frame: Int): Bitmap {
        return explosion[frame]
    }
}
