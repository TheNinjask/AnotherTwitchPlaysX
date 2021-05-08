package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandType;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlType;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoords;

public class CommandPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommandData data;

	private boolean isSaved;

	private List<ControlDataPanel> controls;

	private JPanel addPanel;

	private JPanel controlsPanel;

	private JButton left;

	private JButton right;

	public CommandPanel() {
		this(new CommandData());
		this.isSaved = false;
	}

	public CommandPanel(CommandData data) {
		controls = new ArrayList<ControlDataPanel>();
		this.data = data;
		this.isSaved = true;
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(2, 1));
		JPanel tmp = new JPanel(new BorderLayout());
		tmp.setOpaque(false);
		tmp.add(buildPanelString(), BorderLayout.CENTER);
		tmp.add(buttonMenu(), BorderLayout.EAST);
		this.add(tmp);
		this.add(controlScroll());
	}

	private JPanel buildPanelString() {
		JPanel mainPanel = new JPanel(new GridLayout(4, 1));
		mainPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mainPanel.setOpaque(false);

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titlePanel.setOpaque(false);
		JLabel title = new JLabel("Command");
		title.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		titlePanel.add(title);
		mainPanel.add(titlePanel);

		JPanel leadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leadPanel.setOpaque(false);
		JLabel leadLabel = new JLabel("Lead: ");
		leadLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		leadPanel.add(leadLabel);
		JTextField lead = new JTextField("#TODO");
		lead.setBorder(null);
		lead.setPreferredSize(new Dimension(151, 20));
		leadPanel.add(lead);
		mainPanel.add(leadPanel);

		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		typePanel.setOpaque(false);
		JLabel typeLabel = new JLabel("Type: ");
		typeLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		typePanel.add(typeLabel);
		JComboBox<CommandType> type = new JComboBox<CommandType>(CommandType.getAll());
		type.setSelectedItem(CommandType.UNISON);
		type.setEnabled(false);
		typePanel.add(type);
		mainPanel.add(typePanel);

		JPanel varsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		varsPanel.setOpaque(false);
		JLabel varsLabel = new JLabel("Vars: ");
		varsLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		varsPanel.add(varsLabel);

		JComboBox<String> vars = new JComboBox<String>();
		vars.addItem("ADD");
		vars.setSelectedItem(null);
		vars.setEnabled(false);
		varsPanel.add(vars);

		mainPanel.add(varsPanel);

		return mainPanel;
	}

	private JPanel buttonMenu() {
		JPanel bMenu = new JPanel(new GridLayout(5, 1));

		JButton back = new JButton("Back");
		back.setFocusable(false);
		back.addActionListener(l -> {
			if (!isSaved) {
				JTextArea msg = new JTextArea();
				msg.setText("This command has not saved changes!");
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				msg.setFocusable(false);
				msg.setOpaque(false);
				String[] options = { "Leave", "Go Back" };
				int resp = Constants.showCustomColorOptionDialog(null, msg, "COMMAND NOT SAVED",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null,
						Constants.TWITCH_COLOR);
				switch (resp) {
				case JOptionPane.OK_OPTION:
					break;
				case JOptionPane.CLOSED_OPTION:
				case JOptionPane.CANCEL_OPTION:
				default:
					return;
				}
			}
			MouseCoords.getInstance().clearListeners();
			MainFrame.getInstance().replacePanel(AllCommandPanel.getInstance());
		});
		bMenu.add(back);

		JButton syntax = new JButton("Syntax");
		syntax.setFocusable(false);
		bMenu.add(syntax);

		JButton help = new JButton("Help");
		help.setFocusable(false);
		bMenu.add(help);

		JButton save = new JButton("Save");
		save.setFocusable(false);
		bMenu.add(save);

		JButton delete = new JButton("Delete");
		delete.setFocusable(false);
		bMenu.add(delete);

		return bMenu;
	}

	private JPanel controlScroll() {
		JPanel displayControl = new JPanel(new BorderLayout());
		displayControl.setOpaque(false);

		left = new JButton("<");
		left.setFocusable(false);
		left.addActionListener(l -> {
			moveControlDisplay(false);
		});
		left.setEnabled(false);
		displayControl.add(left, BorderLayout.WEST);

		right = new JButton(">");
		right.setFocusable(false);
		right.addActionListener(l -> {
			moveControlDisplay(true);
		});
		displayControl.add(right, BorderLayout.EAST);

		controlsPanel = new JPanel(new BorderLayout());
		controlsPanel.setOpaque(false);
		displayAdd();
		for (ControlData elem : data.getControls()) {
			controls.add(new ControlDataPanel(elem, controls, this));
		}
		if (controls.isEmpty()) {
			controlsPanel.add(addPanel);
			right.setEnabled(false);
		} else {
			controlsPanel.add(controls.get(0));
		}
		displayControl.add(controlsPanel, BorderLayout.CENTER);
		return displayControl;
	}

	public void moveControlDisplay(boolean right) {
		Component comp = controlsPanel.getComponent(0);
		controlsPanel.removeAll();
		if (!(comp instanceof ControlDataPanel))
			if (controls.isEmpty()) {
				this.right.setEnabled(false);
				this.left.setEnabled(false);
				controlsPanel.add(addPanel);
			} else {
				if (!right) {
					this.right.setEnabled(true);
					ControlDataPanel tmp = controls.get(controls.size() - 1);
					tmp.refreshIndex();
					controlsPanel.add(tmp);
					if (controls.size() == 1)
						this.left.setEnabled(false);
				} else
					this.right.setEnabled(false);
			}
		else {
			int currentIndex = controls.indexOf(comp);
			if (right) {
				if (currentIndex == 0)
					this.left.setEnabled(true);
				if (currentIndex + 1 >= controls.size()) {
					controlsPanel.add(addPanel);
					this.right.setEnabled(false);
				} else {
					ControlDataPanel tmp = controls.get(currentIndex + 1);
					tmp.refreshIndex();
					controlsPanel.add(tmp);
				}
			} else {
				if (currentIndex - 1 <= 0) {
					ControlDataPanel tmp = controls.get(0);
					tmp.refreshIndex();
					controlsPanel.add(tmp);
					this.left.setEnabled(false);
				} else {
					ControlDataPanel tmp = controls.get(currentIndex - 1);
					tmp.refreshIndex();
					controlsPanel.add(tmp);
				}
			}
		}
		controlsPanel.revalidate();
		controlsPanel.repaint();
	}
	
	public void resetMoveControlDisplayButtons() {
		Component comp = controlsPanel.getComponent(0);
		if (!(comp instanceof ControlDataPanel))
			if(controls.isEmpty()) {
				this.right.setEnabled(false);
				this.left.setEnabled(false);
			}else {
				this.left.setEnabled(true);
				this.right.setEnabled(false);
			}
		else
			if(controls.indexOf(comp)==0) {
				this.left.setEnabled(false);
				this.right.setEnabled(true);
			}else {
				this.right.setEnabled(true);
				this.left.setEnabled(true);
			}
	}

	private JPanel displayAdd() {
		addPanel = new JPanel(new GridBagLayout());
		addPanel.setOpaque(false);
		JButton add = new JButton("ADD");
		add.setFocusable(false);
		add.addActionListener(l -> {
			JTextArea msg = new JTextArea();
			msg.setText("Choose a type");
			msg.setOpaque(false);
			msg.setFocusable(false);
			msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			String[] options = { "Keyboard", "Mouse", "Cancel" };
			int resp = Constants.showCustomColorOptionDialog(null, msg, "Adding New Control",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null,
					Constants.TWITCH_COLOR);
			ControlData cData = new ControlData();
			switch (resp) {
			case JOptionPane.NO_OPTION:
				cData.setType(ControlType.MOUSE_CLICK);
				break;
			case JOptionPane.YES_OPTION:
				cData.setType(ControlType.KEY);
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
			default:
				return;
			}
			controlsPanel.removeAll();
			controlsPanel.add(new ControlDataPanel(cData, controls, this));
			controlsPanel.revalidate();
			controlsPanel.repaint();
			right.setEnabled(true);
		});
		addPanel.add(add);

		return addPanel;
	}

	public CommandData getCommandData() {
		return data;
	}
}
