/**
 * The class for individual vectors.  Each has an x and y coordinate.
 * 
 * @author Scott Bell
 * @version 12-5-13
 */

public class Vector {

	private double x, y;

	public Vector(double xVal, double yVal) {
		x = xVal;
		y = yVal;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double newX) {
		x = newX;
	}

	public void setY(double newY) {
		y = newY;
	}
	
}
