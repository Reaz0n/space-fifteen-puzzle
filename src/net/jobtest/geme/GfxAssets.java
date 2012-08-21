package net.jobtest.geme;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

public class GfxAssets {

    private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
    private RepeatingSpriteBackground mBackgroundSpace;
    private TextureRegion[] mAstroidsTextureRegion;
    private Font mFont;
    private ITextureRegion mLogoTextureRegion;
    
    public static final int AST_COUNT = 15;

    public GfxAssets(final BaseGameActivity activity, final Engine engine) {

	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
		activity.getTextureManager(), 1024, 1024,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	
	// Load background
	this.mBackgroundSpace = new RepeatingSpriteBackground(
		GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT,
		activity.getTextureManager(),
		AssetBitmapTextureAtlasSource.create(activity.getAssets(),
			"gfx/background_space.png"),
		activity.getVertexBufferObjectManager());
	
	// Load StepCount font
	final ITexture fontTexture = new BitmapTextureAtlas(
		activity.getTextureManager(), 400, 200, TextureOptions.BILINEAR);
	this.mFont = new Font(activity.getFontManager(), fontTexture,
		Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 36, true,
		Color.WHITE);
	this.mFont.load();

	// Load game logo
	final BitmapTextureAtlas mLogoTextureAtlas = new BitmapTextureAtlas(
		activity.getTextureManager(), 400, 100,
		TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	this.mLogoTextureRegion = BitmapTextureAtlasTextureRegionFactory
		.createFromAsset(mLogoTextureAtlas, activity, "logo_top1.png",
			0, 0);
	mLogoTextureAtlas.load();
	
	// Load game objects
	this.mAstroidsTextureRegion = new TextureRegion[AST_COUNT];
	for (int i = 0; i < AST_COUNT; i++) {
	    this.mAstroidsTextureRegion[i] = BitmapTextureAtlasTextureRegionFactory
		    .createFromAsset(mBitmapTextureAtlas, activity, "asteroid"
			    + (i + 1) + ".png");
	}
	try {
	    this.mBitmapTextureAtlas
		    .build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
			    BitmapTextureAtlas>(0, 0, 1));
	    this.mBitmapTextureAtlas.load();
	} catch (TextureAtlasBuilderException e) {
	    Debug.e(e);
	}

    }
    
    public RepeatingSpriteBackground getBackgroundSpace() {
	return this.mBackgroundSpace;
    }

    /**
     * @param number int > 0 && int <15 
     * @return TextureRegion with texture;
     */
    public TextureRegion getAstroid(int number) {
	if(!(number >= 0 && number < 15)) {
	    number = 0;
	    Log.e("GfxAssets", "Incorrect number");
	}
	return mAstroidsTextureRegion[number];
    }
    
    public TextureRegion[] getAstroids() {
	return mAstroidsTextureRegion;
    }
    
    public Font getFont() {
	return this.mFont;
    }
    
    public ITextureRegion getLogo() {
	return this.mLogoTextureRegion;
    }
    
}
