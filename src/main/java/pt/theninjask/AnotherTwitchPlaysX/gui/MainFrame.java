package pt.theninjask.AnotherTwitchPlaysX.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.App;
import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.ColorThemeUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.MainFrameReplacePanelEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.MainLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.TwitchLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.YoutubeLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.TrayManager;
import pt.theninjask.externalconsole.console.ExternalConsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static MainFrame singleton = new MainFrame();

    private JPanel currentPanel = MainLoginPanel.getInstance();

    private CheckboxMenuItem hide;

    private MenuItem ec;

    private MenuItem exit;

    private MenuItem update;

    private MainFrame() {
        Constants.printVerboseMessage(Level.INFO, String.format("%s()", MainFrame.class.getSimpleName()));
        this.onStart();
        this.getContentPane().setBackground(DataManager.getTheme().background());
        // this.setTitle(DataManager.getLanguage().getTitle());
        this.setTitle(String.format("%s - v%s", DataManager.getLanguage().getID(), App.VERSION));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(300, 300));
        ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
        this.setIconImage(icon.getImage());
        this.add(currentPanel);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setResizable(false);
        // DataManager.registerLangEvent(this);
        // this.setVisible(true);
        EventManager.registerEventListener(this);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                onClose();
            }
        });
        hide = new CheckboxMenuItem(DataManager.getLanguage().getHiddedItemTray());
        hide.addItemListener(l -> {
            if (l.getStateChange() == ItemEvent.SELECTED)
                singleton.dispose();
            else if (l.getStateChange() == ItemEvent.DESELECTED)
                singleton.setVisible(true);
        });
        ec = new MenuItem(DataManager.getLanguage().getConsoleItemTray(), new MenuShortcut(KeyEvent.VK_C));
        ec.addActionListener(l -> {
            if (ExternalConsole.isViewable()) {
                ExternalConsole.revertSystemStreams();
                ExternalConsole.setViewable(false);
            } else {
                ExternalConsole.setSystemStreams();
                ExternalConsole.setViewable(true);
            }
        });
        exit = new MenuItem(DataManager.getLanguage().getExitItemTray(), new MenuShortcut(KeyEvent.VK_C));
        exit.addActionListener(l -> {
            onClose();
            System.exit(0);
        });
        update = new MenuItem(DataManager.getLanguage().getCheckUpdateItemTray());
        update.addActionListener(l -> {
            int result = ExternalConsole.getCommand("update").executeCommand();
            switch (result) {
                case 0:
                    Constants.showMessageDialog(DataManager.getLanguage().getCheckUpdateNoneItemTray(), DataManager.getLanguage().getCheckUpdateNoneTitleItemTray());
                    break;
                case 1:
                    break;
                default:
                    Constants.showMessageDialog(DataManager.getLanguage().getCheckUpdateErrorItemTray(), DataManager.getLanguage().getCheckUpdateErrorTitleItemTray());
                    break;
            }
        });
        TrayManager.getInstance().addMenuItem(exit).addMenuItem(update).addMenuItem(ec).addMenuItem(hide).refresh();
    }

    private void saveTwitchSession() {
        Constants.printVerboseMessage(Level.INFO,
                String.format("%s.saveTwitchSession()", MainFrame.class.getSimpleName()));
        if (currentPanel != TwitchLoginPanel.getInstance()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                File file = new File(Constants.SAVE_PATH, Constants.TWITCH_FILE);
                JTextField tmp = new JTextField(
                        String.format(DataManager.getLanguage().getSavingSession(), file.getAbsolutePath()));
                tmp.setEditable(false);
                tmp.setBorder(null);
                tmp.setOpaque(false);
                tmp.setForeground(DataManager.getTheme().font());
                objectMapper.writeValue(file, DataManager.getTwitchSession());
                Constants.showCustomColorMessageDialog(null, tmp, DataManager.getLanguage().getSavingSessionTitle(), JOptionPane.INFORMATION_MESSAGE,
                        null, DataManager.getTheme().background());
            } catch (IOException e) {
                Constants.showExceptionDialog(e);
            }
        }
    }

    private void saveYouTubeSession() {
        Constants.printVerboseMessage(Level.INFO,
                String.format("%s.saveYouTubeSession()", MainFrame.class.getSimpleName()));
        if (currentPanel != YoutubeLoginPanel.getInstance()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                File file = new File(Constants.SAVE_PATH, Constants.YOUTUBE_FILE);
                JTextField tmp = new JTextField(
                        String.format(DataManager.getLanguage().getSavingSession(), file.getAbsolutePath()));
                tmp.setEditable(false);
                tmp.setBorder(null);
                tmp.setOpaque(false);
                tmp.setForeground(DataManager.getTheme().font());
                objectMapper.writeValue(file, DataManager.getYouTubeSession());
                Constants.showCustomColorMessageDialog(null, tmp, DataManager.getLanguage().getSavingSessionTitle(), JOptionPane.INFORMATION_MESSAGE,
                        null, DataManager.getTheme().background());
            } catch (IOException e) {
                Constants.showExceptionDialog(e);
            }
        }
    }

    public static MainFrame getInstance() {
        Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", MainFrame.class.getSimpleName()));
        return singleton;
    }

    private void onStart() {
        Constants.printVerboseMessage(Level.INFO, String.format("%s.onStart()", MainFrame.class.getSimpleName()));

        File twitchSessionFile = new File(Constants.SAVE_PATH, Constants.TWITCH_FILE);
        if (twitchSessionFile.exists())
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TwitchSessionData session = objectMapper.readValue(twitchSessionFile, TwitchSessionData.class);
                TwitchLoginPanel.getInstance().setSession(session);
            } catch (IOException e) {
                Constants.showExceptionDialog(e);
            }

        File youtubeSessionFile = new File(Constants.SAVE_PATH, Constants.YOUTUBE_FILE);
        if (youtubeSessionFile.exists())
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                YouTubeSessionData session = objectMapper.readValue(youtubeSessionFile, YouTubeSessionData.class);
                YoutubeLoginPanel.getInstance().setSession(session);
            } catch (IOException e) {
                Constants.showExceptionDialog(e);
            }
    }

    private void onClose() {
        Constants.printVerboseMessage(Level.INFO, String.format("%s.onClose()", MainFrame.class.getSimpleName()));
        if (TwitchLoginPanel.getInstance().rememberSession())
            saveTwitchSession();
        if (YoutubeLoginPanel.getInstance().rememberSession())
            saveYouTubeSession();
        if (TwitchPlayer.getInstance().isConnected())
            TwitchPlayer.getInstance().disconnect();

        if (YouTubePlayer.getInstance().isConnected())
            YouTubePlayer.getInstance().disconnect();
        TrayManager.getInstance().stop();
    }

    public static void replacePanel(JPanel newPanel) {
        Constants.printVerboseMessage(Level.INFO, String.format("%s.replacePanel()", MainFrame.class.getSimpleName()));

        MainFrameReplacePanelEvent event = new MainFrameReplacePanelEvent(singleton.currentPanel, newPanel);
        EventManager.triggerEvent(event);
        if (event.isCancelled())
            return;

        singleton.remove(singleton.currentPanel);
        singleton.currentPanel = newPanel;
        singleton.add(newPanel);
        singleton.revalidate();
        singleton.repaint();
    }

    public static JPanel getCurrentPanel() {
        return singleton.currentPanel;
    }

    // @Handler
    public void updateLang(LanguageUpdateEvent event) {
        if (event.getLanguage() != null) {
            this.setTitle(String.format("%s - v%s", event.getLanguage().getID(), App.VERSION));
            hide.setLabel(event.getLanguage().getHiddedItemTray());
            ec.setLabel(DataManager.getLanguage().getConsoleItemTray());
            exit.setLabel(DataManager.getLanguage().getExitItemTray());
            update.setLabel(DataManager.getLanguage().getCheckUpdateItemTray());
        }
    }

    @Handler
    public void updateTheme(ColorThemeUpdateEvent event) {
        if (event.getTheme() != null) {
            this.getContentPane().setBackground(event.getTheme().background());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        hide.setState(true);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        hide.setState(!b);
    }
}
