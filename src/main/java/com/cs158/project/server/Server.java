package com.cs158.project.server;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;

public class Server extends javax.swing.JFrame {

  enum ACTIONS {
    Connect,
    Disconnect,
    Chat
  }

  private static final long serialVersionUID = 1L;
  ArrayList<PrintWriter> clientOutputStreams;
  ArrayList<String> users;
  private final Logger LOGGER = Logger.getLogger(Server.class.getName());

  /*
   * ClientHandler to handle business for each client
   */
  public class ClientHandler implements Runnable {
    Scanner reader;
    Socket sock;
    PrintWriter client;

    private final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    public ClientHandler(Socket clientSocket, PrintWriter user) {
      client = user;
      try {
        sock = clientSocket;
        reader = new Scanner(sock.getInputStream());
      } catch (Exception ex) {
        LOGGER.warning("Error Connecting client");
        serverOutput.append("Error Connecting client \n");
      }
    }

    public void run() {
      String message;
      String[] data;

      try {
        if (reader.hasNext()) {
          while ((message = reader.nextLine().trim()) != null) {

            data = message.split(":");
            // append client message to server log
            serverOutput.append(data[0] + ":" + data[1] + "\n");

            if (data[2].equalsIgnoreCase(ACTIONS.Connect.name())) {
              broadcastToClients((data[0] + ":" + data[1] + ":" + ACTIONS.Chat.name()));
              userAdd(data[0]);
            } else if (data[2].equalsIgnoreCase(ACTIONS.Disconnect.name())) {
              String msg =
                  (data[0] + ": " + data[0] + " has disconnected." + ":" + ACTIONS.Chat.name());
              System.out.println("Debug: " + msg);
              broadcastToClients(msg);
              userRemove(data[0]);
            } else if (data[2].equalsIgnoreCase(ACTIONS.Chat.name())) {
              broadcastToClients(message);
            } else {
              serverOutput.append("Invalid Action \n");
              LOGGER.warning("Invalid OUTPUT");
            }
          }
        }
      } catch (Exception ex) {
        LOGGER.warning("Connection Lost");
        serverOutput.append("Connection Lost \n");
        ex.printStackTrace();
        clientOutputStreams.remove(client);
      }
    }
  }

  public Server() {
    initComponents();
  }

