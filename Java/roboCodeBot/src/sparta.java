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
	 public double heading = 0;
	 
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
	
	
	public Point2D.Double guessPosition(long when) {
	    /**time is when our scan data was produced.  when is the time 
	    that we think the bullet will reach the target.  diff is the 
	    difference between the two **/
	    double diff = when - time;
	    double newX, newY;
	    /**if there is a significant change in heading, use circular 
	    path prediction**/
	    if (Math.abs(changehead) > 0.00001) {
	        double radius = targetVelocity/changehead;
	        double tothead = diff * changehead;
	        newY = myY + (Math.sin(heading + tothead) * radius) - 
	                      (Math.sin(heading) * radius);
	        newX = myX + (Math.cos(heading) * radius) - 
	                      (Math.cos(heading + tothead) * radius);
	    }
	    /**if the change in heading is insignificant, use linear 
	    path prediction**/
	    else {
	        newY = myY + Math.cos(heading) * targetVelocity * diff;
	        newX = myX + Math.sin(heading) * targetVelocity * diff;
	    }
	    return new Point2D.Double(newX, newY);
	}

	
	void doGun() {
	    long time;
	    long nextTime;
	    Point2D.Double p;
	    p = new Point2D.Double(e.x, e.y);
	    for (int i = 0; i < 10; i++){
	        nextTime = (IntMath.round((getrange(getX(),getY(),p.x,p.y)/(20-(3*bulletPower)))));
	        time = getTime() + nextTime;
	        p = target.guessPosition(time);
	    }
	    /**Turn the gun to the correct angle**/
	    double gunOffset = getGunHeadingRadians() - 
	                  (Math.PI/2 - Math.atan2(p.y - getY(), p.x - getX()));
	    setTurnGunLeftRadians(normaliseBearing(gunOffset));
	}

	double normaliseBearing(double ang) {
	    if (ang > Math.PI)
	        ang -= 2*Math.PI;
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
		 heading = getHeading();
		
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
	
	
	
	
