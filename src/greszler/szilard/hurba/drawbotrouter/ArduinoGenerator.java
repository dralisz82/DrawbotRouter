package greszler.szilard.hurba.drawbotrouter;

import java.util.ArrayList;
import java.util.List;

public class ArduinoGenerator {

	private String title, description;
	private String metric;
	private double width, height;
	private SvgViewBox viewBox;
	private List<String> paths;
	
	private double robotPosX, robotPosY;	// in mm
	private double robotOrientation;		// in degrees
	private boolean robotDraws;
	
	private String initRobotStr = ""+
	"#include <AFMotor.h>\n"+
	"#include <Servo.h>\n\n"+
	"// AF_Stepper needs this value\n"+
	"#define STEPS 48\n"+
	"// Number of steps in a revolution (.0 is required by the computations)\n"+
	"#define REVOLUTION 2048.0\n"+
	"// Circumference is 251 mm for 80 mm diameter wheels (lasercut drawbot)\n"+
	"#define WHEELCIRCUMFERENCE 251.0\n"+
	"// Distance of wheels' centerline is 119 mm (lasercut drawbot)\n"+
	"#define WHEELDISTANCE 119.0\n\n"+
	"AF_Stepper motorR(STEPS, 1);\n"+
	"AF_Stepper motorL(STEPS, 2);\n"+
	"Servo pen;\n"+
	"\n"+

	"void forward(double distance){\n"+
	"  // Number of steps required for a full revolution is divided by the wheel circumference, thus traveling distance times unit of circumference\n"+
	"  int steps; // holds number of required steps\n"+
	"  steps = distance * REVOLUTION / WHEELCIRCUMFERENCE;\n"+
	"  for(int i = 0; i < steps; i++){\n"+
	"    motorL.step(1, FORWARD, DOUBLE);\n"+
	"    motorR.step(1, FORWARD, DOUBLE);\n"+
	"  }\n"+
	"}\n\n"+

	"void backward(double distance){\n"+
	"  // Number of steps required for a full revolution is divided by the wheel circumference, thus traveling distance times unit of circumference\n"+
	"  int steps; // holds number of required steps\n"+
	"  steps = distance * REVOLUTION / WHEELCIRCUMFERENCE;\n"+
	"  for(int i = 0; i < steps; i++){\n"+
	"    motorL.step(1, BACKWARD, DOUBLE);\n"+
	"    motorR.step(1, BACKWARD, DOUBLE);\n"+
	"  }\n"+
	"}\n\n"+

	"void turnLeft(double degree) {\n"+
	"  // wheeltravel = degree / 360.0 * WHEELDISTANCE * 3.14\n"+
	"  // numberofsteps = wheeltravel * REVOLUTION / WHEELCIRCUMFERENCE\n"+
	"  int steps; // holds number of required steps\n"+
	"  steps = degree / 360.0 * WHEELDISTANCE * 3.14 * REVOLUTION / WHEELCIRCUMFERENCE;\n"+
	"  for(int i = 0; i < steps; i++){\n"+
	"    motorL.step(1, BACKWARD, DOUBLE);\n"+
	"    motorR.step(1, FORWARD, DOUBLE);\n"+
	"  }\n"+
	"}\n\n"+

	"void turnRight(double degree) {\n"+
	"  // wheeltravel = degree / 360.0 * WHEELDISTANCE * 3.14\n"+
	"  // numberofsteps = wheeltravel * REVOLUTION / WHEELCIRCUMFERENCE\n"+
	"  int steps; // holds number of required steps\n"+
	"  steps = degree / 360.0 * WHEELDISTANCE * 3.14 * REVOLUTION / WHEELCIRCUMFERENCE;\n"+
	"  for(int i = 0; i < steps; i++){\n"+
	"    motorL.step(1, FORWARD, DOUBLE);\n"+
	"    motorR.step(1, BACKWARD, DOUBLE);\n"+
	"  }\n"+
	"}\n\n"+

	"void set_speed(int spd) {\n"+
	"  motorL.setSpeed(spd);\n"+
	"  motorR.setSpeed(spd);\n"+
	"}\n\n"+

	"void penUp() {\n"+
	"  pen.write(90);\n"+
	"}\n\n"+

	"void penDown() {\n"+
	"  pen.write(0);\n"+
	"}\n\n"+

	"void setup()\n"+
	"{\n"+
	"  motorR.setSpeed(500);\n"+
	"  motorL.setSpeed(500);\n"+
	"\n"+
	"  pen.attach(10); // Servo1 on AdaFruit motor control shield\n"+
	   
	"  penUp();\n"+
	"\n"+

	"  delay(3000);\n"+
	"}\n";	

	
	public ArduinoGenerator() {
		title = "untitled";
		description = "none";
		metric = "mm";
		viewBox = new SvgViewBox();
		width = 0;
		height = 0;
	}

