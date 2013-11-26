package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameComponent;
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.scale.GameObjectManager;
import des.game.scale.TimeSystem;

public class LaunchProjectileComponent extends GameComponent{
    private VoidObjectFactory.GameObjectType mObjectTypeToSpawn;
    private float mOffset;
    private GameObject.ActionType mRequiredAction;
    private float mDelayBetweenShots;
    private int mProjectilesInSet;
    private float mDelayBetweenSets;
    private int mSetsPerActivation;
    private float mDelayBeforeFirstSet;
    
    private float mLastProjectileTime;
    private float mSetStartedTime;
    private int mLaunchedCount;
    private int mSetCount;
    
    private boolean mTrackProjectiles;
    private int mMaxTrackedProjectiles;
    private int mTrackedProjectileCount;
    
    
	public LaunchProjectileComponent(){
		super();
		reset();

		setPhase(ComponentPhases.THINK.ordinal());
	}
	@Override
	public void reset(){
        mRequiredAction = GameObject.ActionType.INVALID;
        mObjectTypeToSpawn = VoidObjectFactory.GameObjectType.INVALID;
        mOffset = 0.0f;


        mDelayBetweenShots = 0.0f;
        mProjectilesInSet = 0;
        mDelayBetweenSets = 0.0f;
        mLastProjectileTime = 0.0f;
        mSetStartedTime = -1.0f;
        mLaunchedCount = 0;
        mSetCount = 0;
        mSetsPerActivation = -1;
        mProjectilesInSet = 0;
        mDelayBeforeFirstSet = 0.0f;
        mTrackProjectiles = false;
        mMaxTrackedProjectiles = 0;
        mTrackedProjectileCount = 0;

	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
        GameObject parentObject = (GameObject) parent;
        
        final TimeSystem time = sSystemRegistry.timeSystem;
        final float gameTime = time.getGameTime();

            if (parentObject.getCurrentAction() == mRequiredAction.index() 
                    || mRequiredAction == GameObject.ActionType.INVALID) {

                if (mSetStartedTime == -1.0f) {
                    mLaunchedCount = 0;
                    mLastProjectileTime = 0.0f;
                    mSetStartedTime = gameTime;
                }
                
                final float setDelay = mSetCount > 0 ? mDelayBetweenSets : mDelayBeforeFirstSet;
                
                //DebugLog.i("mob", "gameTime: " + gameTime + " mSetStartedTime:" + mSetStartedTime +  " setDelay " + setDelay + " setCount: " + mSetCount + " sets per activ: " + mSetsPerActivation);

                
                if (gameTime - mSetStartedTime >= setDelay && 
                        (mSetCount < mSetsPerActivation || mSetsPerActivation == -1)) {
                	//DebugLog.i("mob", "Ready to launch");
                    // We can start shooting.
                    final float timeSinceLastShot = gameTime - mLastProjectileTime;
                    
                    if (timeSinceLastShot >= mDelayBetweenShots) {
                   
                        launch(parentObject);
                        mLastProjectileTime = gameTime;
                        
                        if (mLaunchedCount >= mProjectilesInSet && mProjectilesInSet > 0) {
                            mSetStartedTime = -1.0f;
                            mSetCount++;
                        }
                    }
                }
            } else {
                // Force the timer to start counting when the right action is activated.
                mSetStartedTime = -1.0f;
                mSetCount = 0;
            }
        
	}
	
    private void launch(GameObject parentObject) {
        mLaunchedCount++;
        VoidObjectFactory factory = VoidObjectRegistry.gameObjectFactory;
        GameObjectManager manager = sSystemRegistry.gameObjectManager;
        if (factory != null && manager != null) {
            float offsetX = mOffset;
            float offsetY = mOffset;
            float orientation = 0f;
            int dir = parentObject.getCurrentDirection();
            if (dir == 0) {
                offsetX = 0f;
                offsetY = mOffset;
                orientation = (float)Math.PI/2;
            }
            else if(dir == 1){
                offsetX = -mOffset;
                offsetY = 0f;
                orientation = (float)Math.PI;
            }
            else if(dir == 2){
                offsetX = 0f;
                offsetY = -mOffset;
                orientation = (float)((Math.PI*3)/2);
            }
            else if(dir == 3){
                offsetX = mOffset;
                offsetY = 0f;
                orientation = 0f;
            }
            
            final float x = parentObject.mPosition.x + offsetX;
            final float y = parentObject.mPosition.y + offsetY;
            
            GameObject object = factory.spawn(mObjectTypeToSpawn, x, y, orientation);
            if (object != null) {
    	    	if(object.physcisObject.getVector().getVelocityMagnitude() < 10){
    	    		DebugLog.i("mob", "Created projectile moving slow. orientation: " + orientation + " dir: " + dir);
    	    	}
	            manager.add(object);
            }
        }
        
        
    }

    public final void setObjectTypeToSpawn(VoidObjectFactory.GameObjectType objectTypeToSpawn) {
        mObjectTypeToSpawn = objectTypeToSpawn;
    }

    public final void setOffset(float offset) {
        mOffset = offset;
    }


    public final void setRequiredAction(GameObject.ActionType requiredAction) {
        mRequiredAction = requiredAction;
    }

    public final void setDelayBetweenShots(float launchDelay) {
        mDelayBetweenShots = launchDelay;
    }
    
    public final void setDelayBetweenSets(float delayBetweenSets) {
        mDelayBetweenSets = delayBetweenSets;
    }
    
    public final void setDelayBeforeFirstSet(float delayBeforeFirstSet) {
        mDelayBeforeFirstSet = delayBeforeFirstSet;
    }

    public final void setShotsPerSet(int shotCount) {
        mProjectilesInSet = shotCount;
    }
    
    public final void setSetsPerActivation(int setCount) {
        mSetsPerActivation = setCount;
    }
    
    public final void enableProjectileTracking(int max) {
        mMaxTrackedProjectiles = max;
        mTrackProjectiles = true;
    }
    
    public final void disableProjectileTracking() {
        mMaxTrackedProjectiles = 0;
        mTrackProjectiles = false;
    }
    
    public final void trackedProjectileDestroyed() {
        assert mTrackProjectiles;
        if (mTrackedProjectileCount == mMaxTrackedProjectiles) {
            // Let's restart the set.
            mSetStartedTime = -1.0f;
            mSetCount = 0;
        }
        mTrackedProjectileCount--;
    }
    
}
