package com.neu.prattle.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * a basic object for Group that is created by a moderator.
 */
@Entity
@Table(name = "Group")
public class Group {
  /**
   * the id of the group, which is unique
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int groupId;
  /**
   * The group name should be unique.
   */
  @Column(name = "name", unique = true)
  private String name;
  /**
   * a private group should have password.
   */
  @Column(name = "password")
  private String password;
  /**
   * moderator list of this group. One group should have at least one moderator.
   */
  @OneToMany(targetEntity = Group.class)
  @JoinTable(name = "User_moderates_Group", joinColumns = {
          @JoinColumn(name = "User_User_id", referencedColumnName = "User_id"),
          @JoinColumn(name = "Group_Group_id", referencedColumnName = "Group_id")})
  @JsonIgnore
  private List<Moderator> moderators = new ArrayList<>();
  /**
   * member list of this group.
   */
  @OneToMany(targetEntity = Group.class)
  @JoinTable(name = "User_has_Group", joinColumns = {
          @JoinColumn(name = "User_User_id", referencedColumnName = "User_id"),
          @JoinColumn(name = "Group_Group_id", referencedColumnName = "Group_id")})
  @JsonIgnore
  private List<Member> members = new ArrayList<>();
  /**
   * a list of sub-groups inside this group.
   */
  @OneToMany(targetEntity = Group.class)
  @JoinTable(name = "Group_has_Group", joinColumns = {
          @JoinColumn(name = "super_Group_id", referencedColumnName = "Group_id"),
          @JoinColumn(name = "sub_Group_id", referencedColumnName = "Group_id")})
  @JsonIgnore
  private List<Group> groups = new ArrayList<>();
  /**
   * a list of users who follow this group.
   */
  @OneToMany(targetEntity = Group.class)
  @JoinTable(name = "User_follows_Group", joinColumns = {
          @JoinColumn(name = "User_User_id", referencedColumnName = "User_id"),
          @JoinColumn(name = "Group_Group_id", referencedColumnName = "Group_id")})
  @JsonIgnore
  private List<User> followers = new ArrayList<>();


  public Group(String name) {
    this.name = name;
  }

  public Group() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public void addModerator(Moderator moderator) {
    this.moderators.add(moderator);
  }

  public List<Member> getMembers() {
    return members;
  }

  public void addMember(Member member) {
    this.members.add(member);
  }

  public void removeMember(Member member) {
    this.members.remove(member);
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void addGroup(Group group) {
    this.groups.add(group);
  }

  public List<User> getFollowers() {
    return followers;
  }

  public void setFollower(User follower) {
    this.followers.add(follower);
  }


}
