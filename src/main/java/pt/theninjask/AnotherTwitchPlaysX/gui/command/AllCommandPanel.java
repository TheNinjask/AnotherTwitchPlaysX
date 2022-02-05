package pt.theninjask.AnotherTwitchPlaysX.gui.command;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.ColorThemeUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.command.AllCommandPanelBackEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.command.AllCommandPanelLoadEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.command.AllCommandPanelNewEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.command.AllCommandPanelPopoutEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.command.AllCommandPanelSaveEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.command.AllCommandPanelInsertEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.command.AllCommandPanelKeycodesEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.util.JTableCommand;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.util.StringToKeyCodePanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class AllCommandPanel extends JPanel{

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

	private JLabel title;

	private JButton firstLoad;

	private JButton firstBack;

	private AllCommandPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", AllCommandPanel.class.getSimpleName()));
		this.setBackground(DataManager.getTheme().getBackground());
		this.setLayout(new BorderLayout());
		this.add(currentPanel, BorderLayout.CENTER);
		mainCommandPanel();
		//DataManager.registerLangEvent(this);
		EventManager.registerEventListener(this);
	}

	public static AllCommandPanel getInstance() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getInstance()", AllCommandPanel.class.getSimpleName()));
		singleton.refreshTable();
		return singleton;
	}

	private JPanel newOrLoadPanel() {
		JPanel tmp = new JPanel(new GridLayout(1, 2, 75, 0));
		tmp.setFocusable(false);
		tmp.setOpaque(false);

		create = new JButton(DataManager.getLanguage().getAllCommand().getCreate());
		create.setFocusable(false);
		create.setOpaque(false);
		create.addActionListener(l -> {
			AllCommandPanelNewEvent event = new AllCommandPanelNewEvent();
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			replacePanel(mainCommandPanel);
			DataManager.setCommands(new ArrayList<CommandData>());
		});
		tmp.add(create);

		JPanel right = new JPanel(new GridLayout(2, 1, 0, 100));
		right.setFocusable(false);
		right.setOpaque(false);

		firstLoad = new JButton(DataManager.getLanguage().getAllCommand().getLoad());
		firstLoad.setFocusable(false);
		firstLoad.setOpaque(false);
		firstLoad.addActionListener(l -> {
			try {
				AllCommandPanelLoadEvent event = new AllCommandPanelLoadEvent(false);
				EventManager.triggerEvent(event);
				if(event.isCancelled())
					return;
				File file = Constants.showOpenFile(new FileNameExtensionFilter("JSON", "json"), this, Paths.get(Constants.SAVE_PATH, Constants.CMD_FOLDER).toFile());
				if (file != null) {
					ObjectMapper mapper = new ObjectMapper();
					List<CommandData> commands = mapper.readValue(file,
							mapper.getTypeFactory().constructCollectionType(List.class, CommandData.class));
					DataManager.setCommands(commands);
					refreshTable();
					replacePanel(mainCommandPanel);
				}
			} catch (IOException e) {
				Constants.showExpectedExceptionDialog(e);
			}
		});
		right.add(firstLoad);

		firstBack = new JButton(DataManager.getLanguage().getAllCommand().getBack());
		firstBack.setFocusable(false);
		firstBack.setOpaque(false);
		firstBack.addActionListener(l -> {
			AllCommandPanelBackEvent event = new AllCommandPanelBackEvent(false);
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			MainFrame.replacePanel(MainMenuPanel.getInstance());
		});
		right.add(firstBack);

		tmp.add(right);
		return tmp;
	}

	private JPanel mainCommandPanel() {
		mainCommandPanel = new JPanel(new BorderLayout());
		mainCommandPanel.setFocusable(false);
		mainCommandPanel.setOpaque(false);

		title = new JLabel(DataManager.getLanguage().getAllCommand().getListOfCmds());
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font(title.getFont().getName(), title.getFont().getStyle(), 25));
		title.setForeground(DataManager.getTheme().getFont());
		title.setFocusable(false);
		mainCommandPanel.add(title, BorderLayout.NORTH);

		JPanel body = new JPanel(new BorderLayout());
		body.setOpaque(false);

		JScrollPane left = new JScrollPane();
		left.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		table = new JTableCommand();

		// for(int i=0; i<25;i++)
		// table.addRow(new CommandData());

		left.setViewportView(table);
		left.setFocusable(false);
		left.setEnabled(false);
		// left.add(list);

		body.add(left, BorderLayout.CENTER);

		JPanel right = new JPanel(new GridLayout(7, 1));

		right.setOpaque(false);

		insert = new JButton(DataManager.getLanguage().getAllCommand().getInsert());
		insert.setFocusable(false);
		insert.addActionListener(l -> {
			// table.addRow(new CommandData());
			
			AllCommandPanelInsertEvent event = new AllCommandPanelInsertEvent();
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			
			MainFrame.replacePanel(new CommandPanel());
		});
		right.add(insert);
		keycodes = new JButton(DataManager.getLanguage().getAllCommand().getCodes());
		keycodes.setFocusable(false);
		keycodes.addActionListener(l -> {
			
			AllCommandPanelKeycodesEvent event = new AllCommandPanelKeycodesEvent();
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			
			MainFrame.replacePanel(StringToKeyCodePanel.getInstance());
		});
		right.add(keycodes);
		help = new JButton(DataManager.getLanguage().getAllCommand().getHelp());
		help.setEnabled(false);
		help.setFocusable(false);
		right.add(help);

		popout = new JButton(DataManager.getLanguage().getAllCommand().getPopOut());
		popout.setFocusable(false);
		popout.addActionListener(l -> {
			
			AllCommandPanelPopoutEvent event = new AllCommandPanelPopoutEvent();
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			PopOutFrame tmp = new PopOutFrame(left, MainFrame.getInstance());
			tmp.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent event) {
					body.add(left, BorderLayout.CENTER);
					singleton.revalidate();
					singleton.repaint();
				}
			});
			tmp.setVisible(true);
		});
		right.add(popout);

		save = new JButton(DataManager.getLanguage().getAllCommand().getSave());
		save.setFocusable(false);
		save.addActionListener(l -> {
			AllCommandPanelSaveEvent event = new AllCommandPanelSaveEvent();
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			File file = Constants.showSaveFile(new File("commands.json"), new FileNameExtensionFilter("JSON", "json"),
					this, Paths.get(Constants.SAVE_PATH, Constants.CMD_FOLDER).toFile());
			if (file != null) {
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JTextField tmp = new JTextField(String
							.format(DataManager.getLanguage().getAllCommand().getSaveMsg(), file.getAbsolutePath()));
					tmp.setEditable(false);
					tmp.setBorder(null);
					tmp.setOpaque(false);
					tmp.setForeground(DataManager.getTheme().getFont());
					objectMapper.writeValue(file, DataManager.getCommands());
					Constants.showCustomColorMessageDialog(null, tmp,
							DataManager.getLanguage().getAllCommand().getSaveTitle(), JOptionPane.INFORMATION_MESSAGE,
							null, DataManager.getTheme().getBackground());
				} catch (IOException e) {
					Constants.showExceptionDialog(e);
				}
			}
		});
		right.add(save);

		load = new JButton(DataManager.getLanguage().getAllCommand().getLoad());
		load.setFocusable(false);
		load.addActionListener(l -> {
			AllCommandPanelLoadEvent event = new AllCommandPanelLoadEvent(true);
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			try {
				File file = Constants.showOpenFile(new FileNameExtensionFilter("JSON", "json"), this, Paths.get(Constants.SAVE_PATH, Constants.CMD_FOLDER).toFile());
				if (file != null) {
					ObjectMapper mapper = new ObjectMapper();
					List<CommandData> tmp = mapper.readValue(file,
							mapper.getTypeFactory().constructCollectionType(List.class, CommandData.class));
					DataManager.setCommands(tmp);
					refreshTable();
				}
			} catch (IOException e) {
				Constants.showExpectedExceptionDialog(e);
			}
		});
		right.add(load);

		back = new JButton(DataManager.getLanguage().getAllCommand().getBack());
		back.setFocusable(false);
		back.addActionListener(l -> {
			AllCommandPanelBackEvent event = new AllCommandPanelBackEvent(true);
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
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
		if (table != null)
			this.table.clearAndSet(DataManager.getCommands());
	}

	//@Handler
	public void updateLang(LanguageUpdateEvent event) {
		Lang session = event.getLanguage();
		if(session == null)
			return;
		
		create.setText(session.getAllCommand().getCreate());
		firstLoad.setText(session.getAllCommand().getLoad());
		firstBack.setText(session.getAllCommand().getBack());

		title.setText(session.getAllCommand().getListOfCmds());
		insert.setText(session.getAllCommand().getInsert());
		keycodes.setText(session.getAllCommand().getCodes());
		help.setText(session.getAllCommand().getHelp());
		popout.setText(session.getAllCommand().getPopOut());
		save.setText(session.getAllCommand().getSave());
		load.setText(session.getAllCommand().getLoad());
		back.setText(session.getAllCommand().getBack());
		table.updateLang(event);
	}
	
	@Handler
	public void updateTheme(ColorThemeUpdateEvent event) {
		if(event.getTheme()!=null) {
			this.setBackground(event.getTheme().getBackground());
			title.setForeground(event.getTheme().getFont());
		}
	}

}
