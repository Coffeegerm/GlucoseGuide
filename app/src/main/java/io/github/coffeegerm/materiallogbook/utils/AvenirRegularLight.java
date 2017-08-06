package io.github.coffeegerm.materiallogbook.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by dyarz on 8/6/2017.
 * <p>
 * Custom TextView created to use AvenirNext-UltraLight textview
 */

public class AvenirRegularLight extends android.support.v7.widget.AppCompatTextView {
    public AvenirRegularLight(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-UltraLight.otf");
        this.setTypeface(face);
    }

    public AvenirRegularLight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-UltraLight.otf");
        this.setTypeface(face);
    }

    public AvenirRegularLight(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-UltraLight.otf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
