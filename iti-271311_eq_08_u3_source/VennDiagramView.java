package com.z_iti_271311_u3_e08;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VennDiagramView extends View {

    private Paint circlePaint;
    private Paint intersectionPaint;

    // Lista para almacenar todos los círculos que se dibujarán
    private List<Circle> circles;

    // Lista para almacenar todos los bloques de intersección
    private List<Block> blocks;

    // Colores iniciales para los bloques
    private int[] initialColors = {Color.BLUE, Color.WHITE, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA};

    // Variables para cada círculo concéntrico
    private Circle group1Large;
    private Circle group1Medium;
    private Circle group1Small;

    private Circle group2Large;
    private Circle group2Medium;
    private Circle group2Small;

    private Circle group3Large;
    private Circle group3Medium;
    private Circle group3Small;

    private int currentGroup = 1; // Para asignar nombres a los grupos

    public VennDiagramView(Context context) {
        super(context);
        init();
    }

    public VennDiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Configurar pintura para los círculos principales
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(Color.BLACK);

        // Configurar pintura para los círculos de intersección
        intersectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        intersectionPaint.setStyle(Paint.Style.FILL);

        // Inicializar la lista de círculos
        circles = new ArrayList<>();

        // Inicializar la lista de bloques
        blocks = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dimensiones del canvas
        int width = getWidth();
        int height = getHeight();

        // Radio base para los círculos principales
        float baseRadius = Math.min(width, height) / 4f;

        // Coordenadas de los círculos principales
        float centerX1 = width / 2f;                     // Círculo superior
        float centerY1 = height / 3f;
        float centerX2 = width / 3f + baseRadius * 0.2f;  // Círculo inferior izquierdo ajustado a la derecha
        float centerY2 = 2 * height / 3f;
        float centerX3 = 2 * width / 3f;                  // Círculo inferior derecho

        // Ajuste adicional para las posiciones
        centerX3 -= baseRadius * 0.3f;                   // Corrige el círculo derecho para ajustar intersecciones

        // Dibujar círculos principales y agregar sus circunferencias a la lista
        circles.clear(); // Limpiar la lista antes de agregar nuevos círculos
        addConcentricCircles(centerX1, centerY1, baseRadius);
        addConcentricCircles(centerX2, centerY2, baseRadius);
        addConcentricCircles(centerX3, centerY2, baseRadius);

        // Dibujar círculos principales
        for (Circle circle : circles) {
            canvas.drawCircle(circle.centerX, circle.centerY, circle.radius, circlePaint);
        }

        // Calcular y dibujar bloques de intersección
        List<PointF> intersectionPoints = calculateAllIntersections();
        setupBlocks(intersectionPoints); // Inicializar bloques si no se ha hecho
        drawBlocks(canvas);
    }

    /**
     * Agrega círculos concéntricos a la lista de círculos y asigna nombres individuales.
     */
    private void addConcentricCircles(float centerX, float centerY, float baseRadius) {
        Circle large = new Circle(centerX, centerY, baseRadius * 1.4f);
        Circle medium = new Circle(centerX, centerY, baseRadius * 1.2f);
        Circle small = new Circle(centerX, centerY, baseRadius);

        circles.add(large);
        circles.add(medium);
        circles.add(small);

        // Asignar a variables individuales según el grupo
        switch (currentGroup) {
            case 1:
                group1Large = large;
                group1Medium = medium;
                group1Small = small;
                break;
            case 2:
                group2Large = large;
                group2Medium = medium;
                group2Small = small;
                break;
            case 3:
                group3Large = large;
                group3Medium = medium;
                group3Small = small;
                break;
            default:
                break;
        }

        currentGroup++;
    }

    /**
     * Configura los bloques basados en los puntos de intersección.
     */
    private void setupBlocks(List<PointF> points) {
        if (blocks.isEmpty()) {
            // Crear grupos de bloques 3x3 a partir de los puntos
            List<List<PointF>> groups = formGroups(points, 3); // Grupos de 3x3 intersecciones

            // Colores asignados a cada bloque
            int[] colors = {Color.BLUE, Color.WHITE, Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA};

            // Crear bloques con colores organizados por grupo
            for (int i = 0; i < groups.size(); i++) {
                List<PointF> group = groups.get(i);
                int color = colors[i % colors.length]; // Asignar color cíclico a cada grupo
                for (PointF point : group) {
                    blocks.add(new Block(point.x, point.y, color));
                }
            }
        }
    }

    private List<List<PointF>> formGroups(List<PointF> points, int blockSize) {
        List<List<PointF>> groups = new ArrayList<>();
        List<PointF> usedPoints = new ArrayList<>(); // Evitar puntos duplicados

        for (PointF point : points) {
            if (isPointUsed(point, usedPoints)) continue;

            List<PointF> group = new ArrayList<>();
            group.add(point);
            usedPoints.add(point);

            for (PointF candidate : points) {
                if (group.size() >= blockSize * blockSize) break; // Tamaño máximo del grupo alcanzado

                if (isPointUsed(candidate, usedPoints)) continue;

                // Verificar proximidad para formar un grupo compacto
                boolean isNear = isNearToGroup(candidate, group);
                if (isNear) {
                    group.add(candidate);
                    usedPoints.add(candidate);
                }
            }

            // Verificar si el grupo tiene tamaño completo; si no, ajustarlo
            if (group.size() < blockSize * blockSize) {
                completeGroup(group, usedPoints, points, blockSize * blockSize);
            }

            // Agregar el grupo incluso si no alcanza el tamaño completo
            if (!group.isEmpty()) {
                groups.add(group);
            }
        }

        return groups;
    }

    private void completeGroup(List<PointF> group, List<PointF> usedPoints, List<PointF> points, int targetSize) {
        for (PointF candidate : points) {
            if (group.size() >= targetSize) break;
            if (isPointUsed(candidate, usedPoints)) continue;

            group.add(candidate);
            usedPoints.add(candidate);
        }
    }

    private boolean isNearToGroup(PointF candidate, List<PointF> group) {
        for (PointF groupedPoint : group) {
            if (distance(groupedPoint, candidate) < 30) { // Umbral de proximidad
                return true;
            }
        }
        return false;
    }

    private boolean isPointUsed(PointF point, List<PointF> usedPoints) {
        for (PointF used : usedPoints) {
            if (distance(point, used) < 1e-3) { // Tolerancia para considerar duplicados
                return true;
            }
        }
        return false;
    }

    private float distance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Dibuja los bloques como círculos en el canvas.
     */
    private void drawBlocks(Canvas canvas) {
        float circleRadius = 9f; // Radio de cada círculo (más uniforme)

        for (Block block : blocks) {
            intersectionPaint.setColor(block.color);
            canvas.drawCircle(block.x, block.y, circleRadius, intersectionPaint);
        }
    }

    /**
     * Calcula todos los puntos de intersección entre los círculos.
     */
    private List<PointF> calculateAllIntersections() {
        List<PointF> points = new ArrayList<>();

        for (int i = 0; i < circles.size(); i++) {
            for (int j = i + 1; j < circles.size(); j++) {
                List<PointF> intersection = getCircleIntersections(circles.get(i), circles.get(j));
                if (intersection != null && !intersection.isEmpty()) {
                    for (PointF p : intersection) {
                        if (!isPointAlreadyAdded(p, points)) {
                            points.add(p);
                        }
                    }
                }
            }
        }

        return points;
    }

    /**
     * Verifica si un punto ya ha sido añadido a la lista, considerando una pequeña tolerancia.
     */
    private boolean isPointAlreadyAdded(PointF point, List<PointF> points) {
        for (PointF p : points) {
            if (distance(p.x, p.y, point.x, point.y) < 1e-3) { // Tolerancia pequeña
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula la distancia entre dos puntos.
     */
    private float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * Calcula los puntos de intersección entre dos círculos.
     */
    private List<PointF> getCircleIntersections(Circle c1, Circle c2) {
        List<PointF> points = new ArrayList<>();

        float dx = c2.centerX - c1.centerX;
        float dy = c2.centerY - c1.centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Verificar si los círculos se intersectan
        if (distance > (c1.radius + c2.radius)) {
            // No hay intersección
            return points;
        }
        if (distance < Math.abs(c1.radius - c2.radius)) {
            // Un círculo está dentro del otro, sin intersección
            return points;
        }
        if (distance == 0 && c1.radius == c2.radius) {
            // Círculos coincidentes, infinitas intersecciones
            return points;
        }

        // Calcular los puntos de intersección
        float a = (c1.radius * c1.radius - c2.radius * c2.radius + distance * distance) / (2 * distance);
        float hSquared = c1.radius * c1.radius - a * a;
        if (hSquared < 0) {
            // No hay intersección real
            return points;
        }
        float h = (float) Math.sqrt(hSquared);

        float x2 = c1.centerX + (a * dx) / distance;
        float y2 = c1.centerY + (a * dy) / distance;

        float rx = -dy * (h / distance);
        float ry = dx * (h / distance);

        // Primer punto de intersección
        float xi1 = x2 + rx;
        float yi1 = y2 + ry;
        points.add(new PointF(xi1, yi1));

        // Segundo punto de intersección
        float xi2 = x2 - rx;
        float yi2 = y2 - ry;
        points.add(new PointF(xi2, yi2));

        return points;
    }

    /**
     * Representa un círculo con un centro y radio.
     */
    private static class Circle {
        float centerX;
        float centerY;
        float radius;

        Circle(float centerX, float centerY, float radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
    }

    /**
     * Representa un bloque en una posición específica con un color.
     */
    private static class Block {
        float x;
        float y;
        int color;

        Block(float x, float y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    /**
     * Shifts the colors of the specified circle blocks.
     *
     * @param circleBlocks Lista de bloques en el círculo
     * @param shiftAmount  Cantidad de posiciones a desplazar (positivo para adelante, negativo para atrás)
     */
    private void shiftColors(List<Block> circleBlocks, int shiftAmount) {
        if (circleBlocks.size() == 0) return;

        // Ordenar los bloques por ángulo para garantizar una secuencia consistente
        Collections.sort(circleBlocks, new Comparator<Block>() {
            @Override
            public int compare(Block b1, Block b2) {
                double angle1 = Math.atan2(b1.y - getCircleCenterY(circleBlocks), b1.x - getCircleCenterX(circleBlocks));
                double angle2 = Math.atan2(b2.y - getCircleCenterY(circleBlocks), b2.x - getCircleCenterX(circleBlocks));
                return Double.compare(angle1, angle2);
            }
        });

        List<Integer> tempColors = new ArrayList<>();
        int size = circleBlocks.size();
        // Ajustar shiftAmount para evitar desplazamientos mayores que el tamaño de la lista
        int normalizedShift = shiftAmount % size;
        if (normalizedShift < 0) {
            normalizedShift += size;
        }

        for (int i = 0; i < size; i++) {
            int shiftedIndex = (i - normalizedShift + size) % size;
            tempColors.add(circleBlocks.get(shiftedIndex).color);
        }

        // Asignar los nuevos colores a los bloques
        for (int i = 0; i < size; i++) {
            circleBlocks.get(i).color = tempColors.get(i);
        }

        invalidate(); // Redibuja la vista con los nuevos colores
    }

    /**
     * Obtiene el centro Y del círculo al que pertenecen los bloques.
     * Asume que todos los bloques pertenecen al mismo círculo.
     */
    private float getCircleCenterY(List<Block> circleBlocks) {
        if (circleBlocks.isEmpty()) return 0;
        // Calcula el promedio de Y de los bloques
        float sum = 0;
        for (Block block : circleBlocks) {
            sum += block.y;
        }
        return sum / circleBlocks.size();
    }

    /**
     * Obtiene el centro X del círculo al que pertenecen los bloques.
     * Asume que todos los bloques pertenecen al mismo círculo.
     */
    private float getCircleCenterX(List<Block> circleBlocks) {
        if (circleBlocks.isEmpty()) return 0;
        // Calcula el promedio de X de los bloques
        float sum = 0;
        for (Block block : circleBlocks) {
            sum += block.x;
        }
        return sum / circleBlocks.size();
    }

    /**
     * Obtiene los bloques que pertenecen a un círculo específico.
     *
     * @param targetCircle El círculo objetivo
     * @return Lista de bloques pertenecientes al círculo
     */
    private List<Block> getBlocksForCircle(Circle targetCircle) {
        List<Block> circleBlocks = new ArrayList<>();

        float tolerance = targetCircle.radius * 0.05f; // 5% de tolerancia

        for (Block block : blocks) {
            float dist = distance(block.x, block.y, targetCircle.centerX, targetCircle.centerY);
            if (Math.abs(dist - targetCircle.radius) <= tolerance) {
                circleBlocks.add(block);
            }
        }

        return circleBlocks;
    }

    // Métodos públicos para mover colores hacia adelante y hacia atrás de cada círculo

    // Grupo 1
    public void shiftGroup1LargeForward() {
        List<Block> groupBlocks = getBlocksForCircle(group1Large);
        shiftColors(groupBlocks, 3); // Shift forward by 1
    }

    public void shiftGroup1LargeBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group1Large);
        shiftColors(groupBlocks, -3); // Shift backward by 1
    }

    public void shiftGroup1MediumForward() {
        List<Block> groupBlocks = getBlocksForCircle(group1Medium);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup1MediumBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group1Medium);
        shiftColors(groupBlocks, -3);
    }

    public void shiftGroup1SmallForward() {
        List<Block> groupBlocks = getBlocksForCircle(group1Small);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup1SmallBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group1Small);
        shiftColors(groupBlocks, -3);
    }

    // Grupo 2
    public void shiftGroup2LargeForward() {
        List<Block> groupBlocks = getBlocksForCircle(group2Large);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup2LargeBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group2Large);
        shiftColors(groupBlocks, -3);
    }

    public void shiftGroup2MediumForward() {
        List<Block> groupBlocks = getBlocksForCircle(group2Medium);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup2MediumBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group2Medium);
        shiftColors(groupBlocks, -3);
    }

    public void shiftGroup2SmallForward() {
        List<Block> groupBlocks = getBlocksForCircle(group2Small);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup2SmallBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group2Small);
        shiftColors(groupBlocks, -3);
    }

    // Grupo 3
    public void shiftGroup3LargeForward() {
        List<Block> groupBlocks = getBlocksForCircle(group3Large);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup3LargeBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group3Large);
        shiftColors(groupBlocks, -3);
    }

    public void shiftGroup3MediumForward() {
        List<Block> groupBlocks = getBlocksForCircle(group3Medium);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup3MediumBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group3Medium);
        shiftColors(groupBlocks, -3);
    }

    public void shiftGroup3SmallForward() {
        List<Block> groupBlocks = getBlocksForCircle(group3Small);
        shiftColors(groupBlocks, 3);
    }

    public void shiftGroup3SmallBackward() {
        List<Block> groupBlocks = getBlocksForCircle(group3Small);
        shiftColors(groupBlocks, -3);
    }
}
