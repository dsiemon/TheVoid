package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.GameComponent;

public class StatusEffect extends GameComponent{

	public StatusEffect(){
		super();
		
		this.setPhase(GameComponent.ComponentPhases.STATUS_EFFECTS.ordinal());
	}
	
	@Override
	public void update(float timeDelta, BaseObject parent){
		
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
