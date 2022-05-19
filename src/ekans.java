
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

class pair {
    int a, b;
    pair(int x, int y) {
        a = x; b = y;
    }
}
public class ekans extends Application {

    public ArrayList<Rectangle> snake = new ArrayList<>();
    public Character headDirection = 'U';
    public boolean buttonSpawned = false;
    public Rectangle background,a;
    public Button b;
    public int AppleX = -1, AppleY = -1;

    /*
     * Method for spawnning apples. 
    */
    public void spawnApple(Pane p){

            Random r = new Random();
            boolean overlapSnake = false;

            // not used?
            ArrayList<pair> arr = new ArrayList<>();
            do {
                AppleX = r.nextInt(17)*40;
                AppleY = r.nextInt(17)*40;
            }
            while(overlapSnake);
            a = new Rectangle(AppleX,AppleY,40,40);
            a.setFill(Color.GREEN);
            p.getChildren().add(a);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        Pane layout = new Pane();
        Pane layoutStart = new Pane();
        Pane layoutRules = new Pane();

        Scene startScene = new Scene(layoutStart, 40*17, 40*17);
        Scene gameScene = new Scene(layout, 40*17, 40*17);
        Scene viewRules = new Scene (layoutRules, 40*17, 40*17);

        Label startThing = new Label("SNAKE");

        Button startGame = new Button("Start Game");
        Button lookAtRules = new Button("View Rules");
        /*
         * im gonna scream
        */
        //startScene.getStylesheets().add(getClass().getResource("/resources/css/fontStyle.css").toExternalForm());

        startThing.setTextAlignment(TextAlignment.CENTER);
        startThing.relocate(280, 200);

        startGame.setPrefSize(300, 50);
        startGame.relocate(200, 340);

        lookAtRules.setPrefSize(300, 50);
        lookAtRules.relocate(200, 200);

        layoutStart.getChildren().add(startThing);
        layoutStart.getChildren().add(startGame);
        layoutStart.getChildren().add(lookAtRules);

        /*
         * An attempt to create the window in the middle of the screen,
         * unfortunately does not work at the moment.
        */
        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);  
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false);

        primaryStage.show();

        background = new Rectangle(0,0,40*17,40*17);
        background.setFill(Color.RED);
        layout.getChildren().add(background);
        
        // NOT IMPLEMENTED YET!!!!
        
        lookAtRules.setOnAction(event -> {
            primaryStage.setScene(viewRules);
        });
        startGame.setOnAction(event -> {
            
            primaryStage.setScene(gameScene);

            if (b != null) layout.getChildren().remove(b);

            background.requestFocus();
            gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.RIGHT && headDirection != 'L') {
                        headDirection = 'R';
                        System.out.println(headDirection);
                    }
                    if (event.getCode() == KeyCode.LEFT && headDirection != 'R') {
                        headDirection = 'L';
                        System.out.println(headDirection);
                    }
                    if (event.getCode() == KeyCode.UP && headDirection != 'D') {
                        headDirection = 'U';
                        System.out.println(headDirection);
                    }
                    if (event.getCode() == KeyCode.DOWN && headDirection != 'U') {
                        headDirection = 'D';
                        System.out.println(headDirection);
                    }
                }
            });
            
            spawnApple(layout);
            Rectangle rect = new Rectangle(8*40, 8*40, 40, 40);
            
            snake.add(rect);
            headDirection = 'U';
            buttonSpawned = false;
           
            layout.getChildren().add(rect);
            AnimationTimer timer = new MyTimer(layout,primaryStage, startScene);
            timer.start();

        });

    }

    class MyTimer extends AnimationTimer {
        Pane layout;
        Stage s;
        int score = 3;
        Scene startSc;
        public boolean colllided = false;

        MyTimer(Pane p, Stage st, Scene g) {
            layout = p;
            s = st;
            startSc = g;
            
        }

        public void handle(long a) {
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (Exception e) {}
            fun();
        }

        private void fun() {
            /*
             * Checks if snake's head (index 0) is equal to the X & Y coordinates of an apple. 
             * Increases score by one if condition is met.
             * 
             * Removes apple once eaten and spawns a new apple (spawnApple)
             * 
            */
            if (snake.get(0).getX() == AppleX && snake.get(0).getY() == AppleY ) {
                score++;
                layout.getChildren().remove(a);
                spawnApple(layout);
            }
            for (int i = 1; i < snake.size(); i++) {
                if (snake.get(i).getX() == snake.get(0).getX() &&
                        snake.get(i).getY() == snake.get(0).getY())
                    colllided = true;
            }
            if (snake.size() > 1) {
                if (snake.get(0).getX() < 0 || snake.get(0).getX() > 40*16 || snake.get(0).getY() < 0 || snake.get(0).getY()> 40*16)
                colllided = true;
            }
            /*
             * Check to see snake has collided within itself 
             * 
            */
            if (colllided) {
                layout.getChildren().remove(a);
                if (buttonSpawned) {} else {
                    buttonSpawned = true;

                    b = new Button("retry, score is: " + score);
                    b.setPrefSize(300, 50);
                    b.relocate(200, 340);

                    b.setOnAction(event -> {
                        while (snake.size()>0) {
                            layout.getChildren().remove( snake.get(0));
                            snake.remove(0);
                        }
                        this.stop();                       
                        s.setScene(startSc);
                    });
                    layout.getChildren().add(b);
                }
             
            } else {
                Rectangle add = new Rectangle(0, 0, 40, 40);
                double headX = snake.get(0).getX(), headY = snake.get(0).getY();

                if (headDirection == 'U') {
                    add.setY(headY - 40);
                    add.setX(headX);
                }
                if (headDirection == 'D') {
                    add.setY(headY + 40);
                    add.setX(headX);
                }
                if (headDirection == 'R') {
                    add.setY(headY);
                    add.setX(headX + 40);
                }
                if (headDirection == 'L') {
                    add.setY(headY);
                    add.setX(headX - 40);
                }
                snake.add(0, add);

                layout.getChildren().add(add);

                if (snake.size() > score) {
                   layout.getChildren().remove( snake.get(snake.size() - 1));
                    snake.remove(snake.size() - 1);
                }
            }
        }
    }

    public static void main(String[] banana) {
        launch(banana);
    }

}