package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.drawing.DrawableBuffer;
import des.game.drawing.DrawableBufferedTexureCoords;
import des.game.scale.ContextParameters;
import des.game.scale.Game;
import des.game.scale.LevelSystem;
import des.game.scale.ScaleObjectFactory;


public class VoidGame extends Game {
	
	public VoidGame(){
		super();
	}
	@Override
	protected void extentionBootstrap() {
		ContextParameters params = mContextParameters;
        BaseObject.sSystemRegistry.drawableBuffer.add(new DrawableBuffer(BaseObject.sSystemRegistry.shortTermTextureLibrary.allocateTexture(R.drawable.sprite_sheet),-1,101, true));
        DrawableBufferedTexureCoords.loadTextureCoords(params.context.getResources().getXml(R.xml.sprite_sheet));
		
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

}
