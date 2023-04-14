package com.ql.appquanly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ql.appquanly.model.Vehicle;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Long> {

}