	public ArduinoGenerator(String title, String description, String metric, int width, int height, SvgViewBox viewBox, List<String> paths) {
		this.title = title;
		this.description = description;
		this.metric = metric;
		this.width = width;
		this.height = height;
		this.viewBox = viewBox;
		this.paths = paths;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public void setViewBox(SvgViewBox viewBox) {
		this.viewBox = viewBox;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	public void generate() {
		int pathNum = 0;
		
		println("// Title: " + title);
		println("// Description: " + description);
		println("// Dimensions (width * height): " + width + " mm * " + height + " mm\n");
		println(initRobotStr);
		println("void loop()\n{");
		
		// Initialize robot's position
		robotPosX = viewBox.getMinX();
		robotPosY = viewBox.getMinY();
		robotOrientation = 0;
		robotDraws = false;
		println("  // Setting up robot's position: " + robotPosX + ", " + robotPosY + " (top-left corner of image to be drawn)");
		println("  // Initial angle is " + robotOrientation + " degree" + ((robotOrientation > 1)?"s":""));
		println("  // 0 degree points towards positive direction on X axis");
		println("  // Positive angles mean turning to the right\n");

		// Processing path elements
		for(String path : paths) {
			processPath(path, pathNum);
			pathNum++;
		}
		
		println("  penUp();");
		println("  forward(100);");
		println("  while(1) {}");
		println("}");
	}
	
	private void processPath(String path, int pathNum) {
		List<SvgPrimitive> primitives = new ArrayList<SvgPrimitive>();
		String[] tokens = path.split(" ");
		int numberOfCoordinate = 0;
		SvgPrimitive actualPrimitive = null;
		boolean closedPath = false;
		
		// Extracting primitives from "d" tag
		for(String token : tokens) {
			if(token.equals("M")) {
				actualPrimitive = new SvgPrimitive("M");
				numberOfCoordinate = 0;
			} else if(token.equals("L")) {
				actualPrimitive = new SvgPrimitive("L");
				numberOfCoordinate = 0;
			} else if(token.equals("z")) {
				closedPath = true;
			} else if(token.matches("-{0,1}[0-9]+\\.{0,1}[0-9]*")) {
				if(numberOfCoordinate == 0) {
					actualPrimitive.setX(Double.parseDouble(token));
					numberOfCoordinate++;
				} else if(numberOfCoordinate == 1) {
					actualPrimitive.setY(Double.parseDouble(token));
					primitives.add(actualPrimitive);
					numberOfCoordinate++;
				} else
					throw new IllegalArgumentException("More than 2 coordinates not allowed for a point in 2D plane! Extra coordinate: "+ token);
			}
		}
		
		// Processing primitives
		for(SvgPrimitive primitive : primitives)
			processPrimitive(primitive);
		
		// This looks like not needed, as initial coordinates are repeated in svg for closed paths
/*		if(closedPath && firstPrimitive != null) {
			firstPrimitive.setAction("L");
			processPrimitive(firstPrimitive);
		}*/		
	}
	
	private void processPrimitive(SvgPrimitive primitive) {
		double pathToTakeX, pathToTakeY, distanceToTake, newDirection, turningAngle, adjustedTurningAngle;
		boolean reverseDirection = false;
		
		if(primitive == null)
			return;
		
		println("  // " + primitive);

		// Deciding whether to draw or move
		if(primitive.getAction().equals("L")) {
			if(!robotDraws) {
				println("  // Drawing a line");
				println("  penDown();");
			}
			robotDraws = true;
		} else {
			// For M and unknown actions
			if(robotDraws) {
				println("  // Just moving to new position");
				println("  penUp();");
			}
			robotDraws = false;
		}
		
		pathToTakeX = primitive.getX()-robotPosX;
		pathToTakeY = primitive.getY()-robotPosY;
		distanceToTake = Math.sqrt(Math.pow(pathToTakeX, 2) + Math.pow(pathToTakeY, 2));
		
		// Skip too small movements to reduce number of instructions (makes picture a tiny bit rude)
		if(distanceToTake < 2)
			return;
		
		newDirection = Math.atan2(pathToTakeY, pathToTakeX) / Math.PI * 180;	// result is between -180 and 180
		
		turningAngle = newDirection - robotOrientation;	// valid range of robotOrientation is -180 - 180 -> possible values of turningAngle are between -360 and 360
		
		// Normalize turningAngle to range between -180 and 180
		if(turningAngle < -180)
			turningAngle += 360;
		else if(turningAngle > 180)
			turningAngle -= 360;
		
		if(0 < turningAngle && turningAngle <= 90) {
			println("  turnRight(" + turningAngle + ");");
			robotOrientation = newDirection;
		}

		if(-90 <= turningAngle && turningAngle < 0) {
			println("  turnLeft(" + Math.abs(turningAngle) + ");");
			robotOrientation = newDirection;
		}

		adjustedTurningAngle = turningAngle; // this is needed, as we use adjustedTurningAngle in println
		
		if(turningAngle > 90) {
			// Better to turn 180-degree to opposite direction, then go backward
			adjustedTurningAngle = 180 - turningAngle;	// valid range of adjustedTurningAngle is 0 - 90 (to the left)
			println("  turnLeft(" + adjustedTurningAngle + ");");
			reverseDirection = true;
			robotOrientation -= adjustedTurningAngle;
		}
		
		if(turningAngle < -90) {
			// Better to turn 180+degree to opposite direction, then go backward
			adjustedTurningAngle = 180 + turningAngle;
			println("  turnRight(" + adjustedTurningAngle + ");");
			reverseDirection = true;
			robotOrientation += adjustedTurningAngle;
		}
		println("  // newDirection: " + newDirection + ", newOrientation: " + robotOrientation + ", turningAngle: " + ((turningAngle > 90)?"-":"") + adjustedTurningAngle);
		println("  // pathToTakeX: " + pathToTakeX + ", pathToTakeY: " + pathToTakeY + ", distanceToTake: " + distanceToTake + (reverseDirection?" (reversing)":""));
		if(reverseDirection)
			println("  backward(" + distanceToTake + ");\n");
		else
			println("  forward(" + distanceToTake + ");\n");

		robotPosX = primitive.getX();	// TODO should be recalculated from turningAngle and distanceToTake
		robotPosY = primitive.getY();
	}
	
	private void println(Object obj) {
		System.out.println(obj);
	}
}
