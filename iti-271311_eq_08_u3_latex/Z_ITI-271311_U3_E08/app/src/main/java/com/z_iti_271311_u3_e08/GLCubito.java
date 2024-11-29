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

import javax.microedition.khronos.opengles.GL10;



public class GLCubito extends Cubito{

	/**
	 * Otro constructor, si se le indica cual es la posicion del cubito tiene que saber
	 * colocarlo en su posicion
	 * 
	 * @param i
	 * @param j
	 * @param k
	 * @param vertices son los vertices del Cubito 
	 * @param dimension es la dimension del cubo
	 */
	public GLCubito(int id,int i,int j,int k,float[] vertices,int dimension,float lado)
	{
		super(id,i,j,k,vertices,dimension,lado);
	}
	
	
	
	/**
	 * Se encarga de realizar las rotaciones segun en la direccion en la que se este girando
	 * teniendo en cuenta que el ultimo angulo a girar tiene que ser en el que se esta realizando
	 * el movimiento
	 * 
	 * @param gl
	 */
	private void rotarPosicion(GL10 gl) {
		/*
		gl.glPushMatrix();
		
		float[] m={
				this._matrix[0],this._matrix[1],this._matrix[2],0,
				this._matrix[3],this._matrix[4],this._matrix[5],0,
				this._matrix[6],this._matrix[7],this._matrix[8],0,
				0,0,0,1
		};
		
		gl.glLoadMatrixf(m,0);
		*/
		
		gl.glRotatef(this._anguloXposicion, 1, 0, 0);
		gl.glRotatef(this._anguloYposicion, 0, 1, 0);
		gl.glRotatef(this._anguloZposicion, 0, 0, 1);
		
		

	}
	
	/**
	 * Se encarga de realizar el giro sobre el eje central del cubito segun la direccion en la que vaya el movimiento.
	 * 
	 * @param gl
	 */
	private void rotarGiro(GL10 gl) {
		//Giro
		//if(this._angulomovimiento==0 )gl.glRotatef(this._anguloXgiro, _matrix[0], _matrix[1], _matrix[2]);
		//if(this._angulomovimiento==1 )gl.glRotatef(this._anguloYgiro, _matrix[3], _matrix[4], _matrix[5]);
		//if(this._angulomovimiento==2 )gl.glRotatef(this._anguloZgiro, _matrix[6], _matrix[7], _matrix[8]);
		
		if(this._angulomovimiento==0 )gl.glRotatef(this._anguloXgiro, 1, 0, 0);
		if(this._angulomovimiento==1 )gl.glRotatef(this._anguloYgiro, 0, 1, 0);
		if(this._angulomovimiento==2 )gl.glRotatef(this._anguloZgiro, 0, 0, 1);
	}

	
	private void transladar(GL10 gl) {
		gl.glTranslatef(this._PosicionX,this._PosicionY, this._PosicionZ);
	}

	public void pintarAristas(GL10 gl) {
		
        
		gl.glPushMatrix();
		
		//Rotar Giro Actual
		rotarGiro(gl);
		
		//Lo posicionamos
		transladar(gl);
		
		//Rotaciones
		rotarPosicion(gl);
		
		//Se dibuja
		for(int i=0;i<_numcaras;++i){
			//if(_carasValidas[i])
				_caras[i].pintarAristas(gl);

		}	
		
		gl.glPopMatrix();
		
	}


	public void pintarColor(GL10 gl) {
		
		gl.glPushMatrix();

		//Rotar Giro Actual
		rotarGiro(gl);
		
		//Lo posicionamos
		transladar(gl);
		
		//RotacionesPosiciones
		rotarPosicion(gl);
		
		//Se dibuja
		for(int i=0;i<_numcaras;++i){
			//if(_carasValidas[i])
				_caras[i].pintarColor(gl);
		}
		
		gl.glPopMatrix();

	}



	public void pintarTexturas(GL10 gl) {
		
		//Se dibuja
		for(int i=0;i<_numcaras;++i){
			//if(_carasValidas[i])
				_caras[i].pintarTextura(gl);
		}
    	
    	
		
	}



}
