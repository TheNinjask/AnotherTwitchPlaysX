package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import externalconsole.console.ExternalConsole;
import externalconsole.console.ExternalConsoleCommand;
import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.data.CommandDataOnExecute;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.mainMenu.GameButtonClickEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;

@ATPXModProps(hasPanel = false)
public class DemocracyMod extends ATPXMod {

	private static boolean isOn;

	private static boolean isActive;

	private static Thread clock;

	private static int everySec;

	private static Map<CommandData, Bundle> tally;

	public class Bundle {

		private CommandData cmd;

		private List<Map<String, String>> mappings;

		private int amount;

		public static final Comparator<Bundle> comparator = new Comparator<Bundle>() {
			@Override
			public int compare(Bundle o1, Bundle o2) {
				return -Integer.compare(o1.amount, o2.amount);
			}
		};

		public Bundle(CommandData cmd) {
			this(cmd, null);
		}

		public Bundle(CommandData cmd, Map<String, String> map) {
			this.cmd = cmd;
			this.amount = 1;
			this.mappings = new ArrayList<Map<String, String>>();
			this.mappings.add(map);
		}

		public void inc() {
			addMappingAndInc(null);
		}

		public void addMappingAndInc(Map<String, String> map) {
			amount++;
			mappings.add(map);
		}

		public CommandData getCmd() {
			return cmd;
		}

		public int getAmount() {
			return amount;
		}

		public List<Map<String, String>> getAllMappings() {
			return mappings;
		}
	}

	private static ExternalConsoleCommand consoleCmd = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "democracy";
		}

		@Override
		public String getDescription() {
			return "Enables/Disables Democracy";
		}

		@Override
		public int executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup dem = new OptionGroup();
			// print.setRequired(true);
			dem.addOption(new Option("t", "true", false, "Enables Democracy"));
			dem.addOption(new Option("f", "false", false, "Disables Democracy"));
			options.addOptionGroup(dem);
			options.addOption("p", "pollSeconds", false, "Polling time in seconds");
			try {
				CommandLineParser parser = new DefaultParser();
				CommandLine cmd = parser.parse(options, args);
				
				if(cmd.hasOption('p')) {
					if(cmd.getOptionValue('p')==null)
						ExternalConsole.println(String.format("Polling time: %s seconds", everySec));
					else
						everySec = Integer.parseInt(cmd.getOptionValue('p'));
				}
				
				switch (String.valueOf(dem.getSelected())) {
				case "t":
					isOn = true;
					if (isActive && !clock.isAlive()) {
						clock = makeClock();
						clock.start();
					}
					break;
				case "f":
					isOn = false;
					break;
				default:
					if (!cmd.hasOption('p')) {
						ExternalConsole.println(String.format("Democracy is set as: %s", isOn));
						ExternalConsole.println("Options: -t, -f, -p [secs]");
					}
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
		}
	};

	public DemocracyMod() {
		isActive = false;
		isOn = false;
		everySec = 20;
		tally = new HashMap<CommandData, Bundle>();
		ExternalConsole.addCommand(consoleCmd);
		EventManager.registerEventListener(this);
	}

	private static Thread makeClock() {
		return new Thread(() -> {
			while (isActive && isOn) {
				synchronized (tally) {
					try {
						Thread.sleep(everySec * 1000);
					} catch (InterruptedException e) {
					}
					if (!isActive || !isOn)
						return;
					List<Bundle> sorted = tally.values().parallelStream().sorted(Bundle.comparator).toList();
					if (!sorted.isEmpty()) {
						Bundle cmd = sorted.get(0);
						if (cmd.getAllMappings().isEmpty())
							cmd.getCmd().execute();
						else{
							Map<String, String> chosen = cmd.getAllMappings()
									.get(ThreadLocalRandom.current().nextInt(cmd.getAllMappings().size()));
							if(chosen!=null)
								cmd.getCmd().execute(chosen);
							else
								cmd.getCmd().execute();
						}
					}
					tally.clear();
				}
			}
		});
	}

	@Handler
	public void onGameButton(GameButtonClickEvent event) {
		if (event.getActive()) {
			isActive = false;
		} else {
			isActive = true;
			if (isOn && !clock.isAlive()) {
				clock = makeClock();
				clock.start();
			}
		}
	}

	@Handler
	public void onCommandDataExecution(CommandDataOnExecute event) {
		if (isOn && isActive) {
			synchronized (tally) {
				if(event.isCancelled())
					return;
				event.setCancelled(true);
				Bundle bundle = tally.get(event.getCommandData());
				if (bundle == null)
					bundle = new Bundle(event.getCommandData(), event.getMapping());
				else
					bundle.addMappingAndInc(event.getMapping());
				tally.put(event.getCommandData(), bundle);
			}
		}
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public JPanel getJPanelInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}
