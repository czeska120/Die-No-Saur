package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextOutline : AppCompatTextView {
    companion object {
        private const val DEFAULT_OUTLINE_SIZE = 0
        private val DEFAULT_OUTLINE_COLOR = Color.TRANSPARENT
    }

    private var mOutlineSize = DEFAULT_OUTLINE_SIZE
    private var mOutlineColor = DEFAULT_OUTLINE_COLOR
    private var mTextColor = currentTextColor
    private var mShadowRadius = 0f
    private var mShadowDx = 0f
    private var mShadowDy = 0f
    private var mShadowColor = Color.TRANSPARENT

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setAttributes(attrs)
    }

    private fun setAttributes(attrs: AttributeSet?) {
        attrs?.let {
            // set defaults
            mOutlineSize = DEFAULT_OUTLINE_SIZE
            mOutlineColor = DEFAULT_OUTLINE_COLOR
            // text color
            mTextColor = currentTextColor

            val typedArray: TypedArray =
                context.obtainStyledAttributes(it, R.styleable.TextOutline)

            // outline size
            if (typedArray.hasValue(R.styleable.TextOutline_outlineSize)) {
                mOutlineSize =
                    typedArray.getDimension(
                        R.styleable.TextOutline_outlineSize,
                        DEFAULT_OUTLINE_SIZE.toFloat()
                    ).toInt()
            }

            // outline color
            if (typedArray.hasValue(R.styleable.TextOutline_outlineColor)) {
                mOutlineColor =
                    typedArray.getColor(
                        R.styleable.TextOutline_outlineColor,
                        DEFAULT_OUTLINE_COLOR
                    )
            }

            // shadow (the reason we take shadow from attributes is because we use API level 15 and only from 16 we have the get methods for the shadow attributes)
            if (typedArray.hasValue(R.styleable.TextOutline_android_shadowRadius)
                || typedArray.hasValue(R.styleable.TextOutline_android_shadowDx)
                || typedArray.hasValue(R.styleable.TextOutline_android_shadowDy)
                || typedArray.hasValue(R.styleable.TextOutline_android_shadowColor)
            ) {
                mShadowRadius =
                    typedArray.getFloat(R.styleable.TextOutline_android_shadowRadius, 0f)
                mShadowDx =
                    typedArray.getFloat(R.styleable.TextOutline_android_shadowDx, 0f)
                mShadowDy =
                    typedArray.getFloat(R.styleable.TextOutline_android_shadowDy, 0f)
                mShadowColor =
                    typedArray.getColor(
                        R.styleable.TextOutline_android_shadowColor,
                        Color.TRANSPARENT
                    )
            }

            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setPaintToOutline()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun setPaintToOutline() {
        val paint: Paint = paint
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = mOutlineSize.toFloat()
        super.setTextColor(mOutlineColor)
        super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    }

    private fun setPaintToRegular() {
        val paint: Paint = paint
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f
        super.setTextColor(mTextColor)
        super.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        mTextColor = color
    }

    fun setOutlineSize(size: Int) {
        mOutlineSize = size
    }

    fun setOutlineColor(color: Int) {
        mOutlineColor = color
    }

    override fun onDraw(canvas: Canvas) {
        setPaintToOutline()
        super.onDraw(canvas)

        setPaintToRegular()
        super.onDraw(canvas)
    }
}