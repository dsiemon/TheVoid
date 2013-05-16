package des.game.theVoid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.ObjectManager;
import des.game.drawing.TextureLibrary;
import des.game.drawing.TiledVertexGrid;
import des.game.drawing.TiledWorld;
import des.game.scale.ContextParameters;
import des.game.scale.Level;
import des.game.scale.RenderComponent;
import des.game.scale.ScrollerComponent;

public class VoidLevelBuilder {
    public int mWidthInTiles; 
    public int mHeightInTiles;
    public int mTileWidth;
    public int mTileHeight;
    
    public GameObject mBackgroundObject;
    public GameObject mForegroundObject;
    
    public TiledWorld bottomMapTiles;	
    public TiledWorld topMapTiles;	
	public boolean loadLevel(Level level, ObjectManager<BaseObject> root, InputStream inputStream) throws IOException {
		DataInputStream stream = new DataInputStream(inputStream);
		ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
		final VoidObjectFactory factory = VoidObjectRegistry.gameObjectFactory;
		
		// read meta data
		mWidthInTiles = ReadInteger(stream);
		mHeightInTiles = ReadInteger(stream);
		mTileWidth = ReadInteger(stream);
		mTileHeight = ReadInteger(stream);
		
		// read background
		int backgroudId = ReadInteger(stream);
		float scrollX = ReadFloat(stream);
		float scrollY = ReadFloat(stream);
		float backgroundR = ReadFloat(stream);
		float backgroundG = ReadFloat(stream);
		float backgroundB = ReadFloat(stream);
		float backgroundA = ReadFloat(stream);
		
		mBackgroundObject = buildScrollImage(backgroudId, true, mTileWidth * mWidthInTiles, mTileHeight * mHeightInTiles, scrollX, scrollY,backgroundR, backgroundG, backgroundB, backgroundA);
		root.add(mBackgroundObject);
		
		// read foreground
		int foregroundId = ReadInteger(stream);
		float foregroundScrollX = ReadFloat(stream);
		float foregroundScrollY = ReadFloat(stream);
		float foregroundR = ReadFloat(stream);
		float foregroundG = ReadFloat(stream);
		float foregroundB = ReadFloat(stream);
		float foregroundA = ReadFloat(stream);
		
		mForegroundObject = buildScrollImage(foregroundId, false, mTileWidth * mWidthInTiles, mTileHeight * mHeightInTiles, foregroundScrollX, foregroundScrollY,foregroundR, foregroundG, foregroundB, foregroundA);
		root.add(mForegroundObject);
		
		// read in the bottom tile layer
		int bottomTileLayerId = ReadInteger(stream);
		float bottomTileColorR = ReadFloat(stream);
		float bottomTileColorG = ReadFloat(stream);
		float bottomTileColorB = ReadFloat(stream);
		float bottomTileColorA = ReadFloat(stream);
		
		bottomMapTiles = new TiledWorld(mWidthInTiles, mHeightInTiles);
		for(int i = 0; i < mWidthInTiles; i++){
			for(int j = 0; j < mHeightInTiles; j++){
				bottomMapTiles.setTile(i, j, ReadInteger(stream));
			}
		}

		bottomMapTiles.calculateSkips();
		
		addTileMapLayer(mBackgroundObject, SortConstants.BOTTOM_TILE_LAYER, 1.0f, mTileWidth * mWidthInTiles, mTileHeight * mHeightInTiles, 
					    mTileWidth, mTileHeight, bottomMapTiles, bottomTileLayerId, bottomTileColorR, bottomTileColorG, bottomTileColorB, bottomTileColorA);
		
		// read in the top tile layer
		int topTileLayerId = ReadInteger(stream);
		float topTileColorR = ReadFloat(stream);
		float topTileColorG = ReadFloat(stream);
		float topTileColorB = ReadFloat(stream);
		float topTileColorA = ReadFloat(stream);
		
		topMapTiles = new TiledWorld(mWidthInTiles, mHeightInTiles);
		for(int i = 0; i < mWidthInTiles; i++){
			for(int j = 0; j < mHeightInTiles; j++){
				topMapTiles.setTile(i, j, ReadInteger(stream));
			}
		}

		topMapTiles.calculateSkips();
		addTileMapLayer(mBackgroundObject, SortConstants.TOP_TILE_LAYER, 1.0f, params.gameWidth, params.gameHeight, 
					    mTileWidth, mTileHeight, topMapTiles, topTileLayerId, topTileColorR, topTileColorG, topTileColorB, topTileColorA);
		
		// read in boundary data
		int boundaryCount = ReadInteger(stream);
		
		for(int i = 0; i < boundaryCount; i++){
			int boundaryX = ReadInteger(stream);
			int boundaryY = ReadInteger(stream);
			
			int typeId = ReadInteger(stream);
			int colTypeId = ReadInteger(stream);
			
			if(typeId == 1){
				int radius = ReadInteger(stream);
				BaseObject.sSystemRegistry.gameObjectManager.add(factory.spawnInvisibleWall(boundaryX, boundaryY, radius));
			}
			else if(typeId == 0){
				int width = ReadInteger(stream);
				int height = ReadInteger(stream);
				BaseObject.sSystemRegistry.gameObjectManager.add(factory.spawnInvisibleWall(boundaryX, boundaryY, width, height));
			}
			else if(typeId == 2){
				int pointCount = ReadInteger(stream);
				
				int p1x = ReadInteger(stream);
				int p1y = ReadInteger(stream);
				int p2x = ReadInteger(stream);
				int p2y = ReadInteger(stream);
				int p3x = ReadInteger(stream);
				int p3y = ReadInteger(stream);
				int p4x; 
				int p4y;
				int p5x;
				int p5y;
				if(pointCount > 3){
					p4x = ReadInteger(stream);
					p4y = ReadInteger(stream);
				}
				if(pointCount > 4){
					p5x = ReadInteger(stream);
					p5y = ReadInteger(stream);
				}
				
				// polygons not supported yet.
			}
		}
		return true;
	}
    public GameObject buildScrollImage(int imageId, boolean isBackground, int levelWidth, int levelHeight, float scrollX, float scrollY, float r, float g, float b, float a) {
        // Generate the scrolling background.
        TextureLibrary textureLibrary = BaseObject.sSystemRegistry.shortTermTextureLibrary;
        GameObject background = new GameObject();

        if (textureLibrary != null) {
            
            int imageResource = -1;
            
            if(isBackground){
                switch (imageId) {
                case 0:
                	imageResource = R.drawable.background0;
                	break;
                case 1:
                	imageResource = R.drawable.background0;
                    break;
                default:
                    assert false;
                }
            }
            else{
                switch (imageId) {
                case 0:
                	imageResource = R.drawable.foreground0;
                	break;
                case 1:
                	imageResource = R.drawable.foreground1;
                    break;
                default:
                    assert false;
                }
            }

            
            if (imageResource > -1) {
                
                // Background Layer //
                RenderComponent backgroundRender = new RenderComponent();
                if(isBackground){
                	backgroundRender.setPriority(SortConstants.BACKGROUND_START);
                }
                else{
                	backgroundRender.setPriority(SortConstants.FOREGROUND_END);
                }
                ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
                // The background image is ideally 1.5 times the size of the largest screen axis
                // (normally the width, but just in case, let's calculate it).
                final int idealSize = (int)Math.max(params.gameWidth * 1.5f, params.gameHeight * 1.5f);
                int width = idealSize;
                int height = idealSize;
                 
                ScrollerComponent scroller3 = 
                        new ScrollerComponent(0.0f, 0.0f, width, height, 
                            textureLibrary.allocateTexture(imageResource));
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
            int theme, float r, float g, float b, float a) {
        
        int tileMapIndex = 0;
        switch(theme) {
            case 0:
                tileMapIndex = R.drawable.theme0;
                break;
            case 1:
                tileMapIndex = R.drawable.theme1;
                break;            
            case 2:
                    tileMapIndex = R.drawable.theme2;
                    break;
            default:
                assert false;
        }
        
        RenderComponent backgroundRender = new RenderComponent();
        backgroundRender.setPriority(priority);
        
        //Vertex Buffer Code
        TextureLibrary textureLibrary = BaseObject.sSystemRegistry.shortTermTextureLibrary;
        TiledVertexGrid bg = new TiledVertexGrid(textureLibrary.allocateTexture(tileMapIndex), 
                width, height, tileWidth, tileHeight);
        bg.setWorld(world);
        bg.setColor(r, g, b, a);
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
	private static int ReadInteger(DataInputStream stream) throws IOException{
		return stream.readInt();
	}
	private static float ReadFloat(DataInputStream stream) throws IOException{
		return stream.readFloat();
	}
}
