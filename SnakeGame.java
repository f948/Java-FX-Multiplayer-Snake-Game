/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


/**
 *
 * @author Lawrence Chen 
 */
public class SnakeGame extends Application {
    
    // global variables 
   static List<GreenBodyPart> greenBody = new ArrayList<>();
   static int greenRandomX,greenRandomY;
   static int greenHeadX,greenHeadY;
   static String greenDirection="";
   static int greenLength=1;
   
   static int i,randomNum;
   static boolean invalidPlacement;
   static int foodRandomX,foodRandomY;
   static Random rand = new Random();
   
   // set up the scene 
    static Canvas canvas = new Canvas(1280, 640);
    static GraphicsContext context = canvas.getGraphicsContext2D();
    static Group root = new Group(canvas);
    static Scene scene = new Scene(root,1280,640);

   public static class GreenBodyPart {
	int x;
	int y;

	public GreenBodyPart(int x, int y) {
		this.x = x;
		this.y = y;
	}

}
   
   public static void createFood(){
       
       invalidPlacement = true;
       
       while(invalidPlacement){
            foodRandomX=rand.nextInt(37)+1;
            foodRandomY=rand.nextInt(17)+1;
            
            invalidPlacement=false;
            
            // do not put food on same square as green snake 
	    for(i=0;i<=greenBody.size()-1;i++){
						
		if(foodRandomX*32 == greenBody.get(i).x && foodRandomY*32 == greenBody.get(i).y){
		    invalidPlacement=true;
		}
	    }
        }
   }

   public static void restart(){
       
       context.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
       
       greenBody.clear();
       
       // create green snake head 
        greenRandomX=rand.nextInt(37)+1;
        greenRandomY=rand.nextInt(17)+1;
				
	greenHeadX = greenRandomX*32;
	greenHeadY = greenRandomY*32;
				
	greenBody.add(new GreenBodyPart(greenHeadX,greenHeadY));
					
	// create first food piece 
	createFood(); 
   }
   
    @Override
    public void start(Stage primaryStage) {
        
        // control
	scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.W) {
		greenDirection="up";
            }
            if (key.getCode() == KeyCode.A) {
		greenDirection ="left";
            }
            if (key.getCode() == KeyCode.S) {
		greenDirection="down";
	    }
	    if (key.getCode() == KeyCode.D) {
		greenDirection="right";
	    }

	});
        
        restart();
        
        // update screen every 55 milliseconds 
        Timer timer = new Timer(); 
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                
                // clear the screen
                context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // move snake head
		if(greenDirection == "left"){
                    greenHeadX-=32;
		}
		else if(greenDirection == "right"){
                    greenHeadX+=32;
		}
		else if(greenDirection == "up"){
                    greenHeadY-=32;
		}
                else if(greenDirection == "down"){
                    greenHeadY+=32;
		}
             
                // food has been eaten by green snake 
		if(greenHeadX == foodRandomX*32 && greenHeadY== foodRandomY*32){
               
                    createFood();
                    greenBody.add(new GreenBodyPart(greenHeadX,greenHeadY));
     
		}
                
                // collision detection for green snake 
		for(i=1;i<=greenBody.size()-1;i++){
					
                    if(greenBody.get(0).x == greenBody.get(i).x && greenBody.get(0).y == greenBody.get(i).y){
						
			restart();
                        
                    }
		}
                
                // green snake hits the border 
		if(greenBody.get(0).x <=0 || greenBody.get(0).x >=1248 || greenBody.get(0).y <=0 || greenBody.get(0).y >=608){
					
                    restart();
		}
                
                // move snake forward
		greenBody.remove(greenBody.size()-1);
		greenBody.add(0,new GreenBodyPart(greenHeadX,greenHeadY));
		
		// draw food 
		context.setFill(Color.RED);
		context.fillRect(foodRandomX*32,foodRandomY*32,32,32);
                
                // draw green snake body
                context.setFill(Color.GREEN);
		for(i=0;i<=greenBody.size()-1;i++){
							
                    context.fillRect(greenBody.get(i).x,greenBody.get(i).y,32,32);

		}
            }
        },0,55); 
        
        // show stage
        primaryStage.setScene(scene);
	primaryStage.setTitle("Snake game");
	primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
