package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlType;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.JComboItem;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoords;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoordsListener;

public class ControlDataPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ControlData data;

	private List<ControlDataPanel> in;

	private JComboBox<Integer> index;

	private CommandPanel parent;

	private AtomicBoolean isRefreshActive;

	private MouseCoordsListener listener = null;
	
	private MouseCoordsListener listenerFinal = null;
	
	public enum Type{
		NORMAL,
		VAR
	}

	private List<Component> normal = new ArrayList<Component>();
	
	private List<Component> var = new ArrayList<Component>();
	
	public ControlDataPanel(ControlData newData, List<ControlDataPanel> in, CommandPanel parent) {
		this.data = newData;
		this.parent = parent;
		this.in = in;
		this.isRefreshActive = new AtomicBoolean(false);
		this.in.add(this);
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		JPanel center;
		switch (data.getType()) {
		case KEY:
			center = new JPanel(new GridLayout(3, 1));
			break;
		default:
			center = new JPanel(new GridLayout(5, 1));
			break;
		}
		center.setOpaque(false);
		JPanel inputPanel = new JPanel(new FlowLayout());
		inputPanel.setOpaque(false);
		JLabel inputLabel = new JLabel(data.getType() == ControlType.KEY ? "Key" : "Button");
		inputLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		inputPanel.add(inputLabel);
		switch (data.getType()) {
		case KEY:
			JButton key = new JButton("None");
			normal.add(key);
			key.setFocusable(false);
			key.addActionListener(l -> {
				JPanel pressPanel = new JPanel(new GridLayout(2, 1));
				pressPanel.setBackground(Constants.TWITCH_COLOR);
				JLabel press = new JLabel(String.format("Press a key (Current: %s)",
						data.getKey() == null ? "None" : KeyEvent.getKeyText(data.getKey())));
				press.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				press.setHorizontalAlignment(JLabel.CENTER);
				pressPanel.add(press);
				pressPanel.setFocusable(true);
				pressPanel.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							press.setText("Press a key (Current: None)");
							data.setKey(null);
							key.setText("None");
						} else {
							press.setText(
									String.format("Press a key (Current: %s)", KeyEvent.getKeyText(e.getKeyCode())));
							data.setKey(e.getKeyCode());
							key.setText(KeyEvent.getKeyText(e.getKeyCode()));
						}
					}
				});
				// JButton ok = new JButton("Confirm");
				// pressPanel.add(ok);
				new PopOutFrame(pressPanel, MainFrame.getInstance());
			});
			inputPanel.add(key);
			break;
		// case MOUSE_CLICK:
		case MOUSE:
		case MOUSE_DRAG:
			JComboBox<JComboItem<Integer>> opt = new JComboBox<JComboItem<Integer>>();
			normal.add(opt);
			opt.setFocusable(false);
			opt.addItem(new JComboItem<Integer>(null, "None"));
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON1_DOWN_MASK, "Left"));
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON2_DOWN_MASK, "Right"));
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON3_DOWN_MASK, "Middle"));
			opt.addActionListener(l -> {
				this.data.setKey(opt.getItemAt(opt.getSelectedIndex()).get());
			});
			inputPanel.add(opt);
			break;
		// case MOUSE_MOV:
		case MOUSE_WHEEL:
		default:
			JLabel na = new JLabel("N/A");
			normal.add(na);
			na.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			inputPanel.add(na);
			break;
		}

		center.add(inputPanel);

		/*
		 * DecimalFormatSymbols symb = new DecimalFormatSymbols();
		 * symb.setGroupingSeparator(Character.MIN_VALUE); DecimalFormat format = new
		 * DecimalFormat(); format.setDecimalFormatSymbols(symb);
		 */
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);

		if (data.getType() != ControlType.MOUSE_DRAG) {

			NumberFormatter durationFormatter = new NumberFormatter(format);
			durationFormatter.setValueClass(Integer.class);
			durationFormatter.setMinimum(0);
			durationFormatter.setMaximum(Integer.MAX_VALUE);
			durationFormatter.setAllowsInvalid(false);
			JPanel durationPanel = new JPanel();
			durationPanel.setOpaque(false);
			JLabel durationLabel = new JLabel("Duration (sec):");
			durationLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			durationPanel.add(durationLabel);
			JTextField duration = new JFormattedTextField(durationFormatter);
			normal.add(duration);
			duration.setBorder(null);
			if (data.getDuration() != null)
				duration.setText(data.getDuration().toString());
			duration.getDocument().addDocumentListener(new DocumentListener() {

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
						data.setDuration(Integer.parseInt(duration.getText()));
					} catch (NumberFormatException e) {
					}
				}
			});
			/*
			 * duration.addActionListener(l->{
			 * data.setDuration(Integer.parseInt(duration.getText())); });
			 */
			duration.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					default:
						int updated;
						Integer durationVal;
						break;
					case KeyEvent.VK_UP:
					case KeyEvent.VK_KP_UP:
						durationVal = data.getDuration();
						if (durationVal == null)
							break;
						if (durationVal == Integer.MAX_VALUE)
							break;
						updated = durationVal + 1;
						data.setDuration(updated);
						duration.setText(Integer.toString(updated));
						break;
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_KP_DOWN:
						durationVal = data.getDuration();
						if (durationVal == null)
							break;
						updated = durationVal - 1;
						if (updated < 0)
							break;
						data.setDuration(updated);
						duration.setText(Integer.toString(updated));
						break;
					}
				}
			});
			duration.setPreferredSize(new Dimension(50, 20));
			durationPanel.add(duration);
			center.add(durationPanel);
		}

		JPanel aftermathPanel = new JPanel();
		aftermathPanel.setOpaque(false);
		JLabel aftermathLabel = new JLabel("Aftermath (sec):");
		aftermathLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		aftermathPanel.add(aftermathLabel);
		NumberFormatter aftermathFormatter = new NumberFormatter(format);
		aftermathFormatter.setValueClass(Integer.class);
		aftermathFormatter.setMinimum(0);
		aftermathFormatter.setMaximum(Integer.MAX_VALUE);
		aftermathFormatter.setAllowsInvalid(false);
		JTextField aftermath = new JFormattedTextField(aftermathFormatter);
		normal.add(aftermath);
		aftermath.setBorder(null);
		if (data.getAftermathDelay() != null)
			aftermath.setText(data.getAftermathDelay().toString());
		aftermath.getDocument().addDocumentListener(new DocumentListener() {

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
					data.setAftermathDelay(Integer.parseInt(aftermath.getText()));
				} catch (NumberFormatException e) {
				}
			}
		});
		aftermath.setPreferredSize(new Dimension(50, 20));
		aftermath.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				default:
					int updated;
					Integer aftermathVal;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					aftermathVal = data.getAftermathDelay();
					if (aftermathVal == null)
						break;
					if (aftermathVal == Integer.MAX_VALUE)
						break;
					updated = aftermathVal + 1;
					data.setAftermathDelay(updated);
					aftermath.setText(Integer.toString(updated));
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					aftermathVal = data.getAftermathDelay();
					if (aftermathVal == null)
						break;
					updated = aftermathVal - 1;
					if (updated < 0)
						break;
					data.setAftermathDelay(updated);
					aftermath.setText(Integer.toString(updated));
					break;
				}
			}
		});
		aftermathPanel.add(aftermath);
		center.add(aftermathPanel);
		if ((data.getType() == ControlType.MOUSE || data.getType()==ControlType.MOUSE_DRAG)&& data.getInDepthCursor() != null) {
			NumberFormatter xFormatter = new NumberFormatter(format);
			xFormatter.setValueClass(Integer.class);
			xFormatter.setMinimum(Integer.MIN_VALUE);
			xFormatter.setMaximum(Integer.MAX_VALUE);
			xFormatter.setAllowsInvalid(false);
			JPanel xPanel = new JPanel();
			xPanel.setOpaque(false);
			JLabel xLabel = new JLabel(String.format("X (%s):", MouseCoords.getInstance().getX().get()));
			xLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			xPanel.add(xLabel);
			JFormattedTextField x = new JFormattedTextField(xFormatter);
			normal.add(x);
			x.setBorder(null);
			if (data.getInDepthCursor().getX() != null)
				x.setText(data.getInDepthCursor().getX().toString());
			x.getDocument().addDocumentListener(new DocumentListener() {

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
						data.getInDepthCursor().setX(Integer.parseInt(x.getText()));
					} catch (NumberFormatException e) {
					}
				}
			});
			x.setPreferredSize(new Dimension(50, 20));
			x.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					default:
						int updated;
						Integer xVal;
						break;
					case KeyEvent.VK_UP:
					case KeyEvent.VK_KP_UP:
						xVal = data.getInDepthCursor().getX();
						if (xVal == null)
							break;
						if (xVal == Integer.MAX_VALUE)
							break;
						updated = xVal + 1;
						data.getInDepthCursor().setX(updated);
						x.setValue(updated);
						break;
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_KP_DOWN:
						xVal = data.getInDepthCursor().getX();
						if (xVal == null)
							break;
						if (xVal == Integer.MIN_VALUE)
							break;
						updated = xVal - 1;
						data.getInDepthCursor().setX(updated);
						x.setValue(updated);
						break;
					}
				}
			});
			JButton xClear = new JButton("[x]");
			normal.add(xClear);
			xClear.setFocusable(false);
			xClear.setMargin(new Insets(0, 0, 0, 0));
			xClear.setPreferredSize(Constants.X_BUTTON);
			xClear.addActionListener(l->{
				x.setValue(null);
				data.getInDepthCursor().setX(null);
			});
			xPanel.add(xClear);
			xPanel.add(x);
			center.add(xPanel);

			JPanel yPanel = new JPanel();
			yPanel.setOpaque(false);
			JLabel yLabel = new JLabel(String.format("Y (%s):", MouseCoords.getInstance().getY().get()));
			yLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			yPanel.add(yLabel);
			NumberFormatter yFormatter = new NumberFormatter(format);
			yFormatter.setValueClass(Integer.class);
			yFormatter.setMinimum(Integer.MIN_VALUE);
			yFormatter.setMaximum(Integer.MAX_VALUE);
			yFormatter.setAllowsInvalid(false);
			JFormattedTextField y = new JFormattedTextField(yFormatter);
			normal.add(y);
			y.setBorder(null);
			if (data.getInDepthCursor().getY() != null)
				y.setText(data.getInDepthCursor().getY().toString());
			y.getDocument().addDocumentListener(new DocumentListener() {

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
						data.getInDepthCursor().setY(Integer.parseInt(y.getText()));
					} catch (NumberFormatException e) {
					}
				}
			});
			y.setPreferredSize(new Dimension(50, 20));
			y.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					default:
						int updated;
						Integer yVal;
						break;
					case KeyEvent.VK_UP:
					case KeyEvent.VK_KP_UP:
						yVal = data.getInDepthCursor().getY();
						if (yVal == null)
							break;
						if (yVal == Integer.MAX_VALUE)
							break;
						updated = yVal + 1;
						data.getInDepthCursor().setY(updated);
						y.setValue(updated);
						break;
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_KP_DOWN:
						yVal = data.getInDepthCursor().getY();
						if (yVal == null)
							break;
						if (yVal == Integer.MIN_VALUE)
							break;
						updated = yVal - 1;
						data.getInDepthCursor().setY(updated);
						y.setValue(updated);
						break;
					}
				}
			});
			JButton yClear = new JButton("[x]");
			normal.add(yClear);
			yClear.setFocusable(false);
			yClear.setMargin(new Insets(0, 0, 0, 0));
			yClear.setPreferredSize(Constants.X_BUTTON);
			yClear.addActionListener(l->{
				y.setValue(null);
				data.getInDepthCursor().setY(null);
			});
			yPanel.add(yClear);
			yPanel.add(y);
			center.add(yPanel);
			listener = (eX, eY) -> {
				xLabel.setText(String.format("X (%s):", MouseCoords.getInstance().getX().get()));
				yLabel.setText(String.format("Y (%s):", MouseCoords.getInstance().getY().get()));
			};
			MouseCoords.getInstance().registerListener(listener);
			if(data.getType()==ControlType.MOUSE_DRAG) {
				JPanel cyclePanel = new JPanel(new BorderLayout());
				cyclePanel.setOpaque(false);
				JLabel cycleLabel = new JLabel("Initial Coords");
				cycleLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				cycleLabel.setHorizontalAlignment(JLabel.CENTER);
				cyclePanel.add(cycleLabel,BorderLayout.CENTER);
				/*
				 * 
				 */
				NumberFormatter finalXFormatter = new NumberFormatter(format);
				finalXFormatter.setValueClass(Integer.class);
				finalXFormatter.setMinimum(Integer.MIN_VALUE);
				finalXFormatter.setMaximum(Integer.MAX_VALUE);
				finalXFormatter.setAllowsInvalid(false);
				JPanel finalXPanel = new JPanel();
				finalXPanel.setOpaque(false);
				JLabel finalXLabel = new JLabel(String.format("X (%s):", MouseCoords.getInstance().getX().get()));
				finalXLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				finalXPanel.add(finalXLabel);
				JFormattedTextField finalX = new JFormattedTextField(finalXFormatter);
				normal.add(finalX);
				finalX.setBorder(null);
				if (data.getInDepthCursor().getFinalX() != null)
					finalX.setText(data.getInDepthCursor().getX().toString());
				finalX.getDocument().addDocumentListener(new DocumentListener() {

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
							data.getInDepthCursor().setFinalX(Integer.parseInt(finalX.getText()));
						} catch (NumberFormatException e) {
						}
					}
				});
				finalX.setPreferredSize(new Dimension(50, 20));
				finalX.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						switch (e.getKeyCode()) {
						default:
							int updated;
							Integer xVal;
							break;
						case KeyEvent.VK_UP:
						case KeyEvent.VK_KP_UP:
							xVal = data.getInDepthCursor().getFinalX();
							if (xVal == null)
								break;
							if (xVal == Integer.MAX_VALUE)
								break;
							updated = xVal + 1;
							data.getInDepthCursor().setFinalX(updated);
							finalX.setValue(updated);
							break;
						case KeyEvent.VK_DOWN:
						case KeyEvent.VK_KP_DOWN:
							xVal = data.getInDepthCursor().getFinalX();
							if (xVal == null)
								break;
							if (xVal == Integer.MIN_VALUE)
								break;
							updated = xVal - 1;
							data.getInDepthCursor().setFinalX(updated);
							finalX.setValue(updated);
							break;
						}
					}
				});
				JButton finalXClear = new JButton("[x]");
				normal.add(finalXClear);
				finalXClear.setFocusable(false);
				finalXClear.setMargin(new Insets(0, 0, 0, 0));
				finalXClear.setPreferredSize(Constants.X_BUTTON);
				finalXClear.addActionListener(l->{
					finalX.setValue(null);
					data.getInDepthCursor().setFinalX(null);
				});
				finalXPanel.add(finalXClear);
				finalXPanel.add(finalX);
				//center.add(finalXPanel);

				JPanel finalYPanel = new JPanel();
				finalYPanel.setOpaque(false);
				JLabel finalYLabel = new JLabel(String.format("Y (%s):", MouseCoords.getInstance().getY().get()));
				finalYLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				finalYPanel.add(finalYLabel);
				NumberFormatter finalYFormatter = new NumberFormatter(format);
				finalYFormatter.setValueClass(Integer.class);
				finalYFormatter.setMinimum(Integer.MIN_VALUE);
				finalYFormatter.setMaximum(Integer.MAX_VALUE);
				finalYFormatter.setAllowsInvalid(false);
				JFormattedTextField finalY = new JFormattedTextField(finalYFormatter);
				normal.add(finalY);		
				finalY.setBorder(null);
				if (data.getInDepthCursor().getFinalY() != null)
					finalY.setText(data.getInDepthCursor().getFinalY().toString());
				finalY.getDocument().addDocumentListener(new DocumentListener() {

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
							data.getInDepthCursor().setFinalY(Integer.parseInt(finalY.getText()));
						} catch (NumberFormatException e) {
						}
					}
				});
				finalY.setPreferredSize(new Dimension(50, 20));
				finalY.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						switch (e.getKeyCode()) {
						default:
							int updated;
							Integer yVal;
							break;
						case KeyEvent.VK_UP:
						case KeyEvent.VK_KP_UP:
							yVal = data.getInDepthCursor().getFinalY();
							if (yVal == null)
								break;
							if (yVal == Integer.MAX_VALUE)
								break;
							updated = yVal + 1;
							data.getInDepthCursor().setFinalY(updated);
							finalY.setValue(updated);
							break;
						case KeyEvent.VK_DOWN:
						case KeyEvent.VK_KP_DOWN:
							yVal = data.getInDepthCursor().getFinalY();
							if (yVal == null)
								break;
							if (yVal == Integer.MIN_VALUE)
								break;
							updated = yVal - 1;
							data.getInDepthCursor().setY(updated);
							finalY.setValue(updated);
							break;
						}
					}
				});
				JButton finalYClear = new JButton("[x]");
				normal.add(finalYClear);
				finalYClear.setFocusable(false);
				finalYClear.setMargin(new Insets(0, 0, 0, 0));
				finalYClear.setPreferredSize(Constants.X_BUTTON);
				finalYClear.addActionListener(l->{
					finalY.setValue(null);
					data.getInDepthCursor().setFinalY(null);
				});
				finalYPanel.add(finalYClear);
				finalYPanel.add(finalY);
				//center.add(yPanel);
				listenerFinal = (eX, eY) -> {
					finalXLabel.setText(String.format("X (%s):", MouseCoords.getInstance().getX().get()));
					finalYLabel.setText(String.format("Y (%s):", MouseCoords.getInstance().getY().get()));
				};
				MouseCoords.getInstance().registerListener(listenerFinal);
				/*
				 * 
				 */
				JButton cycle = new JButton(">");
				cycle.setFocusable(false);
				cycle.addActionListener(l->{
					switch (cycle.getText()) {
					case ">":
						cycleLabel.setText("Final Coords");
						center.remove(xPanel);
						center.remove(yPanel);
						center.add(finalXPanel);
						center.add(finalYPanel);
						cycle.setText("<");
						break;
					case "<":
						cycleLabel.setText("Initial Coords");
						center.remove(finalXPanel);
						center.remove(finalYPanel);
						center.add(xPanel);
						center.add(yPanel);
						cycle.setText(">");
						break;
					default:
						//NEVER
						break;
					}
					center.revalidate();
					center.repaint();
				});
				cyclePanel.add(cycle, BorderLayout.EAST);
				center.add(cyclePanel, center.getComponentCount()-2);
			}
		}
		this.add(center, BorderLayout.CENTER);
		JPanel left = new JPanel(new GridLayout(5, 1));
		left.setOpaque(false);

		// JPanel indexPanel = new JPanel();
		// indexPanel.setOpaque(false);
		JLabel indexLabel = new JLabel("#index");
		indexLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		left.add(indexLabel);
		index = new JComboBox<Integer>();
		index.setFocusable(false);
		for (JPanel jPanel : in) {
			index.addItem(this.in.indexOf(jPanel));
		}
		index.setSelectedIndex(this.in.indexOf(this));
		index.addActionListener(l -> {
			if (isRefreshActive.get())
				return;
			this.in.remove(this);
			this.in.add(index.getSelectedIndex(), this);
			this.parent.resetMoveControlDisplayButtons();
		});
		left.add(index);
		// left.add(indexPanel);
		// JButton help = new JButton("Help");
		JButton remove = new JButton("[x]");
		remove.setFocusable(false);
		remove.addActionListener(l -> {
			this.in.remove(this);
			if (in.isEmpty())
				this.parent.moveControlDisplay(true);
			else
				this.parent.moveControlDisplay(false);
			if (listener != null)
				MouseCoords.getInstance().unregisterListener(listener);
			if (listenerFinal != null)
				MouseCoords.getInstance().unregisterListener(listenerFinal);
			this.parent.revalidate();
			this.parent.repaint();
		});
		left.add(remove);
		// left.setPreferredSize(new Dimension(50, 151));
		this.add(left, BorderLayout.EAST);
	}

	public void refreshIndex() {
		// index.setSelectedIndex(-1);
		// System.out.println(index.getItemCount());
		isRefreshActive.set(true);
		index.removeAllItems();
		for (JPanel jPanel : in) {
			index.addItem(in.indexOf(jPanel));
		}
		index.setSelectedIndex(in.indexOf(this));
		isRefreshActive.set(false);
	}

	public void setMode(Type type) {
		switch (type) {
			case NORMAL:
				for (Component elem : var) {
					elem.setVisible(false);
				}
				for (Component elem : normal) {
					elem.setVisible(true);
				}
				break;
			case VAR:
				for (Component elem : normal) {
					elem.setVisible(false);
				}
				for (Component elem : var) {
					elem.setVisible(true);
				}
				break;
			default:
				break;
		}
		
	}
	
	public ControlData getControlData() {
		return data;
	}
}
