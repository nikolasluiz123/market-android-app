package br.com.market.domain

import androidx.room.ColumnInfo
import br.com.market.domain.base.BaseDomain
import java.util.*

/**
 * Classe de dominio que representa [br.com.market.models.Brand]
 *
 * @property id
 * @property active
 * @property companyId
 * @property synchronized
 * @property name
 *
 * @author Nikolas Luiz Schmitt
 */
data class BrandDomain(
    override var id: UUID? = null,
    override var active: Boolean = true,
    @ColumnInfo(name = "company_id")
    override var companyId: UUID? = null,
    override var synchronized: Boolean = false,
    var name: String = ""
): BaseDomain()