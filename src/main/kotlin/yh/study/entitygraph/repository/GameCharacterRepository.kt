package yh.study.entitygraph.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import yh.study.entitygraph.domain.GameCharacter

/**
 * @author Yonghui
 * @since 2020. 07. 30
 */
@Repository
interface GameCharacterRepository : JpaRepository<GameCharacter, Long>