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

public class Cubo {



	//Determina de cuanto es la dimension del cubo
	protected int _dimension = 3;

	/*
	 * Almacenamos las caras en un array tfridimensional
	 */
	public Cubito _cubos[][][];

	//Posicion central del cubo, Lo vamos a colocar en el centro de las coordenadas
	public float _posXinicial=0f;
	public float _posYinicial=0f;
	public float _posZinicial=0f;

	//Valor absoluto de las dimensiones del cubo
	protected float _ancho=2f;
	protected float _alto=2f;
	protected float _profundo=2f;

	//Define desde el punto central hasta cada lado el numero de puntos que hay.
	protected float _parteslado = ((float)_dimension)/2; 

	//Unidades de medida
	protected float _ui=_profundo/_dimension;
	protected float _uj=_alto/_dimension;
	protected float _uk=_ancho/_dimension;


	/*
	 * Array que hace referencia a los cubos almacenados en la matriz _cubos
	 * sobre esta matriz se realizaran todos los cambios que haya dejando la matriz de _cubos intacta
	 */
	protected ReferenciaCubito[][][] _ArrayReferenciaCubitos;

	/**
	 * 
	 * Los puntos serian los siguientes, siendo representado (x,y,z):
	 * ' 
	 *    5 __  6
	 *    /4 /*
	 * 1 *--*2| 7
	 *   |  |/
	 * 0 *--*3
	 * 
	 * 
	 * Tabla que indica la posicion de los puntos segun el esquema anterior:
	 * 
	 */
	private int[] _tablavertices=
		{
			//(x, y, z) (k,j,i)
			-1, -1, -1, //0
			-1, 1, -1,  //1
			1, 1, -1,   //2
			1, -1, -1,  //3
			-1, -1, 1,  //4
			-1, 1, 1,   //5
			1, 1, 1,    //6
			1, -1, 1,   //7

		};



	/**
	 * Constructor del cubo, inicializa tanto la matriz donde almacenamos los cubitos como la matriz de
	 * referencia a los cubitos.
	 * 
	 */
	public Cubo ()
	{
		_cubos= new GLCubito[_dimension][_dimension][_dimension];

		_ArrayReferenciaCubitos = new ReferenciaCubito[_dimension][_dimension][_dimension];
	}




	/**
	 * Devuelve el cubito al que hace referencia una determinada posicion
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Cubito getCubitoReferenciado(int x,int y,int z)
	{

		return _cubos[(_ArrayReferenciaCubitos[x][y][z]._x)][(_ArrayReferenciaCubitos[x][y][z]._y)][(_ArrayReferenciaCubitos[x][y][z]._z)];
	}


	/**
	 * Muestra la matriz de referencias
	 * 
	 */
	@Deprecated
	public void imprimirReferencias()
	{
		//Profundidad
		for(int i=0;i<_dimension;++i)
		{
			//Alto
			for(int j=0;j<_dimension;++j)
			{
				//Ancho
				for(int k=0;k<_dimension;++k)
				{
					//_ArrayReferenciaCubitos[i][j][k].imprimirReferencia();
					System.out.print(getCubitoReferenciado(i, j, k)._id+" ");
				}
				System.out.println();
			}

			System.out.println("-----------------------------");	
		}
	}



	/**
	 * Calcula los vertices del cubito centrandolo en el origen (0,0,0)
	 * 
	 * 
	 * @param _dimension
	 * @return
	 */
	protected float[] calcularVerticesCubitoCentrales(int dimension) {

		float ref=_ui/2;

		/*
		 * 
		 * 	 
		 *    4 __  5
		 *    /6 /*
		 * 0 *--*1| 7
		 *   |  |/
		 * 2 *--*3
		 * 
		 * 
		 * 
		 */

		/*
		//8 puntos x,y,z de izquierda a derecha y del frente a atras
		float[] VerticesCubito = {
				-ref,ref,ref, //0
				ref,ref,ref //1
				-ref,-ref,ref, //2
				ref,-ref,ref, //3
				-ref,ref,-ref, //4
				ref,ref,-ref //5
				-ref,-ref,-ref, //6
				ref,-ref,-ref //7

		};
		 */

		float[] VerticesCubito = new float[24] ;

		int u=0;
		while(u<VerticesCubito.length)
		{
			VerticesCubito[u]=ref*_tablavertices[u];
			++u;
		}

		return VerticesCubito;
	}


