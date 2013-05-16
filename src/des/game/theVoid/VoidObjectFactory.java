package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.CollisionComponent;
import des.game.base.DebugLog;
import des.game.base.FixedSizeArray;
import des.game.base.GameObject;
import des.game.base.GameObjectAttributes;
import des.game.base.Utils;
import des.game.boundary.Boundary;
import des.game.boundary.Circle;
import des.game.boundary.Polygon;
import des.game.boundary.Rectangle;
import des.game.drawing.DrawableBufferedTexureCoords;
import des.game.physics.ControlledVectorObject;
import des.game.physics.Field;
import des.game.physics.PhysicsObject;
import des.game.physics.VectorObject;
import des.game.scale.AnimationFrame;
import des.game.scale.CameraBiasComponent;
import des.game.scale.CameraComponent;
import des.game.scale.ComponentClass;
import des.game.scale.GenericAnimationComponent;
import des.game.scale.RenderComponent;
import des.game.scale.ScaleObjectFactory;
import des.game.scale.SpriteAnimation;
import des.game.scale.SpriteComponent;



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
                new ComponentClass(CameraComponent.class, 1),
                new ComponentClass(RenderComponent.class, 384),
                new ComponentClass(SpriteComponent.class, 384),
                new ComponentClass(GenericAnimationComponent.class, 200),
                new ComponentClass(CollisionComponent.class, 200)
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
        INVISIBLE_WALL(2),
        BALL(3),
        PLAYER(4),
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
    
    public void staticDataInvisibleWall(){

        final int staticObjectCount = 0;
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        setStaticData(GameObjectType.INVISIBLE_WALL.ordinal(), staticData);
    }
	public GameObject spawnInvisibleWall(float positionX, float positionY, float width, float height){

        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = width;
        object.height = height;

        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        physics.type = PhysicsObject.INVISIBLE_WALL;
        
        Rectangle rect = (Rectangle)allocateComponent(Rectangle.class);
        rect.initialize(physics.location, width, height);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setRectangle(rect);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
        collision.setPhysicsObject(object.physcisObject);

        object.add(boundary);
        object.add(rect);
        object.add(physics);

        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        object.commitUpdates();
        return object;
	}
	public GameObject spawnInvisibleWall(float positionX, float positionY, float radius){

        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = radius*2;
        object.height = object.width;

        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        physics.type = PhysicsObject.INVISIBLE_WALL;
        
        Circle circ = (Circle)allocateComponent(Circle.class);
        circ.initialize(physics.location, radius);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setCircle(circ);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
        collision.setPhysicsObject(object.physcisObject);

        object.add(boundary);
        object.add(circ);
        object.add(physics);

        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        object.commitUpdates();
        return object;
	}
	public void staticDataBall(){ 
	    final int staticObjectCount = 1; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idle = new SpriteAnimation(0, 2);
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("ball1"), 
	            Utils.framesToTime(24, 6)));
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("ball2"), 
	            Utils.framesToTime(24, 6)));
	
	    idle.setLoop(true);
	
	    
	    staticData.add(idle);
	    setStaticData(GameObjectType.BALL.ordinal(), staticData);
	}
	public GameObject spawnBall(float positionX, float positionY, float orientation, float speed){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 32;
	    object.height = 32;
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.BALL.ordinal());
	    if (staticData == null) {
	    	staticDataBall();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+2);
	    render.setDrawOffset(-16, -16); 
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(false);
	    sprite.setRenderComponent(render);
	 
	    GenericAnimationComponent animation 
	        = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
	    animation.setSprite(sprite);
	
	    object.add(render);
	    object.add(sprite);
	
	    object.add(animation);
	    object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
	    
	    PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
	    physics.location.setX(positionX);
	    physics.location.setY(positionY);
	    physics.location.setZ(0);

	    VectorObject vector = (VectorObject)allocateComponent(VectorObject.class);
	    vector.initialize(1, physics.location, 0, 0);
	    vector.setVelocityMagDir(speed, orientation);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, 16);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = PhysicsObject.MOB;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
	    
	    collision.setPhysicsObject(object.physcisObject);
	    object.add(collision);
        if(boundary == null || circle == null || physics == null || collision == null || vector == null ){
        	DebugLog.i("Level", "ERROR: component is null");
        }
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
	    
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    addStaticData(GameObjectType.BALL.ordinal(), object, sprite);
	    
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}
	
	public void staticDataPlayer(){ 
	    final int staticObjectCount = 1; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idle = new SpriteAnimation(0, 2);
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("ball1"), 
	            Utils.framesToTime(24, 6)));
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("ball2"), 
	            Utils.framesToTime(24, 6)));
	
	    idle.setLoop(true);
	
	    
	    staticData.add(idle);
	    setStaticData(GameObjectType.PLAYER.ordinal(), staticData);
	}
	public GameObject spawnPlayer(float positionX, float positionY, float orientation, float speed){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 32;
	    object.height = 32;
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.PLAYER.ordinal());
	    if (staticData == null) {
	    	staticDataBall();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+2);
	    render.setDrawOffset(-16, -16); 
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(false);
	    sprite.setRenderComponent(render);
	 
	    GenericAnimationComponent animation 
	        = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
	    animation.setSprite(sprite);
	
	    object.add(render);
	    object.add(sprite);
	
	    object.add(animation);
	    object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
	    
	    PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
	    physics.location.setX(positionX);
	    physics.location.setY(positionY);
	    physics.location.setZ(0);

	    VectorObject vector = (VectorObject)allocateComponent(VectorObject.class);
	    vector.initialize(1, physics.location, 0, 0);
	    vector.setVelocityMagDir(speed, orientation);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, 16);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = PhysicsObject.MOB;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
	    
	    collision.setPhysicsObject(object.physcisObject);
	    object.add(collision);
        if(boundary == null || circle == null || physics == null || collision == null || vector == null ){
        	DebugLog.i("Level", "ERROR: component is null");
        }
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
	    
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    addStaticData(GameObjectType.PLAYER.ordinal(), object, sprite);
	    
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}
}
