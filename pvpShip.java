package zackShip;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
	
	private Point midpoint;
	public Point stop1;
	public Point stop2;
	public int w;
	public int h;
	int state = 0;
	int num = 0;
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
			//return new RotateCommand(90);
			return new RadarCommand(4);
		}else if(state == 1){
			state = 2;
			//return new FireTorpedoCommand('F');
			
			RadarResults results = env.getRadar(); // This will have info, because we did a RadarCommand previously
			if(results == null){
				return new ThrustCommand( 'B', rng(2), 1); 
			}
			List<ObjectStatus> ships = results.getByType("Ship");
			List<ObjectStatus> bullet = results.getByType("Torpedo");
			if (ships.size() < 1 && bullet.size() < 1){
				return new ThrustCommand( 'B', rng(2), 1); 
			} if(bullet.size() > 0 && bullet.get(0).getOrientation() != ship.getOrientation()){
				return new RaiseShieldsCommand(.1);
			}
	
			ObjectStatus target = ships.get(0);
			return new RotateCommand(ship.getPosition().getAngleTo(target.getPosition()) - ship.getOrientation());
		}else if(state == 2){
			state = 0;
			
			return new FireTorpedoCommand('F');
		}
		return new IdleCommand(10);
		//return new RotateCommand(ship.getPosition().getAngleTo(this.midpoint) - ship.getOrientation());
		
		//johncespinosa@gmail.com
		
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
	
}