package greszler.szilard.hurba.drawbotrouter;

/**
 * 
 * Container class for an SVG ViewBox
 * 
 */
public class SvgViewBox {

	private double minX, minY, width, height;
	
	public SvgViewBox() {
	}

	public SvgViewBox(double minX, double minY, double width, double height) {
		this.minX = minX;
		this.minY = minY;
		this.width = width;
		this.height = height;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
