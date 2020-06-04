package com.neu.prattle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonIgnore;
import javax.persistence.*;

/***
 * A User object represents a basic account information for a user.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */

@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int _id;

  @Column(unique = true)
  private String name;

  private String password;
  private String logins;
  private String language;

  @OneToMany(targetEntity = Message.class)
  private List<Message> messages = new ArrayList<>();

  @OneToMany(targetEntity = Group.class)
  private List<Group> groups = new ArrayList<>();

  @OneToMany(targetEntity = User.class)
  @JoinTable(name = "user_follows_user",
          joinColumns = {
                  @JoinColumn(name = "FOLLOWEE_ID", referencedColumnName = "ID")},
          inverseJoinColumns = {
                  @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "ID")})
  @JsonIgnore
  private List<User> following = new ArrayList<>();


  @OneToMany(targetEntity = User.class)
  @JoinTable(name = "user_follows_user",
          joinColumns = {
                  @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "ID")},
          inverseJoinColumns = {
                  @JoinColumn(name = "FOLLOWEE_ID", referencedColumnName = "ID")})
  @JsonIgnore
  private List<User> follower = new ArrayList<>();

  private boolean isModerator;
  private boolean watched;

  public User() {

  }

  public User(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLogins() {
    return logins;
  }

  public void setLogins(String logins) {
    this.logins = logins;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public boolean isModerator() {
    return isModerator;
  }

  public void setModerator(boolean moderator) {
    isModerator = moderator;
  }

  public boolean isWatched() {
    return watched;
  }

  public void setWatched(boolean watched) {
    this.watched = watched;
  }

  public List<User> getFollowing() {
    return following;
  }

  public void setFollowing(List<User> following) {
    this.following = following;
  }

  public List<User> getFollower() {
    return follower;
  }

  public void setFollower(List<User> follower) {
    this.follower = follower;
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
