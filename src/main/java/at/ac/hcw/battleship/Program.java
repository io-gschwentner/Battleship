package at.ac.hcw.battleship;

import javafx.application.Application;
import javafx.stage.Stage;

public class Program extends Application{

    @Override
    public void start(Stage stage) {
        BoardView view = new BoardView();
        view.showView(stage);
    }
    public static void main(String[] args){
        launch(args);
        
    }
}
