
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

class pair {
    int a, b;
    pair(int x, int y) {
        a = x; b = y;
    }
    @Override
    public String toString() {
        return "x:" + a + " y:" + b; 
    }
}
public class ekans extends Application {

    public ArrayList<Rectangle> snake = new ArrayList<>();
    public Character headDirection = 'U';
    public boolean buttonSpawned = false;
    public Rectangle background,a;
    public Button b;
    public int AppleX = -1, AppleY = -1;
    public ArrayList<pair> unoccupied = new ArrayList<>();
    public int NumOfTiles = 11;
    public boolean win = false;
    /*
     * Method for spawnning apples. 
    */
    public void removeUnoccupied(pair p ){
        for(int i = 0; i < unoccupied.size();i++){
            pair pa = unoccupied.get(i);
            if(pa.a == p.a && pa.b == p.b){
                unoccupied.remove(i);
                break;
            }
        }
    }
    public void spawnApple(Pane p){

            System.out.println(unoccupied.toString());
            Random r = new Random();

            
            int n = r.nextInt(unoccupied.size());
            AppleX = unoccupied.get(n).a;
            AppleY = unoccupied.get(n).b;

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

        Button startGame = new Button("Start Game");
        Button lookAtRules = new Button("View Rules");
        Button backToMenu = new Button("Menu");

        VBox startVBox = new VBox(20, startGame, lookAtRules);

        Scene startScene = new Scene(startVBox, 40*NumOfTiles, 40*NumOfTiles);
        Scene gameScene = new Scene(layout, 40*NumOfTiles, 40*NumOfTiles);
        Scene viewRules = new Scene (layoutRules, 40*NumOfTiles, 40*NumOfTiles);
        /*
         * im gonna scream
        */
        Text text = new Text();      
        text.setText("- arrows keys to move \n\n - if the head of the snake hits the edge or itself, game ends. \n\n -There is always 1 apple on the field, which increases snake length by 1. \n\n -once the entire board is filled with the snake, the player wins."); 
        text.setX(50); 
        text.setY(50);
        
        backToMenu.setPrefSize(300, 50);
        backToMenu.relocate(50, 340);

        startVBox.setAlignment(Pos.BOTTOM_CENTER);
        startVBox.setPadding(new Insets(50));

        startGame.setPrefSize(300, 50);
        startGame.relocate(50, 340);

        lookAtRules.setPrefSize(300, 50);
        lookAtRules.relocate(50, 200);

        startVBox.getChildren().add(layoutStart);

        //layoutStart.getChildren().add(lookAtRules);

        layoutRules.getChildren().add(backToMenu);
        layoutRules.getChildren().add(text);



        /*
         * An attempt to create the window in the middle of the screen,
         * unfortunately does not work at the moment.
        */
        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);  
        
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false);

        primaryStage.show();

        background = new Rectangle(0,0,40*NumOfTiles,40*NumOfTiles);
        background.setFill(Color.RED);
        layout.getChildren().add(background);
        
        // NOT IMPLEMENTED YET!!!!
        
        lookAtRules.setOnAction(event -> {
            primaryStage.setScene(viewRules);
        });

        backToMenu.setOnAction(event -> {
            primaryStage.setScene(startScene);
        });
        startGame.setOnAction(event -> {
            win = false;
            for(int i =0; i< NumOfTiles;i++){
                for(int k = 0; k < NumOfTiles;k++){
                    unoccupied.add(new pair(40*i,40*k));
                }
            }
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
            

            Rectangle rect = new Rectangle((NumOfTiles)/2*40, (NumOfTiles)/2 *40, 40, 40);
            
            snake.add(rect);
            removeUnoccupied(new pair((int)rect.getX(), (int)rect.getY()));

            headDirection = 'U';
            buttonSpawned = false;
            spawnApple(layout);
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
            if(unoccupied.size() == 0){
                win = true;
            }


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
                if (snake.get(0).getX() < 0 || snake.get(0).getX() > 40*(NumOfTiles-1) || snake.get(0).getY() < 0 || snake.get(0).getY()> 40*(NumOfTiles-1))
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

                    b = new Button( win? "win, click to retry!":("retry, score is: " + score));
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
                removeUnoccupied(new pair((int)headX, (int)headY));
                layout.getChildren().add(add);
                if (snake.size() > score + 99) {
                   layout.getChildren().remove( snake.get(snake.size() - 1));
                   Rectangle remove = snake.get(snake.size()-1);
                    unoccupied.add(new pair((int)remove.getX(), (int)remove.getY()));
                    snake.remove(snake.size() - 1);

                }
            }
        }
    }

    public static void main(String[] banana) {
        launch(banana);
    }

}