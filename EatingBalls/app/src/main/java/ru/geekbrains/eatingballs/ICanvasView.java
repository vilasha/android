package ru.geekbrains.eatingballs;

/**
 * Created by maria on 18/11/2017.
 */

public interface ICanvasView {
    void drawCircle(SimpleCircle circle);
    void redraw();
    void showMessage(String text);
}
