package des.game.theVoid;

public class VoidObjectRegistry {

	public static VoidObjectFactory gameObjectFactory;
	public static UISystem uiSystem;
	
	public static class PhysicsObjectTypes{
		public static final int PASSIVE_TYPE = 0;
		public static final int INVISIBLE_WALL = 1;
		public static final int LOW_WALL = 2;
		public static final int MOB1 = 3;
		public static final int MOB1_PROJECTILE = 4;
		public static final int MOB2 = 5;
		public static final int MOB2_PROJECTILE = 6;
		public static final int SPAWNER = 7;

		public static final int COUNT = 8;
	}

}
