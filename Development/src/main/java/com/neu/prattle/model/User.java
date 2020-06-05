
package com.neu.prattle.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/***
 * A User object represents a basic account information for a user.
 *
 */
@Entity
@Table(name = "User")
public class User {

  /**
   * The id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userId;

  /**
   * The username should be unique.
   */
  @Column(unique = true)
  private String name;

  /**
   * The password.
   */
  private String password;

  /**
   * The user's avatar.
   */
  private String avatar;

  /**
   * The groups.
   */
  @OneToMany(targetEntity = Group.class)
  private List<Group> groups = new ArrayList<>();

  /**
   * The following list.
   */
  @OneToMany(targetEntity = User.class)
  @JoinTable(name = "user_follows_user", joinColumns = {
          @JoinColumn(name = "FOLLOWEE_ID", referencedColumnName = "ID"),
          @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "ID")})
  @JsonIgnore
  private List<User> followedUser = new ArrayList<>();

  /**
   * The follower list.
   */
  @OneToMany(targetEntity = User.class)
  @JoinTable(name = "user_follows_user",
          joinColumns = {
                  @JoinColumn(name = "FOLLOWEE_ID", referencedColumnName = "ID"),},
          inverseJoinColumns = {
                  @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "ID")})
  @JsonIgnore
  private List<User> follower = new ArrayList<>();

  /**
   * The list of group that the user is following.
   */
  @OneToMany(targetEntity = Group.class)
  @JoinTable(name = "user_follows_group", joinColumns = {
          @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
          @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")})
  @JsonIgnore
  private List<Group> followedGroup = new ArrayList<>();

  /**
   * Initiate a new user.
   */
  public User() {

  }

  /**
   * Initiate a new user with user name.
   */
  public User(String name) {
    this.name = name;
  }

  /**
   * Get the user's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the user's name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the user's id.
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Set the user's id.
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Get the user's password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the user's password.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Get the user's avatar.
   */
  public String getAvatar() {
    return avatar;
  }

  /**
   * Set the user's avatar.
   */
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  /**
   * Get the list of user the user is following.
   */
  public List<User> getFollowedUser() {
    return followedUser;
  }

  /**
   * Set the user's following list.
   */
  public void setFollowedUser(List<User> followedUser) {
    this.followedUser = followedUser;
  }

  /**
   * Get the user's follower list.
   */
  public List<User> getFollower() {
    return follower;
  }

  /**
   * Set the user's follower list.
   */
  public void setFollower(List<User> follower) {
    this.follower = follower;
  }

  /**
   * Get the user's groups.
   */
  public List<Group> getGroups() {
    return groups;
  }

  /**
   * Set the user's groups.
   */
  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  /**
   * Get the user's followed groups.
   */
  public List<Group> getFollowedGroup() {
    return followedGroup;
  }

  /**
   * Set the groups that the user is following.
   */
  public void setFollowedGroup(List<Group> followedGroup) {
    this.followedGroup = followedGroup;
  }

  /***
   * Returns the hashCode of this object.
   *
   * As name can be treated as a sort of identifier for
   * this instance, we can use the hashCode of "name"
   * for the complete object.
   *
   *
   * @return hashCode of "this"
   */
  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  /***
   * Makes comparison between two user accounts.
   *
   * Two user objects are equal if their name are equal ( names are case-sensitive )
   *
   * @param obj Object to compare
   * @return a predicate value for the comparison.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof User))
      return false;

    User user = (User) obj;
    return user.name.equals(this.name);
  }
}