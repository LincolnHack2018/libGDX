package com.lincolnhack;

import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import org.moe.natj.general.Pointer;
import com.lincolnhack.LibGDX;
import com.lincolnhack.data.Device;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Network;

import apple.uikit.c.UIKit;

public class IOSMoeLauncher extends IOSApplication.Delegate {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = false;
        return new IOSApplication(new LibGDX(new InitDevice() {
            @Override
            public void init(Device device) throws Exception {

            }
        }, new Network() {
            @Override
            public boolean connect() {
                return false;
            }
        }), config);
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }
}
