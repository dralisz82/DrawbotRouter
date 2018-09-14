package greszler.szilard.hurba.drawbotrouter;

public class SvgPrimitive {

	private String action;
	private double x, y;
	
	public SvgPrimitive() {
		// TODO Auto-generated constructor stub
	}

	public SvgPrimitive(String action) {
		this.action = action;
	}

	public SvgPrimitive(String action, double x, double y) {
		this.action = action;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "SvgPrimitive [action=" + action + ", x=" + x + ", y=" + y + "]";
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
