package com.neu.prattle;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Member;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupEntityTest {


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

  @Test
  public void test2() {
    Group group2 = new Group("group2");
    User user = new User("user1");
    group2.setFollower(user);
    assertTrue(group2.getFollowers().contains(user));
    Member member = new Member();
    member.setName("member1");
    group2.addMember(member);
    assertTrue(group2.getMembers().contains(member));
    Moderator m = new Moderator();
    m.setName("moderator1");
    group2.addModerator(m);
    assertTrue(group2.getModerators().contains(m));
    group2.removeMember(member);
    assertFalse(group2.getMembers().contains(member));

  }
  @Test
  public void test3() {
    Group group1 = new Group("group1");
    Group group2 = new Group("group2");
    Group group3 = new Group("group3");
    group1.addGroup(group2);
    group1.addGroup(group3);
    assertTrue(group1.getGroups().contains(group2));
    assertTrue(group1.getGroups().contains(group3));

  }

  @Test
  public void test4() {
    Group group4 = new Group("group4");
    Group group5 = new Group("group4");
    Group group6 = new Group("group6");
    assertEquals(group4,group5);
    assertFalse(group4.equals(group6));
    assertTrue(group4.hashCode()==group5.hashCode());

  }


}
