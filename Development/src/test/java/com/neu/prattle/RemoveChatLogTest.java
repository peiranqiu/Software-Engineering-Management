package com.neu.prattle;

import com.neu.prattle.model.removeChatLog;

import org.junit.Test;

public class RemoveChatLogTest {
  removeChatLog rcl = new removeChatLog();
  @Test
  public void deleteBeforeOneMonth(){
    rcl.deleteBeforeNumberOfDays("testChatGroup1", 0);
  }
}
