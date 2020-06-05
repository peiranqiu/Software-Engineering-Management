package com.neu.prattle.Repositories;

import com.neu.prattle.model.Group;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface GroupRepo extends CrudRepository<Group, Integer> {
  Optional<Group> findById(@Param("Group_id") Integer id);
}
