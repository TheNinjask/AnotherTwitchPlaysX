package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandType;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandVarType;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlType;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.ControlDataPanel.Type;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager.OnUpdateLanguage;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.JComboItem;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoords;
import pt.theninjask.AnotherTwitchPlaysX.util.Pair;

public class CommandPanel extends JPanel implements OnUpdateLanguage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String varAdd = DataManager.getLanguage().getCommand().getVarAdd();
	
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

	private JTextField cooldown;

	private JLabel leadLabel;

	private JLabel cooldownLabel;

	private JLabel typeLabel;

	private JLabel varsLabel;

	private JLabel modeLabel;

	public CommandPanel() {
		this(null);
	}

	public CommandPanel(CommandData data) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s(%s)", CommandPanel.class.getSimpleName(), this.hashCode()));
		if(data == null) {
			this.current = new CommandData();
			this.saved = null;
		}else {
			this.current = data.clone();
			this.saved = data;			
		}
		controls = new ArrayList<ControlDataPanel>();
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(2, 1));
		JPanel tmp = new JPanel(new BorderLayout());
		tmp.setOpaque(false);
		tmp.add(buildPanelString(), BorderLayout.CENTER);
		tmp.add(buttonMenu(), BorderLayout.EAST);
		this.add(tmp);
		this.add(controlScroll());
		//DataManager.registerLangEvent(this);
	}

	private JPanel buildPanelString() {
		JPanel mainPanel = new JPanel(new GridLayout(5, 1));
		mainPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mainPanel.setOpaque(false);

		/*
		 * JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 * titlePanel.setOpaque(false); JLabel title = new JLabel("Command");
		 * title.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		 * titlePanel.add(title); mainPanel.add(titlePanel);
		 */

		JPanel leadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leadPanel.setOpaque(false);
		leadLabel = new JLabel(DataManager.getLanguage().getCommand().getLead());
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

		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		NumberFormatter cooldownFormatter = new NumberFormatter(format);
		cooldownFormatter.setValueClass(Integer.class);
		cooldownFormatter.setMinimum(0);
		cooldownFormatter.setMaximum(3600);
		cooldownFormatter.setAllowsInvalid(false);

		JPanel cooldownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cooldownPanel.setOpaque(false);
		cooldownLabel = new JLabel(DataManager.getLanguage().getCommand().getCmdCooldown());
		cooldownLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		cooldownPanel.add(cooldownLabel);
		cooldown = new JFormattedTextField(cooldownFormatter);
		if (current.getCooldown() != null)
			cooldown.setText(Long.toString(current.getCooldown().getTimer() / 1000));
		cooldown.setBorder(null);
		cooldown.setPreferredSize(new Dimension(62, 20));
		cooldown.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			private void update() {
				try {
					current.getCooldown().setTimer(Long.parseLong(cooldown.getText()) * 1000);
				} catch (NumberFormatException e) {
				}
			}
		});

		cooldown.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				long cooldownVal = current.getCooldown().getTimer();
				switch (e.getKeyCode()) {
				default:
					long updated;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					if (cooldownVal == 3600 * 1000)
						break;
					updated = cooldownVal + 1 * 1000;
					current.getCooldown().setTimer(updated);
					cooldown.setText(Long.toString(updated / 1000));
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					updated = cooldownVal - 1 * 1000;
					if (updated < 0)
						break;
					current.getCooldown().setTimer(updated);
					cooldown.setText(Long.toString(updated / 1000));
					break;
				}
			}
		});

		cooldownPanel.add(cooldown);
		mainPanel.add(cooldownPanel);

		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		typePanel.setOpaque(false);
		typeLabel = new JLabel(DataManager.getLanguage().getCommand().getCmdType());
		typeLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		typePanel.add(typeLabel);
		type = new JComboBox<CommandType>(CommandType.getAll());
		type.setSelectedItem(current.getType());
		type.addActionListener(l -> {
			current.setType(type.getItemAt(type.getSelectedIndex()));
		});
		type.setFocusable(false);
		typePanel.add(type);
		mainPanel.add(typePanel);

		JPanel varsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		varsPanel.setOpaque(false);
		varsLabel = new JLabel(DataManager.getLanguage().getCommand().getVars());
		varsLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		varsPanel.add(varsLabel);

		// TODO?
		vars = new JComboBox<JComboItem<Pair<String, CommandVarType>>>();
		vars.addItem(new JComboItem<Pair<String, CommandVarType>>(null, varAdd));
		vars.setSelectedItem(null);
		vars.setFocusable(false);
		for (Pair<String, CommandVarType> elem : current.getVars()) {
			if (!elem.getLeft().equals(varAdd)) {
				vars.addItem(new JComboItem<Pair<String, CommandVarType>>(elem, elem.getLeft()));
				varsBag.remove(elem.getLeft());
			}
		}

		varsRemove = new JButton(DataManager.getLanguage().getCommand().getRemove());
		varsRemove.setMargin(new Insets(0, 0, 0, 0));
		varsRemove.setVisible(false);
		varsRemove.setEnabled(false);
		varsRemove.setFocusable(false);
		AtomicBoolean disableVars = new AtomicBoolean(false);
		varsRemove.addActionListener(l -> {
			if(varAdd.equals(vars.getItemAt(vars.getSelectedIndex()).toString()))
				return;
			disableVars.set(true);
			varsBag.add(vars.getItemAt(vars.getSelectedIndex()).get().getLeft());
			current.getVars().remove(vars.getItemAt(vars.getSelectedIndex()).get());
			varsRemove.setVisible(false);
			varsRemove.setEnabled(false);
			controls.forEach(c -> {
				c.removeVar(vars.getItemAt(vars.getSelectedIndex()).get());
			});
			vars.removeItemAt(vars.getSelectedIndex());
			vars.setSelectedItem(null);
			disableVars.set(false);
			/*switch (vars.getItemAt(vars.getSelectedIndex()).toString()) {
			default:
				disableVars.set(true);
				varsBag.add(vars.getItemAt(vars.getSelectedIndex()).get().getLeft());
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
			}*/
		});

		vars.addActionListener(l -> {
			if (vars.getItemAt(vars.getSelectedIndex()) == null)
				return;
			if (disableVars.get())
				return;
			if(varAdd.equals(vars.getItemAt(vars.getSelectedIndex()).toString())) {
				Optional<String> var = varsBag.stream().findAny();
				if (!var.isPresent()) {
					JLabel msg = new JLabel(DataManager.getLanguage().getCommand().getVarAddWarnMsg());
					msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
					Constants.showCustomColorMessageDialog(null, msg,
							DataManager.getLanguage().getCommand().getVarAddWarnTitle(), JOptionPane.WARNING_MESSAGE,
							null, Constants.TWITCH_COLOR);
					vars.setSelectedItem(null);
					varsRemove.setVisible(false);
					varsRemove.setEnabled(false);
					return;
				}

				JLabel msg = new JLabel(DataManager.getLanguage().getCommand().getVarType());
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				String[] opt = { DataManager.getLanguage().getCommand().getVarDigit(),
						DataManager.getLanguage().getCommand().getVarString() };
				int resp = Constants.showCustomColorOptionDialog(null, msg,
						DataManager.getLanguage().getCommand().getVarTitle(), JOptionPane.YES_NO_OPTION,
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
				vars.addItem(new JComboItem<Pair<String, CommandVarType>>(tmp, tmp.getLeft()));
				vars.setSelectedIndex(vars.getItemCount() - 1);
				varsBag.remove(var.get());
				current.getVars().add(tmp);
				controls.forEach(c -> {
					c.addVar(tmp);
				});
				varsRemove.setVisible(true);
				varsRemove.setEnabled(true);
				return;
			}
			varsRemove.setVisible(true);
			varsRemove.setEnabled(true);
			/*switch (vars.getItemAt(vars.getSelectedIndex()).toString()) {
			case "ADD":
				Optional<String> var = varsBag.stream().findAny();
				if (!var.isPresent()) {
					JLabel msg = new JLabel(DataManager.getLanguage().getCommand().getVarAddWarnMsg());
					msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
					Constants.showCustomColorMessageDialog(null, msg,
							DataManager.getLanguage().getCommand().getVarAddWarnTitle(), JOptionPane.WARNING_MESSAGE,
							null, Constants.TWITCH_COLOR);
					vars.setSelectedItem(null);
					varsRemove.setVisible(false);
					varsRemove.setEnabled(false);
					break;
				}

				JLabel msg = new JLabel(DataManager.getLanguage().getCommand().getVarType());
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				String[] opt = { DataManager.getLanguage().getCommand().getVarDigit(),
						DataManager.getLanguage().getCommand().getVarString() };
				int resp = Constants.showCustomColorOptionDialog(null, msg,
						DataManager.getLanguage().getCommand().getVarTitle(), JOptionPane.YES_NO_OPTION,
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
				vars.addItem(new JComboItem<Pair<String, CommandVarType>>(tmp, tmp.getLeft()));
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
			}*/
		});
		// vars.setEnabled(false);
		varsPanel.add(vars);
		varsPanel.add(varsRemove);
		mainPanel.add(varsPanel);

		JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		modePanel.setOpaque(false);
		modeLabel = new JLabel(DataManager.getLanguage().getCommand().getViewMode());
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
				msg.setText(DataManager.getLanguage().getCommand().getNotSavedMsg());
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				msg.setFocusable(false);
				msg.setOpaque(false);
				String[] options = { DataManager.getLanguage().getOkOpt(),
						DataManager.getLanguage().getGoBackOpt() };
				int resp = Constants.showCustomColorOptionDialog(null, msg,
						DataManager.getLanguage().getCommand().getNotSavedTitle(), JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, null, Constants.TWITCH_COLOR);
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
			//DataManager.unregisterLangEvent(this);
			MainFrame.replacePanel(AllCommandPanel.getInstance());
		});
		bMenu.add(back);

		syntax = new JButton(DataManager.getLanguage().getCommand().getSyntax());
		syntax.setFocusable(false);
		syntax.addActionListener(l -> {
			try {

				JPanel tmp = new JPanel(new GridLayout(2, 1));
				tmp.setOpaque(false);
				JLabel syntaxLabel = new JLabel(
						String.format(DataManager.getLanguage().getCommand().getSyntaxFull(), current.getRegex()));
				syntaxLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				tmp.add(syntaxLabel);
				JLabel egLabel = new JLabel(String.format(DataManager.getLanguage().getCommand().getSyntaxDemo(),
						current.getRegexExample()));
				egLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				tmp.add(egLabel);
				Constants.showCustomColorMessageDialog(null, tmp,
						DataManager.getLanguage().getCommand().getSyntaxTitle(), JOptionPane.PLAIN_MESSAGE, null,
						Constants.TWITCH_COLOR);
			} catch (Exception e) {
				Constants.showExpectedExceptionDialog(e);
			}
		});
		bMenu.add(syntax);

		help = new JButton(DataManager.getLanguage().getCommand().getHelp());
		help.setEnabled(false);
		help.setFocusable(false);
		bMenu.add(help);

		save = new JButton(DataManager.getLanguage().getCommand().getSave());
		save.setFocusable(false);
		save.addActionListener(l -> {
			if (saved != null) {
				int index = DataManager.getCommands().indexOf(saved);
				DataManager.getCommands().remove(saved);
				current.getControls().clear();
				for (ControlDataPanel elem : controls) {
					current.getControls().add(elem.getControlData());
				}
				saved = current;
				DataManager.getCommands().add(index, current);
			} else {
				for (ControlDataPanel elem : controls) {
					current.getControls().add(elem.getControlData());
				}
				saved = current;
				DataManager.getCommands().add(current);
			}
		});
		bMenu.add(save);

		delete = new JButton(DataManager.getLanguage().getCommand().getDelete());
		delete.setFocusable(false);
		delete.addActionListener(l -> {
			if (saved != null)
				DataManager.getCommands().remove(saved);
			MainFrame.replacePanel(AllCommandPanel.getInstance());
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
		add = new JButton(DataManager.getLanguage().getCommand().getAddControl());
		add.setFocusable(false);
		add.addActionListener(l -> {
			JTextArea msg = new JTextArea();
			msg.setText(DataManager.getLanguage().getCommand().getAddChooseControlType());
			msg.setOpaque(false);
			msg.setFocusable(false);
			msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			String[] options = { DataManager.getLanguage().getCommand().getAddKeyboard(),
					DataManager.getLanguage().getCommand().getAddMouseClick(),
					DataManager.getLanguage().getCommand().getAddMouseDrag() };
			int resp = Constants.showCustomColorOptionDialog(null, msg,
					DataManager.getLanguage().getCommand().getAddChooseControlTypeTitle(),
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

	@Override
	public void updateLang(Lang session) {
		varAdd = session.getCommand().getVarAdd();
		leadLabel.setText(session.getCommand().getLead());
		cooldownLabel.setText(session.getCommand().getCmdCooldown());
		typeLabel.setText(session.getCommand().getCmdType());
		varsLabel.setText(session.getCommand().getVars());
		varsRemove.setText(session.getCommand().getRemove());
		modeLabel.setText(session.getCommand().getViewMode());
		syntax.setText(session.getCommand().getSyntax());
		help.setText(session.getCommand().getHelp());
		save.setText(session.getCommand().getSave());
		delete.setText(session.getCommand().getDelete());
		add.setText(session.getCommand().getAddControl());
		for (ControlDataPanel elem : controls) {
			elem.updateLang(session);
		}
		
	}
}