	/**
	 * Algoritmo del giro de una cara, puede ser en sentido de las aguajes del reloj o al contrario.
	 * 
	 * El cubo realiza 3 tipos de movimientos segun la cara sobre la que se realize, sobre la frontal (0), izquierda (1), o la superior (2). 
	 * O lo que es lo mismo segun en que eje (x,y,z).
	 * 
	 * Dentro de cada cara se puede realizar en n lineas diferentes siendo 0<n<_dimension-1
	 * 
	 * 
	 * @param cara define la cara sobre la que realizar el movimiento (plano) 1<cara<_dimension-1
	 * @param linea define la linea de la cara sobre la que se va a realizar el movimiento 1<linea<_dimension-1
	 * @param pos True si queremos que gire en sentido de las agujas del reloj y false en sentido contrario
	 */
	protected void girarReferencias(int cara,int linea,boolean pos, float angulogirado)
	{

		//System.out.println("Giramos Referencia: Cara: "+cara+" Linea: "+linea+" Pos: "+pos);

		/*
		 * Variables para los ejes del cubito
		 */
		int x=0;
		int y=1;
		int z=2;



		ReferenciaCubito[][][] copia = new ReferenciaCubito[_dimension][_dimension][_dimension];

		copiarArray(_ArrayReferenciaCubitos,copia);

		
		int clinea=linea; //Copia por si hay recursividad
		if(cara!=1)	linea=_dimension-1-linea; //Las caras 0 y 2 son inversas al array

		//Giro en direccion de las agujas del reloj o al contrario
		if(!pos)
		{
			for(int i=0;i<_dimension;++i)
			{
				for(int k=0;k<_dimension;++k)
				{
					//Cara frontal
					if(cara==0)
					{
						//System.out.println("Vertices: Z");
						
						cambiarEje(getCubitoReferenciado(linea, k, i), y, x, 1, x, y, -1);
						getCubitoReferenciado(linea,k,i).calcularAnguloPosicion();
						
						/*
						 * 
						 * LA COPIA NO SE REALIZA SOBRE EL CUBITO EN CUESTION QUE SE ESTA TRATANDO EN ESTE MOMENTO SINO
						 * EN EL ANTERIOR. HAY QUE CAMBIAR LOS VALORES DEL ARRAY DE REFERENCIACUBITOS PARA QUE SE 
						 * COPIE EL QUE SE ESTA TRABAJANDO EN ESTE MOMENTO.
						 */
						_ArrayReferenciaCubitos[linea][k][i]=copia[linea][i][_dimension-1-k];
						getCubitoReferenciado(linea,k,i).setPosicionCubito(linea, k, i);					
						
						
					}
					//Cara Izquierda
					else if(cara==1)
					{
						//System.out.println("Vertices: X");
						
						cambiarEje(getCubitoReferenciado(k, i, linea), y, z, -1, z, y, 1);
						getCubitoReferenciado(k,i,linea).calcularAnguloPosicion();
						
						_ArrayReferenciaCubitos[k][i][linea]=copia[_dimension-1-i][k][linea];
						getCubitoReferenciado(k,i,linea).setPosicionCubito(k,i,linea);
						
					}
					//Cara derecha
					else if(cara==2)
					{	
						//System.out.println("Vertices: Y");
						
						cambiarEje(getCubitoReferenciado(i, linea, k), z, x, -1, x, z, 1);
						getCubitoReferenciado(i, linea,k).calcularAnguloPosicion();
						
						_ArrayReferenciaCubitos[i][linea][k]=copia[_dimension-1-k][linea][i];
						getCubitoReferenciado(i, linea, k).setPosicionCubito(i, linea, k);
						
					}


				}
			}
			//Giro contrario a las agujas del reloj	
		}else
		{
			for(int i=0;i<_dimension;++i)
			{
				for(int k=0;k<_dimension;++k)
				{
					//Cara frontal
					if(cara==0)
					{	
						//System.out.println("Vertices: Z");
						
						cambiarEje(getCubitoReferenciado(linea, k, i), y, x, -1, x, y, 1);
						getCubitoReferenciado(linea,k,i).calcularAnguloPosicion();
						
						_ArrayReferenciaCubitos[linea][k][i]=copia[linea][_dimension-1-i][k];
						getCubitoReferenciado(linea,k,i).setPosicionCubito(linea, k, i);
						

					}
					//Cara izquierda
					else if(cara==1)
					{	
						//System.out.println("Vertices: X");
						
						cambiarEje(getCubitoReferenciado(k, i, linea), y, z, 1, z, y, -1);
						getCubitoReferenciado(k,i,linea).calcularAnguloPosicion();
						
						_ArrayReferenciaCubitos[k][i][linea]=copia[i][_dimension-1-k][linea];	
						getCubitoReferenciado(k,i,linea).setPosicionCubito(k,i,linea);
			
							

					}
					//Cara derecha
					else if(cara==2)
					{		
						//System.out.println("Vertices: Y");
						
						cambiarEje(getCubitoReferenciado(i, linea, k), z, x, 1, x, z, -1);
						getCubitoReferenciado(i, linea,k).calcularAnguloPosicion();
						
						_ArrayReferenciaCubitos[i][linea][k]=copia[k][linea][_dimension-1-i];
						getCubitoReferenciado(i, linea, k).setPosicionCubito(i, linea, k);					
						
					}


				}
			}
		}



		if(1<(Math.abs(angulogirado)/90)) //RECURSIVA PARA VARIOS GIROS, HAY QUE TESTEAR!
		{
			//System.out.println("RECURSIVA!");
			if(angulogirado<0)girarReferencias(cara,clinea,pos, angulogirado+90);
			if(angulogirado>0)girarReferencias(cara,clinea,pos, angulogirado-90);
			
			
		}
		
		
	}



