package com.example.mixmix.member.domain.repository;

import com.example.mixmix.member.domain.Member;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends
        JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member>,
        MemberCustomRepository {
    Optional<Member> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.isStreakUpdated = false WHERE m.lastStreakUpdate < :yesterday")
    void resetStreakUpdated(LocalDateTime yesterday);

    List<Member> findAllByIsStreakUpdatedFalse();
}