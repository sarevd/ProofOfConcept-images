package carousel.test.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This activity displays an image on the screen.
 * The image has three different regions that can be clicked / touched.
 * When a region is touched, the activity changes the view to show a different
 * image.
 */

public class ImageAreasActivity extends Activity
        implements View.OnTouchListener {

    /**
     * Create the view for the activity.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageView iv = (ImageView) findViewById(R.id.top);
        if (iv != null) {
            iv.setOnTouchListener(this);
        }

        //  toast("Touch the screen to discover where the regions are.");
    }

    /**
     * Respond to the user touching the screen.
     * Change images to make things appear and disappear from the screen.
     */

    public boolean onTouch(View v, MotionEvent ev) {
        boolean handledHere = false;


        final int action = ev.getAction();

        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        int nextImage = -1;            // resource id of the next image to display

        // If we cannot find the imageView, return.
        ImageView imageView = (ImageView) v.findViewById(R.id.top);
        ImageView imageView1 = (ImageView) v.findViewById(R.id.layer1);
        ImageView imageView2 = (ImageView) v.findViewById(R.id.layer2);
        ImageView imageView3 = (ImageView) v.findViewById(R.id.layer3);

        if (imageView == null) return false;


        int touchColor = Color.WHITE;
        int touchColor1 = getHotspotColor(R.id.layer1, evX, evY);
        int touchColor2 = getHotspotColor(R.id.layer2, evX, evY);
        int touchColor3 = getHotspotColor(R.id.layer3, evX, evY);
        if (Color.alpha(touchColor1) > 0 || (touchColor1 != 0 && touchColor1 != Color.WHITE)) {
            touchColor = touchColor1;
        } else if (Color.alpha(touchColor2) > 0 || (touchColor2 != 0 && touchColor2 != Color.WHITE)) {
            touchColor = touchColor2;
        } else if (Color.alpha(touchColor3) > 0 || (touchColor3 != 0 && touchColor3 != Color.WHITE)) {
            touchColor = touchColor3;
        }

        // On the UP, we do the click action.
        // The hidden image (image_areas) has three different hotspots on it.
        // The colors are red, blue, and yellow.
        // Use image_areas to determine which region the user touched.
        ColorTool ct = new ColorTool();
        int tolerance = 0;
        // When the action is Down, see if we should show the "pressed" image for the default image.
        // We do this when the default image is showing. That condition is detectable by looking at the
        // tag of the view. If it is null or contains the resource number of the default image, display the pressed image.
        Integer tagNum = (Integer) imageView.getTag();
        int currentResource = (tagNum == null) ? R.drawable.top : tagNum.intValue();

        // Now that we know the current resource being displayed we can handle the DOWN and UP events.

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (currentResource == R.drawable.top) {
                    nextImage = R.drawable.top;
                    handledHere = true;
       /*
       } else if (currentResource != R.drawable.p2_ship_default) {
         nextImage = R.drawable.p2_ship_default;
         handledHere = true;
       */
                } else handledHere = true;
                break;

            case MotionEvent.ACTION_UP:



                // Compare the touchColor to the expected values. Switch to a different image, depending on what color was touched.
                // Note that we use a Color Tool object to test whether the observed color is close enough to the real color to
                // count as a match. We do this because colors on the screen do not match the map exactly because of scaling and
                // varying pixel density.
                nextImage = R.drawable.top;
                if (ct.closeMatch(Color.RED, touchColor, tolerance))
                    imageView2 = (ImageView) v.findViewById(R.id.layer2);
                else if (ct.closeMatch(Color.BLUE, touchColor, tolerance))
                    imageView3 = (ImageView) v.findViewById(R.id.layer3);
                else if (ct.closeMatch(Color.YELLOW, touchColor, tolerance))
                    imageView1 = (ImageView) v.findViewById(R.id.layer1);
                else if (ct.closeMatch(Color.WHITE, touchColor, tolerance))
                    imageView = (ImageView) v.findViewById(R.id.top);

                // If the next image is the same as the last image, go back to the default.
                // toast ("Current image: " + currentResource + " next: " + nextImage);
                if (currentResource == nextImage) {
                    nextImage = R.drawable.layer2;
                }
                handledHere = true;

                break;

            default:
                handledHere = false;
        } // end switch



        if (handledHere && imageView != null) {
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            int transparency = ((bitmap.getPixel(evX,evY) & 0xff000000) >> 24);
            if (ct.closeMatch(Color.RED, touchColor, tolerance))
                imageView.setForeground(getDrawable(R.drawable.layer2));
            else if (ct.closeMatch(Color.BLUE, touchColor, tolerance))
                imageView.setForeground(getDrawable(R.drawable.layer3));
            else if (ct.closeMatch(Color.YELLOW, touchColor, tolerance))
                imageView.setForeground(getDrawable(R.drawable.layer1));
            else if (ct.closeMatch(Color.WHITE, touchColor, tolerance))
                imageView.setForeground(getDrawable(R.drawable.top));
//        imageView.setVisibility(View.VISIBLE);

//            if (nextImage > 0) {
//                imageView.setImageResource(R.drawable.layer3);
//
//                imageView.setTag(nextImage);
//            }
        }
        return handledHere;
    }

    /**
     * Resume the activity.
     */

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
     * Handle a click on the Wglxy views at the bottom.
     */
//
//    public void onClickWglxy(View v) {
//        Intent viewIntent = new Intent("android.intent.action.VIEW",
//                Uri.parse("http://double-star.appspot.com/blahti/ds-download.html"));
//        startActivity(viewIntent);
//
//    }
//

/**
 */
// More methods

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

    /**
     * Show a string on the screen via Toast.
     *
     * @param msg String
     * @return void
     */

//    public void toast(String msg) {
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//    } // end toast

} // end class
