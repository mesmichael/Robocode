import robocode.*;
import robocode.Robot;
import robocode.Robot.*;
import robocode.TeamRobot.*;
import robocode.util.Utils;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
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
	 int[][] stats = new int[13][31];
	 private double startX, startY, startBearing, power;
	 private long   fireTime;
	 private int    direction;
	 private int[]  returnSegment;
	 List<sparta> waves = new ArrayList<sparta>();
	
	 
	 public sparta(double x, double y, double absBearing, double power2,
			int direction2, long time, int[] currentStats) {
		// TODO Auto-generated constructor stub
	}

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
	 
	 public void WaveBullet(double x, double y, double bearing, double power,
				int direction, long time, int[] segment)
		{
			startX         = x;
			startY         = y;
			startBearing   = bearing;
			this.power     = power;
			this.direction = direction;
			fireTime       = time;
			returnSegment  = segment;
		}
	 	
	 public double getBulletSpeed()
		{
			return 20 - power * 3;
		}
	 
	public double maxEscapeAngle()
		{
			return Math.asin(8 / getBulletSpeed());
		}
	 
	public boolean checkHit(double enemyX, double enemyY, long currentTime)
	{
		// if the distance from the wave origin to our enemy has passed
		// the distance the bullet would have traveled...
		if (Point2D.distance(startX, startY, enemyX, enemyY) <= 
				(currentTime - fireTime) * getBulletSpeed())
		{
			double desiredDirection = Math.atan2(enemyX - startX, enemyY - startY);
			double angleOffset = Utils.normalRelativeAngle(desiredDirection - startBearing);
			double guessFactor =
				Math.max(-1, Math.min(1, angleOffset / maxEscapeAngle())) * direction;
			int index = (int) Math.round((returnSegment.length - 1) /2 * (guessFactor + 1));
			returnSegment[index]++;
			return true;
		}
		return false;
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
		 double angel = (getBulletSpeed() * distance);
		 double speedangel = targetVelocity * getBulletSpeed();
		 heading = getHeading();
		 absoluteBearing = getHeadingRadians() + e.getBearingRadians();		 
		 enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
		 enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
		 double enemyHeadingChange = targetHeading - oldTargetHeading;
		 oldTargetHeading = targetHeading;
		 double absBearing = getHeadingRadians() + e.getBearingRadians();
		 
			// find our enemy's location:
			double ex = getX() + Math.sin(absBearing) * e.getDistance();
			double ey = getY() + Math.cos(absBearing) * e.getDistance();
	 
			// Let's process the waves now:
			for (int i=0; i < waves.size(); i++)
			{
				sparta currentWave = (sparta)waves.get(i);
				if (currentWave.checkHit(ex, ey, getTime()))
				{
					waves.remove(currentWave);
					i--;
				}
			}
	 
		
			if (e.getVelocity() != 0)
			{
				if (Math.sin(e.getHeadingRadians()-absBearing)*e.getVelocity() < 0)
					direction = -1;
				else
					direction = 1;
			}
			int[] currentStats = stats; // This seems silly, but I'm using it to
						    // show something else later
			sparta newWave = new sparta(getX(), getY(), absBearing, power,
	                        direction, getTime(), currentStats);
			int bestindex = 15;	
			for (int i=0; i<31; i++)
				if (currentStats[bestindex] < currentStats[i])
					bestindex = i;
	 
			
			double guessfactor = (double)(bestindex - (stats.length - 1) / 2)
	                        / ((stats.length - 1) / 2);
			double angleOffset = direction * guessfactor * newWave.maxEscapeAngle();
	                double gunAdjust = Utils.normalRelativeAngle(
	                        absBearing - getGunHeadingRadians() + angleOffset);
	                setTurnGunRightRadians(gunAdjust);
	                
	          
            
		
		
		
	}
	
	
	


	public void onHitWall(HitWallEvent e)
	{ 
		moveDirection = moveDirection * -1;
	}
	
	public void onHitRobot(HitRobotEvent e)
	{ 
		moveDirection = moveDirection * -1;
	}
	
	
	

}
	
	
	
	
