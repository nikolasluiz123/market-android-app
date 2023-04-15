package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.domain.CategoryDomain
import br.com.market.models.Category
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
abstract class CategoryDAO {

    @Query("select * from categories")
    abstract fun findCategories(): Flow<List<CategoryDomain>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveCategory(category: Category)

    @Query("select * from categories where id = :categoryId")
    abstract suspend fun findById(categoryId: UUID): CategoryDomain

    @Query("update categories set active = false, synchronized = false where id = :categoryId ")
    abstract suspend fun inactivateCategory(categoryId: UUID)
}