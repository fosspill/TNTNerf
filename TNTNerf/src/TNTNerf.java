//Changelog:
//--1.5--
//Now warns the user first time after each login when they place a TNT block!
//--1.0--
//Working. Nothing left to do, tbh ^^ - I WAS WRONG!


import java.util.logging.Logger; 
public class TNTNerf extends Plugin { 

	private Logger log = Logger.getLogger("Minecraft"); 
	public static TNTNerf.TNTNerfListener listener; 
	public static String name = "TNTNerf";
	public static String version = "1.5"; 
	public static String author = "oleerik";
	//Props file!
	public static PropertiesFile props = new PropertiesFile("plugins/config/TNTNerf.properties");
	public static PropertiesFile firstplacewarning = new PropertiesFile("plugins/config/TNTNerf.firstplacewarning.db");
	//Setting for this plugin
	static int maxAltitude;	

	public TNTNerf(){ listener = new TNTNerf.TNTNerfListener(); }//initializing the listener

	public void disable(){ 
		log.info(name + " disabled."); 
	}

	public void enable(){ 
		log.info(name + " enabled.");
	}

	public void initialize(){ 
		etc.getLoader().addListener(PluginLoader.Hook.EXPLODE, listener, this, PluginListener.Priority.MEDIUM); 
		etc.getLoader().addListener(PluginLoader.Hook.BLOCK_PLACE, listener, this, PluginListener.Priority.MEDIUM); 
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_CONNECT, listener, this, PluginListener.Priority.MEDIUM);
		log.info(name + " v" + version + " by " + author + " initialized.");
		
		if (!props.containsKey("heightlimit")){
			props.setInt("heightlimit", 55);
		}
		maxAltitude = props.getInt("heightlimit");
	}
	

	private class TNTNerfListener extends PluginListener { 

		public boolean onExplode(Block block, OEntity entity, @SuppressWarnings("rawtypes") java.util.HashSet blocksaffected){
			if (block.getStatus() == 1 && block.getY() > maxAltitude){ //if the block was exploded by TNT AND is placed above maxAltitude.
				 return true; //Stop explosion
			} else {
				return false;
			}
			
		}

		public boolean onBlockPlace(Player player, Block blockPlaced, Block blockClicked, Item itemInHand){
			if(blockPlaced.getType() == 46 && blockPlaced.getY() > maxAltitude && !firstplacewarning.containsKey(player.getName())){
				firstplacewarning.setInt(player.getName(), 1);
				player.sendMessage(Colors.Red + "WARNING! TNT only work under layer " + maxAltitude);
			}
			return false;
			
		}
		public Object onPlayerConnect(Player player, HookParametersConnect hookParametersConnect){
			if (firstplacewarning.containsKey(player.getName())){
				firstplacewarning.removeKey(player.getName());
			}
			return hookParametersConnect;
			
		}

	}
}