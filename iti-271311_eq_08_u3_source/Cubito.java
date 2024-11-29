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

//import java.util.Vector;

public class Cubito {


	/*
	 * Un Cubito tiene 6 caras que se almacenan en este array de GLCara
	 * El Orden es el siguiente:
	 * 0-Frontal
	 * 1-Izquierda
	 * 2-Atras
	 * 3-Derecha
	 * 4-Abajo
	 * 5-Arriba
	 */
	public GLCara[] _caras;
	
	//Codigo de identificacion del cubito
	public int _id;
	
	//Numero del cubito
	protected int _numcaras=6;
	
	//Tamanyo del lado
	protected float _lado;
	
	//Dimension del cubo al que pertenece
	private int _dimension;
	
	//Indica las caras que tienen color como true y en false las que no.
	protected boolean[] _carasValidas;
	
	//Array con las coordenadas del cubito
    private float[] _verticesCubito;
    
    //Desplazamiento del cubito para su colocacion
    protected float _PosicionX=0f;
    protected float _PosicionY=0f;
    protected float _PosicionZ=0f;
    
    //Angulos del cubito que indican la posicion inicial del cubo
    public float _anguloXposicion=0.0f;
    public float _anguloYposicion=0.0f;
    public float _anguloZposicion=0.0f;
    
    //Angulos del cubito mientras se esta girando
    public float _anguloXgiro=0.0f;
    public float _anguloYgiro=0.0f;
    public float _anguloZgiro=0.0f;
	
    
    //Matriz de los ejes del cubito
	public float[] _matrix=	{1,0,0,
			 0,1,0,
			 0,0,1};
	
	//lista de con los movimientos que se han realizado, El 0 sera el mas antiguo y el 3 el mas nuevo
	//Vector<Integer> _movimientos;
    
    /*
     * Indica en este momento que angulo que se esta moviendo, siendo:
     * 0: X
     * 1: Y
     * 2: Z
     */
    public int _angulomovimiento=0;
    
	
    
    
	/**
	 * Constructor, si se le indica cual es la posicion del cubito tiene que saber
	 * colocarlo en su posicion
	 * 
	 * @param i
	 * @param j
	 * @param k
	 * @param vertices son los vertices del Cubito 
	 * @param dimension es la dimension del cubo
	 * @param lado es el tamanyo del lado (Arista) del cubito
	 */
	public Cubito(int id,int i,int j,int k,float[] vertices,int dimension,float lado)
	{
		_id=id;
		
		_dimension=dimension;
		
		_lado=lado;
		
		validarCaras(i,j,k);
		
		_caras= new GLCara[6];
		
		setVerticesCubito(vertices);
		
		setPosicionCubito(i, j, k);
		
		
	}
	

	/**
	 * Valida las caras que sean necesarias segun el tipo de cubito
	 * @param i
	 * @param j
	 * @param k
	 * @param dimension
	 */
	private void validarCaras(int i,int j,int k) {
		
		//Se inicializan a false todas
		_carasValidas= new boolean[6];
		for(int u=0;u<_numcaras;++u)
		{
			_carasValidas[u]=false;
		}
		
		//Ahora se comprueban donde estan
		if(i==0) //Frontal
		{
			_carasValidas[2]=true;
		}
		if(i==_dimension-1) //Trasero
		{
			_carasValidas[0]=true;
		}

		if(k==0) //Izquierda
		{
			_carasValidas[1]=true;
		}
		if(k==_dimension-1) //Derecha
		{
			_carasValidas[3]=true;
		}
		
		if(j==0) //Arriba
		{
			_carasValidas[5]=true;
		}
		if(j==_dimension-1) //Abajo
		{
			_carasValidas[4]=true;
		}
		
	}


