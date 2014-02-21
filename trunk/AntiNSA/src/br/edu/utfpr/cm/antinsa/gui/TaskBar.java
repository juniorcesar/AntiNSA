/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Charleston Anjos
 * @version 1.0 - 2013
 */
public class TaskBar {

    //Instance new SystemTray
    private static SystemTray Tray = SystemTray.getSystemTray();
    private static TrayIcon TrayIconBar;
    //variables
    private static String IconMain;
    private static JFrame frame;
    private static MouseListener mlOpcoes;
    private static String NameTask;
    //variables the alteration image
    private static boolean AlterCountTray = false;

    /**
     *
     * @param icon
     * @param frame
     * @param nameTask
     */
    public TaskBar(String icon, JFrame frame, String nameTask) throws Exception {
        TaskBar.IconMain = icon;
        TaskBar.frame = frame;
        TaskBar.NameTask = nameTask;
    }

    public void instanceTask() {

        //alter image and instance task
        alterImageTray(IconMain);

        //add events mouse
        //initEventsMouse();

        //add icon in SystemTray
        try {
            Tray.add(TrayIconBar);
        } catch (AWTException e) {
        }

        AlterCountTray = true;

    }

    /**
     *
     * @param image
     */
    public void alterImageTray(String image) {
        Image imageIcon = new ImageIcon(image).getImage();

        //if not exist instance create new
        if (TaskBar.AlterCountTray == false) {
            TrayIconBar = new TrayIcon(imageIcon, NameTask, initPopUpMenu());
        } else {
            TrayIconBar.setImage(imageIcon);
        }

        //image auto resize
        TrayIconBar.setImageAutoSize(true);
    }

    /**
     *
     * @param imageOne
     * @param imageTwo
     * @param time
     */
    //used with timerbean
    public void useSequenceImage(String imageOne, String imageTwo, int time) {
        try {

            alterImageTray(imageOne); //image footprint parameter changes

            Thread.sleep(time); //time for change

            alterImageTray(imageTwo); //image footprint parameter changes

        } catch (InterruptedException ex) {
            Logger.getLogger(TaskBar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initEventsMouse() throws Exception {
        //instantiates a mouse listener for use user be in TrayIcon
        mlOpcoes = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                     frame.setVisible(true);
                     frame.setExtendedState(JFrame.NORMAL);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                /*  if the mouse is clicked with the wheel mouse or with
                 *  button right close the application 
                 if(e.getButton() == 2 || e.getButton() == 3){
                 //System.exit(0);
                 }*/
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };

        //add mouseListener in TrayIcon
        TrayIconBar.addMouseListener(mlOpcoes);
    }

    /**
     * @return PopUp
     */
    private PopupMenu initPopUpMenu() {

        PopupMenu popup = new PopupMenu();

        MenuItem miAbout = new MenuItem("About");
        miAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "AntiNSA\n"
                        + "Version 1.0\n\n"
                        + "Developed by:\n Junior Cesar de Oliveira \n Luiz Arthur Feitosa dos Santos","",JOptionPane.INFORMATION_MESSAGE);

            }
        });

        MenuItem miReturn = new MenuItem("Preferences");
        miReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setExtendedState(JFrame.NORMAL);
            }
        });

//        MenuItem miHelp = new MenuItem("Help");
//        miHelp.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                frame.setVisible(true);
//                frame.setExtendedState(JFrame.NORMAL);
//            }
//        });
        
        MenuItem miExit = new MenuItem("Exit");
        miExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        popup.add(miAbout);
        popup.addSeparator();
        popup.add(miReturn);
        popup.addSeparator();
//        popup.add(miHelp);
//        popup.addSeparator();
        popup.add(miExit);

        return popup;
    }

    /**
     *
     * @param title
     * @param message
     * @param type
     */
    public void displayMessageTask(String title, String message, int type) {

        String style = "";

        if (type == 0) {
            style = "NONE";
        }
        if (type == 1) {
            style = "INFO";
        }
        if (type == 2) {
            style = "ERROR";
        }
        if (type == 3) {
            style = "WARNING";
        }

        TrayIconBar.displayMessage(title, message, TrayIcon.MessageType.valueOf(style));
    }
}
