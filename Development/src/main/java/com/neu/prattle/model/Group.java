package com.neu.prattle.model;

import java.util.List;

/**
 * a basic object for Group that is created by a moderator.
 */

public class Group {

  private int groupId;
  private List<Moderator> moderators;
  private List<Member> members;
  private List<User> followers;

  public Group(int groupId) {
    this.groupId = groupId;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public List<Moderator> getModerators() {
    return this.moderators;
  }

  public void setModerators(Moderator moderator) {
    this.moderators.add(moderator);
  }

  public List<Member> getMembers() {
    return members;
  }

  public void setMembers(List<Member> members) {
    this.members = members;
  }

  public void addMembers(Member member) {
    this.members.add(member);
  }

  public void removeMember(Member member) {
    this.members.remove(member);
  }

  public List<User> getFollowers() {
    return followers;
  }

  public void setFollowers(User follower) {
    this.followers.add(follower);
  }
}