	/**
	 * Indica la nueva posicion de los vertices del cubito y calcula los vertices de las caras del mismo
	 * 
	 * @param vertices
	 */
	public void setVerticesCubito(float[] vertices) {
		_verticesCubito=vertices;
		
		//Creamos las caras
		for(int u=0;u<_numcaras;++u){
			if(_carasValidas[u])
			_caras[u]= new GLCara(u,getCoordenadasCara(u));
			else _caras[u] = new GLCara(6,getCoordenadasCara(u));
			
		}
		
	}
	
	/**
	 * Determina cual es la transladacion del cubito respecto al cubito centrado en el origen y lo 
	 * 
	 * @param i
	 * @param j
	 * @param k
	 */
	public void setPosicionCubito(int i, int j, int k) {
		//System.out.println("Setposicion de:"+_id);
		
		//unidad de desplazamiento
		float ref=((float)_dimension-1f)/2f;
		ref=_lado*ref;
		/*
		 * De la forma que hemos definido el array y a la forma en la que
		 * representaremos los ejes hay que hacerlo de la siguiente forma 
		 * Utilizaremos la misma forma que usa OpenGL para representar los ejes cardinales.
		 */
		_PosicionX=-ref+(k*_lado); 
		_PosicionY=ref-(j*_lado);
		_PosicionZ=ref-(i*_lado);
		
		/*
		if(this._id==1)
		{
			System.out.println("-----------------------------------"+_id+"----------------------------------");
			System.out.println("MatriX: "+this._matrix[0]+","+this._matrix[1]+","+this._matrix[2]);
			System.out.println("MatriY: "+this._matrix[3]+","+this._matrix[4]+","+this._matrix[5]);
			System.out.println("MatriZ: "+this._matrix[6]+","+this._matrix[7]+","+this._matrix[8]);

		}
		*/
	}


	/**
	 * Le llegara por defecto este array a _verticesCubito:
	 * 
	 *     	-0.5f, -0.5f, -0.5f, //0
     *		-0.5f, 0.5f, -0.5f,  //1
     *		0.5f, 0.5f, -0.5f,   //2
     *		0.5f, -0.5f, -0.5f,  //3
     *		-0.5f, -0.5f, 0.5f,  //4
     *		-0.5f, 0.5f, 0.5f,   //5
     *		0.5f, 0.5f, 0.5f,    //6
     *		0.5f, -0.5f, 0.5f,   //7
     *		
     *		Y segun la cara en la que estemos tenemos que coger cuantro puntos, o lo que es lo mismo
     *		cuatro filas siguiendo el esquema de las caras.
     *
     *	El algoritmo realiza eso siguiento la tabla de puntos que se llamma puntoscara.
	 * 
	 * 
	 * @param cara
	 * @return
	 */
	protected float[] getCoordenadasCara(int cara)
	{
		float[] coordenadas=new float[12];
		
		int k=0;
		
		
		// los 4 puntos
		int[][] puntoscara={
				{1,2,0,3}, //Cara A
				{5,1,4,0}, //Cara B
				{6,5,7,4}, //Cara C
				{2,6,3,7}, //Cara D
				{0,3,4,7}, //Cara E
				{5,6,1,2}, //Cara F
		};
		
		
		//Algoritmo
       int fila=-1;
			for(int i=0;i<3;++i)
			{
				fila=puntoscara[cara][0];
				coordenadas[k]=_verticesCubito[(3*fila)+i];
				
				++k;
			}
			for(int i=0;i<3;++i)
			{
				fila=puntoscara[cara][1];
				coordenadas[k]=_verticesCubito[(3*fila)+i];
				
				++k;
			}
			for(int i=0;i<3;++i)
			{
				fila=puntoscara[cara][2];
				coordenadas[k]=_verticesCubito[(3*fila)+i];
				
				++k;
			}
			for(int i=0;i<3;++i)
			{
				fila=puntoscara[cara][3];
				coordenadas[k]=_verticesCubito[(3*fila)+i];
				
				++k;
			}
		
		
		return coordenadas;
	}
	
	

