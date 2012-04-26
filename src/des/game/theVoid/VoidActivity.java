package des.game.theVoid;

import android.os.Bundle;
import android.view.KeyEvent;
import des.game.scale.Game;
import des.game.scale.ScaleActivity;

public class VoidActivity extends ScaleActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void handleCreate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Game createGame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void handlePause() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleResume() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onMenuButton(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean onBackButton(int keyCode, KeyEvent event) {
		final long time = System.currentTimeMillis();
		if (time - mLastRollTime > ROLL_TO_FACE_BUTTON_DELAY &&
				time - mLastTouchTime > ROLL_TO_FACE_BUTTON_DELAY) {
			showDialog(QUIT_GAME_DIALOG);
		}
		
		return true;
	}

	@Override
	protected void extensionGameFlowEvent(int eventCode, int index) {
		// TODO Auto-generated method stub

	}

}
