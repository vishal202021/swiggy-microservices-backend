package org.todo.restaurantservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.todo.restaurantservice.model.Restaurant;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    List<Restaurant> findByIsOpenTrue();
    List<Restaurant> findByOwnerId(Long ownerId);
}
