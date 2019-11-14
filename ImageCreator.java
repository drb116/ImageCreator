/**
 * @author dburnham
 * Image Creator
 * November 14th, 2019
 */
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

public class ImageCreator extends Application {

	private static java.awt.Color[][] pixColors;
	private static Pane root;

	    @Override
	    public void start(Stage stage) throws Exception {
			Scanner input = new Scanner(System.in);
			System.out.println("Please enter a file name: ");
			String newFile = input.nextLine();

            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(newFile));
            } catch (IOException e) {
                System.out.println("File not found.");
				return;
            }

            int pixelCount = 0;
            long redTotal = 0;
            long greenTotal = 0;
            long blueTotal = 0;
			int height = img.getHeight();
			int width = img.getWidth();
            pixColors = new java.awt.Color[width][height];

            for (int i = 0; i < width; i++){
                for (int j =0; j < height; j++) {
					pixelCount ++;
                    pixColors[i][j] = new java.awt.Color(img.getRGB(i, j));
                    redTotal += pixColors[i][j].getRed();
                    greenTotal += pixColors[i][j].getGreen();
                    blueTotal += pixColors[i][j].getBlue();
                }
            }

            //Initial circle
			int size = height;
			Circle circle = new Circle();
			if (height > width) {
				circle.setRadius(width/2);
				size = width;
			} else {
				circle.setRadius(height/2);

			}
			circle.setCenterX(width/2);
			circle.setCenterY(height/2);
			circle.setFill(Color.rgb((int) (redTotal/pixelCount),
			 						 (int) (greenTotal/pixelCount),
									 (int) (blueTotal/pixelCount)));

		 	circle.addEventHandler(MouseEvent.MOUSE_ENTERED,
         		e -> entered(e));

			root = new Pane();
			root.getChildren().add(circle);
			Scene scene = new Scene(root,width,height);

	    	stage.setTitle(newFile);
	    	 stage.setScene(scene);
	    	stage.show();

	    }

	private static void entered(MouseEvent e){
		Circle enteredCircle = (Circle) e.getTarget();
		double centerX =  enteredCircle.getCenterX();
		double centerY =  enteredCircle.getCenterY();
		double currRadius =  enteredCircle.getRadius();
		double newRadius = currRadius/2;
		double minX = centerX - currRadius;
		double minY = centerY - currRadius;
		double maxX = centerX + currRadius;
		double maxY = centerY + currRadius;
		if (newRadius>1){
			//Circle 1 - upper left
			Circle circle1 = new Circle(centerX-newRadius,
										centerY-newRadius,
										newRadius);
			int[] colors1 = getColors((int)minX,(int)minY,(int)centerX, (int)centerY);
			circle1.setFill(Color.rgb((int) (colors1[1]/colors1[0]),
			 						 (int) (colors1[2]/colors1[0]),
									 (int) (colors1[3]/colors1[0])));

			 //Circle 2 - upper right
 			Circle circle2 = new Circle(centerX+newRadius,
 										centerY-newRadius,
 										newRadius);
 			int[] colors2= getColors((int)centerX, (int)minY, (int)maxX, (int)centerY);
 			circle2.setFill(Color.rgb((int) (colors2[1]/colors2[0]),
 			 						 (int) (colors2[2]/colors2[0]),
 									 (int) (colors2[3]/colors2[0])));

			 //Circle 3 - bottom left
 			Circle circle3 = new Circle(centerX-newRadius,
 										centerY+newRadius,
 										newRadius);
 			int[] colors3 = getColors((int)minX,(int)centerY,(int)centerX, (int)maxY);
 			circle3.setFill(Color.rgb((int) (colors3[1]/colors3[0]),
 			 						 (int) (colors3[2]/colors3[0]),
 									 (int) (colors3[3]/colors3[0])));

			 //Circle 4 - bottom right
 			Circle circle4 = new Circle(centerX+newRadius,
 										centerY+newRadius,
 										newRadius);
 			int[] colors4 = getColors((int)centerX,(int)centerY,(int)maxX, (int)maxY);
 			circle4.setFill(Color.rgb((int) (colors4[1]/colors4[0]),
 			 						 (int) (colors4[2]/colors4[0]),
 									 (int) (colors4[3]/colors4[0])));

			circle1.addEventHandler(MouseEvent.MOUSE_ENTERED,
					e1 -> entered(e1));
			circle2.addEventHandler(MouseEvent.MOUSE_ENTERED,
					e1 -> entered(e1));
			circle3.addEventHandler(MouseEvent.MOUSE_ENTERED,
					e1 -> entered(e1));
			circle4.addEventHandler(MouseEvent.MOUSE_ENTERED,
					e1 -> entered(e1));
			root.getChildren().remove(enteredCircle);
			root.getChildren().addAll(circle1, circle2,circle3,circle4);
		}

	}

	private static int[] getColors(int minX, int minY, int maxX, int maxY){
		int[] returnArray = new int[4];
		for (int i = minY; i < maxY; i++){
			for (int j =minX; j < maxX; j++) {
				returnArray[0] ++;
				returnArray[1] += pixColors[j][i].getRed();
				returnArray[2] += pixColors[j][i].getGreen();
				returnArray[3] += pixColors[j][i].getBlue();
			}
		}
		return returnArray;
	}
	public static void main(String[] args) { launch(args); }
}
