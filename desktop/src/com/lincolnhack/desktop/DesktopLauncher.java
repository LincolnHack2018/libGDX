package com.lincolnhack.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lincolnhack.LibGDX;
import com.lincolnhack.data.Device;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Network;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new LibGDX(), config);
	}
}
