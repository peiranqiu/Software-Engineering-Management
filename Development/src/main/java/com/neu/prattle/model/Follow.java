package com.neu.prattle.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/***
 * A Follow object represents a following action from a follower to a followee.
 */

@Entity
@Table(name = "user_follows_user")
public class Follow {

  /** The id. */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int _id;

  /** The follower. */
  @OneToOne
  private User follower;

  /** The followee. */
  @OneToOne
  private User followee;

  /** Get id of the follow object. */
  public int get_id() {
    return _id;
  }

  /** Set id of the follow object. */
  public void set_id(int _id) {
    this._id = _id;
  }

  /** Get the followee. */
  public User getFollowee() {
    return followee;
  }

  /** Set the followee. */
  public void setFollowee(User followee) {
    this.followee = followee;
  }

  /** Get the follower. */
  public User getFollower() {
    return follower;
  }

  /** Set the follower. */
  public void setFollower(User follower) {
    this.follower = follower;
  }


}
