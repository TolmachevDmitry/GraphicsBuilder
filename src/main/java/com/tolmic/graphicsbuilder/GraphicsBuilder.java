package com.tolmic.graphicsbuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.BinaryOperator;

import com.tolmic.graphicsbuilder.predicate.CurrentArguments;
import com.tolmic.graphicsbuilder.predicate.ElementaryPredicate;
import com.tolmic.graphicsbuilder.predicate.Predicate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class GraphicsBuilder extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GraphicsBuilder.class.getResource("graphics-builder.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 900);

        stage.setTitle("Graphics Builder");
        stage.setScene(scene);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("images/icon.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
