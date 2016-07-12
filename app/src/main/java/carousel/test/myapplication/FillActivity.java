package carousel.test.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by nenad on 7/8/2016.
 */
public class FillActivity extends AppCompatActivity {

    public static final String IMG = null;
    Context con;
    public Bitmap currentbmp;
    private String imgfile;
    public int replacecolor;
    public ImageView showcolor;

    public FillActivity() {
        replacecolor = 0xffff0000;
        imgfile = null;
        currentbmp = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floodfill);
        ImageView imageView = (ImageView) findViewById(R.id.floodfill);
        ImageView imageView1 = (ImageView) findViewById(R.id.colorpal);
        showcolor = (ImageView) findViewById(R.id.showcolor);
        showcolor.setBackgroundColor(replacecolor);

        try {
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.top));
            imageView1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallete));
            imageView1.setOnTouchListener(new View.OnTouchListener() {
                Bitmap pmap;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try
                    {
                        Point point = new Point();
                        point.x = (int)motionEvent.getX();
                        point.y = (int)motionEvent.getY();
                        ImageView imageview2 = (ImageView)findViewById(R.id.colorpal);
                        imageview2.buildDrawingCache();
                        pmap = imageview2.getDrawingCache();
                        replacecolor = pmap.getPixel(point.x, point.y);
                        showcolor.setBackgroundColor(replacecolor);
                    }
                    catch (Exception exception) { }
                    return true;
                }
            });
        } catch (Exception exception) { }

        imageView.setOnTouchListener(new View.OnTouchListener() {
            Bitmap bmap;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    Point point = new Point();
                    point.x = (int)motionEvent.getX();
                    point.y = (int)motionEvent.getY();
                    ImageView imageview2 = (ImageView)findViewById(R.id.floodfill);
                    if (bmap == null)
                    {
                        imageview2.buildDrawingCache();
                        bmap = imageview2.getDrawingCache();
                    }
                    int i = bmap.getPixel(point.x, point.y);
                    int j = replacecolor;
                    (new TheTask(bmap, point, i, j, imageview2)).execute(new Void[0]);

                }
                return true;
            }
        });
    }
}
