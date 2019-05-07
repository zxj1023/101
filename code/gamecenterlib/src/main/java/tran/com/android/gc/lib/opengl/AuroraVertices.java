package tran.com.android.gc.lib.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import tran.com.android.gc.lib.view.AuroraGLSurfaceView;

public class AuroraVertices {

	GL10 mGL;
	
	private boolean hasColor;
	
	private boolean hasTexCoords;
	
	/**
	 * @vertical size per
	 */
	private int vertextSize;
	
	/**
	 * @vertices buffer
	 */
	private FloatBuffer vertices;
	
	/**
	 * @indices buffer
	 */
	private ShortBuffer indices;
	
	/**
	 * 
	 * @param view
	 * @param maxVertices
	 * @param maxIndices
	 * @param hasColor
	 * @param hasTexCoords
	 */
	public AuroraVertices(AuroraGLSurfaceView view , int maxVertices , int maxIndices ,boolean hasColor , boolean hasTexCoords)
	{
		mGL = view.getGL();
		
		this.hasColor = hasColor;
		
		this.hasTexCoords = hasTexCoords;
		
		this.vertextSize = (2 + (hasColor?4:0) + (hasTexCoords?2:0)) * 4;
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertextSize);
		
		buffer.order(ByteOrder.nativeOrder());
		
		vertices = buffer.asFloatBuffer();
		
		if(maxIndices > 0)
		{
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			
			buffer.order(ByteOrder.nativeOrder());
			
			indices = buffer.asShortBuffer();
		}
		else
		{
			indices = null;
		}
	}
	
	/**
	 * 
	 * @param vertices
	 * @param offset
	 * @param length
	 */
	public void setVertices(float[] vertices, int offset , int length)
	{
		this.vertices.clear();
		
		this.vertices.put(vertices, offset, length);
		
		this.vertices.flip();
	}
	
	/**
	 * called by AuroraGLDrawable
	 * @param alpha
	 */
	public void setAlpha(float alpha)
	{
		int numElementsPerVertic = 8;
		
		int numVertics = vertices.capacity()/numElementsPerVertic;
		
		for(int i=0; i < numVertics; i++)
		{
			vertices.position(0);
			//change the alpha data at aplha position !!!
			vertices = vertices.put( i * numElementsPerVertic + 5, alpha);
		}
	}
	
	/**
	 * 
	 * @param indices
	 * @param offset
	 * @param length
	 */
	public void setIndices(short[] indices , int offset , int length)
	{
		this.indices.clear();
		
		this.indices.put(indices, offset, length);
		
		this.indices.flip();
	}
	
	/**
	 * @show vertices and it's content on Screen
	 * @param primitiveType : shape type
	 * @param offset
	 * @param numVetices
	 */
	public void draw(int primitiveType , int offset , int numVetices)
	{
		GL10 gl = mGL;
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		vertices.position(0);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertextSize, vertices);
		
		if(hasColor)
		{
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			
			vertices.position(2);
			
			gl.glColorPointer(4, GL10.GL_FLOAT, vertextSize, vertices);
		}
		
		if(hasTexCoords)
		{
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			vertices.position(hasColor?6:2);
			
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertextSize, vertices);
		}
		
		if(indices != null)
		{
			indices.position(offset);
			
			gl.glDrawElements(primitiveType, numVetices, GL10.GL_UNSIGNED_SHORT, indices);
		}
		else
		{
			gl.glDrawArrays(primitiveType, offset, numVetices);
		}
		
		if(hasColor)
		{
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		
		if(hasTexCoords)
		{
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}
}
