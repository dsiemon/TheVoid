package des.game.theVoid;

import des.game.base.GameObject;
import des.game.base.GameObjectAttributes;
import des.game.boundary.Boundary;
import des.game.boundary.Circle;
import des.game.boundary.Polygon;
import des.game.boundary.Rectangle;
import des.game.physics.ControlledVectorObject;
import des.game.physics.Field;
import des.game.physics.PhysicsObject;
import des.game.physics.VectorObject;
import des.game.scale.CameraBiasComponent;
import des.game.scale.CameraComponent;
import des.game.scale.ComponentClass;
import des.game.scale.GenericAnimationComponent;
import des.game.scale.ScaleObjectFactory;



public class VoidObjectFactory extends ScaleObjectFactory {

	
	public VoidObjectFactory(){
		super(GameObjectType.OBJECT_COUNT.ordinal());
		
		ComponentClass[] componentTypes = {

                new ComponentClass(CameraBiasComponent.class, 8),
                new ComponentClass(PhysicsObject.class, 200),
                new ComponentClass(VectorObject.class, 200),
                new ComponentClass(ControlledVectorObject.class, 100),
                new ComponentClass(Boundary.class, 200),
                new ComponentClass(Circle.class, 200),
                new ComponentClass(Rectangle.class, 200),
                new ComponentClass(Field.class, 100),
                new ComponentClass(Rectangle.class, 200),
                new ComponentClass(Polygon.class, 20),
                new ComponentClass(CameraComponent.class, 1)
        };
        
        this.setupComponentPools(componentTypes);
	}
	@Override
	public void preloadEffects() {
		// TODO Auto-generated method stub

	}
    public enum GameObjectType {
        INVALID(-1),
  
        CAMERA_BIAS(1),
        
        // End
        OBJECT_COUNT(-1);
        
        private final int mIndex;
        GameObjectType(int index) {
            this.mIndex = index;
        }
        
        public int index() {
            return mIndex;
        }
        
        // TODO: Is there any better way to do this?
        public static GameObjectType indexToType(int index) {
            final GameObjectType[] valuesArray = values();
            
            GameObjectType foundType = INVALID;
            for (int x = 0; x < valuesArray.length; x++) {
                GameObjectType type = valuesArray[x];
                if (type.mIndex == index) {
                    foundType = type;
                    break;
                }
            }
            return foundType;
        }
        
    }
    
    public GameObject spawnCameraTarget(float positionX, float positionY) {
        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = 0;
        object.height = 0;

        object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
        
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);

        object.physcisObject = physics;
        object.physcisObject.add();
        
        object.add(physics);

        CameraComponent collision = (CameraComponent)allocateComponent(CameraComponent.class);
        collision.px = positionX;
        collision.py = positionY;
        collision.setPhysicsObject(object.physcisObject);
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);

        object.commitUpdates();

        return object;
    }


}
