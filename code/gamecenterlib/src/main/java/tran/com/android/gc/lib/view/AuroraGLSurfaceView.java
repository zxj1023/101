package tran.com.android.gc.lib.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

public class AuroraGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer,SurfaceHolder.Callback {
	
	
	private final String TAG = "AuroraGLSurfaceView";
	
	protected final boolean Debug = false;
	
	long startTime;
	
	long currentTime;
	
	long fpts = 0;
	
	private void fpsTest()
	{
		if(fpts == 0)
		{
			startTime = currentTime = System.currentTimeMillis();
		}
		else
		{
			currentTime = System.currentTimeMillis();
		}
		
		if(currentTime - startTime >= 1000)
		{
			if(Debug)
				Log.e(TAG,"total fpts = " + fpts);
			
			fpts = 0; 
		}
		else
		{
			fpts++;
		}
	}
	
	/**
	 *  @Paddings
	 */
	protected int mPaddingLeft;
	protected int mPaddingRight;
	protected int mPaddingTop;
	protected int mPaddingBottom;
	
	/**
	 * @the distance to camera , the nearest and fast 
	 * @ mFarToCamera - mNearToCamera = z height
	 */
	private int mNearToCamera = 1;
	private int mFarToCamera = -1;
	
	/**
	 * @opengl canvas
	 */
	private GL10 mGL;
	
	/**
	 * @colors for background
	 */
	private final int RED = 0xff000000;
	private final int GREEN = 0x00ff0000;
	private final int BLUE = 0x0000ff00;
	private final int ALPHA = 0x000000ff;
	
	/**
	 * @vlaues = [0,1] 
	 */
	private float mRed , mGreen , mBlue, mAlpha;
	
	/**
	 * canvas draw all contents on this bitmap, 
	 * I do not care user draw what on it
	 */
	
	public AuroraGLSurfaceView(Context context) {
		this(context , null);
		// TODO Auto-generated constructor stub
	}

	public AuroraGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mPaddingLeft = getPaddingLeft();
		
		mPaddingRight = getPaddingRight();
		
		mPaddingTop = getPaddingTop();
		
		mPaddingBottom = getPaddingBottom();
		
		init(context);
	}
	
	private void init(Context context)
	{
		setBgTranslucentEnable();
		
		setSurfaceBackground(0 , 0 , 0 , 0);
		
		//must be after setBgTranslucentEnable()
		setRenderer(this);
		
		//save cpu this mode !!!
		setRenderModeDirty();

	}
	
	/**
	 * set TRANSLUCENT enable
	 */
	private void setBgTranslucentEnable()
	{
		setZOrderOnTop(true);
		
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}
	
	/**
	 * @in this mode ,we should update surface by ourself with invalidateView() !!!!
	 */
	public void setRenderModeDirty()
	{
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	/**
	 * @in this mode , it can update surface by itself
	 */
	public void setRenderModeAuto()
	{
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		if(Debug)
			fpsTest();
		
		setSurfaceBackground();

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		doDraw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		
		gl.glLoadIdentity();
		
		gl.glOrthof(0, width, 0, height, mNearToCamera, mFarToCamera);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		mGL = gl;
		
		/*start alpha with verticals*/
		blendAlpha(gl);
		
		/* just draw 2d texture temply */
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
	}
	
	/**
	 *  private final int RED = 0xff000000;
	 *	private final int GREEN = 0x00ff0000;
	 *	private final int BLUE = 0x0000ff00;
	 *	private final int ALPHA = 0x000000ff;
	 * @param background 
	 */
	public void setSurfaceBackground(int background)
	{
		int red = (int) (background & RED) >>> 24;
		int green = (int) ((background & GREEN) >>> 16);
		int blue = (int) ((background & BLUE) >>> 8);
		int alpha = (int) (background & ALPHA);

		setSurfaceBackground(red/255f , green/255f , blue/255f , alpha/255f);
	}
	
	/**
	 * 
	 * @param red : [0,1]
	 * @param green : [0,1]
	 * @param blue : [0,1]
	 * @param alpha : [0,1]
	 */
	public void setSurfaceBackground(float red , float green , float blue , float alpha)
	{
		mRed = red;
		
		mGreen = green;
		
		mBlue = blue;
		
		mAlpha = alpha;
		
	}
	
	private void setSurfaceBackground()
	{
		GL10 gl = mGL;
		
		gl.glClearColor(mRed, mGreen, mBlue, mAlpha);
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * @user should draw someThing at here !!!!!
	 * @param gl
	 */
	protected void doDraw(GL10 gl)
	{
		
	}
	
	/**
	 * @set bitmap alpha and background alpha blend
	 * @param gl
	 */
	public void blendAlpha(GL10 gl)
	{
		gl.glEnable(GL10.GL_BLEND);
		
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	/**
	 * @get GL10 object
	 * @return GL10
	 */
	public GL10 getGL()
	{
		return mGL;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int width = widthSize;
        int height = heightSize;
        
        Rect rect = getRect();
        
        if (widthMode == MeasureSpec.AT_MOST) {
            // Parent has told us how big to be. So be it.
        	width = rect.width() + mPaddingLeft + mPaddingRight;
        }
        
        if (heightMode == MeasureSpec.AT_MOST) {
            // Parent has told us how big to be. So be it.
        	height = rect.height() + mPaddingTop + mPaddingBottom;
        }
        
        super.setMeasuredDimension(width, height);
	}
	
	/**
	 * @sub Class Override this method to compute View's size
	 * @return
	 */
	protected Rect getRect()
	{
		return new Rect();
	}
	
	/**
	 * 
	 * @param d
	 * @return
	 */
	public Bitmap drawableToBitmap(Drawable d)
	{
		Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		
		//must set bounds ,otherwise not visible
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		
		d.draw(canvas);
		
		return bitmap;
	}
	
	public void invalidateView()
	{
		requestRender();
	}
}
