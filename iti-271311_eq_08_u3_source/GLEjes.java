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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class GLEjes {
	
	private float[] _verticesX={0f,0f,0f, 100f,0f,0f};
	private float[] _verticesY={0f,0f,0f, 0f,100f,0f};
	private float[] _verticesZ={0f,0f,0f, 0f,0f,100f};
	
	FloatBuffer _bufferX;
	FloatBuffer _bufferY;
	FloatBuffer _bufferZ;
	
	
	public GLEjes(){
		initEjes();
	}
	
	
    private void initEjes(){

        
        // initialize vertex Buffer for triangle  
        ByteBuffer vbb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                _verticesX.length * 4); 
        vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
        _bufferX = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        _bufferX.put(_verticesX);    // add the coordinates to the FloatBuffer
        _bufferX.position(0);            // set the buffer to read the first coordinate
        
        
        // initialize vertex Buffer for triangle  
        ByteBuffer vbb2 = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                _verticesY.length * 4); 
        vbb2.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
        _bufferY = vbb2.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        _bufferY.put(_verticesY);    // add the coordinates to the FloatBuffer
        _bufferY.position(0);            // set the buffer to read the first coordinate
        
        
        // initialize vertex Buffer for triangle  
        ByteBuffer vbb3 = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                _verticesZ.length * 4); 
        vbb3.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
        _bufferZ = vbb3.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        _bufferZ.put(_verticesZ);    // add the coordinates to the FloatBuffer
        _bufferZ.position(0);            // set the buffer to read the first coordinate
    
    } 

    /**
     * Pinta los ejes que se indiquen en las variables con una longitud maxima de 100 unidades
     * 
     * @param gl
     * @param pintarX
     * @param pintarY
     * @param pintarZ
     */
	public void pintarEjes(GL10 gl, boolean pintarX, boolean pintarY, boolean pintarZ)
	{
		
		RenderizadoCubo.situarCamara(gl);
		  
		if(pintarX)
		{
			//Verde
			gl.glColor4f(0f, 1f, 0f, 0f);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _bufferX);
	        gl.glDrawArrays(GL10.GL_LINES, 0, 2);
	        
		}
		
		if(pintarY)
		{
			//Rojo
			gl.glColor4f(1f, 0f, 0f, 0f);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _bufferY);
	        gl.glDrawArrays(GL10.GL_LINES, 0, 2);
		}
		
		if(pintarZ)
		{
			//Azul
			gl.glColor4f(0f, 0f, 1f, 0f);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _bufferZ);
	        gl.glDrawArrays(GL10.GL_LINES, 0, 2);
		}

	}
}
