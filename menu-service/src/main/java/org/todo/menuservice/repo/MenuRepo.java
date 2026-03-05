package org.todo.menuservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.todo.menuservice.model.MenuItem;

import java.awt.*;
import java.util.List;

@Repository
public interface MenuRepo extends JpaRepository<MenuItem,Long> {
    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);
}
