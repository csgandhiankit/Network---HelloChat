package com.cs158.project.model;

public class Message {

  private String message;
  private String senderName;

  public Message(String senderName, String message) {
    this.message = message;
    this.senderName = senderName;
  }

  public String getSenderName() {
    return senderName;
  }

  public String getMessage() {
    return message;
  }
}
