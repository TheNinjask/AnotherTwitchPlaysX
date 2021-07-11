package pt.theninjask.AnotherTwitchPlaysX.gui.login;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class MainLoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static MainLoginPanel singleton = new MainLoginPanel();

	private static final int ICON_WIDTH = 80;

	private AtomicBoolean isTwitchSet = new AtomicBoolean(false);

	private AtomicBoolean isYoutubeSet = new AtomicBoolean(false);

	private JButton start;

	private JButton connectTwitch;

	private JButton clearTwitch;

	private JButton connectYoutube;

	private JButton clearYoutube;

	private MainLoginPanel() {
		this.setLayout(new BorderLayout());
		this.setBackground(Constants.TWITCH_COLOR);

		JPanel menu = new JPanel(new GridLayout(0, 2));
		menu.setOpaque(false);
		menu.add(getTwitchPanel());
		menu.add(getYoutubePanel());
		this.add(menu);

		if (DataManager.getTwitchSession() != null) {
			isTwitchSet.set(true);
			connectTwitch.setText(DataManager.getLanguage().getMainLogin().getConnected());
			clearTwitch.setEnabled(true);
		}
		if (DataManager.getYouTubeSession() != null) {
			isYoutubeSet.set(true);
			connectYoutube.setText(DataManager.getLanguage().getMainLogin().getConnected());
			clearYoutube.setEnabled(true);
		}
		DataManager.registerDataManagerEvent(this);
		start = new JButton(DataManager.getLanguage().getMainLogin().getStart());
		start.setFocusable(false);
		start.addActionListener(l -> {
			TwitchPlayer.getInstance().setSession(DataManager.getTwitchSession());
			YouTubePlayer.getInstance().setSession(DataManager.getYouTubeSession());
			if (TwitchPlayer.getInstance().hasRequired() || YouTubePlayer.getInstance().hasRequired())
				MainFrame.replacePanel(MainMenuPanel.getInstance());
		});
		if (!isTwitchSet.get() && !isYoutubeSet.get())
			start.setEnabled(false);
		this.add(start, BorderLayout.SOUTH);

	}

	private void refresh() {
		if (!isTwitchSet.get() && !isYoutubeSet.get())
			start.setEnabled(false);
		else
			start.setEnabled(true);
	}

	public static MainLoginPanel getInstance() {
		singleton.refresh();
		return singleton;
	}

	private JPanel getTwitchPanel() {
		JPanel twitch = new JPanel(new GridLayout(2, 0));
		twitch.setOpaque(false);
		JLabel title = new JLabel();
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		ImageIcon icon = new ImageIcon(Constants.TWITCH_LOGO_PATH);
		icon.setImage(icon.getImage().getScaledInstance(ICON_WIDTH,
				icon.getIconHeight() * ICON_WIDTH / icon.getIconWidth(), Image.SCALE_DEFAULT));
		title.setIcon(icon);
		twitch.add(title);
		JPanel demo = new JPanel();
		demo.setOpaque(false);
		JPanel buttons = new JPanel(new BorderLayout());
		connectTwitch = new JButton(DataManager.getLanguage().getMainLogin().getConnect());
		connectTwitch.setFocusable(false);
		connectTwitch.addActionListener(l -> {
			MainFrame.replacePanel(TwitchLoginPanel.getInstance());
		});
		buttons.add(connectTwitch);
		clearTwitch = new JButton(DataManager.getLanguage().getMainLogin().getClear());
		clearTwitch.setFocusable(false);
		clearTwitch.setEnabled(false);
		clearTwitch.setMargin(new Insets(2, 2, 2, 2));
		clearTwitch.addActionListener(l -> {
			DataManager.setTwitchSession(null);
			onUpdateTwitch(null);
		});
		buttons.add(clearTwitch, BorderLayout.EAST);
		demo.add(buttons);
		twitch.add(demo);
		return twitch;
	}

	private JPanel getYoutubePanel() {
		JPanel youtube = new JPanel(new GridLayout(2, 0));
		youtube.setOpaque(false);
		JLabel title = new JLabel();
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		ImageIcon icon = new ImageIcon(Constants.YOUTUBE_LOGO_PATH);
		icon.setImage(icon.getImage().getScaledInstance(ICON_WIDTH,
				icon.getIconHeight() * ICON_WIDTH / icon.getIconWidth(), Image.SCALE_DEFAULT));
		title.setIcon(icon);
		youtube.add(title);
		JPanel demo = new JPanel();
		demo.setOpaque(false);
		JPanel buttons = new JPanel(new BorderLayout());
		connectYoutube = new JButton(DataManager.getLanguage().getMainLogin().getConnect());
		connectYoutube.setFocusable(false);
		connectYoutube.addActionListener(l -> {
			MainFrame.replacePanel(YoutubeLoginPanel.getInstance());
		});
		buttons.add(connectYoutube);
		clearYoutube = new JButton(DataManager.getLanguage().getMainLogin().getClear());
		clearYoutube.setFocusable(false);
		clearYoutube.setEnabled(false);
		clearYoutube.setMargin(new Insets(2, 2, 2, 2));
		clearYoutube.addActionListener(l -> {
			DataManager.setYouTubeSession(null);
			onUpdateYoutube(null);
		});
		buttons.add(clearYoutube, BorderLayout.EAST);
		demo.add(buttons);
		youtube.add(demo);
		return youtube;
	}

	@Handler
	public void onUpdateTwitch(TwitchSessionData session) {
		TwitchLoginPanel.getInstance().setSession(session);
		if (session == null) {
			isTwitchSet.set(false);
			connectTwitch.setText(DataManager.getLanguage().getMainLogin().getConnect());
			clearTwitch.setEnabled(false);
		} else {
			isTwitchSet.set(true);
			connectTwitch.setText(DataManager.getLanguage().getMainLogin().getConnected());
			clearTwitch.setEnabled(true);
		}
		refresh();
	}

	@Handler
	public void onUpdateYoutube(YouTubeSessionData session) {
		YoutubeLoginPanel.getInstance().setSession(session);
		if (session == null) {
			isYoutubeSet.set(false);
			connectYoutube.setText(DataManager.getLanguage().getMainLogin().getConnect());
			clearYoutube.setEnabled(false);
		} else {
			isYoutubeSet.set(true);
			connectYoutube.setText(DataManager.getLanguage().getMainLogin().getConnected());
			clearYoutube.setEnabled(true);
		}
		refresh();
	}

	// @Handler
	public void updateLang(Lang session) {
		start.setText(session.getMainLogin().getStart());
		if (isTwitchSet.get())
			connectTwitch.setText(session.getMainLogin().getConnected());
		else
			connectTwitch.setText(session.getMainLogin().getConnect());
		clearTwitch.setText(session.getMainLogin().getClear());
		if (isYoutubeSet.get())
			connectYoutube.setText(session.getMainLogin().getConnected());
		else
			connectYoutube.setText(session.getMainLogin().getConnect());
		clearYoutube.setText(session.getMainLogin().getClear());
	}

}
