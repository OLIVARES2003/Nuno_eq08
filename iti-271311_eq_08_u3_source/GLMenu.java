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

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

@SuppressWarnings("unused")
public class GLMenu {

	/*
	 * Esa clase se encargara de generar y gestionar el menu en 2D del juego.
	 * 
	 * En principio tendra dos tipos de ventana:
	 * 
	 * Pantalla principal (Se ejecutara al arrancar el juego)
	 * 
	 * Dos Botones: Iniciar Partida y Salir
	 * Indicara la dimension y dejara mover estos valores
	 * 
	 * Pausa (Se ejecutara al parar el juego)
	 * 
	 * 
	 */
	
	//Dimensiones del menu
	public float _width=0;
	public float _height=0;
	
	//Indica el valor maximo y minimo de la dimension del cubo

	private int _maxDimensionCubo;
	private int _minDimensionCubo;
	
	/*
	 * Estado del menu, segun esto se mostrara una pantalla de menu diferente.
	 * 
	 * 0: No hay menu
	 * 1: Menu de Inicio
	 * 2: Menu de Pausa
	 */
	public int _estadoMenu;
	
	
	
    //PALETA RGB (Red, Green, Blue) para el fondo del menu
    private float _rojo = 1f;
    private float _verde = 0.5f;
    private float _azul = 0f;
    private float _alpha = 1f;
    GLCara _fondo;
	
	public GLMenu()
	{
		_estadoMenu=0;
		_maxDimensionCubo=6;
		_minDimensionCubo=2;
		
		//Creamos la cara de fondo
		//float[] verticesFondo= { };
		
		//_fondo = new GLCara(_rojo,_verde,_azul,_alpha,verticesFondo);
		
		
	}
	
	public void pintarMenu(GL10 gl){
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		
		GLU.gluOrtho2D(gl, 0, _width ,_height, 0);
		
		if(_estadoMenu==0){
			System.out.println("W: "+_width+"H: "+_height);
			_estadoMenu=1;
		}
		
		//_fondo.pintarColor(gl);
		
	}
	
	private void pintarfondo(GL10 gl){
		
	}
}
