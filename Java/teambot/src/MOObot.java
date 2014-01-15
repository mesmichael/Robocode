import robocode.*;
import robocode.Robot;
import robocode.Robot.*;
import robocode.TeamRobot.*;
import robocode.util.Utils;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.awt.*;
public class MOObot extends TeamRobot {
		//sets basic bullet power
		private static final double Bpower = 1.9;
		//direction
		private static double lDirection;
		//last enemy volocity
		private static double lastEnemyVelocity;
		private static SUCHCOOLMOVES movement;
	 
		public MOObot() {
			movement = new SUCHCOOLMOVES(this);	
		}
	 //run methoid - dont judge my spelling! 
		public void run() {
			//sets collor to all black B)
			setColors(Color.BLACK, Color.BLACK, Color.BLACK);
			//sets directon to 1
			lDirection = 1;
			// inisualises the verible
			lastEnemyVelocity = 0;
			//sets the radar and gun turn independent of the body
			setAdjustRadarForGunTurn(true);
			setAdjustGunForRobotTurn(true);
			//a post test loop thats infanat to turn the radar till something is found
			do {
				turnRadarRightRadians(Double.POSITIVE_INFINITY); 
			} while (true);
		}
	 //the first ONSCANNED event for the first class, yes there are 2 classes for this one robot that use this
		public void onScannedRobot(ScannedRobotEvent e) {
			//trys to get the name for our team :/ not working....
			if (e.getName() == "sparta*" && e.getName() == "MOObot*"){
			}
			else{
				//begins couclulastion for are robot so that he can shoot and kill ZAC.
				double enemyAbsoluteBearing = getHeadingRadians() + e.getBearingRadians();
				// we need the distance from his robot to ours dont we?
				double enemyDistance = e.getDistance();
				//and his Velocity to!!!
				double enemyVelocity = e.getVelocity();
				//if hes not still then we will do a wave stuff ya :D
				if (enemyVelocity != 0) {
					//some math that helps us find are direction
					lDirection = MOOUtils.sign(enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing));
				}
				MOOwave wave = new MOOwave(this);
				// a point that helps us track our x and y
				wave.gunLocation = new Point2D.Double(getX(), getY());
				// this one helps with the targets location
				MOOwave.targetLocation = MOOUtils.project(wave.gunLocation, enemyAbsoluteBearing, enemyDistance);
				// part of our wave
				wave.lDirection = lDirection;
				// adds our bpower form the begining in so we can add that to our planz!
				wave.bulletPower = Bpower;
				wave.setSegmentations(enemyDistance, enemyVelocity, lastEnemyVelocity);
				//got to update that Velocity
				lastEnemyVelocity = enemyVelocity;
				// and dont for get the apsolute bearing we made
				wave.bearing = enemyAbsoluteBearing;
				//alines the SUPER AWSOME TARGETING SYSTEM FOR MASS DESTRUCTION OF YOUR FACE YOU AMERICAN PIG!!! HAIL MOTHER RUSSIA... sry about that XD
				setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians() + wave.mostVisitedBearingOffset()));
				//fires derp :3
				setFire(wave.bulletPower);
				//checks for our current energy over the Bpower
				if (getEnergy() >= Bpower) {
					addCustomEvent(wave);
				}
				//adds that other wave but for movement i was telling you about :D
				movement.onScannedRobot(e);
				setTurnRadarRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getRadarHeadingRadians()) * 2);
			}
		}
	}
	 
	class MOOwave extends Condition {
		static Point2D targetLocation;
	 
		double bulletPower;
		Point2D gunLocation;
		double bearing;
		double lDirection;
	 
		private static final double mostdistance = 1000;
		private static final int distanceList = 5;
		private static final int lovList = 5;
		private static final int bins = 25;
		private static final int middleBin = (bins - 1) / 2;
		private static final double maxEscapeAngles = 0.7;
		private static final double binWidth = maxEscapeAngles / (double)middleBin;
	 
		private static int[][][][] statBuffers = new int[distanceList][lovList][lovList][bins];
	 
		private int[] buffer;
		private AdvancedRobot robot;
		private double distanceTraveled;
	 
		MOOwave(AdvancedRobot _robot) {
			this.robot = _robot;
		}
	 
		public boolean test() {
			advance();
			if (hasArrived()) {
				buffer[currentBin()]++;
				robot.removeCustomEvent(this);
			}
			return false;
		}
	 
		double mostVisitedBearingOffset() {
			return (lDirection * binWidth) * (mostVisitedBin() - middleBin);
		}
	 
		void setSegmentations(double distance, double velocity, double lastVelocity) {
			int distanceIndex = (int)(distance / (mostdistance / distanceList));
			int velocityIndex = (int)Math.abs(velocity / 2);
			int lastVelocityIndex = (int)Math.abs(lastVelocity / 2);
			buffer = statBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
		}
	 // tracks are distance traveled
		private void advance() {
			distanceTraveled += MOOUtils.bulletVelocity(bulletPower);
		}
	 // tryes to see when we arived at a point we want to be at
		private boolean hasArrived() {
			return distanceTraveled > gunLocation.distance(targetLocation) - 18;
		}
	 
		private int currentBin() {
			int bin = (int)Math.round(((Utils.normalRelativeAngle(MOOUtils.absoluteBearing(gunLocation, targetLocation) - bearing)) /
					(lDirection * binWidth)) + middleBin);
			return MOOUtils.minMax(bin, 0, bins - 1);
		}
	 
		private int mostVisitedBin() {
			int mostVisited = middleBin;
			for (int i = 0; i < bins; i++) {
				if (buffer[i] > buffer[mostVisited]) {
					mostVisited = i;
				}
			}
			return mostVisited;
		}	
	}
	 //i got lazy with comments about 9pm 1/14/2014 after break XD
	class MOOUtils {
		//bullet velocity!
		static double bulletVelocity(double power) {
			return 20 - 3 * power;
		}
	 // this is for the angles and lenght we will be useing in our next part ;) everything is part of my master plan to DESTROY THE INFEDELS!... what? i sayed nothing!
		static Point2D project(Point2D sourceLocation, double angle, double length) {
			return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
					sourceLocation.getY() + Math.cos(angle) * length);
		}
	 //a more advanced apsolutebearing thats just a bit redndent but when i was resurching for stuff it worked over what i was useing so why not!
		static double absoluteBearing(Point2D source, Point2D target) {
			return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
		}
		
	 
		static int sign(double v) {
			return v < 0 ? -1 : 1;
		}
	 
		static int minMax(int v, int min, int max) {
			return Math.max(min, Math.min(max, v));
		}
	}
	 
	class SUCHCOOLMOVES {
		//gets me the FIELD OF BATTLES WIDTH
		private static final double battlefieldW = 800;
		//gets me the FIELD OF BATTLES WIDTH FOR MY HONOR WE SHALL FIGHT
		private static final double battlefieldH = 600;
		//dont hit the wall of DEATH
		private static final double wallMarg = 18;
		private static final double maxT = 125;
		private static final double revT = 0.421075;
		private static final double defalt = 1.2;
		private static final double wallB = 0.699484;
	
		private AdvancedRobot robot;
		//time to take it up a notch ;) MAKE THAT 2D SHAPE SO WE DONT SMASH ARE STUFF INTO WALLS BRO.
		private Rectangle2D fieldRectangle = new Rectangle2D.Double(wallMarg, wallMarg,
			battlefieldW - wallMarg * 2, battlefieldH - wallMarg * 2);
		//ENEMEY111111
		private double enemyFirePower = 3;
		//FEAR MY PLAN MR.ROETH
		private double direction = 0.4;
	 //CALLS ANOTHER CLASS. thanks for the maltiple class idea 
		SUCHCOOLMOVES(AdvancedRobot _robot) {
			this.robot = _robot;
		}
	 //that other on scanned robot! that one! yes! here it be! omg! whoa! such amazing! so shiny!
		public void onScannedRobot(ScannedRobotEvent e) {
			//again with this ik ik apsolutebearing XD
			double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
			//and distance ikr!
			double enemyDistance = e.getDistance();
			//spot bro.. a spot
			Point2D robotLocation = new Point2D.Double(robot.getX(), robot.getY());
			Point2D enemyLocation = MOOUtils.project(robotLocation, enemyAbsoluteBearing, enemyDistance);
			Point2D robotDestination;
			double tries = 0;
			while (!fieldRectangle.contains(robotDestination = MOOUtils.project(enemyLocation, enemyAbsoluteBearing + Math.PI + direction,
					enemyDistance * (defalt - tries / 100.0))) && tries < maxT) {
				tries++;
			}
			if ((Math.random() < (MOOUtils.bulletVelocity(enemyFirePower) / revT) / enemyDistance ||
					tries > (enemyDistance / MOOUtils.bulletVelocity(enemyFirePower) / wallB))) {
				direction = -direction;
			}
			
			double angle = MOOUtils.absoluteBearing(robotLocation, robotDestination) - robot.getHeadingRadians();
			//turns are robot
			robot.setAhead(Math.cos(angle) * 100);
			//OMG YOUR ALIVE, YOUR READ MY CODE AND LIVE TO TELL THE TAIL!
			robot.setTurnRightRadians(Math.tan(angle));
		}
	}