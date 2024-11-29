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


public class GLCubo extends Cubo{





	/**
	 * Constructor
	 * 
	 */
	public GLCubo()				
	{

		int cont=0;
		for(int i=0;i<_dimension;++i){
			for(int j=0;j<_dimension;++j){
				for(int k=0;k<_dimension;++k){
					//Creamos el cubito
					_cubos[i][j][k]= new GLCubito(cont,i,j,k,calcularVerticesCubitoCentrales(_dimension),_dimension,_ui);

					//Le anyadimos un id de identificacion
					cont++;
				}
			}
		}
		this.initReferencias();

	}

	



	private void initReferencias()
	{
		for(int i=0;i<_dimension;++i){
			for(int j=0;j<_dimension;++j){
				for(int k=0;k<_dimension;++k){
					//Anyadimos la referencia a ese cubito
					_ArrayReferenciaCubitos[i][j][k] = new ReferenciaCubito(i,j,k);
				}
			}
		}

	}

	/**
	 * Realiza el giro mediante OpenGL, sumara a la cara correspondiente en su linea indicada
	 * la diferencia a aplicar al angulo, esta diferencia puede ser positiva o negativa.
	 * 
	 * 
	 * @param cara
	 * @param linea
	 * @param diff
	 */
	public void girarReferenciasGL(int cara,int linea, float diff){
		
		
		//System.out.println("GiroReferencias Cara: "+cara+" Linea: "+linea);
		
		if(cara!=1)	linea=_dimension-1-linea; //Las caras 0 y 2 son inversas al array
		
		//System.out.println("GiroReferencias NuevaLinea: "+linea);
		
		for(int i=0;i<_dimension;++i)
		{
			for(int k=0;k<_dimension;++k)
			{
				//Cara frontal
				if(cara==0)
				{
					//linea,k,i
					this.getCubitoReferenciado(linea, k, i)._angulomovimiento=2;
					
					//Se ajusta al angulo correspondiente el angulo indicado
					this.getCubitoReferenciado(linea,k,i)._anguloZgiro+=diff;
					
					//System.out.println(this.getCubitoReferenciado(linea,k, i).id);

					

				}
				//Cara Izquierda
				else if(cara==1)
				{
					//k,i,linea
					this.getCubitoReferenciado(k, i,linea)._angulomovimiento=0;
					
					//Se ajusta al angulo correspondiente el angulo indicado
					this.getCubitoReferenciado(k, i, linea)._anguloXgiro+=diff;

					
					
					//System.out.println(this.getCubitoReferenciado(k, i, linea).id);
					
				}
				//Cara Superior
				else if(cara==2)
				{	
					//i,linea,k
					this.getCubitoReferenciado(i,linea,k)._angulomovimiento=1;
					
					//Se ajusta al angulo correspondiente el angulo indicado
					this.getCubitoReferenciado(i,linea,k)._anguloYgiro+=diff;
					
					

					//System.out.println(this.getCubitoReferenciado(i, linea,k).id);


				}


			}
		}


	}
	

