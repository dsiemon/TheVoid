package des.game.theVoid;

import des.game.base.BaseObject;
import des.game.base.Vector2;
import des.game.drawing.DrawableBitmap;
import des.game.drawing.Texture;
import des.game.scale.CameraSystem;
import des.game.scale.ContextParameters;
import des.game.scale.GameThread;
import des.game.scale.InputTouchButton;
import des.game.scale.RenderSystem;


public class UISystem extends BaseObject {
    private static final int MAX_DIGITS = 4;
    
	private DrawableBitmap[] digitDrawables;
	
    // fps
    private int fps;
    private Vector2 FPSLocation;
    private int[] FPSDigits;
    private boolean FPSChanged;
    private boolean showFPS;
    
    
    // button
    InputTouchButton moveButton;
    InputTouchButton attackButton;
	public UISystem(){
		super();
		
		digitDrawables = new DrawableBitmap[10];
		
	    fps = 0;
	    FPSLocation = new Vector2();
	    FPSDigits = new int[MAX_DIGITS];
	    FPSDigits[0] = -1;
	    FPSChanged = false;
	    showFPS = false;
	    
	    moveButton = new InputTouchButton(0,0,224,224);
	    
	    moveButton.priority = SortConstants.HUD + 5;
	    moveButton.indicatorOffset.set(-16,-16);
	    
	    BaseObject.sSystemRegistry.inputSystem.getScreen().AddButton(moveButton);
	    
	    attackButton = new InputTouchButton(BaseObject.sSystemRegistry.contextParameters.gameWidth - 64,0,64,64);
	    
	    attackButton.priority = SortConstants.HUD + 5;
	    
	    BaseObject.sSystemRegistry.inputSystem.getScreen().AddButton(attackButton);
	}
	
	@Override
	public void reset() {
	    fps = 0;
	    FPSDigits[0] = -1;
	    FPSLocation.zero();
	    FPSChanged = false;
	    showFPS = false;
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		//final RenderSystem render = sSystemRegistry.renderSystem;
		//final CameraSystem camera = BaseObject.sSystemRegistry.cameraSystem;
		final ContextParameters params = sSystemRegistry.contextParameters;
		
		moveButton.drawButton();
		attackButton.drawButton();
		
		// draw fps
		if (showFPS) {
			this.setFPS(GameThread.curretFPS);
        	if (FPSChanged) {
            	int count = intToDigitArray(fps, FPSDigits);
            	FPSChanged = false;
                FPSLocation.set(params.gameWidth - 10.0f - ((count + 1) * (digitDrawables[0].getWidth() / 2.0f)), 10.0f);

            }
 
            drawNumber(FPSLocation, FPSDigits, null, 1, 0);
        }
	}
	
	// utility functions
	public int intToDigitArray(int value, int[] digits) {
    	int characterCount = 1;
        if (value >= 1000) {
            characterCount = 4;
        } else if (value >= 100) {
            characterCount = 3;
        } else if (value >= 10) {
            characterCount = 2;
        }
        
    	int remainingValue = value;
        int count = 0;
	    do {
	        int index = remainingValue != 0 ? remainingValue % 10 : 0;
	        remainingValue /= 10;
	        digits[characterCount - 1 - count] = index;
	        count++;
	    } while (remainingValue > 0 && count < digits.length);
	    
	    if (count < digits.length) {
	    	digits[count] = -1;
	    }
	    return characterCount;
    }
	
	private void drawNumber(Vector2 location, int[] digits, DrawableBitmap label, int sortOffset, int labelOffset) {
        final RenderSystem render = sSystemRegistry.renderSystem;
        
        if (digitDrawables[0].getWidth() == 0) {
            // first time init
            for (int x = 0; x < digitDrawables.length; x++) {
                Texture tex = digitDrawables[x].getTexture();
                digitDrawables[x].resize(tex.width, tex.height);
            }
        }
        
        if (label != null && label.getWidth() == 0) {
            // first time init
            Texture tex = label.getTexture();
            label.resize(tex.width, tex.height);
        }
        
        final float characterWidth = digitDrawables[0].getWidth() / 2.0f;
        float offset = 0.0f;
        
        if (label != null) {
            render.scheduleForDraw(label, location, SortConstants.HUD + sortOffset, false); 
            location.x += labelOffset;
            offset += labelOffset;
         }
        
        for (int x = 0; x < digits.length && digits[x] != -1; x++) {
            int index = digits[x];
            DrawableBitmap digit = digitDrawables[index];
            if (digit != null) {
                render.scheduleForDraw(digit, location, SortConstants.HUD + sortOffset, false);
                location.x += characterWidth;
                offset += characterWidth;
            }
        }
        
        location.x -= offset;
        
        
    }
	
	public void setDigitDrawables(DrawableBitmap[] digits) {
        
        for (int x = 0; x < digitDrawables.length && x < digits.length; x++) {
            digitDrawables[x] = digits[x];
        }
    }

	public void setShowFPS(boolean b) {
		this.showFPS = b;
		
	}
	public void setFPS(int fps){
		this.FPSChanged = this.fps != fps;
		this.fps = fps;
	}
	
	public void setMoveButtonDrawable(DrawableBitmap idle, DrawableBitmap down, DrawableBitmap indicator){
		this.moveButton.idleIcon = idle;
		this.moveButton.pressedIcon = down;
		this.moveButton.indicatorIcon = indicator;
		
	}
	
	public void setAttackButtonDrawable(DrawableBitmap idle, DrawableBitmap down){
		this.attackButton.idleIcon = idle;
		this.attackButton.pressedIcon = down;
		this.attackButton.indicatorIcon = null;
		

		
	}
}
