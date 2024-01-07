package com.example.mrjar.demo1;

import java.lang.management.ManagementFactory;

// supports Java 8 and 9+
public class Lib {

    public static long getMyPid() {
        String compVer = "/*$target.java.version$*/";
        String runVer = System.getProperty("java.version");

        System.out.println(
                "[Lib] getMyPid called, compiled for version " + compVer + ", running on version " + runVer + ".");

        // #if target.java.version >= 9
        System.out.println("java.version >= 9");
        return getMyPidNew();
        // #else
        // $System.out.println("java.version < 9");
        // $return getMyPidOld();
        // #endif
    }

    public static long getMyPidOld() {
        System.out.println("[Lib] getMyPidOld called)");
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');
        return Long.parseLong(jvmName.substring(0, index));
    }

    public static long getMyPidNew() {
        System.out.println("[Lib] getMyPidNew called)");
        // #if target.java.version >= 9
        return ProcessHandle.current().pid();
        // #else
        // $return -1;
        // #endif
    }

}
