package yh.study.entitygraph.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import yh.study.entitygraph.domain.User

/**
 * @author Yonghui
 * @since 2020. 07. 30
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    @EntityGraph(attributePaths = ["characters"])
    @Query("select u from User u")
    fun findAllEntityGraph(): MutableList<User>
}