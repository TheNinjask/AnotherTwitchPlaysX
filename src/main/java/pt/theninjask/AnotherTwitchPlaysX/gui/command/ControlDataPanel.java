package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlType;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.util.JComboItem;
import pt.theninjask.AnotherTwitchPlaysX.util.MouseCoords;

public class ControlDataPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ControlData data;

	private List<ControlDataPanel> in;

	private JComboBox<Integer> index;

	private JPanel parent;

	private JPanel defaultPanel;

	public ControlDataPanel(ControlData newData, List<ControlDataPanel> in, JPanel parent, JPanel defaultPanel) {
		this.data = newData;
		this.parent = parent;
		this.defaultPanel = defaultPanel;
		this.in = in;
		this.in.add(this);
		this.setLayout(new BorderLayout());

		JPanel center;
		switch (data.getType()) {
		case KEY:
			center = new JPanel(new GridLayout(3, 1));
			break;
		default:
			center = new JPanel(new GridLayout(5, 1));
			break;
		}

		JPanel inputPanel = new JPanel(new FlowLayout());
		JLabel inputLabel = new JLabel(data.getType() == ControlType.KEY ? "Key" : "Button");
		inputPanel.add(inputLabel);
		switch (data.getType()) {
		case KEY:
			JButton key = new JButton("None");
			key.addActionListener(l -> {
				JPanel pressPanel = new JPanel(new GridLayout(2, 1));
				JLabel press = new JLabel(String.format("Press a key (Current: %s)",
						data.getKey() == null ? "None" : KeyEvent.getKeyText(data.getKey())));
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
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON1_DOWN_MASK, "Left"));
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON2_DOWN_MASK, "Right"));
			opt.addItem(new JComboItem<Integer>(InputEvent.BUTTON3_DOWN_MASK, "Middle"));
			opt.addActionListener(l->{
				this.data.setKey(opt.getItemAt(opt.getSelectedIndex()).get());
			});
			inputPanel.add(opt);
			break;
		case MOUSE_MOV:
		case MOUSE_WHEEL:
		default:
			JLabel na = new JLabel("N/A");
			inputPanel.add(na);
			break;
		}

		center.add(inputPanel);

		NumberFormatter secFormatter = new NumberFormatter(NumberFormat.getInstance());
		secFormatter.setValueClass(Integer.class);
		secFormatter.setMinimum(0);
		secFormatter.setMaximum(Integer.MAX_VALUE);
		secFormatter.setAllowsInvalid(false);

		JPanel durationPanel = new JPanel();
		JLabel durationLabel = new JLabel("Duration (sec):");
		durationPanel.add(durationLabel);
		JTextField duration = new JFormattedTextField(secFormatter);
		duration.setText(data.getDuration().toString());
		duration.addActionListener(l->{
			data.setDuration(Integer.parseInt(duration.getText()));
		});
		durationPanel.add(duration);
		center.add(durationPanel);

		JPanel aftermathPanel = new JPanel();
		JLabel aftermathLabel = new JLabel("Aftermath (sec):");
		aftermathPanel.add(aftermathLabel);
		JTextField aftermath = new JFormattedTextField(secFormatter);
		aftermath.setText(data.getDuration().toString());
		aftermath.addActionListener(l->{
			data.setAftermathDelay(Integer.parseInt(aftermath.getText()));
		});
		aftermathPanel.add(aftermath);
		center.add(aftermathPanel);

		if (data.getType() == ControlType.MOUSE_CLICK || data.getType() == ControlType.MOUSE_MOV) {
			NumberFormatter coordsFormatter = new NumberFormatter(NumberFormat.getInstance());
			coordsFormatter.setValueClass(Integer.class);
			coordsFormatter.setMinimum(Integer.MIN_VALUE);
			coordsFormatter.setMaximum(Integer.MAX_VALUE);
			coordsFormatter.setAllowsInvalid(false);
			
			JPanel xPanel = new JPanel();
			JLabel xLabel = new JLabel(String.format("X (%s) stay:", MouseCoords.getInstance().getX().get()));
			xPanel.add(xLabel);
			JTextField x = new JFormattedTextField(coordsFormatter);
			x.setText(data.getInDepthCursor().getX().toString());
			x.addActionListener(l->{
				data.getInDepthCursor().setX(Integer.parseInt(x.getText()));
			});
			xPanel.add(x);
			center.add(xPanel);
			
			JPanel yPanel = new JPanel();
			JLabel yLabel = new JLabel(String.format("Y (%s) stay:", MouseCoords.getInstance().getY().get()));
			yPanel.add(yLabel);
			JTextField y = new JFormattedTextField(coordsFormatter);
			y.setText(data.getInDepthCursor().getY().toString());
			y.addActionListener(l->{
				data.getInDepthCursor().setY(Integer.parseInt(y.getText()));
			});
			yPanel.add(y);
			center.add(yPanel);
			
			MouseCoords.getInstance().registerListener((eX, eY) -> {
				xLabel.setText(String.format("X (%s) stay:", MouseCoords.getInstance().getX().get()));
				yLabel.setText(String.format("Y (%s) stay:", MouseCoords.getInstance().getY().get()));
			});

		}
		this.add(center, BorderLayout.CENTER);

		JPanel left = new JPanel(new GridLayout(3, 1));

		JPanel indexPanel = new JPanel();
		JLabel indexLabel = new JLabel("#index");
		indexPanel.add(indexLabel);
		index = new JComboBox<Integer>();
		for (JPanel jPanel : in) {
			index.addItem(in.indexOf(jPanel));
		}
		index.setSelectedIndex(in.indexOf(this));
		index.addActionListener(l -> {
			this.in.remove(this);
			this.in.add(index.getSelectedIndex(), this);
			in.forEach(e -> {
				e.refreshIndex();
			});
		});
		indexPanel.add(index);
		left.add(indexPanel);
		// JButton help = new JButton("Help");
		JButton remove = new JButton("Remove");
		remove.addActionListener(l -> {
			this.parent.removeAll();
			int currIndex = this.in.indexOf(this);
			this.in.remove(this);
			if (this.in.size() > 0)
				this.parent.add(this.in.get(currIndex > 0 ? currIndex - 1 : currIndex));
			else
				this.parent.add(this.defaultPanel);
			this.parent.revalidate();
			this.parent.repaint();
		});
		left.add(remove);

		this.add(left, BorderLayout.EAST);
	}

	public void refreshIndex() {
		index = new JComboBox<Integer>();
		for (JPanel jPanel : in) {
			index.addItem(in.indexOf(jPanel));
		}
		index.setSelectedIndex(in.indexOf(this));
	}

	public ControlData getControlData() {
		return data;
	}
}
