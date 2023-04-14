package com.ql.appquanly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ql.appquanly.model.Department;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {

}
