package br.com.market.storage.repository

import br.com.market.localdataaccess.dao.CompanyDAO
import br.com.market.models.Company
import br.com.market.models.ThemeDefinitions
import br.com.market.servicedataaccess.webclients.CompanyWebClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val companyDAO: CompanyDAO,
    private val companyWebClient: CompanyWebClient
) {

    fun findFirstCompany(): Flow<Company?> {
        return companyDAO.findFirstCompany()
    }

    fun findFirstThemeDefinition(): Flow<ThemeDefinitions?> {
        return companyDAO.findFilterThemeDefinition()
    }

    suspend fun sync(deviceId: String) {
        val response = companyWebClient.findByDeviceId(deviceId)

        if (response.success && response.values.isNotEmpty()) {
            with(response.values.first()) {
                val company = Company(
                    id = id,
                    name = name,
                    themeDefinitionsId = themeDefinitions.id
                )

                val themeDefinitions = ThemeDefinitions(
                    id = themeDefinitions.id,
                    colorPrimary = themeDefinitions.colorPrimary,
                    colorSecondary = themeDefinitions.colorSecondary,
                    colorTertiary = themeDefinitions.colorTertiary,
                    imageLogo = themeDefinitions.imageLogo
                )

                companyDAO.saveTheme(themeDefinitions)
                companyDAO.saveCompany(company)
            }
        }
    }
}