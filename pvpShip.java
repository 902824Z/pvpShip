package zackShip;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.ranges.RangeException;
import java.lang.*;
import java.awt.Color;
import ihs.apcs.spacebattle.BasicEnvironment;
import ihs.apcs.spacebattle.BasicSpaceship;
import ihs.apcs.spacebattle.ObjectStatus;
import ihs.apcs.spacebattle.Point;
import ihs.apcs.spacebattle.RadarResults;
import ihs.apcs.spacebattle.RegistrationData;
import ihs.apcs.spacebattle.commands.BrakeCommand;
import ihs.apcs.spacebattle.commands.FireTorpedoCommand;
import ihs.apcs.spacebattle.commands.IdleCommand;
import ihs.apcs.spacebattle.commands.RadarCommand;
import ihs.apcs.spacebattle.commands.RaiseShieldsCommand;
import ihs.apcs.spacebattle.commands.RotateCommand;
import ihs.apcs.spacebattle.commands.ShipCommand;
import ihs.apcs.spacebattle.commands.ThrustCommand;
import ihs.apcs.spacebattle.commands.ScanCommand;
public class pvpShip extends BasicSpaceship {
	boolean debug = true;
	private Point midpoint;
	public Point stop1;
	public Point stop2;
	public int w;
	public int h;
	int state = 0;
	int num = 0;
	boolean error = false;
	public Point tar;
	Point NaNError = new Point (Math.sqrt(-1), Math.sqrt(-1));
	double xNaN;
	@Override
	public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight)
	{
		w = worldWidth;
		h = worldHeight;
		midpoint = new Point (worldWidth/2, worldHeight/2);
		return new RegistrationData(randomName(), new Color(rng(255), rng(255), rng(255)), rng(15));
	}

	@Override
	public ShipCommand getNextCommand(BasicEnvironment env)
	{
		
		ObjectStatus ship = env.getShipStatus();
		if(state == 0){
			state =1;

			return new RadarCommand(5);
		}else if(state == 1){
			state = 2;
			//return new FireTorpedoCommand('F');
			
			RadarResults results = env.getRadar(); // This will have info, because we did a RadarCommand previously
			try{
				List<ObjectStatus> ships = results.getByType("ship");
				ObjectStatus target = ships.get(0);
			}catch(Exception e){
				error = true;
				return new IdleCommand(0.1);
			}finally{
				
			}
			error = false;
			List<ObjectStatus> ships = results.getByType("ship");
			ObjectStatus target = ships.get(0);
			if(debug){

				System.out.println(target + " target");
				System.out.println(target.getPosition() + "Position of target");
				System.out.println(ship.getPosition() + " My position");
				System.out.println(target.getOrientation() + " Orientation of the target");
				System.out.println(target.getSpeed() + " speed of the target");
				System.out.println(ship.getPosition().getAngleTo(target.getPosition()) + "  Hopefully the agnle of the other ship");
				System.out.println(target.getName() + " name");
				System.out.println(distance(target.getPosition(), ship));
				System.out.println(shoot(target.getPosition(), ship) + "  idk something lol" );
				//return new IdleCommand(10);

			}
			double speedOfTar = target.getSpeed();
			double orientationOfTar = target.getOrientation();
			double tX = target.getPosition().getX();
			double tY = target.getPosition().getY();
			//Point newPostionOfTar = new Point(tX + speedOfTar, tY + speedOfTar);
			Point newPostionOfTar =shoot(target.getPosition(), ship);
			tar = newPostionOfTar; //this is to check if it is NaN
			
			//return new RotateCommand(ship.getPosition().getAngleTo(newPostionOfTar) - ship.getOrientation());
			
			return new RotateCommand(ship.getPosition().getAngleTo(target.getPosition()) - ship.getOrientation());
			
			
		}else if(state == 2){
			
			state = 0;
			//return new IdleCommand(5);
			//return new FireTorpedoCommand('F');
		}
		xNaN = tar.getX();
		if(error){
			return new IdleCommand(.1);
		}else{
			System.out.println(tar.getX());
			if(Double.isNaN(xNaN)){
				return new IdleCommand(.1);
			}else{
				return new FireTorpedoCommand('F');
			}
		}
		//return new IdleCommand(10);
		//return new RotateCommand(ship.getPosition().getAngleTo(this.midpoint) - ship.getOrientation());
		

	}
	//Could be Ship, Planet, BlackHole, Star, Nebula, Asteroid, Torpedo, Bauble, Bubble, or Outpost.
	public static String randomName(){
		int ran = rng(15);
		if (ran == 1){
			return "Zach";
		} else if (ran == 2){
			return "Zack";
		} else if (ran == 3){
			return "Back";
		} else if (ran == 4){
			return "Zeus";
		} else if (ran == 5){
			return "ISS Wolf";
		} else if (ran == 6){
			return "USS Thunderbird";
		} else if (ran == 7){
			return "BC Battlestar";
		} else if (ran == 8){
			return"BC Dragontooth";
		} else if (ran == 9){
			return "SC Meteor";
		} else if(ran == 9){
			return "Voyager 1";
		} else if(ran == 10){
			return "Detector";
		}else if(ran == 11){
			return "Hummingbird";
		}else if(ran == 12){
			return "HMS The Exterminator";
		}else if(ran == 13){
			return "Traveler";
		}else if(ran == 13){
			return "SSE Galactica";
		}else if(ran == 14){
			return "HMS Spitfire";
		}
		return "USS Challenger";
	}
	public static int rng(int num){//method for random number just bc
		int random = (int )(Math.random() * num + 1);
		return(random);
	}
	//returns the distance from you to the target :)
	//i guess i did not need to add this because there is a getDistanceTo() method but oh well i did
	public static double distance(Point pointOfTarget, ObjectStatus ship){
		Point pointOfShip = ship.getPosition();
		return Math.sqrt((pointOfTarget.getX() - pointOfShip.getX()) * (pointOfTarget.getX() - pointOfShip.getX()) + (pointOfTarget.getY() - pointOfShip.getY()) * (pointOfTarget.getY() - pointOfShip.getY()));
	}
	
	public Point shoot(Point target, ObjectStatus ship)
	{
		Point pointOfShip = ship.getPosition();
		int speedBullet = 250;
		double dx = target.getX() - pointOfShip.getX();
		double dy = target.getY() - pointOfShip.getY();
		
		double a = target.getX() * target.getX() + target.getY() * target.getY() - speedBullet * speedBullet;
		System.out.println(a + "  a");
		double b = 2 * (target.getX() * dx + target.getY() * dy);
		System.out.println(b + "  b");
		double c = dx * dx + dy * dy;
		System.out.println(c + "  c");
		
		// Check we're not breaking into complex numbers
		double q = b * b - 4 * a * c;
		System.out.println(q + " Q");
		
		// The time that we will hit the target
		//double t = ((a < 0 ? -1 : 1)*Math.sqrt(q) - b) / (2 * a);
		double t = ((a < 0 ? -1 : 1)*distance(target, ship));
		//System.out.println(t);
		// Aim for where the target will be after time t
		dx += t * target.getX();
		dy += t * target.getY();
		
		Double theta = Math.atan2(dy, dx);
		
		Point hitPoint = new Point(target.getX() + target.getX() * t, target.getY() + target.getY() * t);
		
		//double bulletX = speedBullet * Math.cos(theta);
		//double bulletY = speedBullet * Math.sin(theta);
		//Point hit = new Point (Math.abs(bulletX), Math.abs(bulletY));
		return hitPoint;
	}
}