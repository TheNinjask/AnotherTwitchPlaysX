package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.JComboItem;
import pt.theninjask.AnotherTwitchPlaysX.util.TaskCooldown;
import pt.theninjask.AnotherTwitchPlaysX.util.mock.ChannelMessageEventMock;

@ATPXModProps(keepLoaded = false)
public class TestCommandsMod extends ATPXMod {

	private JPanel mainPanel;

	private String[] users = { "TheNinjask", "Dragonboil", "Drakekax", "GoncaAC", "JoGoKa?" };

	private TaskCooldown worker;

	private JComboBox<JComboItem<CommandData>> selec;

	public TestCommandsMod() {

		worker = new TaskCooldown(1);

		mainPanel = new JPanel(new FlowLayout());
		mainPanel.setBackground(Constants.TWITCH_COLOR);

		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		NumberFormatter cooldownFormatter = new NumberFormatter(format);
		cooldownFormatter.setValueClass(Integer.class);
		cooldownFormatter.setMinimum(0);
		cooldownFormatter.setMaximum(60);
		cooldownFormatter.setAllowsInvalid(false);

		JPanel cooldownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cooldownPanel.setOpaque(false);
		JLabel cooldownLabel = new JLabel("Start in(sec): ");
		cooldownLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		cooldownPanel.add(cooldownLabel);
		JFormattedTextField cooldown = new JFormattedTextField(cooldownFormatter);
		cooldown.setBorder(null);
		cooldown.setText("0");
		cooldown.setPreferredSize(new Dimension(62, 20));
		cooldown.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				long cooldownVal = Long.parseLong(cooldown.getText());
				switch (e.getKeyCode()) {
				default:
					long updated;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					if (cooldownVal == 60)
						break;
					updated = cooldownVal + 1;
					cooldown.setText(Long.toString(updated));
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					updated = cooldownVal - 1;
					if (updated < 0)
						break;
					cooldown.setText(Long.toString(updated));
					break;
				}
			}
		});
		cooldownPanel.add(cooldown);

		//
		NumberFormatter delayFormatter = new NumberFormatter(format);
		delayFormatter.setValueClass(Integer.class);
		delayFormatter.setMinimum(0);
		delayFormatter.setMaximum(60);
		delayFormatter.setAllowsInvalid(false);

		JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		delayPanel.setOpaque(false);
		JLabel delayLabel = new JLabel("Delay between Cmd(sec): ");
		delayLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		delayPanel.add(delayLabel);
		JFormattedTextField delay = new JFormattedTextField(delayFormatter);
		delay.setBorder(null);
		delay.setText("0");
		delay.setPreferredSize(new Dimension(62, 20));
		delay.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				long delayVal = Long.parseLong(delay.getText());
				switch (e.getKeyCode()) {
				default:
					long updated;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					if (delayVal == 60)
						break;
					updated = delayVal + 1;
					delay.setText(Long.toString(updated));
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					updated = delayVal - 1;
					if (updated < 0)
						break;
					delay.setText(Long.toString(updated));
					break;
				}
			}
		});
		delayPanel.add(delay);
		//

		JButton start = new JButton("Start!");
		start.addActionListener(l -> {
			if (worker.inCooldown()) {
				Constants.showMessageDialog("Task is on cooldown", "Still in progress...");
				return;
			}
			if (selec.getItemAt(selec.getSelectedIndex()) != null
					&& selec.getItemAt(selec.getSelectedIndex()).get() != null) {
				try {
					Thread.sleep(Long.parseLong(cooldown.getText()) * 1000);
					worker.run(() -> {
						ChannelMessageEventMock mockMessage = new ChannelMessageEventMock(
								users[ThreadLocalRandom.current().nextInt(users.length)],
								selec.getItemAt(selec.getSelectedIndex()).get().getRegexExample());
						selec.getItemAt(selec.getSelectedIndex()).get().onMessage(mockMessage);
					});
				} catch (Exception e) {
					Constants.showExceptionDialog(e);
				}
				return;
			}
			worker.run(() -> {
				try {
					Thread.sleep(Long.parseLong(cooldown.getText()) * 1000);
					for (CommandData cmd : DataManager.getInstance().getCommands()) {
						ChannelMessageEventMock mockMessage = new ChannelMessageEventMock(
								users[ThreadLocalRandom.current().nextInt(users.length)], cmd.getRegexExample());
						cmd.onMessage(mockMessage);
						Thread.sleep(Long.parseLong(delay.getText()) * 1000);
					}
				} catch (Exception e) {
					Constants.showExceptionDialog(e);
				}
			});
		});
		cooldownPanel.add(start);

		mainPanel.add(cooldownPanel);
		mainPanel.add(delayPanel);

		JButton back = new JButton("Back");
		back.addActionListener(l -> {
			MainFrame.replacePanel(EmbeddedModMenuPanel.getInstance());
		});
		mainPanel.add(back);
		JLabel selecLabel = new JLabel("Select:");
		selecLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		mainPanel.add(selecLabel);
		selec = new JComboBox<JComboItem<CommandData>>();
		mainPanel.add(selec);
	}

	@Override
	public void refresh() {
		selec.removeAllItems();
		selec.addItem(new JComboItem<CommandData>(null, "ALL"));
		for (CommandData cmd : DataManager.getInstance().getCommands()) {
			selec.addItem(new JComboItem<CommandData>(cmd, cmd.getLead()));
		}
	}

	@Override
	public JPanel getJPanelInstance() {
		return mainPanel;
	}

}
