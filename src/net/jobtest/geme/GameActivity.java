package net.jobtest.geme;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import android.util.Log;
import android.widget.Toast;

public class GameActivity extends SimpleBaseGameActivity implements IOnAreaTouchListener {

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;
    
    private long stepCount = 0;
    private GfxAssets mTextures;
    private Scene mGameScene;
    private Sprite[] mAstroidSprites;
    private Game mGame;
    
    private Text mStepCountText;
    
    @Override
    public EngineOptions onCreateEngineOptions() {
	Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
		new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
    }

    @Override
    protected void onCreateResources() {
	mTextures = new GfxAssets(this, this.mEngine);
    }

    @Override
    protected Scene onCreateScene() {
	//this.mEngine.registerUpdateHandler(new FPSLogger());
	mGameScene = new Scene();
	mGameScene.setBackground(mTextures.getBackgroundSpace());
	mGameScene.setOnAreaTouchListener(this);
	mGame = new Game();
	
	final Sprite mLogoSprite = new Sprite(40, 10, mTextures.getLogo(),
		this.getVertexBufferObjectManager());
	mGameScene.attachChild(mLogoSprite);
	
	mAstroidSprites = new Sprite[15];
	for (int i = 0; i < 15; i++) {
	    mAstroidSprites[i] = new Sprite(
		    0, 0, mTextures.getAstroid(i),
		    this.getVertexBufferObjectManager());
	    mGameScene.registerTouchArea(mAstroidSprites[i]);
	    mGameScene.attachChild(mAstroidSprites[i]);
	}
	
	mStepCountText = new Text(40, 600, mTextures.getFont(), "Количество ходов: 0",
		this.getVertexBufferObjectManager());
	mGameScene.attachChild(mStepCountText);
	
	updateField(40,150);
	
	/*mGameScene.registerUpdateHandler(new IUpdateHandler() {
	    
	    @Override
	    public void reset() {
		// TODO Auto-generated method stub
		
	    }
	    
	    @Override
	    public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	    }
	});*/
	
	return mGameScene;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
	    ITouchArea pTouchArea, float pTouchAreaLocalX,
	    float pTouchAreaLocalY) {
	if (pSceneTouchEvent.isActionDown()) {
	    if (mGame.move(
		    sceneXToGame((int) pSceneTouchEvent.getX()),
		    sceneYToGame((int) pSceneTouchEvent.getY()))) {
		updateField(40,150);
		
		stepCount++;
		
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {

			mStepCountText.setText("Количество ходов: " + stepCount);
		    }
		});
		
		if (mGame.checkGameOver()) {
		    runOnUiThread(new Runnable() {
			@Override
			public void run() {

			    Toast.makeText(
				    getApplicationContext(),
				    "Ура! Вы победили! Количество ходов: "
					    + stepCount, Toast.LENGTH_LONG)
				    .show();
			}
		    });
		    stepCount = 0;
		}
		
	    }
	    
	    return true;
	    
	}
	return false;
    }
    
    public int sceneXToGame(int x) {
	int pX = (int) (CAMERA_WIDTH / 2 - (4 * (mAstroidSprites[0].getWidth()) / 2));
	x = x - pX;
	x = (int) (x / mAstroidSprites[0].getWidth());
	Log.i("GameActivity.sceneXToGame()", "x = " + x);
	return x;
    }

    public int sceneYToGame(int y) {
	int pY = 150;
	y = y - pY;
	y = (int) (y / mAstroidSprites[0].getWidth());
	y = y < 4 ? y : y - 1;
	Log.i("GameActivity.sceneYToGame()", "y = " + y);
	return y;
    }
    
    public void updateField(float pX, float pY) {
	int field[][] = mGame.field;
	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {

		if (field[i][j] != 0) {
		    mAstroidSprites[field[i][j] - 1].setPosition(pX
			    + mAstroidSprites[field[i][j] - 1].getWidth() * i,
			    pY + mAstroidSprites[field[i][j] - 1].getHeight()
				    * j);
		}

	    }
	}
    }

}
