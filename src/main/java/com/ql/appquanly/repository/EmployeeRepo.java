package com.ql.appquanly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ql.appquanly.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    Employee findByCccd(String cccd);
}
