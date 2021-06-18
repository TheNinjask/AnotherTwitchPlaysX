package pt.theninjask.AnotherTwitchPlaysX.gui.command.util;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.AllCommandPanel;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class StringToKeyCodePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static StringToKeyCodePanel singleton = new StringToKeyCodePanel();
	
	private JButton back;
	
	private JButton reset;

	private JTableStringToKeyCode table;

	private JScrollPane up;

	private JPanel down;
	
	private StringToKeyCodePanel() {
		this.setLayout(new BorderLayout());
		
		up = new JScrollPane();
		up.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		table = new JTableStringToKeyCode();
		ArrayList<String> tmp = new ArrayList<String>(Constants.STRING_TO_KEYCODE.keySet());
		Collections.sort(tmp);
		table.clearAndSet(tmp);
		up.setViewportView(table);
		up.setFocusable(false);
		up.setEnabled(false);
		//left.add(list);
		
		this.add(up, BorderLayout.CENTER);
		
		down = new JPanel(new BorderLayout());
		
		back = new JButton("Back");
		back.addActionListener(l->{
			MainFrame.replacePanel(AllCommandPanel.getInstance());
		});
		down.add(back, BorderLayout.CENTER);
		reset = new JButton("Reset");
		reset.addActionListener(l->{
			Constants.resetStringToKeyCode();
			ArrayList<String> tmpy = new ArrayList<String>(Constants.STRING_TO_KEYCODE.keySet());
			Collections.sort(tmpy);
			table.clearAndSet(tmpy);
		});
		down.add(reset, BorderLayout.WEST);
		this.add(down, BorderLayout.SOUTH);
	}
	
	public static StringToKeyCodePanel getInstance() {
		return singleton;
	}

	public JButton getBack() {
		return back;
	}
	
	public JButton getReset() {
		return reset;
	}

	public JTableStringToKeyCode getTable() {
		return table;
	}

	public JScrollPane getUp() {
		return up;
	}

	public JPanel getDown() {
		return down;
	}
}
