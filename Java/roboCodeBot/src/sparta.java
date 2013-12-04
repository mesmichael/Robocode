import robocode.*;
import robocode.Robot;
import robocode.Robot.*;
import robocode.TeamRobot.*;
import robocode.util.Utils;
import java.awt.geom.*;
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
	 double absoluteBearing = 0;
	 double enemyY = 0;
	 double enemyX = 0;
	 
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
		 targetHeading = e.getHeadingRadians();
		 double oldTargetHeading = 0;
		 targetVelocity = e.getVelocity();
		 distance = e.getDistance();
		 bulletPower = bPower;
		 double bullet = 20-3*bulletPower;
		 double angel = (bullet * distance);
		 double speedangel = targetVelocity * bullet;
		 heading = getHeading();
		 absoluteBearing = getHeadingRadians() + e.getBearingRadians();		 
		 enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
		 enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
		 double enemyHeadingChange = targetHeading - oldTargetHeading;
		 oldTargetHeading = targetHeading;
		 double deltaTime = 0;
		 double predictedX = enemyX, predictedY = enemyY;
	
		 
		if (isTeammate(e.getName()) == false) {
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
			
			 // creates a lock on a robot
			 double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
			 setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
			 
			 
			 //perdicts acording to power lvl
			 double headOnBearing = getHeadingRadians() + e.getBearingRadians();
			 double linearBearing = headOnBearing + Math.asin(e.getVelocity() / Rules.getBulletSpeed(bPower) * Math.sin(e.getHeadingRadians() - headOnBearing));
			 setTurnGunRightRadians(Utils.normalRelativeAngle(linearBearing - getGunHeadingRadians()));
			 setFire(bPower);
		
			 
			 
			 //fires bPower
			 fireBullet(bPower);
			 // updates bullet power
				buletpower = bPower;
			 // updates targets energy
				targetEnergy = e.getEnergy();
		}
	}
	/**public void onScannedRobot(ScannedRobotEvent e) {
		 
		 while((++deltaTime) * (20.0 - 3.0 * bulletPower) < Point2D.Double.distance(myX, myY, predictedX, predictedY))
			 {
				 predictedX += Math.sin(targetHeading) * targetVelocity;
				 predictedY += Math.cos(targetHeading) * targetVelocity;
				 targetHeading += enemyHeadingChange;
				 if(predictedX < 18.0 || predictedY < 18.0|| predictedX > fieldWidth - 18.0 || predictedY > fieldHeight - 18.0)
				 {
					 
					 predictedX = Math.min(Math.max(18.0, predictedX), fieldWidth - 18.0);	
					 predictedY = Math.min(Math.max(18.0, predictedY), fieldHeight - 18.0);
					 break; 
					 
				 }
				 double theta = Utils.normalAbsoluteAngle(Math.atan2(predictedX - getX(), predictedY - getY()));
				 setTurnRadarRightRadians(Utils.normalRelativeAngle(
						    absoluteBearing - getRadarHeadingRadians()));
						setTurnGunRightRadians(Utils.normalRelativeAngle(
						    theta - getGunHeadingRadians()));
						
			 }
		 
	
			
		 
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
	
	
	
	
