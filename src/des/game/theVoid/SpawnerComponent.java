package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.GameComponent;
import des.game.base.GameObject;
import des.game.scale.GameObjectManager;

public class SpawnerComponent extends GameComponent{

	private VoidObjectFactory.GameObjectType objectTypeToSpawn;
	private float delayBetweenSpawns;
	private float spawnCountDown;
	private boolean specifyDirection;
	private boolean up;
	private boolean left;
	private boolean down;
	private boolean right;
	private boolean mustChangeDir;
	private float lifetime;
	
	public SpawnerComponent(){
		super();
		this.reset();
		this.setPhase(GameComponent.ComponentPhases.THINK.ordinal());
		
	}
	
    @Override
    public void reset() {
    	objectTypeToSpawn = VoidObjectFactory.GameObjectType.INVALID;
    	delayBetweenSpawns = 3f;
    	spawnCountDown = 0f;
    	specifyDirection = false;
    	up = true;
    	left = true;
    	down = true;
    	right = true;
    	mustChangeDir = false;
    	lifetime = -1f;
    }
    
    @Override
    public void update(float timeDelta, BaseObject parent) {
    	spawnCountDown -= timeDelta;
    	
    	if(spawnCountDown <= 0){
    		VoidObjectFactory factory = VoidObjectRegistry.gameObjectFactory;
    		GameObjectManager manager = sSystemRegistry.gameObjectManager;
    		GameObject parentObject = (GameObject) parent;
    		
            GameObject object = factory.spawn(objectTypeToSpawn, parentObject.getCenteredPositionX(), parentObject.getCenteredPositionY(), 0);
            if (object != null) {    
            	if(lifetime >= 0f){
	                LifetimeComponent projectileLife = object.findByClass(LifetimeComponent.class);
	                if (projectileLife != null) {
	                    projectileLife.setTimeUntilDeath(lifetime);
	                }
            	}
            	
            	if(specifyDirection){
	                OctorokComponent collision = object.findByClass(OctorokComponent.class);
	                if (collision != null) {
	                    collision.validDirections[0] = up;
	                    collision.validDirections[1] = left;
	                    collision.validDirections[2] = down;
	                    collision.validDirections[3] = right;
	                    collision.mustChangeDirection = this.mustChangeDir;
	                }
            	}
            	
	            manager.add(object);
            }
    		
    		spawnCountDown = delayBetweenSpawns;
    	}
    }

	public void setObjectTypeToSpawn(VoidObjectFactory.GameObjectType objectTypeToSpawn) {
		this.objectTypeToSpawn = objectTypeToSpawn;
	}

	public void setDelayBetweenSpawns(float delayBetweenSpawns) {
		this.delayBetweenSpawns = delayBetweenSpawns;
	}

	public void setDirection(boolean up, boolean left, boolean down, boolean right, boolean mustChangeDir) {
		this.down = down;
		this.up = up;
		this.left = left;
		this.right = right;
		this.mustChangeDir = mustChangeDir;
		this.specifyDirection = true;
	}

	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}
    
	
}
