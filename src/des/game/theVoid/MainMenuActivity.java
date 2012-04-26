package des.game.theVoid;

import java.lang.reflect.InvocationTargetException;

import des.game.base.DebugLog;
import des.game.scale.UIConstants;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class MainMenuActivity extends Activity {
	private boolean mPaused;
    private View mQuickButton;
    private Animation mButtonFlickerAnimation;


    private boolean mJustCreated;
    
    
    private View.OnClickListener sQuickPlayButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mPaused) {
            	Intent i = new Intent(getBaseContext(), null);
                v.startAnimation(mButtonFlickerAnimation);
                mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
                mPaused = true;
            }
        }
    }; 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPaused = true;
        mJustCreated = true;
        DebugLog.e("menu", "create");
        mQuickButton = findViewById(R.id.startButton);

        
        mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);

        
        // Keep the volume control type consistent across all activities.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
        
        if (mQuickButton != null) {
        	mQuickButton.setOnClickListener(sQuickPlayButtonListener);
        }
        
        if (mJustCreated) {
        	if (mQuickButton != null) {
        		mQuickButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_slide));
            }
 
            mJustCreated = false;
        } else {
        	mQuickButton.clearAnimation();
        }
    }
	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
        private Intent mIntent;
        
        StartActivityAfterAnimation(Intent intent) {
            mIntent = intent;
        }
            

        public void onAnimationEnd(Animation animation) {
        	
            startActivity(mIntent);      
            
            if (UIConstants.mOverridePendingTransition != null) {
		       try {
		    	   UIConstants.mOverridePendingTransition.invoke(MainMenuActivity.this, R.anim.fade_in, R.anim.fade_out);
		       } catch (InvocationTargetException ite) {
		           DebugLog.d("Activity Transition", "Invocation Target Exception");
		       } catch (IllegalAccessException ie) {
		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
		       }
            }
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
        
    }
}