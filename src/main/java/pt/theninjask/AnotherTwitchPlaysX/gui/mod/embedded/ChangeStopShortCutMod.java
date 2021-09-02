package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

@ATPXModProps(keepLoaded = false)
public class ChangeStopShortCutMod extends ATPXMod {

	private JLabel press;

	private JPanel panel;

	public ChangeStopShortCutMod() {
		panel = new JPanel(new GridLayout(2, 1));
		panel.setBackground(Constants.TWITCH_THEME.getBackground());
		press = new JLabel(String.format("Press a key (Current: %s)", NativeKeyEvent.getKeyText(Constants.stopKey)));
		press.setForeground(Constants.TWITCH_THEME.getFont());
		press.setHorizontalAlignment(JLabel.CENTER);
		panel.add(press);
		panel.setFocusable(true);
		JButton ok = new JButton("Confirm");
		ok.addActionListener(l -> {
			try {
				GlobalScreen.unregisterNativeHook();

				Constants.STRING_TO_KEYCODE.remove(NativeKeyEvent.getKeyText(Constants.stopKey));

				MainFrame.replacePanel(MainMenuPanel.getInstance());
			} catch (NativeHookException e1) {
				Constants.showExceptionDialog(e1);
				MainMenuPanel.getInstance().getModButton().setEnabled(false);
				MainFrame.replacePanel(MainMenuPanel.getInstance());
			}
		});
		panel.add(ok);
		MainMenuPanel.getInstance().getModButton().setText("Change Stop Key");
	}

	@Override
	public void refresh() {
		press.setText(String.format("Press a key (Current: %s)", NativeKeyEvent.getKeyText(Constants.stopKey)));
		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

				@Override
				public void nativeKeyTyped(NativeKeyEvent e) {
					// DO NOTHING
				}

				@Override
				public void nativeKeyReleased(NativeKeyEvent e) {
					// DO NOTHING
				}

				@Override
				public void nativeKeyPressed(NativeKeyEvent e) {
					press.setText(
							String.format("Press a key (Current: %s)", NativeKeyEvent.getKeyText(e.getKeyCode())));
					Constants.stopKey = e.getKeyCode();
				}
			});
		} catch (NativeHookException e) {
			Constants.showExceptionDialog(e);
			MainMenuPanel.getInstance().getModButton().setEnabled(false);
			MainFrame.replacePanel(MainMenuPanel.getInstance());
		}
	}

	@Override
	public JPanel getJPanelInstance() {
		return panel;
	}

}
