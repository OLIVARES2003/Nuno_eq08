package com.z_iti_271311_u3_e08;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class VistaCubo extends GLSurfaceView {
	private RenderizadoCubo _renderizado;

	public VistaCubo(Context context) {
		super(context);
		_renderizado = new RenderizadoCubo();
		setRenderer(_renderizado);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
	}

	public RenderizadoCubo getRenderizado() {
		return _renderizado;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.KEYCODE_MENU){
			System.out.println("Opciones!");
		}
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			System.out.println("Salir!");
			onPause();
			return false;
		}
		if(event.getKeyCode()==KeyEvent.KEYCODE_POWER){
			System.out.println("Bloquear!");
		}
		return super.onKeyDown(keyCode, event);
	}

	// Simplemente retornamos false para ignorar todos los eventos táctiles
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		return false;
	}

	@Override
	public void onResume() {
		System.out.println("Reinicia!");
	}

	@Override
	public void onPause() {
		System.out.println("Menu Pausa");
	}
}