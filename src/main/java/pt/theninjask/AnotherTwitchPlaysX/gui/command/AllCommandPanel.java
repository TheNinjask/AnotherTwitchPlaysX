package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.util.JTableCommand;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.util.StringToKeyCodePanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class AllCommandPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static AllCommandPanel singleton = new AllCommandPanel();
	
	private JPanel currentPanel = newOrLoadPanel();
	
	private JPanel mainCommandPanel;
	
	private JButton create;
	
	private JButton load;
	
	private JButton back;
	
	private JTableCommand table;

	private JButton insert;

	private JButton help;

	private JButton popout;

	private JButton save;

	private JButton keycodes;

	private AllCommandPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", AllCommandPanel.class.getSimpleName()));
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new BorderLayout());
		this.add(currentPanel, BorderLayout.CENTER);
		mainCommandPanel();
	}
	
	public static AllCommandPanel getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", AllCommandPanel.class.getSimpleName()));
		singleton.refreshTable();
		return singleton;
	}
	
	private JPanel newOrLoadPanel() {
		JPanel tmp = new JPanel(new GridLayout(1,2,75,0));
		tmp.setFocusable(false);
		tmp.setOpaque(false);
		
		create = new JButton("New");
		create.setFocusable(false);
		create.setOpaque(false);
		create.addActionListener(l->{
			replacePanel(mainCommandPanel);
			DataManager.getInstance().setCommands(new ArrayList<CommandData>());
		});
		tmp.add(create);
		
		JPanel right = new JPanel(new GridLayout(2,1,0,100));
		right.setFocusable(false);
		right.setOpaque(false);
		
		load = new JButton("Load");
		load.setFocusable(false);
		load.setOpaque(false);
		load.addActionListener(l->{
			try {
				File file = Constants.showOpenFile(new FileNameExtensionFilter("JSON", "json"), this);
				if(file!=null) {
					ObjectMapper mapper = new ObjectMapper();
					List<CommandData> commands = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, CommandData.class));
					DataManager.getInstance().setCommands(commands);
					refreshTable();
					replacePanel(mainCommandPanel);
				}
			} catch (IOException e) {
				Constants.showExpectedExceptionDialog(e);
			}
		});
		right.add(load);
		
		back = new JButton("Back");
		back.setFocusable(false);
		back.setOpaque(false);
		back.addActionListener(l->{
			MainFrame.replacePanel(MainMenuPanel.getInstance());
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
		left.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		table = new JTableCommand();
		
		
		//for(int i=0; i<25;i++)
		//	table.addRow(new CommandData());
		
		
		left.setViewportView(table);
		left.setFocusable(false);
		left.setEnabled(false);
		//left.add(list);
		
		body.add(left, BorderLayout.CENTER);
		
		JPanel right = new JPanel(new GridLayout(7, 1));
		
		right.setOpaque(false);
		
		insert = new JButton("Insert");
		insert.setFocusable(false);
		insert.addActionListener(l->{
			//table.addRow(new CommandData());
			MainFrame.replacePanel(new CommandPanel());
		});
		right.add(insert);
		keycodes = new JButton("Codes");
		keycodes.setFocusable(false);
		keycodes.addActionListener(l->{
			MainFrame.replacePanel(StringToKeyCodePanel.getInstance());
		});
		right.add(keycodes);
		help = new JButton("Help");
		help.setEnabled(false);
		help.setFocusable(false);
		right.add(help);
		
		popout = new JButton("Pop Out");
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
		
		save = new JButton("Save");
		save.setFocusable(false);
		save.addActionListener(l->{
			File file = Constants.showSaveFile(new File("commands.json"),new FileNameExtensionFilter("JSON", "json"), this);
			if(file!=null){
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JTextField tmp = new JTextField(String.format("Commands saved in %s", file.getAbsolutePath()));
					tmp.setEditable(false);
					tmp.setBorder(null);
					tmp.setOpaque(false);
					tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
					objectMapper.writeValue(file, DataManager.getInstance().getCommands());
					Constants.showCustomColorMessageDialog(null, 
							tmp, 
							"Saving Commands", JOptionPane.INFORMATION_MESSAGE, null, Constants.TWITCH_COLOR);			
				} catch (IOException e) {
					Constants.showExceptionDialog(e);
				}
			}
		});
		right.add(save);
		
		load = new JButton("Load");
		load.setFocusable(false);
		load.addActionListener(l->{
			try {
			File file = Constants.showOpenFile(new FileNameExtensionFilter("JSON", "json"), this);
			if(file!=null) {
				ObjectMapper mapper = new ObjectMapper();
				List<CommandData> tmp = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, CommandData.class));
				DataManager.getInstance().setCommands(tmp);
				refreshTable();
			}
		} catch (IOException e) {
			Constants.showExpectedExceptionDialog(e);
		}
		});
		right.add(load);
		
		back = new JButton("Back");
		back.setFocusable(false);
		back.addActionListener(l->{
			MainFrame.replacePanel(MainMenuPanel.getInstance());
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
	
	private void refreshTable() {
		if(table!=null)
			this.table.clearAndSet(DataManager.getInstance().getCommands());
	}
	
	public JPanel getCurrentPanel() {
		return currentPanel;
	}
	
	public void setCurrentPanel(JPanel newer) {
		this.currentPanel = newer;
	}
	
	public JButton getCreate() {
		return create;
	}

	public JButton getLoad() {
		return load;
	}

	public JButton getBack() {
		return back;
	}

	public JTableCommand getTable() {
		return table;
	}

	public JPanel getMainCommandPanel() {
		return mainCommandPanel;
	}

	public JButton getInsert() {
		return insert;
	}

	public JButton getHelp() {
		return help;
	}

	public JButton getPopout() {
		return popout;
	}

	public JButton getSave() {
		return save;
	}

	public JButton getKeycodes() {
		return keycodes;
	}
	
}
