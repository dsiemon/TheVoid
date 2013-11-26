package des.game.theVoid;

import java.util.Random;

import des.game.base.BaseObject;
import des.game.base.CollisionComponent;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.GameObjectAttributes;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.ControlledVectorObject;


public class OctorokComponent extends CollisionComponent{
	public static Random generator = new Random();
	public static int directionSelection[] = new int[4];
	
	public float stateChangeCountdown;
	public boolean collision;
	
	public ControlledVectorObject vector;
	public boolean mustChangeDirection;
	public boolean validDirections[];
	float moveTime;
	float idleTime;
	float hitReactTime;
	float velocity;
	
	int touchDamageType;
	float touchDamageMagnitude;
	public OctorokComponent(){
		super();
		validDirections = new boolean[4];
		
		reset();
	}
	
	@Override
	public void reset(){
		stateChangeCountdown = 0;
		collision = false;
		vector = null;
		
		mustChangeDirection = false;
		validDirections[0] = true;
		validDirections[1] = true;
		validDirections[2] = true;
		validDirections[3] = true;
		moveTime = 3.0f;
		idleTime = 1.0f;
		hitReactTime = 1.0f;
		velocity = 100f;
		
		touchDamageType = GameObjectAttributes.ElementalTypes.NORMAL;
		touchDamageMagnitude = 1.0f;
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		final GameObject gameObject = (GameObject)parent;
		
		this.vector.brakeVector(timeDelta);
		
		Vector2 position = gameObject.getPosition();
		position.x = (float)this.physicsObject.getLocation().getX();
    	position.y = (float)this.physicsObject.getLocation().getY();
    	
    	stateChangeCountdown -= timeDelta;
    	int action = gameObject.getCurrentAction();
    	
		if(gameObject.attributes.damageThisFrame){
			setHitReactState(gameObject);
		}
		else if(collision){
			setIdleState(gameObject);
    	}
    	
    	collision = false;
    	if(stateChangeCountdown <= 0.0f){
    	
    		// if idle or hit react, start moving again.
    		if(action == GameObject.ActionType.IDLE.index() || action == GameObject.ActionType.HIT_REACT.index()){
    			setMoveState(gameObject);
    		}
    		else if(action == GameObject.ActionType.MOVE.index()){
    			if(idleTime <= 0.0f){
    				setMoveState(gameObject);
    			}
    			else{
    				setIdleState(gameObject);
    			}
    		}
    	}
    	

		this.vector.setControlledComponents(gameObject.targetVelocity.x, gameObject.targetVelocity.y);
	}
	
	private void setMoveState(final GameObject gameObject){
		gameObject.setCurrentAction(GameObject.ActionType.MOVE.index());
		stateChangeCountdown = moveTime;
		
		int newDir = this.changeDirection(gameObject.getCurrentDirection());
		gameObject.setCurrentDirection(newDir);
		setVelocity(newDir, gameObject.targetVelocity, velocity);
	}
	
	private void setIdleState(final GameObject gameObject){
		gameObject.setCurrentAction(GameObject.ActionType.IDLE.index());
		stateChangeCountdown = idleTime;
		
		gameObject.targetVelocity.x = 0f;
		gameObject.targetVelocity.y = 0f;
	}
	
	private void setHitReactState(final GameObject gameObject){
		gameObject.setCurrentAction(GameObject.ActionType.HIT_REACT.index());
		stateChangeCountdown = 1.0f;
		
		gameObject.targetVelocity.x = 0f;
		gameObject.targetVelocity.y = 0f;
	}
	
	private int changeDirection(int currentDirection){
		int directionCount = 0;
		
		if(validDirections[0] && (!mustChangeDirection || currentDirection != 0)){
			directionSelection[directionCount] = 0;
			++directionCount;
		}
		if(validDirections[1] && (!mustChangeDirection || currentDirection != 1)){
			directionSelection[directionCount] = 1;
			++directionCount;
		}
		if(validDirections[2] && (!mustChangeDirection || currentDirection != 2)){
			directionSelection[directionCount] = 2;
			++directionCount;
		}
		if(validDirections[3] && (!mustChangeDirection || currentDirection != 3)){
			directionSelection[directionCount] = 3;
			++directionCount;
		}
		
		if(directionCount == 0) return currentDirection;
		return directionSelection[generator.nextInt(directionCount)];
	}
	
	private static void setVelocity(int direction, Vector2 targetVelocity, float velocity){
		if(direction == 0){
			targetVelocity.x = 0f;
			targetVelocity.y = velocity;
		}
		else if(direction == 1){
			targetVelocity.x = -velocity;
			targetVelocity.y = 0f;
		}
		else if(direction == 2){
			targetVelocity.x = 0f;
			targetVelocity.y = -velocity;
		}
		else if(direction == 3){
			targetVelocity.x = velocity;
			targetVelocity.y = 0f;
		}
	}
	
	@Override
	public void handleCollision(CollisionBehavior other) {
		collision = true;
		
		if(CollisionComponent.class.isInstance(other)){
			CollisionComponent col = (CollisionComponent) other;
			
			if(col.parent != null && col.parent.attributes != null && !col.parent.attributes.invulnerable){
				HealthModifier mod = ((HealthModifier)VoidObjectRegistry.gameObjectFactory.allocateComponent(HealthModifier.class));
				mod.setupModifier(this.touchDamageMagnitude, this.touchDamageType, 0);
				col.parent.add(mod);
			}
		}
	}
}
