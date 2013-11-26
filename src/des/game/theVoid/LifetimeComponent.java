package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.GameComponent;
import des.game.base.GameObject;
import des.game.scale.GameObjectManager;


public class LifetimeComponent extends GameComponent{
	private float mTimeUntilDeath;
	
	public LifetimeComponent(){
		super();
		this.reset();
		
		this.setPhase(GameComponent.ComponentPhases.THINK.ordinal());
	}
	
	@Override
	public void update(float timeDelta, BaseObject parent){
        GameObject parentObject = (GameObject)parent;
        if (mTimeUntilDeath > 0) {
            mTimeUntilDeath -= timeDelta;
            if (mTimeUntilDeath <= 0) {
                die(parentObject);
                return;
            }
        }

        if (parentObject.attributes != null && parentObject.attributes.health <= 0) {
            die(parentObject);
            return;
        }
	}
	
	@Override
	public void reset() {
        mTimeUntilDeath = -1;
		
	}
    public void setTimeUntilDeath(float time) {
        mTimeUntilDeath = time;
    }
    private void die(GameObject parentObject) {
        //VoidObjectFactory factory = VoidObjectRegistry.gameObjectFactory;
        GameObjectManager manager = sSystemRegistry.gameObjectManager;
        
        if (manager != null) {
            manager.destroy(parentObject);
        }
    }
}
