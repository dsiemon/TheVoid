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
import des.game.scale.GenericDirectionalAnimationComponent;
import des.game.scale.RenderComponent;
import des.game.scale.ScaleObjectFactory;
import des.game.scale.SpriteAnimation;
import des.game.scale.SpriteComponent;
import des.game.theVoid.VoidObjectFactory.GameObjectType;



public class VoidObjectFactory extends ScaleObjectFactory {

	
	public VoidObjectFactory(){ 
		super(GameObjectType.OBJECT_COUNT.ordinal());
		
		ComponentClass[] componentTypes = {
 
                new ComponentClass(CameraBiasComponent.class, 8),
                new ComponentClass(PhysicsObject.class, 400),
                new ComponentClass(VectorObject.class, 400),
                new ComponentClass(ControlledVectorObject.class, 400),
                new ComponentClass(Boundary.class, 400),
                new ComponentClass(Circle.class, 400),
                new ComponentClass(Rectangle.class, 400),
                new ComponentClass(Field.class, 100),
                new ComponentClass(Rectangle.class, 400),
                new ComponentClass(Polygon.class, 20),
                new ComponentClass(CameraComponent.class, 1),
                new ComponentClass(RenderComponent.class, 400),
                new ComponentClass(SpriteComponent.class, 400),
                new ComponentClass(GenericAnimationComponent.class, 400),
                new ComponentClass(GenericDirectionalAnimationComponent.class, 400),
                new ComponentClass(CollisionComponent.class, 200),
                new ComponentClass(OctorokComponent.class, 200),
                new ComponentClass(HealthModifier.class, 200),
                new ComponentClass(StatusEffect.class, 200),
                new ComponentClass(LifetimeComponent.class, 400),
                new ComponentClass(GameObjectAttributes.class, 400),
                new ComponentClass(ProjectileCollisionComponent.class, 400),
                new ComponentClass(BasicColorComponent.class, 400),
                new ComponentClass(LaunchProjectileComponent.class, 400),
                new ComponentClass(SpawnerComponent.class, 20),
                new ComponentClass(PlayerCollisionComponent.class, 20)
                
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
        OCTOROK(5),
        ROCK(6),
        PIGMAN(7),
        SPAWNER(8),
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
    
	public GameObject spawn(GameObjectType type, float x, float y, float orientation) {
		GameObject object = null;
		
		switch(type){
			case ROCK:
				object = spawnRock(x,y,orientation);
				break;
			case PIGMAN:
				object = spawnPigMan(x, y);
				break;
			case OCTOROK:
				object = spawnOctorok(x, y);
				break;
		}
		return object;
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
        collision.setPhysicsObject(object.physcisObject, object);
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);
        object.add(object.attributes);
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
        physics.type = VoidObjectRegistry.PhysicsObjectTypes.INVISIBLE_WALL;
        
        Rectangle rect = (Rectangle)allocateComponent(Rectangle.class);
        rect.initialize(physics.location, width, height);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setRectangle(rect);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
        collision.setPhysicsObject(object.physcisObject, object);

        object.add(boundary);
        object.add(rect);
        object.add(physics);

        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class); 
        object.add(object.attributes);
        object.attributes.invulnerable = true;
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
        physics.type = VoidObjectRegistry.PhysicsObjectTypes.INVISIBLE_WALL;
        
        Circle circ = (Circle)allocateComponent(Circle.class);
        circ.initialize(physics.location, radius);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setCircle(circ);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
        collision.setPhysicsObject(object.physcisObject, object);

        object.add(boundary);
        object.add(circ);
        object.add(physics);

        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
        object.add(object.attributes);
        object.attributes.invulnerable = true;
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
	    object.team = GameObject.Team.PLAYER.index();
	    
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
	    physics.type = VoidObjectRegistry.PhysicsObjectTypes.MOB2_PROJECTILE;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
	    
	    collision.setPhysicsObject(object.physcisObject, object);
	    object.add(collision);
        if(boundary == null || circle == null || physics == null || collision == null || vector == null ){
        	DebugLog.i("Level", "ERROR: component is null");
        }
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
	    
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);
	    object.attributes.invulnerable = true;
	    object.add(object.attributes);
	    addStaticData(GameObjectType.BALL.ordinal(), object, sprite);
	    
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}
	public void staticDataRock(){ 
	    final int staticObjectCount = 1; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idle = new SpriteAnimation(0, 4);
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("rock1"), 
	            Utils.framesToTime(24, 3)));
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("rock2"), 
	            Utils.framesToTime(24, 3)));
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("rock3"), 
	            Utils.framesToTime(24, 3)));
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("rock4"), 
	            Utils.framesToTime(24, 3)));
	
	    idle.setLoop(true);
	
	    
	    staticData.add(idle);
	    setStaticData(GameObjectType.ROCK.ordinal(), staticData);
	}
	public GameObject spawnRock(float positionX, float positionY, float orientation){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 12;
	    object.height = 12;
	    object.team = GameObject.Team.ENEMY.index();
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.ROCK.ordinal());
	    if (staticData == null) {
	    	staticDataRock();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+2);
	    render.setDrawOffset(-6, -6); 
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
	    vector.initialize(10, physics.location, 0, 0);
	    vector.setVelocityMagDir(300f, orientation);
	    physics.vector = vector;

	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, 5);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = VoidObjectRegistry.PhysicsObjectTypes.MOB1_PROJECTILE;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    ProjectileCollisionComponent collision = (ProjectileCollisionComponent)allocateComponent(ProjectileCollisionComponent.class);
	    
	    collision.setPhysicsObject(object.physcisObject, object);
	    object.add(collision);
	    if(vector.getVelocityMagnitude() < 290f || vector.getVelocityMagnitude() > 310f){
	    	DebugLog.e("mob", "ERROR: vector speed is: " + vector.getVelocityMagnitude());
	    }
        if(boundary == null || circle == null || physics == null || collision == null || vector == null ){
        	DebugLog.e("mob", "ERROR: component is null");
        }
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
	    
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    object.attributes.invulnerable = true;
	    
	    LifetimeComponent lifetime = (LifetimeComponent)allocateComponent(LifetimeComponent.class); 
	    object.attributes.health = object.attributes.maxHealth = 1;
	    lifetime.setTimeUntilDeath(5);
	    object.add(lifetime);
	    
	    object.add(object.attributes);
	    addStaticData(GameObjectType.ROCK.ordinal(), object, sprite);
	    
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}
	
	public void staticDataOctorok(){ 
	    final int staticObjectCount = 12; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idleUp = new SpriteAnimation(0, 1);
	    idleUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_up3"), 
	            Utils.framesToTime(24, 6)));
	    idleUp.setLoop(true);
	    SpriteAnimation idleLeft = new SpriteAnimation(1, 1);
	    idleLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_left2"), 
	            Utils.framesToTime(24, 6)));
	    idleLeft.setLoop(true);
	    SpriteAnimation idleDown = new SpriteAnimation(2, 1);
	    idleDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_down3"), 
	            Utils.framesToTime(24, 6)));
	    idleDown.setLoop(true);
	    SpriteAnimation idleRight = new SpriteAnimation(3, 1);
	    idleRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_right2"), 
	            Utils.framesToTime(24, 6)));
	    idleRight.setLoop(true);
	    //////////////////
	    SpriteAnimation moveUp = new SpriteAnimation(4, 2);
	    moveUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_up1"), 
	            Utils.framesToTime(24, 6)));
	    moveUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_up2"), 
	            Utils.framesToTime(24, 6)));
	    moveUp.setLoop(true);
	    SpriteAnimation moveLeft = new SpriteAnimation(5, 2);
	    moveLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_left1"), 
	            Utils.framesToTime(24, 6)));
	    moveLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_left2"), 
	            Utils.framesToTime(24, 6)));
	    moveLeft.setLoop(true);
	    SpriteAnimation moveDown = new SpriteAnimation(6, 2);
	    moveDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_down1"), 
	            Utils.framesToTime(24, 6)));
	    moveDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_down2"), 
	            Utils.framesToTime(24, 6)));
	    moveDown.setLoop(true);
	    SpriteAnimation moveRight = new SpriteAnimation(7, 2);
	    moveRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_right1"), 
	            Utils.framesToTime(24, 6)));
	    moveRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_right2"), 
	            Utils.framesToTime(24, 6)));
	    moveRight.setLoop(true);
	    ////////////////////////
	    SpriteAnimation hitUp = new SpriteAnimation(12, 1);
	    hitUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_up3"), 
	            Utils.framesToTime(24, 6)));
	    hitUp.setLoop(true);
	    SpriteAnimation hitLeft = new SpriteAnimation(13, 1);
	    hitLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_left2"), 
	            Utils.framesToTime(24, 6)));
	    hitLeft.setLoop(true);
	    SpriteAnimation hitDown = new SpriteAnimation(14, 1);
	    hitDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_down3"), 
	            Utils.framesToTime(24, 6)));
	    hitDown.setLoop(true);
	    SpriteAnimation hitRight = new SpriteAnimation(15, 1);
	    hitRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("octorok_right2"), 
	            Utils.framesToTime(24, 6)));
	    hitRight.setLoop(true);
	    
	    staticData.add(idleUp);
	    staticData.add(idleLeft);
	    staticData.add(idleDown);
	    staticData.add(idleRight);
	    
	    staticData.add(moveUp);
	    staticData.add(moveLeft);
	    staticData.add(moveDown);
	    staticData.add(moveRight);
	    
	    staticData.add(hitUp);
	    staticData.add(hitLeft);
	    staticData.add(hitDown);
	    staticData.add(hitRight);
	    
	    setStaticData(GameObjectType.OCTOROK.ordinal(), staticData);
	}
	
	public GameObject spawnOctorok(float positionX, float positionY){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 32;
	    object.height = 32;
	    object.team = GameObject.Team.ENEMY.index();
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.OCTOROK.ordinal());
	    if (staticData == null) {
	    	staticDataOctorok();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+1);
	    render.setDrawOffset(-16, -16); 
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(false);
	    sprite.setRenderComponent(render);
	 
	    BasicColorComponent color = (BasicColorComponent)allocateComponent(BasicColorComponent.class);
	    color.sprite = sprite;
	    object.add(color);
	    
	    GenericDirectionalAnimationComponent animation 
	        = (GenericDirectionalAnimationComponent)allocateComponent(GenericDirectionalAnimationComponent.class);
	    animation.setSprite(sprite);
	
	    object.add(render);
	    object.add(sprite);
	
	    object.add(animation);
	    
	    PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
	    physics.location.setX(positionX);
	    physics.location.setY(positionY);
	    physics.location.setZ(0);

	    ControlledVectorObject vector = (ControlledVectorObject)allocateComponent(ControlledVectorObject.class);
	    vector.initialize(10, physics.location, 0, 0);
	    vector.setVelocityMagDir(0.0f, 0.0f);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, 14);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = VoidObjectRegistry.PhysicsObjectTypes.MOB1;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    OctorokComponent collision = (OctorokComponent)allocateComponent(OctorokComponent.class);
	    collision.vector = vector;
	    collision.idleTime = 1.2f;
	    collision.setPhysicsObject(object.physcisObject, object);
	    
	    object.add(collision);
        if(boundary == null || circle == null || physics == null || collision == null || vector == null ){
        	DebugLog.i("Level", "ERROR: component is null");
        }
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
        
        LaunchProjectileComponent launchProjectile = (LaunchProjectileComponent)allocateComponent(LaunchProjectileComponent.class);
        launchProjectile.setRequiredAction(GameObject.ActionType.IDLE);
        launchProjectile.setSetsPerActivation(1);
        launchProjectile.setShotsPerSet(3);
        launchProjectile.setDelayBeforeFirstSet(0f);
        launchProjectile.setDelayBetweenShots(0.25f);
        launchProjectile.setObjectTypeToSpawn(GameObjectType.ROCK);
        launchProjectile.setOffset(16);

        object.add(launchProjectile);
        
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    object.add(object.attributes);
	    addStaticData(GameObjectType.OCTOROK.ordinal(), object, sprite);
	    object.attributes.health = object.attributes.maxHealth = 2;
	    object.attributes.invulnerable = false;
	    
	    LifetimeComponent lifetime = (LifetimeComponent)allocateComponent(LifetimeComponent.class);  
	    //lifetime.setTimeUntilDeath(5);
	    object.add(lifetime);
	    
	    object.setCurrentDirection(0);
	    sprite.playAnimation(0);
	    object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
	    object.commitUpdates();
	    return object;
	}
	
	public void staticDataPigMan(){ 
	    final int staticObjectCount = 12; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idleUp = new SpriteAnimation(0, 1);
	    idleUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_up3"), 
	            Utils.framesToTime(24, 6)));
	    idleUp.setLoop(true);
	    SpriteAnimation idleLeft = new SpriteAnimation(1, 1);
	    idleLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_left2"), 
	            Utils.framesToTime(24, 6)));
	    idleLeft.setLoop(true);
	    SpriteAnimation idleDown = new SpriteAnimation(2, 1);
	    idleDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_down3"), 
	            Utils.framesToTime(24, 6)));
	    idleDown.setLoop(true);
	    SpriteAnimation idleRight = new SpriteAnimation(3, 1);
	    idleRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_right2"), 
	            Utils.framesToTime(24, 6)));
	    idleRight.setLoop(true);
	    //////////////////
	    SpriteAnimation moveUp = new SpriteAnimation(4, 2);
	    moveUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_up1"), 
	            Utils.framesToTime(24, 6)));
	    moveUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_up2"), 
	            Utils.framesToTime(24, 6)));
	    moveUp.setLoop(true);
	    SpriteAnimation moveLeft = new SpriteAnimation(5, 2);
	    moveLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_left1"), 
	            Utils.framesToTime(24, 6)));
	    moveLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_left2"), 
	            Utils.framesToTime(24, 6)));
	    moveLeft.setLoop(true);
	    SpriteAnimation moveDown = new SpriteAnimation(6, 2);
	    moveDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_down1"), 
	            Utils.framesToTime(24, 6)));
	    moveDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_down2"), 
	            Utils.framesToTime(24, 6)));
	    moveDown.setLoop(true);
	    SpriteAnimation moveRight = new SpriteAnimation(7, 2);
	    moveRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_right1"), 
	            Utils.framesToTime(24, 6)));
	    moveRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_right2"), 
	            Utils.framesToTime(24, 6)));
	    moveRight.setLoop(true);
	    ////////////////////////
	    SpriteAnimation hitUp = new SpriteAnimation(12, 1);
	    hitUp.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_up3"), 
	            Utils.framesToTime(24, 6)));
	    hitUp.setLoop(true);
	    SpriteAnimation hitLeft = new SpriteAnimation(13, 1);
	    hitLeft.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_left2"), 
	            Utils.framesToTime(24, 6)));
	    hitLeft.setLoop(true);
	    SpriteAnimation hitDown = new SpriteAnimation(14, 1);
	    hitDown.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_down3"), 
	            Utils.framesToTime(24, 6)));
	    hitDown.setLoop(true);
	    SpriteAnimation hitRight = new SpriteAnimation(15, 1);
	    hitRight.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("pigMan_right2"), 
	            Utils.framesToTime(24, 6)));
	    hitRight.setLoop(true);
	    
	    staticData.add(idleUp);
	    staticData.add(idleLeft);
	    staticData.add(idleDown);
	    staticData.add(idleRight);
	    
	    staticData.add(moveUp);
	    staticData.add(moveLeft);
	    staticData.add(moveDown);
	    staticData.add(moveRight);
	    
	    staticData.add(hitUp);
	    staticData.add(hitLeft);
	    staticData.add(hitDown);
	    staticData.add(hitRight);
	    
	    setStaticData(GameObjectType.PIGMAN.ordinal(), staticData);
	}
	
	public GameObject spawnPigMan(float positionX, float positionY){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 32;
	    object.height = 32;
	    object.team = GameObject.Team.ENEMY2.index();
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.PIGMAN.ordinal());
	    if (staticData == null) {
	    	staticDataPigMan();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+1);
	    render.setDrawOffset(-16, -16); 
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(false);
	    sprite.setRenderComponent(render);
	 
	    BasicColorComponent color = (BasicColorComponent)allocateComponent(BasicColorComponent.class);
	    color.sprite = sprite;
	    object.add(color);
	    
	    GenericDirectionalAnimationComponent animation 
	        = (GenericDirectionalAnimationComponent)allocateComponent(GenericDirectionalAnimationComponent.class);
	    animation.setSprite(sprite);
	
	    object.add(render);
	    object.add(sprite);
	
	    object.add(animation);

	    
	    PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
	    physics.location.setX(positionX);
	    physics.location.setY(positionY);
	    physics.location.setZ(0);

	    ControlledVectorObject vector = (ControlledVectorObject)allocateComponent(ControlledVectorObject.class);
	    vector.initialize(10, physics.location, 0, 0);
	    vector.setVelocityMagDir(0.0f, 0.0f);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, 15);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = VoidObjectRegistry.PhysicsObjectTypes.MOB2;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    OctorokComponent collision = (OctorokComponent)allocateComponent(OctorokComponent.class);
	    collision.vector = vector;
	    collision.mustChangeDirection = true;
	    collision.idleTime = -1f;
	    collision.touchDamageType = GameObjectAttributes.ElementalTypes.NORMAL;
	    collision.touchDamageMagnitude = 2f;
	    
	    collision.setPhysicsObject(object.physcisObject, object);
	    
	    object.add(collision);
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
        
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    object.add(object.attributes);
	    addStaticData(GameObjectType.PIGMAN.ordinal(), object, sprite);
	    object.attributes.health = object.attributes.maxHealth = 10;
	    object.attributes.invulnerable = false;
	    
	    LifetimeComponent lifetime = (LifetimeComponent)allocateComponent(LifetimeComponent.class);  
	    //lifetime.setTimeUntilDeath(5);
	    object.add(lifetime);
	    
	    object.setCurrentDirection(0);
	    object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}
	
	public void staticDataSpawner(){ 
	    final int staticObjectCount = 1; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idle = new SpriteAnimation(0, 3);
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("spawner1"), 
	            Utils.framesToTime(24, 6)));
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("spawner2"), 
	            Utils.framesToTime(24, 6)));
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("spawner3"), 
	            Utils.framesToTime(24, 6)));
	    idle.setLoop(true);

	    staticData.add(idle);
	    setStaticData(GameObjectType.SPAWNER.ordinal(), staticData);
	}
	
	public GameObject spawnSpawner(float positionX, float positionY, GameObjectType type, float objectLifetime, float delay, boolean up, boolean left, boolean down, boolean right, boolean mustChagneDir){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 32;
	    object.height = 32;
	    object.team = GameObject.Team.ENEMY.index();
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.SPAWNER.ordinal());
	    if (staticData == null) {
	    	staticDataSpawner();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START);
	    render.setDrawOffset(0, 0); 
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
	    
        Rectangle rect = (Rectangle)allocateComponent(Rectangle.class);
        rect.initialize(physics.location, 32, 32);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setRectangle(rect);
	    
	    physics.boundary = boundary;
	    physics.type = VoidObjectRegistry.PhysicsObjectTypes.SPAWNER;
	    object.physcisObject = physics;
	    object.physcisObject.add();


	    object.setCurrentDirection(4);
	    
        CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
        collision.setPhysicsObject(object.physcisObject, object);
	    object.add(boundary);
        object.add(rect);
        object.add(physics);
        object.add(collision);
        
        
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    object.add(object.attributes);
	    addStaticData(GameObjectType.SPAWNER.ordinal(), object, sprite);
	    object.attributes.health = object.attributes.maxHealth = 2;
	    object.attributes.invulnerable = false;
	    
	    LifetimeComponent lifetime = (LifetimeComponent)allocateComponent(LifetimeComponent.class);  
	    object.add(lifetime);
	    
	    SpawnerComponent spawner = (SpawnerComponent)allocateComponent(SpawnerComponent.class); 
	    spawner.setDelayBetweenSpawns(delay);
	    spawner.setLifetime(objectLifetime);
	    spawner.setObjectTypeToSpawn(type);
	    spawner.setDirection(up, left, down, right, mustChagneDir);
	    
	    object.add(spawner);

	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}
	
	public void staticDataPlayer(){ 
	    final int staticObjectCount = 16; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation tempAnim = new SpriteAnimation(0, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_idle1"), Utils.framesToTime(24, 6)));
	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(1, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_idle1" ), Utils.framesToTime(24, 6)));
	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(2, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_idle1" ), Utils.framesToTime(24, 6)));
	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(3, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_idle1" ), Utils.framesToTime(24, 6)));
	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);


	    ////////////////////////////////
	    tempAnim = new SpriteAnimation(4, 8);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move1" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move2" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move3" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move4" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move5" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move6" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move7" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_move8" ), Utils.framesToTime(24, 4)));
	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(5, 8);
	    
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move2" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move3" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move4" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move5" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move6" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move7" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move8" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_move1" ), Utils.framesToTime(24, 4)));
	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(6, 8);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move1" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move2" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move3" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move4" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move5" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move6" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move7" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_move8" ), Utils.framesToTime(24, 4)));

	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(7, 8);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move2" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move3" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move4" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move5" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move6" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move7" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move8" ), Utils.framesToTime(24, 4)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_move1" ), Utils.framesToTime(24, 4)));
	    tempAnim.setLoop(true);
	    staticData.add(tempAnim);

	    /////////////////////////////

	    tempAnim = new SpriteAnimation(8, 6);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_attack1" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_attack2" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_attack3" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_attack4" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_attack5" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_attack6" ), Utils.framesToTime(24, 6)));
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(9, 6);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_attack1" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_attack2" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_attack3" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_attack4" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_attack5" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_attack6" ), Utils.framesToTime(24, 6)));
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(10, 5);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_attack1" ), Utils.framesToTime(24, 7)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_attack2" ), Utils.framesToTime(24, 7)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_attack3" ), Utils.framesToTime(24, 7)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_attack4" ), Utils.framesToTime(24, 7)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_attack5" ), Utils.framesToTime(24, 7)));
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(11, 6);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_attack1" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_attack2" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_attack3" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_attack4" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_attack5" ), Utils.framesToTime(24, 6)));
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_attack6" ), Utils.framesToTime(24, 6)));
	    staticData.add(tempAnim);

	    //////////////////////////

	    tempAnim = new SpriteAnimation(12, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_up_knockback1" ), Utils.framesToTime(24, 24)));
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(13, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_left_knockback1" ), Utils.framesToTime(24, 24)));
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(14, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_down_knockback1" ), Utils.framesToTime(24, 24)));
	    staticData.add(tempAnim);

	    tempAnim = new SpriteAnimation(15, 1);
	    tempAnim.addFrame(new AnimationFrame(DrawableBufferedTexureCoords.getTexture("player_right_knockback1" ), Utils.framesToTime(24, 24)));
	    staticData.add(tempAnim);

	    setStaticData(GameObjectType.PLAYER.ordinal(), staticData);
	}
	public GameObject spawnPlayer(float positionX, float positionY, float orientation, float speed){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 48;
	    object.height = 48;
	    object.team = GameObject.Team.PLAYER.index();
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.PLAYER.ordinal());
	    if (staticData == null) {
	    	staticDataPlayer();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+2);
	    render.setDrawOffset(-24, -24); 
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(false);
	    sprite.setRenderComponent(render);
	 
	    GenericDirectionalAnimationComponent animation 
        	= (GenericDirectionalAnimationComponent)allocateComponent(GenericDirectionalAnimationComponent.class);
	    animation.setSprite(sprite);
	
	    object.add(render);
	    object.add(sprite);
	
	    object.add(animation);
	    object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
	    
	    BasicColorComponent color = (BasicColorComponent)allocateComponent(BasicColorComponent.class);
	    color.sprite = sprite;
	    object.add(color);
	    
	    PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
	    physics.location.setX(positionX);
	    physics.location.setY(positionY);
	    physics.location.setZ(0);

	    ControlledVectorObject vector = (ControlledVectorObject)allocateComponent(ControlledVectorObject.class);
	    vector.initialize(10, physics.location, 0, 0);
	    vector.setVelocityMagDir(0, 0);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, 8);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = VoidObjectRegistry.PhysicsObjectTypes.MOB1;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    PlayerCollisionComponent collision = (PlayerCollisionComponent)allocateComponent(PlayerCollisionComponent.class);
	    
	    collision.vector = vector;
	    collision.setPhysicsObject(object.physcisObject, object);
	    object.add(collision);
        if(boundary == null || circle == null || physics == null || collision == null || vector == null ){
        	DebugLog.i("Level", "ERROR: component is null");
        }
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
	    
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);  
	    object.attributes.health = object.attributes.maxHealth = 100000;
	    object.add(object.attributes);
	    addStaticData(GameObjectType.PLAYER.ordinal(), object, sprite);

	    sprite.playAnimation(0);
	    object.setCurrentDirection(0);
	    object.commitUpdates();
	    return object;
	}
}
