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
    Group group1 = new Group();

    group1.setGroupId(4);
    assertEquals(4, group1.getGroupId());
    group1.setName("group1");
    assertEquals("group1", group1.getName());
    group1.setPassword("test");
    assertEquals("test", group1.getPassword());
}
