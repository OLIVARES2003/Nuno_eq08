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
import java.nio.ShortBuffer;


public class Cara {

    public static final int CUBITOAMARILLO=R.drawable.rojo;
    public static final int CUBITOAZUL=R.drawable.rojo;
    public static final int CUBITOVERDE=R.drawable.rojo;
    public static final int CUBITOBLANCO=R.drawable.rojo;
    public static final int CUBITOROJO=R.drawable.rojo;
    public static final int CUBITOROSA=R.drawable.rojo;
    public static final int CUBITONEGRO=R.drawable.rojo;

    //Variables para almacenar la paleta RGBA
    protected float _rojo;
    protected float _verde;
    protected float _azul;
    protected float _alpha;

    //Imagen de la cara
    protected int _img;

    /*
     * Tendra el siguiente esquema dentro del array:
     *
     * 0     1
     *  +---+
     *  |   |
     *  +---+
     * 2     3
     *
     */
    protected float[] _verticesCara;

    // Our UV texture buffer.
    private FloatBuffer _bufferTextura;

    //Triangulo 1
    protected ShortBuffer _indiceBuffer1;
    protected FloatBuffer _verticesBuffer1;
    protected short[] _indicesArray1={0,2,3};
    protected int _numeroVertices1 = 3;

    //Triangulo 2
    protected ShortBuffer _indiceBuffer2;
    protected FloatBuffer _verticesBuffer2;
    protected short[] _indicesArray2={0,3,1};
    protected int _numeroVertices2 = 3;

    //Aristas
    protected ShortBuffer _indiceBufferA;
    protected FloatBuffer _verticesBufferA;
    protected short[] _indicesArrayA={0,2,3,1,0,1,2,3}; //Se repite dos veces porque un cuadrado tiene 4 aristas que uno dos puntos cada una.
    protected int _numeroVerticesA = 8;


    public Cara(int color,float[] vertices){

        setVerticesCara(vertices);

        //Le indicamos el color a la cara

        //Amarillo
        if (color==0){
            _rojo=1f;
            _verde=1f;
            _azul=0f;
            _alpha=1f;
            _img=this. CUBITOAMARILLO;
        }
        //Azul
        else if (color==1){
            _rojo=0f;
            _verde=0f;
            _azul=1f;
            _alpha=1f;
            _img=this. CUBITOAZUL;
        }
        //Verde
        else if (color==2){
            _rojo=0f;
            _verde=1f;
            _azul=0f;
            _alpha=1f;
            _img=this. CUBITOVERDE;
        }
        //Blanco
        else if (color==3){
            _rojo=1f;
            _verde=1f;
            _azul=1f;
            _alpha=1f;
            _img=this. CUBITOBLANCO;
        }
        //Rojo
        else if (color==4){
            _rojo=1f;
            _verde=0f;
            _azul=0f;
            _alpha=1f;
            _img=this. CUBITOROJO;
        }
        //Rosa
        else if (color==5){
            _rojo=1f;
            _verde=0f;
            _azul=1f;
            _alpha=1f;
            _img=this. CUBITOROSA;
        }
        //Negro
        else if (color==6){
            _rojo=0f;
            _verde=0f;
            _azul=0f;
            _alpha=1f;
            _img=this. CUBITONEGRO;
        }



    }

    public Cara(float red, float green, float blue, float alpha, float[] vertices) {

        setVerticesCara(vertices);

        _rojo=red;
        _verde=green;
        _azul=blue;
        _alpha=alpha;
    }

    /**
     * Metodo encargado de inicializar los valores de las variables
     */
    private void init(){
        _numeroVertices1 = _verticesCara.length;
        _numeroVertices2 = _verticesCara.length;
        _numeroVerticesA = _verticesCara.length;

        // float tiene 4 bytes para guardar en memoria
        ByteBuffer vbb = ByteBuffer.allocateDirect(_numeroVertices1 * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        _verticesBuffer1 = vbb.asFloatBuffer();

        // short tiene 2 bytes
        ByteBuffer ibb = ByteBuffer.allocateDirect(_numeroVertices1 * 2);
        ibb.order(ByteOrder.nativeOrder());
        _indiceBuffer1 = ibb.asShortBuffer();

        ByteBuffer vbb2 = ByteBuffer.allocateDirect(_numeroVertices2 * 3 * 4);
        vbb2.order(ByteOrder.nativeOrder());
        _verticesBuffer2 = vbb2.asFloatBuffer();

        // short tiene 2 bytes
        ByteBuffer ibb2 = ByteBuffer.allocateDirect(_numeroVertices2 * 2);
        ibb2.order(ByteOrder.nativeOrder());
        _indiceBuffer2 = ibb2.asShortBuffer();

        ByteBuffer vbb3 = ByteBuffer.allocateDirect(_numeroVerticesA * 3 * 4);
        vbb3.order(ByteOrder.nativeOrder());
        _verticesBufferA = vbb3.asFloatBuffer();

        // short tiene 2 bytes
        ByteBuffer ibb3 = ByteBuffer.allocateDirect(_numeroVerticesA * 2);
        ibb3.order(ByteOrder.nativeOrder());
        _indiceBufferA = ibb3.asShortBuffer();


        _verticesBuffer1.put(_verticesCara);
        _indiceBuffer1.put(_indicesArray1);

        _verticesBuffer1.position(0);
        _indiceBuffer1.position(0);

        _verticesBuffer2.put(_verticesCara);
        _indiceBuffer2.put(_indicesArray2);

        _verticesBuffer2.position(0);
        _indiceBuffer2.position(0);

        _verticesBufferA.put(_verticesCara);
        _indiceBufferA.put(_indicesArrayA);

        _verticesBufferA.position(0);
        _indiceBufferA.position(0);

    }


    /**
     * Indica los nuevos vertices de la cara
     *
     * @param vertices
     */
    protected void setVerticesCara(float[] vertices)
    {
        _verticesCara=vertices;

        setVerticesTexturas(vertices);

        init();
    }

    /**
     * Set the texture coordinates.
     *
     * @param vertices
     */
    protected void setVerticesTexturas(float[] vertices) {
        // float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        _bufferTextura = byteBuf.asFloatBuffer();
        _bufferTextura.put(vertices);
        _bufferTextura.position(0);
    }

}