	/**
	 * Calcula el angulo de posicion X, Y, Z del cubito segun su matriz.
	 * 
	 * Hay un total de 6x4=24 posiciones posibles del cubo
	 */
	public void calcularAnguloPosicion(){
		
		//System.out.println("CalcularAngulo de:"+_id);
		

		/*
		 * 1 0 0
		 * 0 x x
		 * 0 x x
		 */
		if(_matrix[0]==1f)
		{
			
			if( _matrix[4]==1f && _matrix[8]==1f)
			{
				//Rosa Verde Blanco
				_anguloXposicion=0;
				_anguloYposicion=0;
				_anguloZposicion=0;
			}
			else if( _matrix[5]==1f && _matrix[7]==-1f)
			{
				//Verde rojo blanco
				_anguloXposicion=270;
				_anguloYposicion=0;
				_anguloZposicion=0;
			}
			else if( _matrix[4]==-1f && _matrix[8]==-1f)
			{
				//Rojo Amarillo Blanco
				_anguloXposicion=180;
				_anguloYposicion=0;
				_anguloZposicion=0;
			}
			else if( _matrix[5]==-1f && _matrix[7]==1f)
			{
				//Amarillo Rosa Blanco
				_anguloXposicion=90;
				_anguloYposicion=0;
				_anguloZposicion=0;
			}
		}
		
		/*
		 * x x 0
		 * x x 0
		 * 0 0 1
		 */
		if(_matrix[8]==1f)
		{
			
			if( _matrix[1]==-1f && _matrix[3]==1f)
			{
				//Blanco Verde Rojo
				_anguloXposicion=0;
				_anguloYposicion=0;
				_anguloZposicion=90;
			}
			else if( _matrix[1]==1f && _matrix[3]==-1f)
			{
				//Azul Verde Rosa
				_anguloXposicion=0;
				_anguloYposicion=0;
				_anguloZposicion=270;
			}
		}
		
		/*
		 * x 0 x
		 * 0 1 0
		 * x 0 x
		 */
		if(_matrix[4]==1f)
		{
			
			if( _matrix[2]==1f && _matrix[6]==-1f)
			{
				//Rosa Azul Verde
				_anguloXposicion=0;
				_anguloYposicion=90;
				_anguloZposicion=0;
			}
			else if( _matrix[0]==-1f && _matrix[8]==-1f)
			{
				//Rosa Amarillo Azul
				_anguloXposicion=0;
				_anguloYposicion=180;
				_anguloZposicion=0;
			}
			else if( _matrix[2]==-1f && _matrix[6]==1f)
			{
				//Rosa Blanco Amarillo
				_anguloXposicion=0;
				_anguloYposicion=270;
				_anguloZposicion=0;
			}
		}
		
		
		
		/*
		 * x x 0
		 * 0 0 1
		 * x x 0
		 */
		if(_matrix[5]==1f)
		{
			
			if( _matrix[1]==-1f && _matrix[6]==-1f)
			{
				//Verde Azul Rojo
				_anguloXposicion=270;
				_anguloYposicion=0;
				_anguloZposicion=90;
			}
			else if( _matrix[0]==-1f && _matrix[7]==1f)
			{
				//Verde Rosa Azul
				_anguloXposicion=270;
				_anguloYposicion=0;
				_anguloZposicion=180;
			}
			else if( _matrix[1]==1f && _matrix[6]==1f)
			{
				//Verde blanco rosa
				_anguloXposicion=270;
				_anguloYposicion=0;
				_anguloZposicion=270;
			}
		}
		
		
		/*
		 * x  0  x
		 * 0 -1  0
		 * x  0  x
		 */
		if(_matrix[4]==-1f)
		{
			
			if( _matrix[2]==-1f && _matrix[6]==-1f)
			{
				//Rojo Azul Amarillo
				_anguloXposicion=180;
				_anguloYposicion=270;
				_anguloZposicion=0;
			}
			else if( _matrix[2]==1f && _matrix[6]==1f)
			{
				//Rojo Blanco Verde
				_anguloXposicion=180;
				_anguloYposicion=90;
				_anguloZposicion=0;
			}
		}
		
		/*
		 * x  x  0
		 * 0  0 -1
		 * x  x  0
		 */
		if(_matrix[5]==-1f)
		{
			
			if( _matrix[1]==1f && _matrix[6]==-1f)
			{
				//Amarillo Azul Rosa
				_anguloXposicion=90;
				_anguloYposicion=0;
				_anguloZposicion=270;
			}
			else if( _matrix[0]==-1f && _matrix[7]==-1f)
			{
				//Amarillo Rojo Azul
				_anguloXposicion=90;
				_anguloYposicion=0;
				_anguloZposicion=180;
			}
			else if( _matrix[1]==-1f && _matrix[6]==1f)
			{
				//Amarillo Blanco Rojo
				_anguloXposicion=90;
				_anguloYposicion=0;
				_anguloZposicion=90;
			}
		}
		
		
		//SUELTOS:
		
		
		/*
		 *  0  0 -1
		 *  1  0  0
		 *  0 -1  0
		 * 
		 */
		if(_matrix[2]==-1f && _matrix[3]==1f && _matrix[7]==-1f)
		{
			//Blanco Rojo Amarillo
			_anguloXposicion=270;
			_anguloYposicion=270;
			_anguloZposicion=0;
		}
		
		/*
		 *  0  0  1
		 * -1  0  0
		 *  0 -1  0
		 * 
		 */
		if(_matrix[2]==1f && _matrix[3]==-1f && _matrix[7]==-1f)
		{
			//Azul Rojo Verde
			_anguloXposicion=270;
			_anguloYposicion=90;
			_anguloZposicion=0;
		}
		
		/*
		 * -1  0  0
		 *  0 -1  0
		 *  0  0  1
		 * 
		 */
		if(_matrix[0]==-1f && _matrix[4]==-1f && _matrix[8]==1f)
		{
			//Rojo Verde Azul
			_anguloXposicion=0;
			_anguloYposicion=0;
			_anguloZposicion=180;
		}
		
		/*
		 *  0  0  1
		 *  1  0  0
		 *  0  1  0
		 *  
		 */
		if(_matrix[2]==1f && _matrix[3]==1f && _matrix[7]==1f)
		{
			//Blanco Rosa Verde
			_anguloXposicion=0;
			_anguloYposicion=90;
			_anguloZposicion=90;
		}
		
		/*
		 *  0  0 -1
		 * -1  0  0
		 *  0  1  0
		 *  
		 */
		if(_matrix[2]==-1f && _matrix[3]==-1f && _matrix[7]==1f)
		{
			//Azul Rosa Amarillo
			_anguloXposicion=90;
			_anguloYposicion=270;
			_anguloZposicion=0;
		}
		
		/*
		 *  0  1  0
		 *  1  0  0
		 *  0  0 -1
		 *  
		 */
		if(_matrix[1]==1f && _matrix[3]==1f && _matrix[8]==-1f)
		{
			//Blanco Amarillo Rosa
			_anguloXposicion=180;
			_anguloYposicion=0;
			_anguloZposicion=270;
		}
		
		/*
		 *  0 -1  0
		 * -1  0  0
		 *  0  0 -1
		 *  
		 */
		if(_matrix[1]==-1f && _matrix[3]==-1f && _matrix[8]==-1f)
		{
			//Azul Amarillo Rojo
			_anguloXposicion=180;
			_anguloYposicion=0;
			_anguloZposicion=90;
		}
		
		/*
		System.out.println("posicionX: "+this._anguloXposicion);
		System.out.println("posicionY: "+this._anguloYposicion);
		System.out.println("posicionZ: "+this._anguloZposicion);
		*/
	}
	
	
	
}
