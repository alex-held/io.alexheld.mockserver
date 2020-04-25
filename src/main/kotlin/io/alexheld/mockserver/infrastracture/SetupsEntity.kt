package io.alexheld.mockserver.infrastracture

import javax.persistence.*

@Entity
@Table(name = "setups", schema = "setup", catalog = "postgres")
open class SetupsEntity {
    @get:Basic
    @get:Column(name = "id", nullable = true)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "action_id", nullable = true)
    var actionId: Int? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "actionId = $actionId " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SetupsEntity

        if (id != other.id) return false
        if (actionId != other.actionId) return false

        return true
    }

}

