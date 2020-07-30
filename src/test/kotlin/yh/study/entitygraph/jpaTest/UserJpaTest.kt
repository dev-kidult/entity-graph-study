package yh.study.entitygraph.jpaTest

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor
import yh.study.entitygraph.domain.GameCharacter
import yh.study.entitygraph.domain.User
import yh.study.entitygraph.repository.UserRepository

/**
 * @author Yonghui
 * @since 2020. 07. 30
 */
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserJpaTest(val userRepository: UserRepository) {
    @AfterAll
    fun deleteAll() {
        userRepository.deleteAll()
    }

    @BeforeAll
    fun init() {
        val users: MutableList<User> = mutableListOf()

        for (i in 1..10) {
            val user = User(username = "username", password = "password")
            user.addCharacter(GameCharacter(name = "character", gameLevel = 1))
            users.add(user)
        }

        userRepository.saveAll(users)
    }

    @Test
    internal fun `user 를 여러개 조회하면 N1쿼리가 발생한다`() {
        val extractCharacterName = extractCharacterName(userRepository.findAll())

        /*
        * select user0_.id as id1_1_, user0_.password as password2_1_, user0_.username as username3_1_ from user user0_
        * select
            characters0_.user_id as user_id4_0_0_,
            characters0_.id as id1_0_0_,
            characters0_.id as id1_0_1_,
            characters0_.game_level as game_lev2_0_1_,
            characters0_.name as name3_0_1_,
            characters0_.user_id as user_id4_0_1_
        from
            game_character characters0_
        where
            characters0_.user_id =? * 10개
        총 11개의 쿼리가 날라감
        * */


        then(extractCharacterName.size).isEqualTo(10)
    }

    @Test
    internal fun `entityGraph 어노테이션을 쓰면 N1쿼리가 안생김`() {
        val extractCharacterName = extractCharacterName(userRepository.findAllEntityGraph())

        /*
        * select
	        user0_.id as id1_1_0_,
	        characters1_.id as id1_0_1_,
	        user0_.password as password2_1_0_,
	        user0_.username as username3_1_0_,
	        characters1_.game_level as game_lev2_0_1_,
	        characters1_.name as name3_0_1_,
	        characters1_.user_id as user_id4_0_1_,
	        characters1_.user_id as user_id4_0_0__,
	        characters1_.id as id1_0_0__
        from
	        user user0_
        left outer join game_character characters1_ on
	        user0_.id = characters1_.user_id
	    단건의 쿼리로 해결
        * */

        then(extractCharacterName.size).isEqualTo(10)
    }

    fun extractCharacterName(users: MutableList<User>): MutableList<String> {
        val result = mutableListOf<String>()
        users.forEach {
            it.characters
                    ?.mapNotNull { _it -> _it.name }
                    ?.toMutableSet()
                    ?.let { _it -> result.addAll(_it) }
        }
        return result
    }
}