  private void initComponents() {

    serverPanel = new JScrollPane();
    serverOutput = new JTextArea();
    startServer = new JButton();
    stopServer = new JButton();
    onlineUsers = new JButton();
    clearServerOutput = new JButton();
    inputIP = new JTextField();
    inputPort = new JTextField();

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Admin");
    setName("server");
    setResizable(false);

    serverOutput.setColumns(20);
    serverOutput.setRows(5);
    serverPanel.setViewportView(serverOutput);
    serverOutput.getCaret().setVisible(false);

    startServer.setText("Start Server");
    startServer.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            startServerActionPerformed(evt);
          }
        });

    stopServer.setText("Stop Server");
    stopServer.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            stopServerActionPerformed(evt);
          }
        });

    onlineUsers.setText("Users");
    onlineUsers.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            onlineUsersActionPerformed(evt);
          }
        });

    clearServerOutput.setText("Clear");
    clearServerOutput.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            clearServerOutputActionPerformed(evt);
          }
        });

    JPanel panel = new JPanel();

    GroupLayout layout = new GroupLayout(getContentPane());
    layout.setHorizontalGroup(
        layout
            .createParallelGroup(Alignment.TRAILING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        layout
                            .createParallelGroup(Alignment.LEADING)
                            .addComponent(
                                serverPanel,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        layout
                                            .createParallelGroup(Alignment.LEADING)
                                            .addComponent(
                                                stopServer,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                            .addComponent(
                                                startServer,
                                                GroupLayout.PREFERRED_SIZE,
                                                114,
                                                Short.MAX_VALUE))
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(
                                        panel,
                                        GroupLayout.PREFERRED_SIZE,
                                        64,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(Alignment.LEADING, false)
                                            .addComponent(
                                                clearServerOutput,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                            .addComponent(
                                                onlineUsers,
                                                GroupLayout.PREFERRED_SIZE,
                                                103,
                                                Short.MAX_VALUE))))
                    .addContainerGap()));
    layout.setVerticalGroup(
        layout
            .createParallelGroup(Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(serverPanel, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(Alignment.LEADING)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        layout
                                            .createParallelGroup(Alignment.LEADING, false)
                                            .addComponent(
                                                startServer,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                            .addComponent(
                                                onlineUsers,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE))
                                    .addGap(18)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(Alignment.BASELINE)
                                            .addComponent(clearServerOutput)
                                            .addComponent(stopServer)))
                            .addComponent(
                                panel, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
                    .addGap(24)));
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[] {0, 0};
    gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 0};
    gbl_panel.columnWeights = new double[] {1.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    panel.setLayout(gbl_panel);

    JLabel lblIp = new JLabel("IP:");
    GridBagConstraints gbc_lblIp = new GridBagConstraints();
    gbc_lblIp.insets = new Insets(0, 0, 5, 0);
    gbc_lblIp.gridx = 0;
    gbc_lblIp.gridy = 0;
    panel.add(lblIp, gbc_lblIp);

    inputIP = new JTextField();
    inputIP.setToolTipText("IP Address ");
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 0, 5, 0);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 0;
    gbc_textField.gridy = 1;
    panel.add(inputIP, gbc_textField);
    inputIP.setColumns(10);

    JLabel lblPort = new JLabel("Port");
    GridBagConstraints gbc_lblPort = new GridBagConstraints();
    gbc_lblPort.insets = new Insets(0, 0, 5, 0);
    gbc_lblPort.gridx = 0;
    gbc_lblPort.gridy = 2;
    panel.add(lblPort, gbc_lblPort);

    inputPort = new JTextField();
    inputPort.setToolTipText("Port");
    GridBagConstraints gbc_textField_1 = new GridBagConstraints();
    gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField_1.gridx = 0;
    gbc_textField_1.gridy = 3;
    panel.add(inputPort, gbc_textField_1);
    inputPort.setColumns(10);
    getContentPane().setLayout(layout);

    pack();
  }

  private void stopServerActionPerformed(ActionEvent evt) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    broadcastToClients("Server:Server Stopped, disconnecting all clients\n:Chat");
    serverOutput.append("Server Stopped \n");
    serverOutput.setText("");
    LOGGER.info("Server stopped..");
  }

  private void startServerActionPerformed(ActionEvent evt) {
    if (!inputIP.getText().isEmpty() && !inputPort.getText().isEmpty()) {
      String inIP = inputIP.getText().trim();
      String inPort = inputPort.getText().trim();
      int port = Integer.parseInt(inPort);
      if (inIP != null && inPort != null) {
        Thread starter = new Thread(new ServerStart(inIP, port));
        starter.start();
        serverOutput.append("Server started...\n");
      }
    } else {
      LOGGER.warning("Please enter IP and PORT");
    }
  }

  private void onlineUsersActionPerformed(ActionEvent evt) {
    serverOutput.append("\n Online users : \n");
    for (String current_user : users) {
      serverOutput.append(current_user);
      serverOutput.append("\n");
    }
  }

  private void clearServerOutputActionPerformed(ActionEvent evt) {
    serverOutput.setText("");
  }

  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(
        new Runnable() {

          public void run() {
            new Server().setVisible(true);
          }
        });
  }

  public class ServerStart implements Runnable {
    public String IP = "127.0.0.1";
    public int PORT = 2222;

    public ServerStart(String ip, int port) {
      IP = ip;
      PORT = port;
    }

    public void run() {
      clientOutputStreams = new ArrayList<PrintWriter>();
      users = new ArrayList<String>();

      try {

        InetSocketAddress insa = new InetSocketAddress(IP, PORT);
        ServerSocket serverSock = new ServerSocket();
        serverSock.bind(insa);
        String host = serverSock.getInetAddress().getHostAddress();
        System.out.println(host);

        while (true) {
          Socket clientSock = serverSock.accept();
          PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
          clientOutputStreams.add(writer);

          Thread listener = new Thread(new ClientHandler(clientSock, writer));
          listener.start();
          serverOutput.append("Got a connection. \n");
        }
      } catch (Exception ex) {
        serverOutput.append("Error making a connection. \n");
      }
    }
  }

  public void userAdd(String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    serverOutput.append("Before " + name + " added. \n");
    users.add(name);
    serverOutput.append("After " + name + " added. \n");
    String[] tempList = new String[(users.size())];
    users.toArray(tempList);

    for (String token : tempList) {
      message = (token + add);
      broadcastToClients(message);
    }
    broadcastToClients(done);
  }

  public void userRemove(String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    users.remove(name);
    String[] tempList = new String[(users.size())];
    users.toArray(tempList);

    for (String token : tempList) {
      message = (token + add);
      broadcastToClients(message);
    }
    broadcastToClients(done);
  }

  /*
   * Broadcast message to all online users
   */
  public void broadcastToClients(String message) {
    for (PrintWriter pWriter : clientOutputStreams) {
      try {
        pWriter.println(message);
        pWriter.flush();
        serverOutput.setCaretPosition(serverOutput.getDocument().getLength());
      } catch (Exception ex) {
        serverOutput.append("Error telling everyone. \n");
      }
    }
  }

  private JButton clearServerOutput;
  private JButton stopServer;
  private JButton startServer;
  private JButton onlineUsers;
  private JScrollPane serverPanel;
  private JTextArea serverOutput;
  private JTextField inputIP;
  private JTextField inputPort;
}
