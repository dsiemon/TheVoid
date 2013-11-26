package des.game.theVoid;

import java.io.IOException;
import java.io.InputStream;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.ObjectManager;
import des.game.drawing.TextureLibrary;
import des.game.drawing.TiledVertexGrid;
import des.game.drawing.TiledWorld;
import des.game.physics.PhysicsObjectSet;
import des.game.scale.ContextParameters;
import des.game.scale.Level;
import des.game.scale.LevelSystem;
import des.game.scale.RenderComponent;
import des.game.scale.ScrollerComponent;


public class VoidLevelSystem extends LevelSystem {

	
    public int mWidthInTiles; 
    public int mHeightInTiles;
    public int mTileWidth;
    public int mTileHeight;
    public GameObject mBackgroundObject;
    public GameObject mForegroundObject;
    public ObjectManager<BaseObject> mRoot;

    private int mAttempts;
    private String mCurrentLevel;
public TiledWorld mapTiles;	
	public VoidLevelSystem() {
	    super(); 

	    reset();
	}
	
	@Override
	public void reset() {
		DebugLog.i("Level", "reset level system");
	    if (mBackgroundObject != null && mRoot != null) {
	        mBackgroundObject.removeAll();
	        mBackgroundObject.commitUpdates();
	        mForegroundObject.removeAll();
	        mForegroundObject.commitUpdates();
	        mRoot.remove(mForegroundObject);
	        mRoot.remove(mBackgroundObject);
	        mBackgroundObject = null;
	        mForegroundObject = null;
	        mRoot = null; 
	    }

	    mAttempts = 0;
	    mCurrentLevel = null;
	}

	@Override
	public boolean loadLevel(Level level, ObjectManager<BaseObject> root) {
		DebugLog.i("Level", "load level");
        boolean success = true; 
        PhysicsObjectSet.instance.clear();
        ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
		InputStream stream = params.context.getResources().openRawResource(R.raw.test1);
		
		VoidLevelBuilder builder = new VoidLevelBuilder();
		
		try {
			builder.loadLevel(level, root, stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        mWidthInTiles = builder.mWidthInTiles;
        mHeightInTiles = builder.mHeightInTiles;
        mTileWidth = builder.mTileWidth;
        mTileHeight = builder.mTileHeight;
        this.mBackgroundObject = builder.mBackgroundObject;
        this.mForegroundObject = builder.mForegroundObject;


    	
    	final VoidObjectFactory factory = VoidObjectRegistry.gameObjectFactory;
/*////////////////////////////////////////////////////////////////////////////////*/
    	for(int i = 0; i < 4; i++){
    		for(int j = 0; j < 3; j++){
    			int orientation = 1;
    			if(j == 1){
    				orientation = -1;
    			}
    			BaseObject.sSystemRegistry.gameObjectManager.add(factory.spawnBall(500 + i*38, 500 + j*38, orientation, 300f));
        	}
    	}
    	for(int i = 0; i < 7; i++){
    		for(int j = 0; j < 7; j++){

    			//BaseObject.sSystemRegistry.gameObjectManager.add(factory.spawnOctorok(100 + i*70, 100 + j*70));
        	}
    	}
    	for(int i = 0; i < 1; i++){
    		for(int j = 0; j < 1; j++){

    			//BaseObject.sSystemRegistry.gameObjectManager.add(factory.spawnPigMan(620 + i*70, 100 + j*70));
        	}
    	}
    	GameObject player = factory.spawnPlayer(300, 300, 0, 200);
    	BaseObject.sSystemRegistry.gameObjectManager.add(player);
    	BaseObject.sSystemRegistry.cameraTarget = player;
    	BaseObject.sSystemRegistry.cameraSystem.setTarget(player);
    	
    	
    	BaseObject.sSystemRegistry.gameObjectManager.add(factory.spawnSpawner(64, 1472, VoidObjectFactory.GameObjectType.PIGMAN, 30f, 3f, false, false, false, true, false));
    	BaseObject.sSystemRegistry.gameObjectManager.add(factory.spawnSpawner(1504, 1472, VoidObjectFactory.GameObjectType.OCTOROK, 30f, 3f, false, true, false, false, false));
/*////////////////////////////////////////////////////////////////////////////////*/
        /*if(BaseObject.sSystemRegistry.cameraTarget != null){
        	BaseObject.sSystemRegistry.gameObjectFactory.destroy(BaseObject.sSystemRegistry.cameraTarget);
        }
        
    	final GameObject target = VoidObjectRegistry.gameObjectFactory.spawnCameraTarget(100, 100);
    	BaseObject.sSystemRegistry.cameraTarget = target;
    	BaseObject.sSystemRegistry.cameraSystem.setTarget(target);
    	BaseObject.sSystemRegistry.gameObjectManager.add(target);*/
		return success;
	}
	
    @Override
    public float getLevelWidth() {
        return mWidthInTiles * mTileWidth;
    }
    @Override
    public float getLevelHeight() {
        return mHeightInTiles * mTileHeight;
    }

    @Override
    public void incrementAttemptsCount() {
        mAttempts++;
    }
    @Override
    public int getAttemptsCount() {
        return mAttempts;
    }
    @Override
    public String getCurrentLevel() {
    	return mCurrentLevel;
    }

	@Override
	public Level parseLevelId(String levelId) {
		return new Level(0,"");
	}

	@Override
	public int getTileWidth() {
		return this.mTileWidth;
	}

	@Override
	public int getTileHeight() {
		return this.mTileHeight;
	}

	@Override
	public int getWidthInTiles() {
		return this.mWidthInTiles;
	}

	@Override
	public int getHeightInTiles() {
		return this.mHeightInTiles;
	}

	@Override
	public TiledWorld getMapTiles() {
		return this.mapTiles;
	}


}
