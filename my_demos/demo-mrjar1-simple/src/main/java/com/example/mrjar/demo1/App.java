package com.example.mrjar.demo1;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class App {

    public static void main(String[] args) {
        System.out.println("[App] Compiled for version: /*$target.java.version$*/");
        System.out.println("[App] Current Java version: " + System.getProperty("java.version"));

        System.out.println("\nMy pid = " + Lib.getMyPid());
        System.out.println("My pid old = " + Lib.getMyPidOld());
        System.out.println("My pid new = " + Lib.getMyPidNew());

        System.out.println("\nSimple color test:");

        AnsiConsole.systemInstall();

        System.out.println(ansi().fg(YELLOW).a("Hello world in Yellow").reset());
        System.out.println(ansi().fg(GREEN).a("Hello world in Green").reset());
        System.out.println();

        AnsiConsole.systemUninstall();
    }

}
