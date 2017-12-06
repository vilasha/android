package ru.geekbrains.eatingballs;

import android.graphics.Paint;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by maria on 17/11/2017.
 */

public class GameManager {
    public static final int MAX_CIRCLES = 18;
    private MainCircle mainCircle;
    private ArrayList<EnemyCircle> circles;
    private CanvasView canvasView;
    private static int width;
    private static int height;

    public GameManager(CanvasView canvasView, int w, int h) {
        this.canvasView = canvasView;
        width = w;
        height = h;
        initMainCircle();
        initEnemyCircles();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    private void initMainCircle() {

        mainCircle = new MainCircle(width / 2, height / 2);
    }

    private void initEnemyCircles() {
        circles = new ArrayList<EnemyCircle>();
        for (int i = 0; i< MAX_CIRCLES; i++) {
            EnemyCircle circle;
            SimpleCircle mainCircleArea = mainCircle.getCircleArea();
            do {
                circle = EnemyCircle.getRandomCircle();
            } while (circle.isIntersect(mainCircleArea));
            circles.add(circle);
        }
        calculateAndSetCirclesColor();
    }

    private void calculateAndSetCirclesColor() {
        for(EnemyCircle circle : circles) {
            circle.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    public void onDraw() {
        canvasView.drawCircle(mainCircle);
        for (EnemyCircle circle : circles) {
            canvasView.drawCircle(circle);
        }
    }

    public void onTouchEvent(int x, int y) {
        mainCircle.moveMainCircleWhenTouchAt(x, y);
        checkCollision();
        moveCircles();
    }

    private void checkCollision() {
        SimpleCircle circleForDel = null;
        for (EnemyCircle circle : circles) {
            if (mainCircle.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCircle)) {
                    mainCircle.growRadius(circle);
                    circleForDel = circle;
                    calculateAndSetCirclesColor();
                    break;
                } else {
                    gameEnd("You lost :(");
                    return;
                }
            }
        }
        if (circleForDel != null) {
            circles.remove(circleForDel);
        }
        if (circles.isEmpty()) {
            gameEnd("You win!");
        }
    }

    private void gameEnd(String text) {
        canvasView.showMessage(text);
        mainCircle.initRadius();
        initEnemyCircles();
        canvasView.redraw();
    }

    private void moveCircles() {
        for (EnemyCircle circle : circles) {
            circle.moveOneStep();
        }
    }
}
