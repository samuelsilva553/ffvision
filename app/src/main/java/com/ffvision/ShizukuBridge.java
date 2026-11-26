package com.ffvision;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShizukuBridge {
    public static void requestPermission() {
        try {
            if (rikka.shizuku.Shizuku.pingBinder() && rikka.shizuku.Shizuku.checkSelfPermission() != 0) {
                rikka.shizuku.Shizuku.requestPermission(2001);
            }
        } catch (Throwable ignored) {}
    }

    public static String launchApp(String pkg) {
        return shell("monkey -p " + pkg + " -c android.intent.category.LAUNCHER 1");
    }

    public static String shell(String cmd) {
        try {
            Process p = rikka.shizuku.Shizuku.newProcess(new String[]{"sh", "-c", cmd}, null, null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
            p.waitFor();
            return sb.toString();
        } catch (Throwable t) { return ""; }
    }
}
