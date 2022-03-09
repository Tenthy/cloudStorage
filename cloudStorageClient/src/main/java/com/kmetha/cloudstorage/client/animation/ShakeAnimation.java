package com.kmetha.cloudstorage.client.animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ShakeAnimation {

    private TranslateTransition tt;

    public ShakeAnimation(Node node) {
         tt = new TranslateTransition(Duration.millis(80), node);
         tt.setFromX(-10);
         tt.setByX(10);
         tt.setCycleCount(3);
         tt.setAutoReverse(true);
    }

    public void playAnimation() {
        tt.playFromStart();
    }
}
