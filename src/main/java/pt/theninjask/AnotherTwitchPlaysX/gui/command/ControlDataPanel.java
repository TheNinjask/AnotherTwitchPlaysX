package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
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
		case MOUSE_CLICK:
			JComboBox<JComboItem<Integer>> opt = new JComboBox<JComboItem<Integer>>();
			opt.setFocusable(false);
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON1_DOWN_MASK, "Left"));
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON2_DOWN_MASK, "Right"));
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON3_DOWN_MASK, "Middle"));
			opt.addActionListener(l -> {
				this.data.setKey(opt.getItemAt(opt.getSelectedIndex()).get());
			});
			inputPanel.add(opt);
			break;
		case MOUSE_MOV:
		case MOUSE_WHEEL:
		default:
			JLabel na = new JLabel("N/A");
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
		;
		format.setGroupingUsed(false);

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

		if ((data.getType() == ControlType.MOUSE_CLICK || data.getType() == ControlType.MOUSE_MOV)
				&& data.getInDepthCursor() != null) {
			NumberFormatter xFormatter = new NumberFormatter(format);
			xFormatter.setValueClass(Integer.class);
			xFormatter.setMinimum(Integer.MIN_VALUE);
			xFormatter.setMaximum(Integer.MAX_VALUE);
			xFormatter.setAllowsInvalid(false);

			JPanel xPanel = new JPanel();
			xPanel.setOpaque(false);
			JLabel xLabel = new JLabel(String.format("X (%s) stay:", MouseCoords.getInstance().getX().get()));
			xLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			xPanel.add(xLabel);
			JTextField x = new JFormattedTextField(xFormatter);
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
						x.setText(Integer.toString(updated));
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
						x.setText(Integer.toString(updated));
						break;
					}
				}
			});
			xPanel.add(x);
			center.add(xPanel);

			JPanel yPanel = new JPanel();
			yPanel.setOpaque(false);
			JLabel yLabel = new JLabel(String.format("Y (%s) stay:", MouseCoords.getInstance().getY().get()));
			yLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			yPanel.add(yLabel);
			NumberFormatter yFormatter = new NumberFormatter(format);
			yFormatter.setValueClass(Integer.class);
			yFormatter.setMinimum(Integer.MIN_VALUE);
			yFormatter.setMaximum(Integer.MAX_VALUE);
			yFormatter.setAllowsInvalid(false);
			JTextField y = new JFormattedTextField(yFormatter);
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
						y.setText(Integer.toString(updated));
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
						y.setText(Integer.toString(updated));
						break;
					}
				}
			});
			yPanel.add(y);
			center.add(yPanel);
			listener = (eX, eY) -> {
				xLabel.setText(String.format("X (%s) stay:", MouseCoords.getInstance().getX().get()));
				yLabel.setText(String.format("Y (%s) stay:", MouseCoords.getInstance().getY().get()));
			};
			MouseCoords.getInstance().registerListener(listener);

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
		JButton remove = new JButton("[X]");
		remove.setFocusable(false);
		remove.addActionListener(l -> {
			this.in.remove(this);
			if (in.isEmpty())
				this.parent.moveControlDisplay(true);
			else
				this.parent.moveControlDisplay(false);
			if (listener != null)
				MouseCoords.getInstance().unregisterListener(listener);
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

	public ControlData getControlData() {
		return data;
	}
}
