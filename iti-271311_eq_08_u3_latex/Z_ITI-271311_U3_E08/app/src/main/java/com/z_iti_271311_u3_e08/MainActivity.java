package com.z_iti_271311_u3_e08;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private VennDiagramView vennDiagramView;
    private VistaCubo vistaCubo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Eliminar título y configurar pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Inicializar la vista del diagrama de Venn
        vennDiagramView = findViewById(R.id.vennDiagramView);

        // Inicializar la vista del cubo
        vistaCubo = new VistaCubo(this);

        // Añadir vistaCubo al right_container
        FrameLayout rightContainer = findViewById(R.id.right_container);
        if (rightContainer != null) {
            rightContainer.addView(vistaCubo);
        }

        // Configurar los botones
        setupButtons();
    }

    private void setupButtons() {
        // Grupo 1
        setupMoveButton(R.id.button_L, "L", () -> vennDiagramView.shiftGroup3LargeForward());
        setupMoveButton(R.id.button_L_prime, "L'", () -> vennDiagramView.shiftGroup3LargeBackward());

        setupMoveButton(R.id.button_F, "F", () -> vennDiagramView.shiftGroup2SmallForward());
        setupMoveButton(R.id.button_F_prime, "F'", () -> vennDiagramView.shiftGroup2SmallBackward());

        setupMoveButton(R.id.button_R, "R", () -> vennDiagramView.shiftGroup3SmallForward());
        setupMoveButton(R.id.button_R_prime, "R'", () -> vennDiagramView.shiftGroup3SmallBackward());

        // Grupo 2
        setupMoveButton(R.id.button_B, "B", () -> vennDiagramView.shiftGroup2LargeForward());
        setupMoveButton(R.id.button_B_prime, "B'", () -> vennDiagramView.shiftGroup2LargeBackward());

        setupMoveButton(R.id.button_U, "U", () -> vennDiagramView.shiftGroup1SmallForward());
        setupMoveButton(R.id.button_U_prime, "U'", () -> vennDiagramView.shiftGroup1SmallBackward());

        setupMoveButton(R.id.button_D, "D", () -> vennDiagramView.shiftGroup1LargeForward());
        setupMoveButton(R.id.button_D_prime, "D'", () -> vennDiagramView.shiftGroup1LargeBackward());

        // Grupo 3
        setupMoveButton(R.id.button_M, "M", () -> vennDiagramView.shiftGroup3MediumForward());
        setupMoveButton(R.id.button_M_prime, "M'", () -> vennDiagramView.shiftGroup3MediumBackward());

        setupMoveButton(R.id.button_E, "E", () -> vennDiagramView.shiftGroup1MediumForward());
        setupMoveButton(R.id.button_E_prime, "E'", () -> vennDiagramView.shiftGroup1MediumBackward());

        setupMoveButton(R.id.button_S, "S", () -> vennDiagramView.shiftGroup2MediumForward());
        setupMoveButton(R.id.button_S_prime, "S'", () -> vennDiagramView.shiftGroup2MediumBackward());
    }

    /**
     * Configura un botón para ejecutar acciones específicas al ser presionado.
     *
     * @param buttonId  ID del botón en el layout
     * @param move      Movimiento para el cubo
     * @param vennAction Acción a ejecutar al presionar el botón para el diagrama de Venn
     */
    private void setupMoveButton(int buttonId, final String move, final Runnable vennAction) {
        MaterialButton button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleMove(move);
                    // Ejecutar la acción en el diagrama de Venn
                    if (vennAction != null) {
                        vennAction.run();
                    }
                }
            });
        }
    }

    private void handleMove(final String move) {
        vistaCubo.queueEvent(new Runnable() {
            @Override
            public void run() {
                processMove(move);
            }
        });
    }

    private void processMove(String move) {
        GLCubo cubo = vistaCubo.getRenderizado().getCubo();
        if (cubo == null) {
            // El cubo aún no ha sido inicializado
            return;
        }
        boolean clockwise = !move.endsWith("'");
        String basicMove = move.replace("'", "");

        switch (basicMove) {
            case "L":
                rotateFace(cubo, 1, 0, clockwise);
                break;
            case "F":
                rotateFace(cubo, 0, cubo._dimension - 1, clockwise);
                break;
            case "R":
                rotateFace(cubo, 1, cubo._dimension - 1, clockwise);
                break;
            case "B":
                rotateFace(cubo, 0, 0, clockwise);
                break;
            case "U":
                rotateFace(cubo, 2, cubo._dimension - 1, clockwise);
                break;
            case "D":
                rotateFace(cubo, 2, 0, clockwise);
                break;
            // Giros de capas medias
            case "M":
                rotateFace(cubo, 1, 1, clockwise);
                break;
            case "E":
                rotateFace(cubo, 2, 1, clockwise);
                break;
            case "S":
                rotateFace(cubo, 0, 1, clockwise);
                break;
        }
    }

    private void rotateFace(GLCubo cubo, final int cara, final int linea, final boolean clockwise) {
        float angle = clockwise ? 90f : -90f;

        // Realizar el giro visual
        cubo.girarReferenciasGL(cara, linea, angle);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Actualizar la estructura de datos del cubo
        cubo.girarReferencias(cara, linea, clockwise, angle);

        // Ajustar las referencias
        cubo.ajustarReferencias(cara, linea);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vistaCubo != null) {
            vistaCubo.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vistaCubo != null) {
            vistaCubo.onPause();
        }
    }
}
