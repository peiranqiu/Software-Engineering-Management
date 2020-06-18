package com.neu.prattle;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.RemoveChatLog;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Optional;

import javax.websocket.EncodeException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RemoveChatLogTest {
  RemoveChatLog rcl = new RemoveChatLog();

  @Mock
  private GroupService groupService;

  @Before
  public void setup() throws IOException, EncodeException {
    groupService = GroupServiceImpl.getInstance();
    groupService = mock(GroupService.class);
  }

  @Test
  public void deleteBeforeOneMonth(){
    Group testGroup1 = new Group("testChatGroup1");
    when(groupService.findGroupByName(anyString())).thenReturn(Optional.of(testGroup1));
    rcl.setGroupService(groupService);
    rcl.deleteBeforeNumberOfDays("testChatGroup1", 0);
  }
}
