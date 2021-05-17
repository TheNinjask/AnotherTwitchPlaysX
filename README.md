![alt text][logo]

[logo]: https://fontmeme.com/permalink/210517/3e4924b31b184892fc5b77d4a4ca094c.png

### Before Using App Reminder

* Requires Java 16;

* This is no where explicit so for now I'll leave a message here.
```
The Key escape is a special key that can be used to stop the app from running after pressing the button start. (You will know, do not fret)
```
<!-- omit in toc -->
## Table of Contents


- [Introduction](#introduction)
- [Modding](#modding)
- [F.A.Q. with out the F.A.](#faq-with-out-the-fa)
- [Bugs](#bugs)
- [Future](#future)
- [Libraries Used](#libraries-used)


## Introduction

This app was made with the intention to any type of user to have it easy to play with their chat maybe just their chat alone.

So I hope you can have fun using this application!

## Modding

Yes! In this application, you can load a single mod (but is there can be a mod that loads multiple mods but for now I am not doing that.)

To make a mod for this app, you can download the `AnotherTwitchPlaysXLib.jar` and add the jar has an external jar to your project.

It will be required for the mod at least 1 class (it will be the first that it finds) with the annotation `@Mod` with the value `main` as `true` which is by default and must have an empty constructor.

Also there will be 3 types of mods:
* First Party - That will show no problems.
* Third Party Approved - This will show a message when loading that has be approved by me/us.
* Unknown - This will show a warning message that the code being loaded has not been overseer by me (the user will have the option to opt out of loading the mod.) 

If you wish to be a Third Party Approved, for now wait but if you wanna get ready, it will require that jar has the source code attached.

For any issues/wishes please refer to [bugs](#bugs) section (even tho it might not be a bug) and feel free to say the problem.

Example-Changing the Mod Button to say greetings to the user:

```
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.Mod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ModPanel;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

@Mod(hasPanel = false)
public class HelloWorld implements ModPanel {

	public HelloWorld() {
		JButton modButton = MainMenuPanel.getInstance().getModButton();
		for (ActionListener elem : modButton.getActionListeners()) {
			modButton.removeActionListener(elem);
		}
		JLabel hello = new JLabel("Greetings World!");
		hello.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		modButton.addActionListener(l->{
			Constants.showCustomColorMessageDialog(null, hello, "Hello World!", JOptionPane.INFORMATION_MESSAGE, null, Constants.TWITCH_COLOR);
		});
	}
	
	@Override
	public JPanel getJPanelInstance() {
		return null;
	}

	@Override
	public void refresh() {
		//DO NOTHING
	}

}
```

## F.A.Q. with out the F.A.
<!-- omit in toc -->
### Why is the UI so small?

Because I actually kinda liked it but I hid behind the excuse of to make me think to use the space more carefully. For example, I ran out of space for more twitch chat options, so I made that slider to preserve the size and still be intuitive enough for users!
<!-- omit in toc -->
### Why is there no support for controllers

The first intention of this project (even tho it can't be seen through the commits) was to have controller support. But arised the problem that it would require for the user to install another requirement (I had and still have the objective to not have to many requirements) and if there would be an external requirement, I would like that the app itself installed it. `Then why are you using Java if you want the least requirements possible?` Well because is the language I am most used to and I kinda like it, I still like C and Python and at some point in the beginning thought of using Python but I decided to go for Java.

## Bugs

Create an issue following a template in ISSUES_TEMPLATE. If you feel that there is no suitable template, at least write the issues as simple and precise as possible.

## Future
For now, I do not intend to touch more this project besided to fix any issues at the beginning but if this somehow takes off I'll try to make time for it and for both futures that why I at the last minute decided to add support for mods for at least to be able to grow even if I am not present.

## Libraries Used

I'll like to give credits to:

* [KittehOrg](https://github.com/KittehOrg) for [KittehIRCClientLib](https://github.com/KittehOrg/KittehIRCClientLib) to allow connection with Twitch;
* [Alex Barker](https://github.com/kwhat) for [JNativeHook](https://github.com/kwhat/jnativehook) for the global keyboard and mouse listeners;
* [Universal-Team](https://github.com/Universal-Team) from the [Pokémon Chest Team](https://github.com/Universal-Team/pkmn-chest) for the templates.