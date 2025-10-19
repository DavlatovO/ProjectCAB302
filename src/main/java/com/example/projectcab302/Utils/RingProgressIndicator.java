package com.example.projectcab302.Utils;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * A custom circular progress indicator shaped like a ring.
 * Use setProgress(value) to update from 0.0 to 1.0.
 */
public class RingProgressIndicator extends StackPane {

    private final DoubleProperty progress = new SimpleDoubleProperty(0);
    private final Arc arc = new Arc();
    private final Text label = new Text();

    public RingProgressIndicator(double size, Color color) {
        setPrefSize(size, size);

        Circle backgroundCircle = new Circle(size / 2.0);
        backgroundCircle.setFill(Color.TRANSPARENT);
        backgroundCircle.setStroke(Color.web("#E0E0E0"));
        backgroundCircle.setStrokeWidth(size * 0.1);

        arc.setCenterX(0);
        arc.setCenterY(0);
        arc.setRadiusX(size / 2.0);
        arc.setRadiusY(size / 2.0);
        arc.setStartAngle(90); // Start at top
        arc.setLength(0);
        arc.setType(ArcType.OPEN);
        arc.setStroke(color);
        arc.setStrokeWidth(size * 0.1);
        arc.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        arc.setFill(Color.TRANSPARENT);

        label.setFont(Font.font("Arial", FontWeight.BOLD, size * 0.25));
        label.setFill(Color.web("#333"));

        Group arcGroup = new Group(arc);
        getChildren().addAll(backgroundCircle, arcGroup, label);

        progress.addListener((obs, oldVal, newVal) -> updateArc());

        updateArc();
    }

    private void updateArc() {
        arc.setLength(-360 * progress.get());
        label.setText(String.format("%.0f%%", progress.get() * 100));
    }

    public void setProgress(double value) {
        progress.set(Math.max(0, Math.min(1, value))); // clamp 0–1
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void animateTo(double targetProgress, double durationMs) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(durationMs),
                        new KeyValue(progress, targetProgress, Interpolator.EASE_BOTH))
        );
        timeline.play();
    }
}

