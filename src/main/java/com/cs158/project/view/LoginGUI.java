package com.cs158.project.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;

public class LoginGUI {

  private JFrame frame;
  public JTextField textField;
  public JTextField textField_1;
  public JTextField textField_2;
  public JButton btnLetsChat;

  /** Create the application. */
  public LoginGUI() {
    initialize();
    frame.setVisible(true);
  }

  /** Initialize the contents of the frame. */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 500, 350);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);

    JMenu mnMenu = new JMenu("Menu");
    menuBar.add(mnMenu);

    JLabel lblIpAdress = new JLabel("IP Address:");
    lblIpAdress.setBounds(38, 62, 77, 16);

    JLabel lblPort = new JLabel("Port:");
    lblPort.setBounds(254, 62, 29, 16);

    textField_1 = new JTextField();
    textField_1.setBounds(112, 57, 130, 26);
    textField_1.setColumns(10);

    textField_2 = new JTextField();
    textField_2.setBounds(289, 57, 130, 26);
    textField_2.setColumns(10);

    JLabel lblUserName = new JLabel("User Name:");
    lblUserName.setBounds(118, 112, 72, 16);

    textField = new JTextField();
    textField.setBounds(196, 107, 130, 26);
    textField.setColumns(10);

    btnLetsChat = new JButton("Let's Chat!!");
    btnLetsChat.setBounds(182, 166, 114, 29);
    frame.getContentPane().setLayout(null);
    frame.getContentPane().add(lblUserName);
    frame.getContentPane().add(textField);
    frame.getContentPane().add(lblIpAdress);
    frame.getContentPane().add(textField_1);
    frame.getContentPane().add(lblPort);
    frame.getContentPane().add(textField_2);
    frame.getContentPane().add(btnLetsChat);
  }

  public void despose() {
    frame.dispose();
  }
}
