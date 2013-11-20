import robocode.*;
import robocode.Robot.*;
import robocode.TeamRobot.*;
import robocode.util.Utils;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.*;
import java.awt.*;


public class sparta extends TeamRobot{
	
	 
	 public int moveDirection = 1;
	 public double targetX = 0;
	 public double targetY = 0;
	 public double targetDistance = -1;
	 public double targetBearing = -200;
	 public double targetHeading;
	 public double targetVelocity;
	 public double targetEnergy;
	 public double bulletPower;
	 public double angleThreshold;
	 public double distance;
	 public double fieldWidth = 0;
	 public double fieldHeight = 0;
	 public double myX; 
	 public double myY;
	 public double Ebearing = 0;
	 public double buletpower = 0;
	 
	public void EnemyWave() {}
	
	 
	 public void run() {
		 //all parts (gun, body, radar) move indepentent from one each other
		 setAdjustRadarForRobotTurn(true);
		 setAdjustGunForRobotTurn(true);
		 setAdjustRadarForGunTurn(true);
		 /**
		  * this is the infinite loop for our while event -M.S.
		  */
		 turnRadarRightRadians(Double.POSITIVE_INFINITY);
		
		 
		 while(true)
		 {
			 scan();
			 move();
		
		 }
	
	 }
	 
	 
		

	private void move() {
		// TODO Auto-generated method stub
		ahead(Double.POSITIVE_INFINITY * moveDirection);
	}
	
	
	void doGun() {
	    long TheTime;
	    long secondTime;
	    Point2D.Double Point;
	    // creates a point so we can find the time differance
	    Point = new Point2D.Double(target.x, target.y);
	    //when i is less than 10 do
	    for (int i = 0; i < 10; i++){
	        nextTime = (intMath.round((getRange(getX(),getY(),Point.x,Point.y)/(20-(3*firePower))));
	        theTime = getTime() + secondTime;
	        Point = target.guessPosition(time);
	    }
	   //calculates gun difrence
	    double gunOffset = getGunHeadingRadians() - 
	                  (Math.PI/2 - Math.atan2(Point.y - getY(), Point.x - getX()));
	    setTurnGunLeftRadians(normaliseBearing(gunOffset));
	}

	double normaliseBearing(double ang) {
	    if (ang > Math.PI)
	        ang -= 2*PI;
	    if (ang < -Math.PI)
	        ang += 2*Math.PI;
	    return ang;
	}

	public double getrange(double x1,double y1, double x2,double y2) {
	    double x = x2-x1;
	    double y = y2-y1;
	    double h = Math.sqrt( x*x + y*y );
	    return h;	
	}
	
	
	public void onScannedRobot(ScannedRobotEvent e) {
		 fieldWidth = getBattleFieldWidth();
		 fieldHeight = getBattleFieldHeight();
		 Ebearing = e.getBearing();
		 myX = getX();
		 myY = getY();
		 double bPower = 1;
		 targetHeading = e.getHeading();
		 targetVelocity = e.getVelocity();
		 distance = e.getDistance();
		 bulletPower = bPower;
		 double bullet = 20-3*bulletPower;
		 double angel;
		 double speedangel;
		
		 angel= (bullet * distance); 
		 speedangel = targetVelocity * bullet;
		 
		
		
		
		if (isTeammate(e.getName()) == false) {
		doGun();
		 //turns so that alwas sidewase if enemy
		setTurnRight(e.getBearing() + 90);
			//calculates the power of the robots shot
			 if(distance > 350)
			 {
				 bPower = 0.5; 
			 }
			 if(distance > 300)
			 {
				 bPower = 1; 
			 }
			 if(distance > 250)
			 {
				 bPower = 2; 
			 }
			 else
			 {
				 bPower = 3; 
			 }
			
			 fireBullet(bPower);
				buletpower = bPower;
				targetEnergy = e.getEnergy();
		}
	}
	/**public void onScannedRobot(ScannedRobotEvent e) {
		 
		
		 
	
			
		 
		 // creates a lock on a robot
		 double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
		 setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
		 
		 
		 //perdicts acording to power lvl
		 double headOnBearing = getHeadingRadians() + e.getBearingRadians();
		 double linearBearing = headOnBearing + Math.asin(e.getVelocity() / Rules.getBulletSpeed(bPower) * Math.sin(e.getHeadingRadians() - headOnBearing));
		 setTurnGunRightRadians(Utils.normalRelativeAngle(linearBearing - getGunHeadingRadians()));
		 setFire(bPower);
	
		}
	}**/
	
	


	public void onHitWall(HitWallEvent e)
	{ 
		moveDirection = moveDirection * -1;
	}
	
	public void onHitRobot(HitRobotEvent e)
	{ 
		moveDirection = moveDirection * -1;
	}
	
	
	

}
	
	
	
	
