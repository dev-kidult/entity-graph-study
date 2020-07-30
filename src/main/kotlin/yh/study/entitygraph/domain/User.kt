package yh.study.entitygraph.domain

import javax.persistence.*

/**
 * @author Yonghui
 * @since 2020. 07. 30
 */
@Table
@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @Column
        var username: String? = null,
        @Column
        var password: String? = null,

        @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
        var characters: MutableSet<GameCharacter>? = null
) {
    fun addCharacter(character: GameCharacter) {
        if (characters == null) {
            characters = mutableSetOf()
        }
        this.characters?.add(character)
        character.updateUser(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}