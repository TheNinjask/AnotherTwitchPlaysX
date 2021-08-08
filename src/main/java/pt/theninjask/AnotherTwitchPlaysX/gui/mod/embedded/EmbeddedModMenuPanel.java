package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import java.awt.GridLayout;
import java.awt.Image;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.mod.EmbeddedModLoadEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.JComboItem;

public class EmbeddedModMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static EmbeddedModMenuPanel singleton = new EmbeddedModMenuPanel();
	
	private static final int ICON_WIDTH = 80;
	
	private ATPXMod[] mods = {new ChangeStopShortCutMod(), new TestChatCommandsMod(), new StringChatCommandsMod(), new TestCommandsMod()};
	
	private EmbeddedModMenuPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", EmbeddedModMenuPanel.class.getSimpleName()));
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(4, 1));
		
		JLabel title = new JLabel(DataManager.getLanguage().getEmbeddedModMenu().getLabel());
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		ImageIcon icon = new ImageIcon(this.getClass()
				.getResource("/pt/theninjask/AnotherTwitchPlaysX/resource/image/ninjask.gif"));
		icon.setImage(icon.getImage().getScaledInstance(ICON_WIDTH, icon.getIconHeight()*ICON_WIDTH/icon.getIconWidth(), Image.SCALE_DEFAULT));
		title.setIcon(icon);
		this.add(title);
		
		JLabel warn = new JLabel(DataManager.getLanguage().getEmbeddedModMenu().getWarning());
		warn.setHorizontalAlignment(JLabel.CENTER);
		warn.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		
		this.add(warn);
		
		JPanel dummy = new JPanel();
		dummy.setOpaque(false);
		
		JComboBox<JComboItem<ATPXMod>> mods = new JComboBox<JComboItem<ATPXMod>>();
		for (ATPXMod atpxMod : this.mods) {
			mods.addItem(new JComboItem<ATPXMod>(atpxMod, atpxMod.getClass().getSimpleName()));
		}
		mods.setSelectedIndex(-1);
		mods.addActionListener(l->{
			ATPXMod mod = mods.getItemAt(mods.getSelectedIndex()).get();
			
			EmbeddedModLoadEvent event = new EmbeddedModLoadEvent(mod);
			EventManager.triggerEvent(event);
			if(event.isCancelled())
				return;
			
			mod.refresh();
			if(mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
				if(mod.getClass().getAnnotation(ATPXModProps.class).popout())
					new PopOutFrame(mod.getJPanelInstance());
				else
					MainFrame.replacePanel(mod.getJPanelInstance());
			if(mod.getClass().getAnnotation(ATPXModProps.class).keepLoaded())
				MainMenuPanel.getInstance().setMod(mod);
			mods.setSelectedIndex(-1);
		});
		dummy.add(mods);
		this.add(dummy);
				
		dummy = new JPanel();
		dummy.setOpaque(false);
		
		JButton back = new JButton(DataManager.getLanguage().getEmbeddedModMenu().getGoBack());
		back.addActionListener(l->{
			MainFrame.replacePanel(MainMenuPanel.getInstance());
		});
		dummy.add(back);
		this.add(dummy);
		
		MainMenuPanel.getInstance().getModButton().setText(DataManager.getLanguage().getMainMenu().getMod());
	}
	
	public static EmbeddedModMenuPanel getInstance() {
		return singleton;
	}
}
