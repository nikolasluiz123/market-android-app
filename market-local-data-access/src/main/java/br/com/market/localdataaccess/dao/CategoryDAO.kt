package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.domain.CategoryDomain
import br.com.market.models.Category
import java.util.*

@Dao
abstract class CategoryDAO {

    @Query("select * from categories limit :loadSize offset :position")
    abstract suspend fun findCategories(position: Int, loadSize: Int): List<CategoryDomain>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(category: Category)

    @Query("select * from categories where id = :categoryId")
    abstract suspend fun findById(categoryId: UUID): Category

    @Query("update categories set active = :active, synchronized = :sync where id = :categoryId ")
    abstract suspend fun updateActive(categoryId: UUID, active: Boolean, sync: Boolean)

    suspend fun toggleActive(category: Category) {
        updateActive(category.id, !category.active, category.synchronized)
    }
}