package com.example.mixmix.feed.domain.repository;

import com.example.mixmix.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed,Long>, FeedCustomRepository {
}