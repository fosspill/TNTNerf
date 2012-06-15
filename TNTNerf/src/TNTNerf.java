//Changelog:
//--1.5--
//Now warns the user first time after each login when they place a TNT block!
//--1.0--
//Working. Nothing left to do, tbh ^^ - I WAS WRONG!


import java.util.logging.Logger; //imports let you use features outside of your libraries

public class TNTNerf extends Plugin { //The opening for the plugin. all code goes inside here

	private Logger log = Logger.getLogger("Minecraft"); //this allows you to send messages to the server console
	public static TNTNerf.TNTNerfListener listener; //this is a simple way of initializing your listener. all canary api code goes inside the listener
	public static String name = "TNTNerf"; //name of your plugin
	public static String version = "1.5"; //version number
	public static String author = "oleerik"; //your name
	//Props file!
	public static PropertiesFile props = new PropertiesFile("plugins/config/TNTNerf.properties");
	public static PropertiesFile firstplacewarning = new PropertiesFile("plugins/config/TNTNerf.firstplacewarning.db");
	//Setting for this plugin
	static int maxAltitude;	

	public TNTNerf(){ listener = new TNTNerf.TNTNerfListener(); }//initializing the listener

	public void disable(){ //these actions are called when the plugin is disabled
		log.info(name + " disabled."); //this will print in the console 'TNTNerf disabled'
	}

	public void enable(){ //these actions are called when the plugin is enabled
		log.info(name + " enabled.");
	}

	public void initialize(){ //these actions are called after the plugin is enabled
		etc.getLoader().addListener(PluginLoader.Hook.EXPLODE, listener, this, PluginListener.Priority.MEDIUM); //Calls the EXPLOSION hook
		etc.getLoader().addListener(PluginLoader.Hook.BLOCK_PLACE, listener, this, PluginListener.Priority.MEDIUM); //Calls the Block_place hook
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_CONNECT, listener, this, PluginListener.Priority.MEDIUM); //Calls the Block_place hook
		log.info(name + " v" + version + " by " + author + " initialized.");
		
		if (!props.containsKey("heightlimit")){
			props.setInt("heightlimit", 55);
		}
		maxAltitude = props.getInt("heightlimit");
	}
	

	private class TNTNerfListener extends PluginListener { //this is the listener, where it all happens. Put your canary methods code inside this

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