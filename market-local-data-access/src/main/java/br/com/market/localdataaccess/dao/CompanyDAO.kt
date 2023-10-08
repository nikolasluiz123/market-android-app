package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.market.models.Company
import br.com.market.models.ThemeDefinitions
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CompanyDAO: AbstractBaseDAO() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveCompany(company: Company)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveCompanies(company: List<Company>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveTheme(themeDefinitions: ThemeDefinitions)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveThemes(themeDefinitions: List<ThemeDefinitions>)

    @Query("select * from companies limit 1")
    abstract fun findFirstCompany(): Flow<Company?>

    @Query("select * from theme_definitions limit 1")
    abstract fun findFilterThemeDefinition(): Flow<ThemeDefinitions?>

    @Transaction
    open suspend fun clearAll() {
        clearAllThemes()
        clearAllCompanies()
    }

    @Query("delete from companies")
    abstract suspend fun clearAllCompanies()

    @Query("delete from theme_definitions")
    abstract suspend fun clearAllThemes()
}