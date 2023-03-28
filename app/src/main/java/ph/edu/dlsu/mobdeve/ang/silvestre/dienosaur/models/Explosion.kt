package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R

class Explosion(private val context: Context) {
    private val explosion: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_large)
    var explosionX: Int = 0
    var explosionY: Int = 0

    fun getExplosion(): Bitmap {
        return explosion
    }
}
