package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.ApiUseTime;
import com.sparta.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
    Optional<ApiUseTime> findByUser(User loginuser);
}
