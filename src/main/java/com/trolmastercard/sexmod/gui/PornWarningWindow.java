/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  org.apache.commons.io.FileUtils
 */
package com.trolmastercard.sexmod.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import net.minecraft.client.resources.I18n;

public class PornWarningWindow extends JFrame {
    private final JPanel panel;
    static PornWarningWindow window;
    static public boolean isAdult;

    public static void Launch() {
        EventQueue.invokeLater(() -> {
            try {
                window = new PornWarningWindow();
                window.setVisible(true);
                window.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PornWarningWindow() {
        this.setResizable(false);
        this.setBounds(100, 100, 600, 260);
        this.panel = new JPanel();
        this.panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.panel.setLayout(new BorderLayout(0, 0));
        this.setContentPane(this.panel);
        JPanel jPanel = new JPanel();
        this.panel.add((Component)jPanel, "North");
        JTextPane jTextPane = new JTextPane();
        jTextPane.setFont(new Font("Tahoma", 0, 16));
        jTextPane.setBackground(SystemColor.control);
        jTextPane.setText(I18n.format("window.pornwarning.title", new Object[0]));
        jPanel.add(jTextPane);
        JPanel jPanel2 = new JPanel();
        this.panel.add((Component)jPanel2, "South");
        JCheckBox jCheckBox = new JCheckBox(I18n.format("window.pornwarning.dontaskagain", new Object[0]));
        jPanel2.add(jCheckBox);
        JButton iamnotminor = new JButton(I18n.format("window.pornwarning.am18", new Object[0]));
        iamnotminor.addActionListener(actionEvent -> {
            isAdult = false;
            if (jCheckBox.isSelected()) {
                File file = new File("sexmod");
                file.mkdir();
                File file2 = new File("sexmod/dontAskAgain");
                try {
                    file2.createNewFile();
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            window.dispose();
        });
        jPanel2.add(iamnotminor);
        JButton iamminor = new JButton(I18n.format("window.pornwarning.not18", new Object[0]));
        iamminor.addActionListener(actionEvent -> {
            isAdult = false;
            System.out.println("MINOR!!! WHEOO WOOO WHEEE WHOOO WHEEE WHOO");
            //File file = new File("sexmod");
            //try {
            //    FileUtils.deleteDirectory((File)file);
            //} catch (IOException iOException) {
            //    iOException.printStackTrace();
            //}
            //File file2 = new File("mods/youCanJustDeleteMe.bat");
            //try {
            //    FileWriter fileWriter = new FileWriter(file2);
            //    fileWriter.write("@echo off\n");
            //    fileWriter.write("TIMEOUT /T 5\n");
            //    fileWriter.write("DEL \"mods\\*sexmod*.jar\"\n");
            //    fileWriter.write("exit 0");
            //    fileWriter.close();
            //    Runtime.getRuntime().exec("cmd /c start " + file2.getPath());
            //} catch (IOException iOException) {
            //    iOException.printStackTrace();
            //}
            //FMLCommonHandler.instance().exitJava(0, true);
        });
        jPanel2.add(iamminor);
        JPanel jPanel3 = new JPanel();
        this.panel.add((Component)jPanel3, "Center");
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        JTextPane jTextPane2 = new JTextPane();
        jTextPane2.setContentType("text/html");
        jTextPane2.setBackground(SystemColor.control);
        jTextPane2.setEditable(false);
        jTextPane2.setText("<html><center><p style='font-family: Tahoma'>" + I18n.format("window.pornwarning.text", new Object[0]) + "</p></center></html> ");
        jPanel3.add(jTextPane2);
    }

    static {
        isAdult = true;
    }
}

