package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.ObjectManager;
import des.game.drawing.DrawableBitmap;
import des.game.drawing.DrawableBuffer;
import des.game.drawing.DrawableBufferedTexureCoords;
import des.game.drawing.Texture;
import des.game.drawing.TextureLibrary;
import des.game.scale.ContextParameters;
import des.game.scale.Game;
import des.game.scale.LevelSystem;
import des.game.scale.ScaleActivity;
import des.game.scale.ScaleObjectFactory;



public class VoidGame extends Game {
	
	public VoidGame(){
		super();
	}
	@Override
	protected void extentionBootstrap() {
        // Short-term textures are cleared between levels.
        TextureLibrary shortTermTextureLibrary = BaseObject.sSystemRegistry.shortTermTextureLibrary;

        // Long-term textures persist between levels.
        TextureLibrary longTermTextureLibrary = BaseObject.sSystemRegistry.longTermTextureLibrary;
        
        ObjectManager<BaseObject> gameRoot = this.mGameRoot;
        
		ContextParameters params = mContextParameters;
        BaseObject.sSystemRegistry.drawableBuffer.add(new DrawableBuffer(BaseObject.sSystemRegistry.shortTermTextureLibrary.allocateTexture(R.drawable.sprite_sheet),-1,101, true));
        DrawableBufferedTexureCoords.loadTextureCoords(params.context.getResources().getXml(R.xml.sprite_sheet));
        
        UISystem uiSystem = new UISystem();
        VoidObjectRegistry.uiSystem = uiSystem;
        BaseObject.sSystemRegistry.registerForReset(uiSystem);
        
        
        Texture[] digitTextures = {
        longTermTextureLibrary.allocateTexture(R.drawable.ui_0),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_1),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_2),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_3),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_4),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_5),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_6),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_7),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_8),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_9)};
        
        DrawableBitmap[] digits = {
        new DrawableBitmap(digitTextures[0], 0, 0),
        new DrawableBitmap(digitTextures[1], 0, 0),
        new DrawableBitmap(digitTextures[2], 0, 0),
        new DrawableBitmap(digitTextures[3], 0, 0),
        new DrawableBitmap(digitTextures[4], 0, 0),
        new DrawableBitmap(digitTextures[5], 0, 0),
        new DrawableBitmap(digitTextures[6], 0, 0),
        new DrawableBitmap(digitTextures[7], 0, 0),
        new DrawableBitmap(digitTextures[8], 0, 0),
        new DrawableBitmap(digitTextures[9], 0, 0)};
        
        uiSystem.setDigitDrawables(digits);
        
        uiSystem.setMoveButtonDrawable(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.idle_button), 0, 0), new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.pressed_button), 0, 0), new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.indicator), 0, 0));
        uiSystem.setAttackButtonDrawable(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.small_idle_button), 0, 0), new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.small_pressed_button), 0, 0));
                
        if (ScaleActivity.VERSION < 0) {
        	uiSystem.setShowFPS(true);
        }
        gameRoot.add(uiSystem);
		
	}

	@Override
	protected LevelSystem createLevelSystem() {
		return new VoidLevelSystem();
	}

	@Override
	protected ScaleObjectFactory createObjectFactory() {
		VoidObjectFactory objectFactory = new VoidObjectFactory();
		VoidObjectRegistry.gameObjectFactory = objectFactory;
		
		return objectFactory;
	}
	@Override
	protected boolean[][] setupCollisionMatrix() {
		boolean[][] matrix = new boolean [VoidObjectRegistry.PhysicsObjectTypes.COUNT][];
		
		matrix[VoidObjectRegistry.PhysicsObjectTypes.PASSIVE_TYPE]           = new boolean[] {false, false, false, true , false, true , false, false};
		matrix[VoidObjectRegistry.PhysicsObjectTypes.INVISIBLE_WALL]         = new boolean[] {false, false, false, true , true , true , true , false};
		matrix[VoidObjectRegistry.PhysicsObjectTypes.LOW_WALL]               = new boolean[] {false, false, false, true , false, true , false, false};
		matrix[VoidObjectRegistry.PhysicsObjectTypes.MOB1]                   = new boolean[] {true , true , true , false, false, true , true , false};
		matrix[VoidObjectRegistry.PhysicsObjectTypes.MOB1_PROJECTILE]        = new boolean[] {false, true , false, false, false, true , true , false};
		matrix[VoidObjectRegistry.PhysicsObjectTypes.MOB2]                   = new boolean[] {true , true , true , true , true , false, false, false};
		matrix[VoidObjectRegistry.PhysicsObjectTypes.MOB2_PROJECTILE]        = new boolean[] {false, true , false, true , true , false, false, false};
		matrix[VoidObjectRegistry.PhysicsObjectTypes.SPAWNER]                = new boolean[] {false, false, false, false, false, false, false, false};

		return matrix;
	}

}
