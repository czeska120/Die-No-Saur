package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R

class Heart(private val context: Context) {
    private val heart: Array<Bitmap> = arrayOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.heart_1),
        BitmapFactory.decodeResource(context.resources, R.drawable.heart_2),
        BitmapFactory.decodeResource(context.resources, R.drawable.heart_3),
        BitmapFactory.decodeResource(context.resources, R.drawable.heart_4),
        BitmapFactory.decodeResource(context.resources, R.drawable.heart_5)
    )
    var heartFrame: Int = 0

    fun getHeart(frame: Int): Bitmap {
        return heart[frame]
    }
}