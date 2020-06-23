package com.neu.prattle.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
  @Column(unique = true)
  private String name;
  /**
   * a private group should have password.
   */
  @Column(name = "password")
  private String password = null;
  /**
   * moderator list of this group. One group should have at least one moderator.
   */

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
   * Makes comparison between two groups.
   *
   * Two group objects are equal if their name are equal ( names are case-sensitive )
   *
   * @param obj Object to compare
   * @return a predicate value for the comparison.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Group))
      return false;

    Group group = (Group) obj;
    return group.name.equals(this.name);
  }

}
