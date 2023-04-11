package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.market.domain.CategoryDomain
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoryDAO {

    @Query("select * from categories")
    abstract fun findCategories(): Flow<List<CategoryDomain>>
}