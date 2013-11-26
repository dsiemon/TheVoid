package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameComponent;
import des.game.base.GameObject;

public class HealthModifier extends GameComponent{
	float magnitude;
	int type;
	int pierce;
	float duration;
	boolean overTime;
	
	public HealthModifier(){
		super();
		
		this.setPhase(GameComponent.ComponentPhases.CALCULATE_DAMAGE.ordinal());
		this.reset();
	}
	
	@Override
	public void reset() {
		this.magnitude = 0;
		this.type = 0;
		this.pierce = 0;
		this.duration = 0;
		this.overTime = false;
	}
	
	public void setupModifierOverTime(float mag, int type, int pierce, float duration){
		this.magnitude = mag;
		this.type = type;
		this.pierce = pierce;
		this.duration = duration;
		this.overTime = true;
	}
	
	public void setupModifier(float mag, int type, int pierce){
		this.magnitude = mag;
		this.type = type;
		this.pierce = pierce;
		this.overTime = false;
	}
	
	@Override
	public void update(float timeDelta, BaseObject parent){
		final GameObject gameObject = (GameObject)parent;
		
		float tmpMagnitude = magnitude;
		duration -= timeDelta;
		
		if(overTime){
			if(duration < 0) tmpMagnitude = magnitude * (timeDelta + duration);
			else tmpMagnitude = magnitude * timeDelta;
		}

		if(magnitude > 0)
			gameObject.attributes.damage(tmpMagnitude, type, pierce);
		else
			gameObject.attributes.heal(tmpMagnitude);
		
		if(duration <= 0 || !overTime){
			gameObject.remove(this);
			VoidObjectRegistry.gameObjectFactory.releaseComponent(this);
		}
	}
	

}
