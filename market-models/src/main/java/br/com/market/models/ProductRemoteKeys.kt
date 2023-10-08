package br.com.market.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.market.models.base.BaseRemoteKeyModel

@Entity(tableName = "product_remote_keys")
data class ProductRemoteKeys(
    @PrimaryKey
    override val id: String,
    override val prevKey: Int?,
    override val currentPage: Int,
    override val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis()
): BaseRemoteKeyModel()