package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import com.google.api.services.youtube.model.LiveChatMessage;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.mainMenu.GameButtonClickEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.TaskCooldown;

@ATPXModProps(keepLoaded = false, popout = true)
public class StringChatCommandsMod extends ATPXMod {

	private JPanel mainPanel;

	private List<SimpleCmd> cmds;

	private JButton action;

	private JTextField cmd;

	private JTextArea resp;

	private JComboBox<SimpleCmd> options;

	public StringChatCommandsMod() {
		cmds = new ArrayList<SimpleCmd>();

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Constants.TWITCH_COLOR);

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setOpaque(false);
		JLabel title = new JLabel("Set Cmds for Chat");
		title.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		titlePanel.add(title);
		mainPanel.add(titlePanel, BorderLayout.NORTH);

		JPanel buttonMenu = new JPanel(new FlowLayout());
		buttonMenu.setOpaque(false);
		action = new JButton("Add");
		action.addActionListener(l -> {
			switch (options.getSelectedIndex()) {
			case -1:
				break;
			case 0:
				if (cmd.getText().isBlank() || resp.getText().isBlank()) {
					Constants.showMessageDialog("Please insert the command and/or text", "Warning on adding");
					break;
				}
				SimpleCmd newCmd = new SimpleCmd(cmd.getText(), resp.getText());
				cmds.add(newCmd);
				options.addItem(newCmd);
				options.setSelectedItem(newCmd);
				cmd.setEditable(false);
				resp.setEditable(false);
				action.setText("Remove");
				EventManager.registerEventListener(newCmd);
				break;
			default:
				SimpleCmd toDelete = options.getItemAt(options.getSelectedIndex());
				if (toDelete == null)
					break;
				cmds.remove(toDelete);
				options.removeItemAt(options.getSelectedIndex());
				options.setSelectedIndex(0);
				cmd.setText("");
				cmd.setEditable(true);
				resp.setText("");
				resp.setEditable(true);
				action.setText("Add");
				EventManager.unregisterEventListener(toDelete);
				break;
			}
		});
		buttonMenu.add(action);
		JButton removeAll = new JButton("Remove All");
		removeAll.addActionListener(l -> {

			cmds.forEach(cmd -> {
				EventManager.unregisterEventListener(cmd);
			});

			cmds.clear();
			options.removeAllItems();

			options.insertItemAt(null, 0);
			options.setSelectedIndex(0);
			cmd.setText("");
			cmd.setEditable(true);
			resp.setText("");
			resp.setEditable(true);
			action.setText("Add");
			action.setText("Add");
		});
		buttonMenu.add(removeAll);
		mainPanel.add(buttonMenu, BorderLayout.SOUTH);

		JPanel modContent = new JPanel(new GridLayout(3, 2));
		modContent.setOpaque(false);

		JLabel selectLabel = new JLabel("Select:");
		selectLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		modContent.add(selectLabel);
		options = new JComboBox<SimpleCmd>();
		options.addItem(new SimpleCmd(null, null));
		options.setSelectedIndex(0);

		options.addActionListener(l -> {
			switch (options.getSelectedIndex()) {
			case -1:
				break;
			case 0:
				cmd.setText("");
				cmd.setEditable(true);
				resp.setText("");
				resp.setEditable(true);
				action.setText("Add");
				break;
			default:
				SimpleCmd selectedCmd = options.getItemAt(options.getSelectedIndex());
				if (selectedCmd == null)
					break;
				cmd.setText(selectedCmd.getCmd());
				cmd.setEditable(false);
				resp.setText(selectedCmd.getResponse());
				resp.setEditable(false);
				action.setText("Remove");
				break;
			}
		});

		modContent.add(options);

		JLabel cmdLabel = new JLabel("Cmd:");
		cmdLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		modContent.add(cmdLabel);
		FlowLayout tmp = new FlowLayout(FlowLayout.LEADING);
		tmp.setHgap(0);
		JPanel cmdPanel = new JPanel(tmp);
		cmdPanel.setOpaque(false);
		cmd = new JTextField();
		cmd.setPreferredSize(new Dimension(150, 25));
		cmd.setBorder(null);
		cmdPanel.add(cmd);
		modContent.add(cmdPanel);

		JLabel respLabel = new JLabel("Text:");
		respLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		modContent.add(respLabel);

		resp = new JTextArea();
		resp.setBorder(null);
		JScrollPane respPane = new JScrollPane(resp);
		respPane.setBorder(null);
		modContent.add(respPane);

		mainPanel.add(modContent, BorderLayout.CENTER);

	}

	@Override
	public void refresh() {
	}

	@Override
	public JPanel getJPanelInstance() {
		return mainPanel;
	}

	private class SimpleCmd{

		private String cmd;

		private String response;

		private static final int DEFAULT_COOLDOWN_SECS = 30;

		private TaskCooldown cooldown;

		public SimpleCmd(String cmd, String response) {
			this.cmd = cmd;
			this.response = response;
			this.cooldown = new TaskCooldown(DEFAULT_COOLDOWN_SECS);
		}

		public String getCmd() {
			return cmd;
		}

		public String getResponse() {
			return response;
		}

		@Override
		public String toString() {
			return cmd;
		}

		@Handler
		public void onMessage(ChannelMessageEvent event) {
			Pattern pattern = Pattern.compile(cmd, Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher(event.getMessage());
			if (!match.matches())
				return;
			cooldown.run(() -> {
				TwitchPlayer.getInstance().sendMessage(response);
			});

		}

		@Handler
		public void onMessage(LiveChatMessage event) {
			Pattern pattern = Pattern.compile(cmd, Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher(event.getSnippet().getDisplayMessage());
			if (!match.matches())
				return;
			cooldown.run(() -> {
				YouTubePlayer.getInstance().sendMessage(response);
			});

		}

		@Handler
		public void run(GameButtonClickEvent event) {
			if (MainMenuPanel.getInstance().getIsAppStarted().get()) {
				Constants.printVerboseMessage(Level.INFO, String.format("Registering cmd: %s", cmd));
				if (TwitchPlayer.getInstance().isSetup())
					TwitchPlayer.getInstance().registerEventListener(this);
				if(YouTubePlayer.getInstance().isSetup())
					YouTubePlayer.getInstance().registerEventListener(this);
			} else {
				Constants.printVerboseMessage(Level.INFO, String.format("Unregistering cmd: %s", cmd));
				if (TwitchPlayer.getInstance().isSetup())
					TwitchPlayer.getInstance().unregisterEventListener(this);
				if(YouTubePlayer.getInstance().isSetup())
					YouTubePlayer.getInstance().unregisterEventListener(this);
			}
		}

	}
}
