package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.GameComponent;
import des.game.base.GameObject;
import des.game.scale.SpriteComponent;


public class BasicColorComponent extends GameComponent{
	
	SpriteComponent sprite;
	
	public BasicColorComponent(){
		super();
		
	
		this.reset();
		this.setPhase(GameComponent.ComponentPhases.COLOR.ordinal());
	}
	
	@Override
	public void reset() {
		sprite = null;
	}
	
	@Override
	public void update(float timeDelta, BaseObject parent){
		final GameObject gameObject = (GameObject)parent;
		
		if(gameObject.getCurrentAction() == 3){
			sprite.setColor(1.0f, 0f, 0f, 1.0f);
		}
		else {
			sprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	

}
