package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.util.JTableCommand;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class CommandPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static CommandPanel singleton = new CommandPanel();
	
	private JPanel currentPanel = newOrLoadPanel();
	
	private boolean isSaved;
	
	private boolean isOn;
	
	private JPanel mainCommandPanel;
	
	private JTableCommand table;
	
	private CommandPanel() {
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new BorderLayout());
		this.add(currentPanel, BorderLayout.CENTER);
		mainCommandPanel();
		this.isSaved = true;
		this.isOn = false;
	}
	
	public static CommandPanel getInstance() {
		return singleton;
	}
	
	public boolean isSaved() {
		return isSaved;
	}
	
	public boolean isOn() {
		return isOn;
	}
	
	private JPanel newOrLoadPanel() {
		JPanel tmp = new JPanel(new GridLayout(1,2,75,0));
		tmp.setFocusable(false);
		tmp.setOpaque(false);
		
		JButton create = new JButton("New");
		create.setFocusable(false);
		create.setOpaque(false);
		create.addActionListener(l->{
			replacePanel(mainCommandPanel);
		});
		tmp.add(create);
		
		JPanel right = new JPanel(new GridLayout(2,1,0,100));
		right.setFocusable(false);
		right.setOpaque(false);
		
		JButton load = new JButton("Load");
		load.setFocusable(false);
		load.setOpaque(false);
		right.add(load);
		
		JButton back = new JButton("Back");
		back.setFocusable(false);
		back.setOpaque(false);
		back.addActionListener(l->{
			MainFrame.getInstance().replacePanel(MainMenuPanel.getInstance());
		});
		right.add(back);
		
		tmp.add(right);
		return tmp;
	}

	private JPanel mainCommandPanel() {
		mainCommandPanel = new JPanel(new BorderLayout());
		mainCommandPanel.setFocusable(false);
		mainCommandPanel.setOpaque(false);
		
		JLabel title = new JLabel("List of commands");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font(title.getFont().getName(), title.getFont().getStyle(), 25));
		title.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		title.setFocusable(false);
		mainCommandPanel.add(title, BorderLayout.NORTH);
		
		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);
		
		JScrollPane left = new JScrollPane();
		
		table = new JTableCommand();
		
		
		for(int i=0; i<25;i++)
			table.addRow(new CommandData());
		
		
		left.setViewportView(table);
		left.setFocusable(false);
		left.setEnabled(false);
		//left.add(list);
		
		body.add(left, BorderLayout.CENTER);
		
		JPanel right = new JPanel(new GridLayout(7, 1));
		
		right.setOpaque(false);
		
		JButton insert = new JButton("Insert");
		insert.setFocusable(false);
		right.add(insert);
		
		JButton start = new JButton("Start");
		start.setFocusable(false);
		start.addActionListener(l->{
			if(isOn) {
				isOn = false;
				start.setText("Start");
			}else {
				isOn = true;
				start.setText("Stop");
			}
		});
		right.add(start);
		
		JButton help = new JButton("Help");
		help.setFocusable(false);
		right.add(help);
		
		JButton popout = new JButton("Pop Out");
		popout.setFocusable(false);
		popout.addActionListener(l->{
			new PopOutFrame(left, MainFrame.getInstance()).addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent event) {
					body.add(left, BorderLayout.CENTER);
					singleton.revalidate();
					singleton.repaint();
				}
			});
		});
		right.add(popout);
		
		JButton save = new JButton("Save");
		save.setFocusable(false);
		right.add(save);
		
		JButton load = new JButton("Load");
		load.setFocusable(false);
		right.add(load);
		
		JButton back = new JButton("Back");
		back.setFocusable(false);
		back.addActionListener(l->{
			MainFrame.getInstance().replacePanel(MainMenuPanel.getInstance());
		});
		right.add(back);
		
		body.add(right, BorderLayout.EAST);
		
		mainCommandPanel.add(body, BorderLayout.CENTER);
		
		return mainCommandPanel;
	}
	
	private void replacePanel(JPanel newPanel) {
		this.remove(currentPanel);
		this.currentPanel = newPanel;
		this.add(newPanel, BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}
}
