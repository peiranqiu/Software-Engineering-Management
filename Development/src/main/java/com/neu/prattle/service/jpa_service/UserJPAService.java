package com.neu.prattle.service.jpa_service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import java.util.Optional;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * The User JPA service handles all user related operations on database.
 */
public class UserJPAService {

  private static final Logger LOGGER = Logger.getLogger(UserJPAService.class.getName());
  private static final UserJPAService userJpaServiceInstance = new UserJPAService();

  /**
   * The entity manager factory.
   **/
  private EntityManagerFactory entityManagerFactory;

  /**
   * Construct a User JPA Service.
   **/
  private UserJPAService() {
    entityManagerFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
  }

  /**
   * Get singleton instance of the user jpa service
   *
   * @return a UserJPAService instance.
   */
  public static UserJPAService getInstance() {
    return userJpaServiceInstance;
  }

  /**
   * Get the entity manager for the user JPA service.
   **/
  public EntityManagerFactory getEntityManagerFactory() {
    return entityManagerFactory;
  }

  /**
   * Set the entity manager for the user JPA service.
   **/
  public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  /**
   * Login a particular user.
   *
   * @param user the user to login
   * @return User the logined user
   */
  public User loginUser(User user) throws UserNotFoundException {

    Optional<User> u = findUserByName(user.getName());
    if (!u.isPresent()) {
      throw new UserNotFoundException("We couldn't find a user account associated with the username you entered.");
    }
    User newUser = u.get();
    if (!user.getName().equals(newUser.getName()) || !user.getPassword().equals(newUser.getPassword())) {
      throw new UserNotFoundException("Please check if the password you entered is correct.");
    }
    return newUser;
  }

  /**
   * Search for a particular user with given user name.
   *
   * @param name name of the user
   * @return the user associated with given user name if found
   */
  public Optional<User> findUserByName(String name) throws UserNotFoundException {
    try {
      String queryString = "SELECT u FROM User u WHERE u.username ='" + name + "'";
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
      User user = query.getSingleResult();
      entityManager.getTransaction().commit();
      entityManager.close();
      return Optional.of(user);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Persist the given user in the Database.
   *
   * @param user the user to be created
   * @return True if successful creation of user
   */
  public boolean createUser(User user) throws UserAlreadyPresentException {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      entityManager.persist(user);
      entityManager.flush();
      entityManager.getTransaction().commit();
      entityManager.close();
      return true;
    } catch (Exception e) {
      LOGGER.info(e.getMessage());
      throw new UserAlreadyPresentException("The user name already exists. Please choose another one.");
    }
  }

  /**
   * Get a particular user from the DB
   * @param id The userid
   * @return a particular user from the DB
   */
  public Object getUser(int id) {
    String queryString = "SELECT u FROM User u WHERE u._id = " + id;
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
    return query.getSingleResult();
  }
}
