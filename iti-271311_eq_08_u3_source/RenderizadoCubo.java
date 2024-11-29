package com.z_iti_271311_u3_e08;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RenderizadoCubo implements GLSurfaceView.Renderer {
	public GLCubo _uncubo;
	private GLEjes _ejes;
	public GLMenu _menu;

	// PALETA RGB (Red, Green, Blue) para el fondo
	private float _rojo = 0.5f;
	private float _verde = 0.5f;
	private float _azul = 0.5f;

	// Definicion punto donde se situara la camara de vision del cubo
	private static float _camaraX = 4f; // 3.3 con 0.7 de tamanyo de cubo
	private static float _camaraY = 3f; // 2.33 con 0.7 de tamanyo de cubo
	private static float _camaraZ = 6f; // 3.5 con 0.7 de tamanyo de cubo

	public static float mirarX = 0;
	public static float mirarY = 0;

	private boolean _pintarAristas = true;
	private boolean _pintarColor = true;
	private boolean _pintarEjes = false;
	private boolean _pintarTexturas = false;
	private boolean _pintarMenus = false;

	// Add getter method for the cube
	public GLCubo getCubo() {
		return _uncubo;
	}

	/**
	 * Cada vez que haya un cambio hay que dibujar, este metodo sera el usado para dibujar cada objeto
	 * en cuanto hay un cambio.
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		// Se cambia a la matriz para ver modelos
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// Limpiar los buffers
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Dibujamos lo que sea necesario
		if (_pintarAristas) _uncubo.pintarAristas(gl);

		if (_pintarColor) _uncubo.pintarColor(gl);

		if (_pintarTexturas) _uncubo.pintarTexturas(gl);

		if (_pintarEjes) _ejes.pintarEjes(gl, true, true, true);

		if (_pintarMenus) _menu.pintarMenu(gl);
	}

	/**
	 * Se usa para el glViewPort, Se ejecuta cuando se crea y cuando hay un cambio. Tambien cuando
	 * cambiamos la orientacion de la pantalla.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		// Se ajusta el ratio segun el tamanyo de la pantalla
		float ratio = (float) width / height;

		// Se carga la matriz de proyeccion
		gl.glMatrixMode(GL10.GL_PROJECTION);

		// Se resetea la matriz identidad
		gl.glLoadIdentity();

		// Se aplica la matriz de proyeccion
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 70);

		RenderizadoCubo.situarCamara(gl);
	}

	/**
	 * Se ejecuta solamente cuando se crea. Aqui se inicializaran las variables y objetos.
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_CULL_FACE); // habilitar la cara que se muestra.

		// Antes estaba en GL_CW
		gl.glFrontFace(GL10.GL_CW); // El del frente es el que este dibujado en sentido a las agujas del reloj

		gl.glCullFace(GL10.GL_BACK); // La cara que no se dibuja no se podra ver.

		_uncubo = new GLCubo();

		_ejes = new GLEjes();

		_menu = new GLMenu();

		// FONDO
		// Definimos el color de la mezcla RGB para el fondo de pantalla
		gl.glClearColor(_rojo, _verde, _azul, 1.0f);
		// preparacion del array de vertices para poder dibujarlos
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		if (_pintarTexturas) {
			// Activamos las texturas
			gl.glEnable(GL10.GL_TEXTURE_2D);
		}
	}

	/**
	 * Funcion encargada de situar la camara en el punto correspondiente, la llamaran
	 * desde los objetos que necesiten dibujarse antes de dibujarse.
	 */
	public static void situarCamara(GL10 gl) {
		// Habilitamos el poder ver delante el objeto que tiene el z menos profundo, osea el mas cercano
		gl.glEnable(GL10.GL_DEPTH_TEST);

		// Se cambia a la matriz para ver modelos
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// Se carga la matriz identidad
		gl.glLoadIdentity();

		// Se situa la camara para que mire al punto(0,0,0) siendo el vector que indica la parte superior de la camara el (0,1,0)
		GLU.gluLookAt(gl, _camaraX, _camaraY, _camaraZ, 0, 0, 0, 0, 1, 0);
	}
}