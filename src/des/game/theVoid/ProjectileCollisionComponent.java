package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.CollisionComponent;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.VectorObject;

public class ProjectileCollisionComponent extends CollisionComponent{
	private int collisionCount;
	public ProjectileCollisionComponent(){
		super();
		this.reset();
	}
	
	@Override
	public void reset() {
		collisionCount = 0;
		super.reset();
	}
	
	@Override
	public void update(float timeDelta, BaseObject parent){
		final GameObject gameObject = (GameObject)parent;
		gameObject.colCalled = true;
		this.parent.attributes.health -= collisionCount;
		collisionCount = 0;
		
		Vector2 position = gameObject.getPosition();
		position.x = (float)this.physicsObject.getLocation().getX();
    	position.y = (float)this.physicsObject.getLocation().getY();

    	final VectorObject v = this.physicsObject.getVector();
    	if(v != null){
    		
	    	final Vector2 velocity = gameObject.velocity;
	    	velocity.x = (float)v.getVelocityXComponent();
	    	velocity.y = (float)v.getVelocityYComponent();
	    	
	    	gameObject.targetVelocity.x = velocity.x;
	    	gameObject.targetVelocity.y = velocity.y;
    	}
	}
	
	@Override
	public void handleCollision(CollisionBehavior other) {

		collisionCount++;
		if(CollisionComponent.class.isInstance(other)){
			CollisionComponent col = (CollisionComponent) other;
			
			if(col.parent != null && col.parent.attributes != null && !col.parent.attributes.invulnerable){
				HealthModifier mod = ((HealthModifier)VoidObjectRegistry.gameObjectFactory.allocateComponent(HealthModifier.class));
				mod.setupModifier(1, 0, 0);
				col.parent.add(mod);
			}
		}
		
	}
}
