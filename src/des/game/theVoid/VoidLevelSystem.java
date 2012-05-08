package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.ObjectManager;
import des.game.drawing.TextureLibrary;
import des.game.drawing.TiledVertexGrid;
import des.game.drawing.TiledWorld;
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
	    if (mBackgroundObject != null && mRoot != null) {
	        mBackgroundObject.removeAll();
	        mBackgroundObject.commitUpdates();
	        mRoot.remove(mBackgroundObject);
	        mBackgroundObject = null;
	        mRoot = null;
	    }

	    mAttempts = 0;
	    mCurrentLevel = null;
	}

	@Override
	public boolean loadLevel(Level level, ObjectManager<BaseObject> root) {
		DebugLog.i("Level", "load level");
        boolean success = true;
        
        
        mWidthInTiles = 100;
        mHeightInTiles = 100;
        mTileWidth = 32;
        mTileHeight = 32;
        mapTiles = new TiledWorld(mWidthInTiles, mHeightInTiles);
        mapTiles.setTileRange(4, 5, 60, 60, 0);
        mapTiles.calculateSkips();
        //mapTiles = mapInfo.world;
        
        if (mBackgroundObject == null) {
            mBackgroundObject = 
                this.buildBackground(
                		0, 
                		(int)this.getLevelWidth(),
                		(int)this.getLevelHeight(), .5f, .5f);
            root.add(mBackgroundObject);
        }
        
        this.addTileMapLayer(mBackgroundObject, -50 + 2, 
                1, 100, 100, 
                mTileWidth, mTileHeight, mapTiles, 0);
        
        if(BaseObject.sSystemRegistry.cameraTarget != null){
        	BaseObject.sSystemRegistry.gameObjectFactory.destroy(BaseObject.sSystemRegistry.cameraTarget);
        }
        
    	final GameObject target = VoidObjectRegistry.gameObjectFactory.spawnCameraTarget(this.getLevelWidth()/2, this.getLevelHeight()/2);
    	BaseObject.sSystemRegistry.cameraTarget = target;
    	BaseObject.sSystemRegistry.cameraSystem.setTarget(target);
    	BaseObject.sSystemRegistry.gameObjectManager.add(target);
    	
		return success;
	}
	
	

    public GameObject buildBackground(int backgroundImage, int levelWidth, int levelHeight, float scrollX, float scrollY) {
        // Generate the scrolling background.
        TextureLibrary textureLibrary = sSystemRegistry.shortTermTextureLibrary;
        
        GameObject background = new GameObject();

        if (textureLibrary != null) {
            
            int backgroundResource = -1;
            
            switch (backgroundImage) {
                case 0:
                    backgroundResource = R.drawable.background0;
                    break;
                default:
                    assert false;
            }
            
            if (backgroundResource > -1) {
                
                // Background Layer //
                RenderComponent backgroundRender = new RenderComponent();
                backgroundRender.setPriority(-100);
                
                ContextParameters params = sSystemRegistry.contextParameters;
                // The background image is ideally 1.5 times the size of the largest screen axis
                // (normally the width, but just in case, let's calculate it).
                final int idealSize = (int)Math.max(params.gameWidth * 1.5f, params.gameHeight * 1.5f);
                int width = idealSize;
                int height = idealSize;
                 
                ScrollerComponent scroller3 = 
                        new ScrollerComponent(0.0f, 0.0f, width, height, 
                            textureLibrary.allocateTexture(backgroundResource));
                scroller3.setRenderComponent(backgroundRender);
                
                // Scroll speeds such that the background will evenly match the beginning
                // and end of the level.  Don't allow speeds > 1.0, though; that would be faster than
                // the foreground, which is disorienting and looks like rotation.

                
                 
                scroller3.setScrollSpeed(scrollX, scrollY);
                
                backgroundRender.setCameraRelative(false);
                
                background.add(scroller3);
                background.add(backgroundRender);
            }
        }
        return background;
    }
    
    public void addTileMapLayer(GameObject background, int priority, float scrollSpeed, 
            int width, int height, int tileWidth, int tileHeight, TiledWorld world, 
            int theme) {
        
        int tileMapIndex = 0;
        switch(theme) {
            case 0:
                tileMapIndex = R.drawable.theme0;
                break;
 


            default:
                assert false;
        }
        
        RenderComponent backgroundRender = new RenderComponent();
        backgroundRender.setPriority(priority);
        
        //Vertex Buffer Code
        TextureLibrary textureLibrary = sSystemRegistry.shortTermTextureLibrary;
        TiledVertexGrid bg = new TiledVertexGrid(textureLibrary.allocateTexture(tileMapIndex), 
                width, height, tileWidth, tileHeight);
        bg.setWorld(world);
        bg.setColor(0f, 0f, 1f, .5f);
        //TODO: The map format should really just output independent speeds for x and y,
        // but as a short term solution we can assume parallax layers lock in the smaller
        // direction of movement.
        float xScrollSpeed = 1.0f;
        float yScrollSpeed = 1.0f;
        
        if (world.getWidth() > world.getHeight()) {
        	xScrollSpeed = scrollSpeed;
        } else {
        	yScrollSpeed = scrollSpeed;
        }
        
        ScrollerComponent scroller = new ScrollerComponent(xScrollSpeed, yScrollSpeed,
                width, height, bg);
        scroller.setRenderComponent(backgroundRender);

        background.add(scroller);
        background.add(backgroundRender);
        backgroundRender.setCameraRelative(false);
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
