import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * This class creates the viewing area. It draws a shape and the axis. Also
 * includes matrix operations.
 * 
 * @author Scott Bell
 * @version 12-5-13
 */
public class Shape extends JPanel {

	private static ArrayList<Vector> shape; // Essentially a 2-D matrix
	private static ArrayList<Integer> connected; // Holds the positions of the connected

	private Color BACKGROUND = Color.white;
	private static int zoom;
	private final int BOX_SIZE = 700; // Length and width of display in pixels
	private final int MID = BOX_SIZE / 2; // Middle of display
	private final int START_ZOOM = 35; // Initial zoom level
	private final int DOT_RADIUS = 5; // Vertices

	/**
	 * Basic constructor.
	 */
	public Shape() {
		shape = new ArrayList<Vector>();
		connected = new ArrayList<Integer>();
		zoom = START_ZOOM;
		guiSetUp();
	}

	/**
	 * Draws a shape from a list of instructions. The instructions hold
	 * coordinates in pairs of two (Vectors), a -1 to signal a switch to
	 * connections, and then coordinates of points to connect.
	 * 
	 */
	public void drawShape(ArrayList<Integer> points, ArrayList<Integer> connect) {
		
		clear(); // Empties current shape and connections
		
		for( int i = 0; i < points.size(); i+=2)
			addIntoMatrix(points.get(i),points.get(i+1));
		for( int j = 0; j < connect.size() - 1;j+=2){
			connectVectors(connect.get(j),connect.get(j+1));
		}

		repaint(); 

	}

	/**
	 * Paints to shape onto the display.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawLine(0, MID, BOX_SIZE, MID); // X-Axis
		g.drawLine(MID, 0, MID, BOX_SIZE); // Y-Axis

		//tick marks
		boolean endX = false;
		boolean endY = false;
		int spacer = 1;
		while(!endX){
			if(zoom * spacer >= BOX_SIZE)
				endX = true;
			else{
				g.drawLine(zoom * spacer + MID + 2 , MID - 5, zoom * spacer + MID + 2 , MID + 5);
				g.drawLine(zoom * -spacer + MID +2  , MID - 5, zoom * -spacer + MID +2 , MID + 5);
				spacer += 1;
			}
		}
		spacer = 1;
		while(!endY){
			if(zoom * spacer >= BOX_SIZE)
				endY = true;
			else{
				g.drawLine(MID - 5,zoom * spacer + MID -2, MID + 5, zoom * spacer + MID -2 );
				g.drawLine(MID - 5,zoom * -spacer + MID-2, MID + 5, zoom * -spacer + MID-2);
				spacer += 1;
			}
		}
		
		// Plotting Vectors
		if (shape.size() > 0) {
			double x, y;
			int newX, newY;

			for (Vector v : shape) {
				x = v.getX();
				newX = (int) (x * zoom + MID); // Places correctly according to X-Axis
				y = v.getY();
				newY = (int) (y * -zoom + MID); // Places correctly according to Y-Axis

				g.setColor(Color.black);
				g.fillOval(newX, newY - DOT_RADIUS, DOT_RADIUS, DOT_RADIUS); // Makes a vertex
			}
		}


		// Connecting Vectors
			double x1, x2, y1, y2;
			for (int c = 0; c < connected.size() - 1;c+=2) {
				x1 = shape.get(connected.get(c)).getX();
				x2 = shape.get(connected.get(c+1)).getX();
				y1 = shape.get(connected.get(c)).getY();
				y2 = shape.get(connected.get(c+1)).getY();

				g.drawLine((int) (x1 * zoom + MID + DOT_RADIUS / 2), (int) (y1 // Draws a line between both Vectors
																				
						* -zoom + MID - DOT_RADIUS / 2), // Line connects from center of vertex to center of vertex													 
						(int) (x2 * zoom + MID + DOT_RADIUS / 2), (int) (y2
								* -zoom + MID - DOT_RADIUS / 2));
			}

		}


	/**
	 * Basic GUI setup - size, color, border.
	 */
	public void guiSetUp() {
		setBackground(BACKGROUND);
		setSize(BOX_SIZE, BOX_SIZE);
		setPreferredSize(new Dimension(BOX_SIZE, BOX_SIZE));
		setMinimumSize(new Dimension(BOX_SIZE, BOX_SIZE));
		setMaximumSize(new Dimension(BOX_SIZE, BOX_SIZE));

		Border raised = BorderFactory.createRaisedBevelBorder();
		Border lowered = BorderFactory.createLoweredBevelBorder();
		Border compound = BorderFactory.createCompoundBorder(raised, lowered);
		setBorder(compound);
	}

	/**
	 * Makes a Vector from coordinates. Places Vector in matrix (shape).
	 */
	private void addIntoMatrix(int x, int y) {

		Vector newVector = new Vector(x, y);
		shape.add(newVector);
	}

	/**
	 * Connects two Vectors.
	 */
	private void connectVectors(int m, int n) {
		connected.add(m);
		connected.add(n);

	}

	/**
	 * Scale operation.
	 * 
	 * Multiplies each coordinate of each Vector in the shape by the scalar.
	 */
	public void scaleShape(double scalar) {
		for (Vector v : shape) {
			double x = v.getX() * scalar;
			double y = v.getY() * scalar;

			v.setX(x);
			v.setY(y);

		}
		repaint();
	}

	/**
	 * Translate operation.
	 * 
	 * For each x coordinate of each Vector in shape add the amount to move.
	 * Same for each y coordinate.
	 */
	public void translateShape(double moveX, double moveY) {
		for (Vector v : shape) {
			double x = v.getX() + moveX;
			double y = v.getY() + moveY;

			v.setX(x);
			v.setY(y);

		}
		repaint();
	}

