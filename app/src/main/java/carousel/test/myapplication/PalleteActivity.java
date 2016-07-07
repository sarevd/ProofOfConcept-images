package carousel.test.myapplication;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by nenad on 7/6/2016.
 */
public class PalleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pallete_layout);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.pallete);
        final View view = findViewById(R.id.view);
        final Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float eventX = event.getX();
                float eventY = event.getY();
                float[] eventXY = new float[] {eventX, eventY};
                Matrix invertMatrix = new Matrix();
                ((ImageView)v).getImageMatrix().invert(invertMatrix);

                invertMatrix.mapPoints(eventXY);
                int x = (int) eventXY[0];
                int y = (int) eventXY[1];
                //Limit x, y range within bitmap
                if(x < 0){
                    x = 0;
                }else if(x > bitmap.getWidth()-1){
                    x = bitmap.getWidth()-1;
                }

                if(y < 0){
                    y = 0;
                }else if(y > bitmap.getHeight()-1){
                    y = bitmap.getHeight()-1;
                }

                int pixel = bitmap.getPixel(x, y);

                view.setBackgroundColor(pixel);

                return false;
            }
        });

    }

}
