package br.com.market.core.utils

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * Classe que pode ser utilizada para transferir dados de um
 * objeto para outro, sendo de diferentes tipos ou não.
 *
 * @author Nikolas Luiz Schmitt
 */
object TransformClassHelper {

    /**
     * Função para converter um modelo e domínio.
     *
     * @param M Tipo do Modelo
     * @param D Tipo do Domínio
     * @param model Objeto modelo
     * @param domain Objeto domínio
     *
     * @author Nikolas Luiz Schmitt
     */
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

    /**
     * Função para converter um domínio em modelo
     *
     * @param M Tipo do Modelo
     * @param D Tipo do Domínio
     * @param model Objeto modelo
     * @param domain Objeto domínio
     *
     * @author Nikolas Luiz Schmitt
     */
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