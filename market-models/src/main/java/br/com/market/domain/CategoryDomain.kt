package br.com.market.domain

import androidx.room.ColumnInfo
import br.com.market.domain.base.BaseDomain

/**
 * Classe de dominio que representa [br.com.market.models.Category]
 *
 * @property id
 * @property active
 * @property companyId
 * @property synchronized
 * @property name
 *
 * @author Nikolas Luiz Schmitt
 */
data class CategoryDomain(
    override var id: String? = null,
    override var active: Boolean = true,
    @ColumnInfo(name = "company_id")
    override var companyId: Long? = null,
    override var synchronized: Boolean = false,
    var name: String = ""
): BaseDomain()