	/**
	 * Rotate operation.
	 * 
	 * Makes a rotation Matrix based on degrees Rotates the shape clockwise or
	 * counter-clockwise Multiplies the rotation matrix by the shape, one Vector
	 * at a time Calls multiplyVector to handle the multiplication
	 */
	public void rotateShape(double degrees, boolean clockwise) {
		if (clockwise)
			degrees *= -1;

		double radians = Math.toRadians(degrees);
		ArrayList<Vector> rotationMatrix = new ArrayList<Vector>(2);
		rotationMatrix.add(new Vector(Math.cos(radians), Math.sin(radians)));
		rotationMatrix
				.add(new Vector(-1 * Math.sin(radians), Math.cos(radians)));

		for (Vector v : shape) {
			multiplyVector(rotationMatrix, v);

		}
		repaint();

	}

	/**
	 * Reflect operation.
	 * 
	 * Reflects the shape over the x-axis, y-axis, or origin Also calls
	 * multiplyVector to handle multiplication
	 */

	public void reflectShape(char rVector) {

		ArrayList<Vector> reflectionMatrix = new ArrayList<Vector>(2);
		if (rVector == 'x') {
			reflectionMatrix.add(new Vector(1, 0));
			reflectionMatrix.add(new Vector(0, -1));
		} else if (rVector == 'y') {
			reflectionMatrix.add(new Vector(-1, 0));
			reflectionMatrix.add(new Vector(0, 1));
		} else {
			reflectionMatrix.add(new Vector(-1, 0));
			reflectionMatrix.add(new Vector(0, -1));
		}

		for (Vector v : shape) {
			multiplyVector(reflectionMatrix, v);

		}
		repaint();

	}

	/**
	 * Multiplies a 2x2 matrix with a Vector (2x1 matrix)
	 * 
	 */
	private void multiplyVector(ArrayList<Vector> multiplier, Vector v) {
		double x, y;

		x = v.getX() * multiplier.get(0).getX() + v.getY()
				* multiplier.get(1).getX();
		y = v.getX() * multiplier.get(0).getY() + v.getY()
				* multiplier.get(1).getY();

		v.setX(x);
		v.setY(y);

	}

	// Clears shape array and connections array
	public void clear() {
		shape.clear();
		connected.clear();
		repaint();
	}

	// Increase zoom level by 5
	public void zoomIn() {
		zoom += 10;
		repaint();

	}

	// Decrease zoom level by 5.  Cannot be at or below 0
	public void zoomOut() {
		if (zoom > 10) {
			zoom -= 10;
			repaint();
		}
	}

	//  Reset to default zoom level
	public void resetZoom() {
		zoom = START_ZOOM;
		repaint();
	}

	/**
	 * Draws a square given side length and the center (x, y).
	 * 
	 * Vertices are  (x +- sideLength / 2, y +- sideLength / 2)
	 */
	public void drawSquare(double sideLength, Vector center) {
		clear();
		Vector v1 = new Vector(center.getX() - sideLength / 2, center.getY()
				- sideLength / 2);
		Vector v2 = new Vector(center.getX() - sideLength / 2, center.getY()
				+ sideLength / 2);
		Vector v3 = new Vector(center.getX() + sideLength / 2, center.getY()
				+ sideLength / 2);
		Vector v4 = new Vector(center.getX() + sideLength / 2, center.getY()
				- sideLength / 2);

		shape.add(v1);
		shape.add(v2);
		shape.add(v3);
		shape.add(v4);

		connectVectors(0, 1);
		connectVectors(1, 2);
		connectVectors(2, 3);
		connectVectors(3, 0);

		repaint();

	}

	/**
	 * Draws an equilateral triangle given side length and the center (x, y).
	 * 
	 * Vertices are  (x +- sideLength / 2, y - sideLength / 2) and (x, y + sideLength / 2)
	 */
	public void drawTriangle(double sideLength, Vector center) {
		clear();
		Vector v1 = new Vector(center.getX() - sideLength / 2, center.getY()
				- sideLength / 2);
		Vector v2 = new Vector(center.getX() + sideLength / 2, center.getY()
				- sideLength / 2);
		Vector v3 = new Vector(center.getX(), center.getY() + sideLength / 2);

		shape.add(v1);
		shape.add(v2);
		shape.add(v3);

		connectVectors(0, 1);
		connectVectors(1, 2);
		connectVectors(2, 0);

		repaint();

	}

	/**
	 * Draws an circle given radius and the center (x, y).
	 * 
	 * A point is drawn 1 radius away from the center
	 * That point is rotated 360 degrees in increments of 5 degrees.
	 */
	public void drawCircle(double radius, Vector center) {
		clear();

		Vector v1 = new Vector(radius, 0);	// First point at (radius, 0)
		double radians = 0;
		for (int degrees = 0; degrees/5 < 72; degrees+=5) {		// Rotate 360 degrees, increments of 5 degrees
			
			radians = Math.toRadians(degrees);
			ArrayList<Vector> rotationMatrix = new ArrayList<Vector>(2);
			rotationMatrix
					.add(new Vector(Math.cos(radians), Math.sin(radians)));
			rotationMatrix.add(new Vector(-1 * Math.sin(radians), Math
					.cos(radians)));

			double x, y;
			x = v1.getX() * rotationMatrix.get(0).getX() + v1.getY()
					* rotationMatrix.get(1).getX();
			y = v1.getX() * rotationMatrix.get(0).getY() + v1.getY()
					* rotationMatrix.get(1).getY();

			shape.add(new Vector(x + center.getX(), y + center.getY()));
		}

		for (int c = 0; c < 71; c++) {
			connectVectors(c, c + 1);
		}
		connectVectors(71, 0);
		repaint();

	}
}
