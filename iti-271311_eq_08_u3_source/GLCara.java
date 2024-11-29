/*
 *     This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.z_iti_271311_u3_e08;

import android.graphics.Bitmap;

import javax.microedition.khronos.opengles.GL10;

/**
 * La Cara puede ser de 6 colores diferentes:
 * (0)-Blanco
 * (1)-Rojo
 * (2)-Verde
 * (3)-Azul
 * (4)-Amarillo
 * (5)-Rosa
 * 
 * Hay un septimo color que es el negro que se usa para las caras sin color:
 * (6)-Negro
 * 
 * En su constructor se le pasara el codigo del color que se quiera la cara.
 * 
 * @author Gorka Revilla Fernandez
 *
 */
public class GLCara extends Cara{


	private boolean _cargarTextura=false;

	private Bitmap _bitmap;



	public GLCara(int color, float[] vertices) {
		super(color, vertices);
		
		
		//cargarBitmap(BitmapFactory.decodeResource(_vista.getResources(),this._img));
	}

	@Deprecated
	public GLCara(float red, float green,float blue,float alpha, float[] vertices) {
		super(red,green,blue,alpha, vertices);
	}



	public void pintarAristas(GL10 gl)
	{

		gl.glColor4f(0f, 0f, 0f, 0f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _verticesBufferA);
		gl.glDrawElements(GL10.GL_LINES, 8, GL10.GL_UNSIGNED_SHORT, _indiceBufferA);
	}

	public void pintarColor(GL10 gl)
	{

		gl.glColor4f(_rojo, _verde, _azul, _alpha);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _verticesBuffer1);
		gl.glDrawElements(GL10.GL_TRIANGLES, _numeroVertices1, GL10.GL_UNSIGNED_SHORT, _indiceBuffer1);
		gl.glDrawElements(GL10.GL_TRIANGLES, _numeroVertices2, GL10.GL_UNSIGNED_SHORT, _indiceBuffer2);
	}



	public void pintarTextura(GL10 gl)
	{
		if (_cargarTextura) {
			cargarGLTexture(gl);
			_cargarTextura = false;
		}


		gl.glEnable(GL10.GL_TEXTURE_2D);
		// Enable the texture state
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		// Point to our buffers
		//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
		//gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

	}


	/**
	 * Set the bitmap to load into a texture.
	 * 
	 * @param bitmap
	 */
	public void cargarBitmap(Bitmap bitmap) { // New function.
		this._bitmap = bitmap;
		_cargarTextura = true;
	}

	/**
	 * Carga la textura.
	 * 
	 * @param gl
	 */
	private void cargarGLTexture(GL10 gl) { 

		// Generate one texture pointer...
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		//mTextureId = textures[0];

		// ...and bind it to our array
		//gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		//GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
	}




}
