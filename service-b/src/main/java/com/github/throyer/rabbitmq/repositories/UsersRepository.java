package com.github.throyer.rabbitmq.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.throyer.rabbitmq.models.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> { }
