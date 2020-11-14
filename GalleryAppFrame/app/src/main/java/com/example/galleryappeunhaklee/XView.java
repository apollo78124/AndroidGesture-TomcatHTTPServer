package com.example.galleryappeunhaklee;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

public class XView extends View implements View.OnClickListener{

    private Paint mPaint = new Paint();
    int oX = 200;
    int oY = 200;


    public XView(Context context, AttributeSet attrs){
        super(context,attrs);
        setOnClickListener(this);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.MAGENTA);

        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(oX, oY, 50, mPaint);

    }
    public void onClick(View view){
        oX=oX+10;
        view.invalidate();
    }
    public void setCoordinates(int x, int y) {
        this.oX = x;
        this.oY = y;
    }
    public boolean onKeyDown(int keuCode, KeyEvent event){
        oX=oX+10;
        this.invalidate();
        return true;
    }
}
