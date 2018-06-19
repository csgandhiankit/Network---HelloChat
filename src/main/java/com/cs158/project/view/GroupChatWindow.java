package com.cs158.project.view;

import com.cs158.project.client.logInClient;
import com.cs158.project.model.Message;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GroupChatWindow {

  private JFrame frame;
  private JTextField txtHowAreYou;
  private String name;
  private ArrayList<Message> messages;
  private PrintWriter out;
  private JButton btnBack;
  private JLabel lblMainChatRoom;
  private JPanel panel;
  private JScrollPane scrollPane;
  private Socket sock;

  /** Launch the application. */
  public static void main(String[] args) {
    EventQueue.invokeLater(
        new Runnable() {
          public void run() {
            try {
              GroupChatWindow window = new GroupChatWindow("Tommy", null, null);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  /** Create the application. */
  public GroupChatWindow(String name, PrintWriter out, Socket sock) {
    this.sock = sock;
    this.name = name;
    this.out = out;
    initialize();
    frame.setVisible(true);
  }

  /** Initialize the contents of the frame. */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 450, 550);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);

    btnBack = new JButton("Back");
    btnBack.setBounds(10, 7, 100, 20);
    frame.getContentPane().add(btnBack);

    lblMainChatRoom = new JLabel("Main Chat Room");
    lblMainChatRoom.setBounds(0, 7, 450, 16);
    lblMainChatRoom.setHorizontalAlignment(SwingConstants.CENTER);
    frame.getContentPane().add(lblMainChatRoom);

    scrollPane = new JScrollPane();
    scrollPane.setBounds(0, 35, 450, 450);
    frame.getContentPane().add(scrollPane);

    panel = new JPanel();
    panel.setBackground(Color.WHITE);
    scrollPane.setViewportView(panel);
    panel.setLayout(null);

    messages = new ArrayList<Message>();
    loadMessages();

    // start at the bottom of the scroll bar
    JScrollBar vScrollBar = scrollPane.getVerticalScrollBar();
    vScrollBar.setValue(vScrollBar.getMaximum());
    vScrollBar.setValue(vScrollBar.getMaximum());

    final JTextArea inputText = new JTextArea();
    inputText.setBounds(0, 502, 307, 26);
    frame.getContentPane().add(inputText);

    JButton btnSend = new JButton("Send");
    btnSend.setBounds(306, 502, 144, 26);
    btnSend.setBackground(Color.GREEN);
    frame.getContentPane().add(btnSend);

    inputText.addKeyListener(
        new KeyListener() {

          public void keyTyped(KeyEvent e) {}

          public void keyReleased(KeyEvent e) {}

          public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              String message = inputText.getText().trim();

              if (!(message).equals("")) {
                try {
                  out.println(name + ":" + message + ":" + "Chat");
                  out.flush(); // flushes the buffer
                } catch (Exception ex) {
                  System.out.println("Message was not sent. \n");
                }
                inputText.setText("");
                inputText.requestFocus();
              }
            }
          }
        });

    // ........Action Listener.......

    btnSend.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String message = inputText.getText().trim();

            if (!(message).equals("")) {
              try {
                out.println(name + ":" + message + ":" + "Chat");
                out.flush(); // flushes the buffer
              } catch (Exception ex) {
                System.out.println("Message was not sent. \n");
              }
              inputText.setText("");
              inputText.requestFocus();
            }
          }
        });

    btnSend.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {}
        });

    btnBack.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            sendDisconnect();
            Disconnect();
          }
        });
  }

  public void sendDisconnect() {
    String bye = (name + ": :Disconnect");
    try {
      out.println(bye);
      out.flush();
    } catch (Exception e) {

      System.out.println("Could not send Disconnect message.\n");
    }
  }

  // --------------------------//

  public void Disconnect() {
    try {
      sock.close();
      frame.dispose();
      logInClient client = new logInClient();

    } catch (Exception ex) {
      System.out.println("Failed to disconnect. \n");
    }
  }

  public void addMessage(String name, String message) {
    Message message1 = new Message(name, message);
    messages.add(message1);
    loadMessages();
  }

  public void loadMessages() {

    panel.removeAll();

    int height = messages.size() * 30;
    int maxHieght = Math.max(460, height);
    panel.setPreferredSize(new Dimension(450, maxHieght));
    int counter = 1;
    System.out.println();

    for (int x = messages.size() - 1; x >= 0; x--) {
      Message message = messages.get(x);
      String senderName = message.getSenderName();
      String txtMessage = message.getMessage();

      System.out.println(
          name
              + ": in loop: "
              + senderName
              + ": "
              + txtMessage
              + ": possition"
              + (maxHieght - (counter * 30))
              + "Height"
              + panel.getHeight());

      Color color = ((name.equals(senderName)) ? SystemColor.textHighlight : Color.GREEN);
      int alignMent = ((name.equals(senderName)) ? SwingConstants.RIGHT : SwingConstants.LEFT);
      ;

      JTextField txtHellowworld = new JTextField();
      txtHellowworld.setEditable(false);
      txtHellowworld.setBackground(color);
      txtHellowworld.setHorizontalAlignment(alignMent);
      txtHellowworld.setText(txtMessage);
      txtHellowworld.setBounds(0, maxHieght - (counter * 30), 430, 30);
      panel.add(txtHellowworld);
      txtHellowworld.setColumns(10);
      counter++;
    }
    JScrollBar vScrollBar = scrollPane.getVerticalScrollBar();
    vScrollBar.setValue(vScrollBar.getMaximum());
    vScrollBar.setValue(vScrollBar.getMaximum());
    panel.repaint();
  }

  public void messageAdded(String message) {
    Message m = new Message("unkown", message);
    messages.add(m);
    loadMessages();
  }

  public JFrame getFrame() {
    return frame;
  }
}
