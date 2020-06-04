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

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int _id;

  @OneToOne
  private User follower;

  @OneToOne
  private User followee;

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public User getFollowee() {
    return followee;
  }

  public void setFollowee(User followee) {
    this.followee = followee;
  }

  public User getFollower() {
    return follower;
  }

  public void setFollower(User follower) {
    this.follower = follower;
  }


}
