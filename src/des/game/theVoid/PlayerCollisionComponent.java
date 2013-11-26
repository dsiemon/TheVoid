package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.CollisionComponent;
import des.game.base.GameObject;
import des.game.base.GameObjectAttributes;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.ControlledVectorObject;
import des.game.scale.InputTouchButton;
import des.game.scale.InputTouchEvent.TouchState;

public class PlayerCollisionComponent extends CollisionComponent{

	public Vector2 scratch;
	public ControlledVectorObject vector;
	public boolean collision;
	public PlayerCollisionComponent(){
		super();
		scratch = new Vector2();
		reset();
	}
	
	@Override
	public void reset(){
		scratch.zero();
		collision = false;
		vector = null;
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		final GameObject gameObject = (GameObject)parent;
		
		this.vector.brakeVector(timeDelta);
		
		Vector2 position = gameObject.getPosition();
		position.x = (float)this.physicsObject.getLocation().getX();
    	position.y = (float)this.physicsObject.getLocation().getY();
    	
    	InputTouchButton moveButton = VoidObjectRegistry.uiSystem.moveButton;
    	
    	if(moveButton.state.equals(TouchState.IDLE) || moveButton.state.equals(TouchState.UP)){
    		gameObject.setCurrentAction(GameObject.ActionType.IDLE.index());
    		
    		gameObject.targetVelocity.x = 0f;
    		gameObject.targetVelocity.y = 0f;
    	}
    	else{
    		gameObject.setCurrentAction(GameObject.ActionType.MOVE.index());
    		scratch.set(moveButton.getRelativeX(), moveButton.getRelativeY());
    		scratch.normalize();
    		
    		if(scratch.x == 0f && scratch.y == 0f){
    			scratch.x = 1f;
    		}
    		float orientation = scratch.orientation();
    		scratch.multiply(200f);
    		
    		gameObject.setCurrentDirection(toDirectionConstant(orientation));
    		
    		gameObject.targetVelocity.set(scratch);
    	}
    	
    	this.vector.setControlledComponents(gameObject.targetVelocity.x, gameObject.targetVelocity.y);
	}
	
	@Override
	public void handleCollision(CollisionBehavior other) {
		collision = true;
		
	}
	
	public static int toDirectionConstant(float orientation){
		if(orientation < 0){
			orientation += Math.PI * 2;
		}
		
		if(orientation < Math.PI/4){
			return 3;
		}
		else if(orientation < Math.PI*3/4){
			return 0;
		}
		else if(orientation < Math.PI*5/4){
			return 1;
		}
		else if(orientation < Math.PI*7/4){
		return 2;
		}
		return 3;
	}
}
