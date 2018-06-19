package com.cs158.project.client;

import com.cs158.project.view.GroupChatWindow;
import com.cs158.project.view.LoginGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class logInClient {

  String username, address = "172.20.10.6";
  ArrayList<String> users = new ArrayList();
  Boolean isConnected = false;

  Socket sock;
  BufferedReader reader;
  PrintWriter writer;

  LoginGUI loginWindow;
  GroupChatWindow chatWindow;

  public void ListenThread() {
    Thread IncomingReader = new Thread(new IncomingReader());
    IncomingReader.start();
  }

  public void userAdd(String data) {
    users.add(data);
  }

  public void writeUsers() {
    String[] tempList = new String[(users.size())];
    users.toArray(tempList);
    for (String token : tempList) {}
  }

  public void sendDisconnect() {
    String bye = (username + ": :Disconnect");
    try {
      writer.println(bye);
      writer.flush();
    } catch (Exception e) {
    }
  }

  public void Disconnect() {
    try {
      sock.close();
    } catch (Exception ex) {
    }
    isConnected = false;
  }

  public logInClient() {
    initComponents();
  }

  public void initComponents() {
    loginWindow = new LoginGUI();

    loginWindow.btnLetsChat.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String name = loginWindow.textField.getText();
            String address = loginWindow.textField_1.getText();
            String p = loginWindow.textField_2.getText();
            // TODO Auto-generated method stub
            if (name.length() > 0 && address.length() > 3 && p.length() > 2) {

              int port = Integer.parseInt(p);
              handleSocetLogic(name, address, port);
              loginWindow.despose();
            }
          }
        });
  }

  public void handleSocetLogic(String name, String address, int port) {
    username = address;
    address = address;

    if (isConnected) return;
    try {
      sock = new Socket(address, port);
      InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
      reader = new BufferedReader(streamreader);
      writer = new PrintWriter(sock.getOutputStream());
      writer.println(name + ":" + name + " joined group.:Connect");
      writer.flush();
      isConnected = true;
    } catch (Exception ex) {
      System.out.println(ex);
    }
    chatWindow = new GroupChatWindow(name, writer, sock);
    ListenThread();
  }

  // reader class
  public class IncomingReader implements Runnable {
    public void run() {

      String[] data;
      String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

      try {

        while ((stream = reader.readLine()) != null) {
          System.out.println("data: " + stream);
          data = stream.split(":");

          if (data[2].equals(chat)) {
            System.out.println("about to call method");
            chatWindow.addMessage(data[0], data[1]);
          } else if (data[2].equals(connect)) {
            userAdd(data[0]);
          } else if (data[2].equals(disconnect)) {
          } else if (data[2].equals(done)) {
            writeUsers();
            users.clear();
          }
        }
        System.out.println("Out of the loop");

      } catch (Exception ex) {
      }
    }
  }

  public static void main(String[] args) {
    logInClient client = new logInClient();
  }
}
