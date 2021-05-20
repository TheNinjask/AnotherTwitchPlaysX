package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandType;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandVarType;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlType;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.ControlDataPanel.Type;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.JComboItem;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoords;
import pt.theninjask.AnotherTwitchPlaysX.util.Pair;

public class CommandPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommandData saved;

	private CommandData current;

	private List<ControlDataPanel> controls;

	private JPanel addPanel;

	private JPanel controlsPanel;

	private JButton left;

	private JButton right;

	private JButton mode;

	private Set<String> varsBag = new HashSet<String>(Arrays.asList("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
			"A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M"));

	private JButton back;

	private JButton syntax;

	private JButton help;

	private JButton save;

	private JButton delete;

	private JTextField lead;

	private JComboBox<CommandType> type;

	private JComboBox<JComboItem<Pair<String, CommandVarType>>> vars;

	private JButton varsRemove;

	private JButton add;

	public CommandPanel() {
		this.current = new CommandData();
		this.saved = null;
		controls = new ArrayList<ControlDataPanel>();
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(2, 1));
		JPanel tmp = new JPanel(new BorderLayout());
		tmp.setOpaque(false);
		tmp.add(buildPanelString(), BorderLayout.CENTER);
		tmp.add(buttonMenu(), BorderLayout.EAST);
		this.add(tmp);
		this.add(controlScroll());
	}

	public CommandPanel(CommandData data) {
		this.current = data.clone();
		this.saved = data;
		controls = new ArrayList<ControlDataPanel>();
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
		JPanel mainPanel = new JPanel(new GridLayout(5, 1));
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
		lead = new JTextField("");
		if (current.getLead() != null)
			lead.setText(current.getLead());
		lead.setBorder(null);
		lead.setPreferredSize(new Dimension(151, 20));
		lead.addCaretListener(l -> {
			current.setLead(lead.getText());
		});
		leadPanel.add(lead);
		mainPanel.add(leadPanel);

		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		typePanel.setOpaque(false);
		JLabel typeLabel = new JLabel("Type: ");
		typeLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		typePanel.add(typeLabel);
		type = new JComboBox<CommandType>(CommandType.getAll());
		type.setSelectedItem(CommandType.UNISON);
		type.addActionListener(l -> {
			current.setType(type.getItemAt(type.getSelectedIndex()));
		});
		type.setFocusable(false);
		type.setEnabled(false);
		typePanel.add(type);
		mainPanel.add(typePanel);

		JPanel varsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		varsPanel.setOpaque(false);
		JLabel varsLabel = new JLabel("Vars: ");
		varsLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		varsPanel.add(varsLabel);

		// TODO?
		vars = new JComboBox<JComboItem<Pair<String, CommandVarType>>>();
		vars.addItem(new JComboItem<Pair<String, CommandVarType>>(null, "ADD"));
		vars.setSelectedItem(null);
		vars.setFocusable(false);
		for (Pair<String, CommandVarType> elem : current.getVars()) {
			if (!elem.getKey().equals("ADD")) {
				vars.addItem(new JComboItem<Pair<String, CommandVarType>>(elem, elem.getKey()));
				varsBag.remove(elem.getKey());
			}
		}

		varsRemove = new JButton("Remove");
		varsRemove.setMargin(new Insets(0, 0, 0, 0));
		varsRemove.setVisible(false);
		varsRemove.setEnabled(false);
		varsRemove.setFocusable(false);
		AtomicBoolean disableVars = new AtomicBoolean(false);
		varsRemove.addActionListener(l -> {
			switch (vars.getItemAt(vars.getSelectedIndex()).toString()) {
			default:
				disableVars.set(true);
				varsBag.add(vars.getItemAt(vars.getSelectedIndex()).get().getKey());
				current.getVars().remove(vars.getItemAt(vars.getSelectedIndex()).get());
				varsRemove.setVisible(false);
				varsRemove.setEnabled(false);
				controls.forEach(c -> {
					c.removeVar(vars.getItemAt(vars.getSelectedIndex()).get());
				});
				vars.removeItemAt(vars.getSelectedIndex());
				vars.setSelectedItem(null);
				disableVars.set(false);
				break;
			case "ADD":
				break;
			}
		});

		vars.addActionListener(l -> {
			if (vars.getItemAt(vars.getSelectedIndex()) == null)
				return;
			if (disableVars.get())
				return;
			switch (vars.getItemAt(vars.getSelectedIndex()).toString()) {
			case "ADD":
				Optional<String> var = varsBag.stream().findAny();
				if (!var.isPresent()) {
					JLabel msg = new JLabel("Standard (and Already In) Variables Ran Out!");
					msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
					Constants.showCustomColorMessageDialog(null, msg, "Ran Out Of Vars", JOptionPane.WARNING_MESSAGE,
							null, Constants.TWITCH_COLOR);
					vars.setSelectedItem(null);
					varsRemove.setVisible(false);
					varsRemove.setEnabled(false);
					break;
				}

				JLabel msg = new JLabel("Type of variable");
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				String[] opt = { "Digit", "String" };
				int resp = Constants.showCustomColorOptionDialog(null, msg, "Choose type", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, opt, null, Constants.TWITCH_COLOR);
				CommandVarType type;
				switch (resp) {
				case JOptionPane.YES_OPTION:
					type = CommandVarType.DIGIT;
					break;
				case JOptionPane.NO_OPTION:
					type = CommandVarType.STRING;
					break;
				case JOptionPane.CLOSED_OPTION:
				default:
					return;
				}
				Pair<String, CommandVarType> tmp = new Pair<String, CommandVarType>(var.get(), type);
				vars.addItem(new JComboItem<Pair<String, CommandVarType>>(tmp, tmp.getKey()));
				vars.setSelectedIndex(vars.getItemCount() - 1);
				varsBag.remove(var.get());
				current.getVars().add(tmp);
				controls.forEach(c -> {
					c.addVar(tmp);
				});
				varsRemove.setVisible(true);
				varsRemove.setEnabled(true);
				break;
			default:
				varsRemove.setVisible(true);
				varsRemove.setEnabled(true);
				break;
			}
		});
		// vars.setEnabled(false);
		varsPanel.add(vars);
		varsPanel.add(varsRemove);
		mainPanel.add(varsPanel);

		JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		modePanel.setOpaque(false);
		JLabel modeLabel = new JLabel("View Mode: ");
		modeLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		modePanel.add(modeLabel);
		mode = new JButton("Normal");
		mode.setMargin(new Insets(0, 0, 0, 0));
		mode.setFocusable(false);
		mode.addActionListener(l -> {
			switch (mode.getText()) {
			case "Normal":
				for (ControlDataPanel elem : controls) {
					elem.setMode(Type.VAR);
				}
				mode.setText("Vars");
				break;
			case "Vars":
				for (ControlDataPanel elem : controls) {
					elem.setMode(Type.NORMAL);
				}
				mode.setText("Normal");
				break;
			default:
				break;
			}
		});

		modePanel.add(mode);
		mainPanel.add(modePanel);

		return mainPanel;
	}

	private JPanel buttonMenu() {
		JPanel bMenu = new JPanel(new GridLayout(5, 1));

		back = new JButton("Back");
		back.setFocusable(false);
		back.addActionListener(l -> {
			if (saved == null || !saved.equals(current)) {
				JTextArea msg = new JTextArea();
				msg.setText("This command has not saved changes!");
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				msg.setFocusable(false);
				msg.setOpaque(false);
				String[] options = { "Ok", "Go Back" };
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

		syntax = new JButton("Syntax");
		syntax.setFocusable(false);
		syntax.addActionListener(l -> {
			try {

				JPanel tmp = new JPanel(new GridLayout(2, 1));
				tmp.setOpaque(false);
				JLabel syntaxLabel = new JLabel(String.format("Syntax: %s", current.getRegex()));
				syntaxLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				tmp.add(syntaxLabel);
				JLabel egLabel = new JLabel(String.format("Example: %s", current.getRegexExample()));
				egLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				tmp.add(egLabel);
				Constants.showCustomColorMessageDialog(null, tmp, "Syntax of command", JOptionPane.PLAIN_MESSAGE, null,
						Constants.TWITCH_COLOR);
			} catch (Exception e) {
				Constants.showExpectedExceptionDialog(e);
			}
		});
		bMenu.add(syntax);

		help = new JButton("Help");
		help.setEnabled(false);
		help.setFocusable(false);
		bMenu.add(help);

		save = new JButton("Save");
		save.setFocusable(false);
		save.addActionListener(l -> {
			if (saved != null) {
				int index = DataManager.getInstance().getCommands().indexOf(saved);
				DataManager.getInstance().getCommands().remove(saved);
				for (ControlDataPanel elem : controls) {
					current.getControls().add(elem.getControlData());
				}
				saved = current;
				DataManager.getInstance().getCommands().add(index, current);
			} else {
				for (ControlDataPanel elem : controls) {
					current.getControls().add(elem.getControlData());
				}
				saved = current;
				DataManager.getInstance().getCommands().add(current);
			}
		});
		bMenu.add(save);

		delete = new JButton("Delete");
		delete.setFocusable(false);
		delete.addActionListener(l -> {
			if (saved != null)
				DataManager.getInstance().getCommands().remove(saved);
			MainFrame.getInstance().replacePanel(AllCommandPanel.getInstance());
		});
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
		for (ControlData elem : current.getControls()) {
			new ControlDataPanel(elem, controls, this);
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
		if (!(comp instanceof ControlDataPanel)) {
			this.right.setEnabled(false);
			if (controls.isEmpty()) {
				this.left.setEnabled(false);
			} else {
				this.left.setEnabled(true);
			}
		} else {
			this.right.setEnabled(true);
			if (controls.indexOf(comp) == 0) {
				this.left.setEnabled(false);
			} else {
				this.left.setEnabled(true);
			}
		}
	}

	private JPanel displayAdd() {
		addPanel = new JPanel(new GridBagLayout());
		addPanel.setOpaque(false);
		add = new JButton("ADD");
		add.setFocusable(false);
		add.addActionListener(l -> {
			JTextArea msg = new JTextArea();
			msg.setText("Choose a type");
			msg.setOpaque(false);
			msg.setFocusable(false);
			msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			String[] options = { "Keyboard", "Mouse Click", "Mouse Drag" };
			int resp = Constants.showCustomColorOptionDialog(null, msg, "Adding New Control",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null,
					Constants.TWITCH_COLOR);
			ControlData cData = new ControlData();
			switch (resp) {
			case JOptionPane.NO_OPTION:
				cData.setType(ControlType.MOUSE);
				break;
			case JOptionPane.YES_OPTION:
				cData.setType(ControlType.KEY);
				break;
			case JOptionPane.CANCEL_OPTION:
				cData.setType(ControlType.MOUSE_DRAG);
				break;
			case JOptionPane.CLOSED_OPTION:
			default:
				return;
			}
			controlsPanel.removeAll();
			ControlDataPanel tmp = new ControlDataPanel(cData, controls, this);
			if (mode.getText().equals("Vars"))
				tmp.setMode(Type.VAR);
			controlsPanel.add(tmp);
			controlsPanel.revalidate();
			controlsPanel.repaint();
			right.setEnabled(true);
		});
		addPanel.add(add);

		return addPanel;
	}

	public CommandData getCurrentCommandData() {
		return current;
	}

	public List<ControlDataPanel> getControls() {
		return controls;
	}

	public JPanel getAddPanel() {
		return addPanel;
	}

	public JButton getLeft() {
		return left;
	}

	public JButton getRight() {
		return right;
	}

	public JButton getMode() {
		return mode;
	}

	public Set<String> getVarsBag() {
		return varsBag;
	}

	public JButton getBack() {
		return back;
	}

	public JButton getSyntax() {
		return syntax;
	}

	public JButton getHelp() {
		return help;
	}

	public JButton getSave() {
		return save;
	}

	public JButton getDelete() {
		return delete;
	}

	public JTextField getLead() {
		return lead;
	}

	public JComboBox<CommandType> getType() {
		return type;
	}

	public JComboBox<JComboItem<Pair<String, CommandVarType>>> getVars() {
		return vars;
	}

	public JButton getVarsRemove() {
		return varsRemove;
	}

	public JButton getAdd() {
		return add;
	}
}
