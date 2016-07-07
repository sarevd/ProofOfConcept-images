package carousel.test.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * This activity displays an image on the screen.
 * The image has three different regions that can be clicked / touched.
 * When a region is touched, the activity changes the view to show a different
 * image.
 */

public class ImageAreasActivity extends Activity implements View.OnTouchListener {

    private HashMap<Integer, Integer> colorLayerMap = new HashMap<Integer, Integer>();
    private int changeFromColor = 0;
    private int changeToColor = 0;
    private ImageView topImageView;
    private ImageView paletteImageView;

    /**
     * Create the view for the activity.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        colorLayerMap.put(R.id.layer1, Color.YELLOW);
        colorLayerMap.put(R.id.layer2, Color.RED);
        colorLayerMap.put(R.id.layer3, Color.BLUE);

        topImageView = (ImageView) findViewById(R.id.top);
        topImageView.setOnTouchListener(this);

        paletteImageView = (ImageView) findViewById(R.id.palette);
        paletteImageView.setOnTouchListener(this);

        //  toast("Touch the screen to discover where the regions are.");
    }

    /**
     * Respond to the user touching the screen.
     * Change images to make things appear and disappear from the screen.
     */

    public boolean onTouch(View v, MotionEvent ev) {
        boolean handledHere = false;
        final int action = ev.getAction();


        switch (action) {
            case MotionEvent.ACTION_DOWN:

                final int evX = (int) ev.getX();
                final int evY = (int) ev.getY();

                ImageView imageView = null;
                int touchColor = Color.WHITE;

                if (v.getId() == paletteImageView.getId()) {
                    changeToColor = getHotspotColor(R.id.palette, evX, evY);
                    Log.v("test", "we hit palette color " + changeToColor);
                    if (changeToColor == Color.WHITE || changeToColor == Color.BLACK) {
                        changeToColor = 0; // click outside the color circles
                    }
                }

                if (getHotspotColor(R.id.layer1, evX, evY) != 0) {
                    touchColor = getHotspotColor(R.id.layer1, evX, evY);
                } else if (getHotspotColor(R.id.layer2, evX, evY) != 0) {
                    touchColor = getHotspotColor(R.id.layer2, evX, evY);
                } else if (getHotspotColor(R.id.layer3, evX, evY) != 0) {
                    touchColor = getHotspotColor(R.id.layer3, evX, evY);
                }

                ColorTool ct = new ColorTool();
                int tolerance = 0;

                if (ct.closeMatch(colorLayerMap.get(R.id.layer1), touchColor, tolerance)) {

                    imageView = (ImageView) ((FrameLayout) v.getParent()).findViewById(R.id.layer1);
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    if (changeToColor != 0) {
                        imageView.setImageBitmap(changeToColor(bitmap, touchColor, R.id.layer1));
                        changeToColor = 0;
                    } else {
                        imageView.setImageBitmap(bitmap);
                    }

                    topImageView.setForeground(imageView.getDrawable());

                } else if (ct.closeMatch(colorLayerMap.get(R.id.layer2), touchColor, tolerance)) {

                    imageView = (ImageView) ((FrameLayout) v.getParent()).findViewById(R.id.layer2);
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    if (changeToColor != 0) {
                        imageView.setImageBitmap(changeToColor(bitmap, touchColor, R.id.layer2));
                        changeToColor = 0;
                    } else {
                        imageView.setImageBitmap(bitmap);
                    }

                    topImageView.setForeground(imageView.getDrawable());

                } else if (ct.closeMatch(colorLayerMap.get(R.id.layer3), touchColor, tolerance)) {

                    imageView = (ImageView) ((FrameLayout) v.getParent()).findViewById(R.id.layer3);
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    if (changeToColor != 0) {
                        imageView.setImageBitmap(changeToColor(bitmap, touchColor, R.id.layer3));
                        changeToColor = 0;
                    } else {
                        imageView.setImageBitmap(bitmap);
                    }

                    topImageView.setForeground(imageView.getDrawable());

                } else {
                    topImageView.setForeground(getDrawable(R.drawable.top));
                }

                handledHere = true;

                break;

            default:
                handledHere = false;
        } // end switch

        return handledHere;
    }

    @Override
    protected void onResume() {
        super.onResume();

        View v = findViewById(R.id.wglxy_bar);
        if (v != null) {
            Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            //anim1.setAnimationListener (new StartActivityAfterAnimation (i));
            v.startAnimation(anim1);
        }
    }

    /**
     * Get the color from the hotspot image at point x-y.
     */

    public int getHotspotColor(int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById(hotspotId);
        if (img == null) {
            Log.d("ImageAreasActivity", "Hot spot image not found");
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }

    private Bitmap changeToColor(Bitmap sourceBitmap, int srcColor, int resourceLayoutId) {

        int bmpWidth = sourceBitmap.getWidth() - 1;
        int bmpHeight = sourceBitmap.getHeight() - 1;

        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);


        int [] allPixels = new int[sourceBitmap.getHeight() * sourceBitmap.getWidth()];

        sourceBitmap.getPixels(allPixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);

        changeFromColor = colorLayerMap.get(resourceLayoutId);

        for (int i = 0; i < allPixels.length; i++) {
            if (allPixels[i] == srcColor) {
                allPixels[i] = changeToColor;
            }
        }

        colorLayerMap.put(resourceLayoutId, changeToColor);


        resultBitmap.setPixels(allPixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
        return resultBitmap;
    }

} // end class