	/**
	 * Realiza dos copias (que son los dos ejes que cambiaran en un cubito al realizar un giro, ya
	 * que uno permanecera igual). Se realiza de esta forma porque hay que hacerlo a bloque.
	 * Copia el origen en el destino en la matriz de ejes del cubito, en caso de ser negativo
	 * por lo que se sustituye se debe de indicar en negativo un -1, en caso contrario un 1
	 * 
	 * Los ejes son:
	 * x:0
	 * y:1
	 * z:2
	 * 
	 * @param cubito
	 * @param destino
	 * @param origen
	 * @param negativo
	 */
	public void cambiarEje(Cubito cubito,int destino1, int origen1,int negativo1,int destino2, int origen2,int negativo2) {

		//Primero realizamos una copia auxiliar
		float[] aux=new float[9];
		for(int u=0;u<9;++u) aux[u]=cubito._matrix[u];
		/*
		System.out.println("->Cambiamos Ejes: "+cubito._id);
		System.out.println("MatriX: "+cubito._matrix[0]+","+cubito._matrix[1]+","+cubito._matrix[2]);
		System.out.println("MatriY: "+cubito._matrix[3]+","+cubito._matrix[4]+","+cubito._matrix[5]);
		System.out.println("MatriZ: "+cubito._matrix[6]+","+cubito._matrix[7]+","+cubito._matrix[8]);
		 */
		//Ahora copiamos
		cubito._matrix[destino1*3] = negativo1*aux[origen1*3];
		cubito._matrix[(destino1*3)+1] = negativo1*aux[(origen1*3)+1];
		cubito._matrix[(destino1*3)+2] = negativo1*aux[(origen1*3)+2];

		cubito._matrix[destino2*3] = negativo2*aux[origen2*3];
		cubito._matrix[(destino2*3)+1] = negativo2*aux[(origen2*3)+1];
		cubito._matrix[(destino2*3)+2] = negativo2*aux[(origen2*3)+2];
		
		/*
		System.out.println("->Ejes Cambiados de: "+cubito._id);
		System.out.println("MatriX: "+cubito._matrix[0]+","+cubito._matrix[1]+","+cubito._matrix[2]);
		System.out.println("MatriY: "+cubito._matrix[3]+","+cubito._matrix[4]+","+cubito._matrix[5]);
		System.out.println("MatriZ: "+cubito._matrix[6]+","+cubito._matrix[7]+","+cubito._matrix[8]);
		 */
	}


	/**
	 * Metodo que se encarga de copiar un Array tridimensional a otro
	 * 
	 * @param arrayReferenciaCubitos
	 * @param copia
	 */
	private void copiarArray(ReferenciaCubito[][][] arrayReferenciaCubitos, ReferenciaCubito[][][] copia) {
		for (int i=0;i<_dimension;++i){
			for(int j=0;j<_dimension;++j){
				for(int k=0;k<_dimension;++k){
					copia[i][j][k]=arrayReferenciaCubitos[i][j][k];
				}
			}
		}

	}






}
