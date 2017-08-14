package io.github.coffeegerm.materiallogbook.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by dyarz on 8/13/2017.
 * <p>
 * Custom TextView for AvenirRegular-Medium
 */

public class AvenirRegularMedium extends android.support.v7.widget.AppCompatTextView {
    public AvenirRegularMedium(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Medium.otf");
        this.setTypeface(face);
    }

    public AvenirRegularMedium(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Medium.otf");
        this.setTypeface(face);
    }

    public AvenirRegularMedium(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNext-Medium.otf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
