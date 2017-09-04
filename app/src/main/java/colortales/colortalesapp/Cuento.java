package colortales.colortalesapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Cuento extends AppCompatActivity {

    int imageNo[] = {R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4,
            R.drawable.c5, R.drawable.c6, R.drawable.c7, R.drawable.c8,
            R.drawable.c9, R.drawable.c10};

    int textNo[] = {R.string.cuento_texto_01, R.string.cuento_texto_02, R.string.cuento_texto_03,
            R.string.cuento_texto_04, R.string.cuento_texto_05, R.string.cuento_texto_06,
            R.string.cuento_texto_07, R.string.cuento_texto_08, R.string.cuento_texto_08, R.string.cuento_texto_fin};

    int countImage = imageNo.length;

    int currentImage = 0;

    ImageButton myButton2,myButton1;
    MediaPlayer fond;
    private int flag2;
    ImageButton btnSonido;

    float initialX;
    private Cursor cursor;
    private  int columnIndex, position = 0;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cuento);

        final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageChanger);

        final TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textChanger);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(Cuento.this);
                textView.setTextSize(getResources().getDimension(R.dimen.txt_lectura_cuenta));;
                textView.setGravity(Gravity.BOTTOM |Gravity.CENTER_HORIZONTAL);
                textView.setPadding(10,0,10,0);
                textView.setBackgroundColor(Color.parseColor("#AA000000"));
                return textView;
            }
        });

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);

        fond = MediaPlayer.create(this, R.raw.fondo);
        fond.setLooping(true);
        fond.start();


        myButton1 = (ImageButton) findViewById(R.id.btnAnt);
        myButton2 = (ImageButton) findViewById(R.id.btnSig);
        btnSonido = (ImageButton) findViewById(R.id.btnSound);


        myButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImage == 0){
                    startActivity(new Intent(Cuento.this, ListaCuentos.class)) ;
                }
                else {
                    currentImage--;
                    imageSwitcher.setImageResource(imageNo[currentImage]);
                    textSwitcher.setText(getString(textNo[currentImage]));
                }
            }
        });

        imageSwitcher.setImageResource(imageNo[currentImage]);
        textSwitcher.setText(getString(textNo[currentImage]));

        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImage == (countImage - 1)){

                    startActivity(new Intent(Cuento.this, Estrellas.class)) ;
                }
                else {
                    currentImage++;
                    imageSwitcher.setImageResource(imageNo[currentImage]);
                    textSwitcher.setText(getString(textNo[currentImage]));
                }

            }
        });

        btnSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2 == 0){
                    fond.pause();
                    btnSonido.setImageResource(R.drawable.sound);
                    flag2=1;
                }
                else {
                    fond.start();
                    btnSonido.setImageResource(R.drawable.mute);
                    flag2=0;
                }

            }
        });




        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //Oculta las barras de notificaciones y estado
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void onStop() {

        super.onStop();
        fond.stop();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageChanger);

        final TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textChanger);
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                if (initialX > finalX)
                {

                    if(currentImage == (countImage - 1)){

                        startActivity(new Intent(Cuento.this, Estrellas.class)) ;
                    }
                    else {
                        currentImage++;
                        imageSwitcher.setImageResource(imageNo[currentImage]);
                        textSwitcher.setText(getString(textNo[currentImage]));
                    }

                }
                if (initialX < finalX)
                {

                    if(currentImage == 0){
                        startActivity(new Intent(Cuento.this, ListaCuentos.class)) ;
                    }
                    else {
                        currentImage--;
                        imageSwitcher.setImageResource(imageNo[currentImage]);
                        textSwitcher.setText(getString(textNo[currentImage]));
                    }

                }
                break;
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


}
