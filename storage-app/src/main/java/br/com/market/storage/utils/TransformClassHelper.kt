package br.com.market.storage.utils

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

object TransformClassHelper {

    fun <M: Any, D: Any> modelToDomain(model: M, domain: D) {
        val modelClass = model::class
        val domainClass = domain::class

        val modelMembers = modelClass.members.filterIsInstance<KProperty<Any>>()
        val domainMembers = domainClass.members.filterIsInstance<KMutableProperty<Any>>()

        for (modelMember: KProperty<Any> in modelMembers) {
            for (domainMember: KMutableProperty<Any> in domainMembers) {
                if (modelMember.name == domainMember.name) {
                    domainMember.setter.call(domain, modelMember.getter.call(model))
                    break
                }
            }
        }
    }

    fun <M : Any, D: Any> domainToModel(domain: D, model: M) {
        val domainClass = domain::class
        val modelClass = model::class

        val domainMembers = domainClass.members.filterIsInstance<KProperty<Any>>()
        val modelMembers = modelClass.members.filterIsInstance<KMutableProperty<Any>>()

        for (domainMember: KProperty<Any> in domainMembers) {
            for (modelMember: KMutableProperty<Any> in modelMembers) {
                if (modelMember.name == domainMember.name) {
                    modelMember.setter.call(model, domainMember.getter.call(domain))
                    break
                }
            }
        }
    }
}