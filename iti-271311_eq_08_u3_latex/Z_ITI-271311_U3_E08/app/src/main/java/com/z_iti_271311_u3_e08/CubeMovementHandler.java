package com.z_iti_271311_u3_e08;

public class CubeMovementHandler {
    private final VistaCubo vistaCubo;
    private final GLCubo cubo;
    private static final float ROTATION_ANGLE = 90f;

    public CubeMovementHandler(VistaCubo vistaCubo, GLCubo cubo) {
        this.vistaCubo = vistaCubo;
        this.cubo = cubo;
    }

    public void handleMove(final String move) {
        vistaCubo.queueEvent(new Runnable() {
            @Override
            public void run() {
                processMove(move);
            }
        });
    }

    private void processMove(String move) {
        boolean clockwise = move.endsWith("'");
        String basicMove = move.replace("'", "");

        switch (basicMove) {
            case "L":
                rotateFace(1, 0, clockwise);
                break;
            case "F":
                rotateFace(0, cubo._dimension - 1, !clockwise);
                break;
            case "R":
                rotateFace(1, cubo._dimension - 1, !clockwise);
                break;
            case "B": // Invertido el movimiento y cambiada la posición
                rotateFace(0, 0, clockwise);
                break;
            case "U":
                rotateFace(2, cubo._dimension - 1, !clockwise);
                break;
            case "D":
                rotateFace(2, 0, clockwise);
                break;

            // Middle slice turns
            case "M":
                rotateFace(1, 1, clockwise);
                break;
            case "E":
                rotateFace(2, 1, clockwise);
                break;
            case "S":
                rotateFace(0, 1, !clockwise);
                break;
        }
    }

    private void rotateFace(final int cara, final int linea, final boolean clockwise) {
        float angle = clockwise ? ROTATION_ANGLE : -ROTATION_ANGLE;

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
}