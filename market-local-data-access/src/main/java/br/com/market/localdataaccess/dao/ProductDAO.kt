package br.com.market.localdataaccess.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.market.models.Product
import java.util.*

@Dao
abstract class ProductDAO {

    @Query("select * from products where id = :productId")
    abstract suspend fun findProductById(productId: UUID): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun save(product: Product)
}