	/**
	 * 
	 * Hace que una linea se ajuste a la posicion a la que deberia de estar. Este metodo deberia de ser
	 * llamado al soltar el cursor para que el cubo de rubik vuelva a ser un cubo.
	 * 
	 * @param cara Si es 0 es la cara derecha, si es 1 la frontal y si es 2 es la superior
	 * @param linea con cara 0 van de izquierda a derecha (de 0 a dimension-1) con cara 0 de fondo hacia adelante, y con 2 de abajo a arriba
	 */
	public float ajustarReferencias(int cara, int linea) {

		//System.out.println("Ajusto Cara: "+cara+" Linea: "+linea);
		
		
		if(cara!=1)	linea=_dimension-1-linea; //Las caras 0 y 2 son inversas al array
		
		float angulo=0;
		//Cara frontal
		if(cara==0)
		{
			//linea,k,i
			
			//Calculamos el angulo cercano de uno de los cubitos
			angulo=this.angulocercano(getCubitoReferenciado(linea,0,0)._anguloZgiro);
			
			//System.out.println("giroZ: "+angulo);

			//Se aplica a todos los cubitos de la fila
			for(int i=0;i<_dimension;++i)
			{
				for(int k=0;k<_dimension;++k)
				{
					//getCubitoReferenciado(linea,k,i).anadirAngulo(2,angulo);
					
					
					getCubitoReferenciado(linea,k,i)._anguloZgiro=0;
					
					
					
					
				}

			}


		}
		//Cara Izquierda
		else if(cara==1)
		{
			//k,i,linea
			
			//Calculamos el angulo cercano de uno de los cubitos
			angulo=this.angulocercano(getCubitoReferenciado(0,0,linea)._anguloXgiro);
			
			//System.out.println("giroX: "+angulo);

			//Se aplica a todos los cubitos de la fila
			for(int i=0;i<_dimension;++i)
			{
				for(int k=0;k<_dimension;++k)
				{
					//getCubitoReferenciado(k,i,linea).anadirAngulo(0,angulo);
					
					getCubitoReferenciado(k,i,linea)._anguloXgiro=0;

					

				}


			}
		}
		//Cara superior
		else if(cara==2)
		{	
			//i,linea,k
			
			//Calculamos el angulo cercano de uno de los cubitos
			angulo=this.angulocercano(getCubitoReferenciado(0, linea,0)._anguloYgiro);

			//System.out.println("giroY: "+angulo);
			
			//Se aplica a todos los cubitos de la fila
			for(int i=0;i<_dimension;++i)
			{
				for(int k=0;k<_dimension;++k)
				{
					//getCubitoReferenciado(i, linea,k).anadirAngulo(1,angulo);
					
					getCubitoReferenciado(i, linea,k)._anguloYgiro=0;

					

				}


			}
		}
		return angulo;

	}



	/**
	 * Calcula el angulo cercano al que esta un determinado angulo para que se ajuste a ese
	 * angulo y el cubito vuelva a una posicion manteniendo la forma de cubo.
	 * 
	 * 
	 * @param angulo
	 * @return
	 */
	private float angulocercano(float angulo) 
	{

		float nuevoangulo=0;
		angulo= angulo % 360;

		//Angulo 90
		if(angulo>45 && angulo<=135)
		{
			nuevoangulo=90;
		}
		//Angulo 180
		else if(angulo>135 && angulo<=225)
		{
			nuevoangulo=180;
		}
		//Angulo 270
		else if(angulo>225 && angulo<=315)
		{
			nuevoangulo=270;
		}//En caso de no entrar en ninguno no se habra movido lo suficiente como para cambiar de angulo, angulo=0


		//Angulos Negativos
		//Angulo 90
		if(angulo<-45 && angulo>=-135)
		{
			nuevoangulo=-90;
		}
		//Angulo 180
		else if(angulo<-135 && angulo>=-225)
		{
			nuevoangulo=-180;
		}
		//Angulo 270
		else if(angulo<-225 && angulo>=-315)
		{
			nuevoangulo=-270;
		}//En caso de no entrar en ninguno tampoco se habra movido lo suficiente y el angulo seguira siendo =0

		return nuevoangulo;
	}



	/**
	 * Se encarga de pintar las aristas mediante OpenGL del cubo, para lo cual usara
	 * el pintar de cada cubito.
	 * 
	 * @param gl
	 */
	public void pintarAristas(GL10 gl) 
	{

		for(int i=0;i<_dimension;++i){
			for(int j=0;j<_dimension;++j){
				for(int k=0;k<_dimension;++k){
					((GLCubito) _cubos[i][j][k]).pintarAristas(gl);
				}
			}
		}

	}


	/**
	 * Se encarga de pintar los colores mediante OpenGL del cubo, para lo cual usara
	 * el pintar de cada cubito.
	 * 
	 * @param gl
	 */
	public void pintarColor(GL10 gl)
	{

		for(int i=0;i<_dimension;++i){
			for(int j=0;j<_dimension;++j){
				for(int k=0;k<_dimension;++k){	
					((GLCubito) _cubos[i][j][k]).pintarColor(gl);
					
				}
			}
		}


	}





	/**
	 * Dibuja las texturas de los cubitos
	 * 
	 * @param gl
	 */
	public void pintarTexturas(GL10 gl) {
		
		for(int i=0;i<_dimension;++i){
			for(int j=0;j<_dimension;++j){
				for(int k=0;k<_dimension;++k){	
					((GLCubito) _cubos[i][j][k]).pintarTexturas(gl);
					
				}
			}
		}
		
	}










}
