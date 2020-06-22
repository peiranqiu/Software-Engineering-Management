package com.neu.prattle.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
   * Check if the user's role is moderator.
   */
  private Boolean isModerator = false;

  /**
   * Set government watched.
   */
  private Boolean isWatched = false;

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
   * Initiate a new user with user name and password.
   */
  public User(String name, String password) {

    this.name = name;
    this.password = password;
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
   * Get is watched
   */
  public Boolean getWatched() {
    return isWatched;
  }

  /**
   * set is watched
   * @param watched
   */
  public void setWatched(Boolean watched) {
    isWatched = watched;
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
   * Get the user's role if it's moderator.
   *
   * @return the user's role
   */
  public Boolean getModerator() {
    return isModerator;
  }

  /**
   * Set the user's role.
   *
   * @param moderator whether the user is moderator
   */
  public void setModerator(Boolean moderator) {
    isModerator = moderator;
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
