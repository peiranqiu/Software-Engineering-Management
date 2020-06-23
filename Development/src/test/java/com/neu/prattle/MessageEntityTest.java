package com.neu.prattle;

import com.neu.prattle.model.Message;
import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageEntityTest {
  @Test
  public void test1() {
    Message msg = new Message();
    msg.setFrom("Julia123");
    msg.setTo("Yijia123");
    msg.setContent("Hello World!");
    msg.setSendToGroup(false);
    msg.setGroupId(-1);
    msg.setTimeStamp(msg.getTimeStamp());
    msg.setDate(msg.getDate());
  }

}
