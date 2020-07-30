package yh.study.entitygraph.domain

import javax.persistence.*

/**
 * @author Yonghui
 * @since 2020. 07. 30
 */
@Table
@Entity
data class GameCharacter(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User? = null,
        @Column
        var name: String? = null,
        @Column
        var gameLevel: Long? = null
) {
    fun updateUser(user: User) {
        this.user = user
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameCharacter

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}