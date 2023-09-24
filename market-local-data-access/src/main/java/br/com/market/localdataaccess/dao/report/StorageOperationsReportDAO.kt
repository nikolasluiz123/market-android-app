package br.com.market.localdataaccess.dao.report

import androidx.room.Dao
import androidx.room.Query
import br.com.market.enums.EnumOperationType
import br.com.market.localdataaccess.dao.AbstractBaseDAO
import br.com.market.report.data.BrandReportData
import br.com.market.report.data.CategoryReportData
import br.com.market.report.data.ProductReportData
import br.com.market.report.data.ReportCommonData
import br.com.market.report.data.StorageOperationReportData

@Dao
abstract class StorageOperationsReportDAO : AbstractBaseDAO() {

    @Query(
        """
            select comp.name as companyName,  
                   market.name as marketName,  
                   theme.image_logo as logo  
            from companies comp  
            inner join theme_definitions theme on theme.id = comp.theme_definitions_id  
            inner join markets market on market.company_id = comp.id 
        """
    )
    abstract suspend fun getCommonInformation(): ReportCommonData

    @Query(
        """ 
            select cat.id as categoryId,  
                   cat.name as categoryName  
            from categories cat
            where cat.active
            order by cat.name
        """
    )
    abstract suspend fun getCategories(): List<CategoryReportData>

    @Query(
        """
            select brand.id as brandId,  
                   brand.name as brandName 
            from brands brand 
            inner join categories_brands cb on cb.brand_id = brand.id 
            where cb.category_id = :categoryId 
            and brand.active
            order by brand.name
        """
    )
    abstract suspend fun getBrands(categoryId: String): List<BrandReportData>

    @Query(
        """
            select prod.id as productId, 
                   prod.name as productName, 
                   prod.quantity as productQuantity, 
                   prod.quantity_unit as quantityUnit,
                   image.bytes as image
            from products prod
            inner join categories_brands cb on cb.id = prod.category_brand_id
            inner join products_images image on image.product_id = prod.id and image.principal = 1 
            where cb.category_id = :categoryId
            and cb.brand_id = :brandId
            and prod.active
            order by prod.name
        """
    )
    abstract suspend fun getProducts(categoryId: String, brandId: String): List<ProductReportData>

    @Query(
        """
            select operation.date_prevision as datePrevision,
                   operation.date_realization as dateRealization,
                   operation.operation_type as operationType,
                   operation.quantity as quantity
            from storage_operations_history operation
            where operation.product_id = :productId
        """
    )
    abstract suspend fun getOperations(productId: String): List<StorageOperationReportData>

    @Query(
        """
            select sum(operation.quantity)
            from storage_operations_history operation
            where operation.product_id = :productId
            and operation_type = :operationType
        """
    )
    abstract suspend fun getInputOperationsCount(productId: String, operationType: EnumOperationType = EnumOperationType.Input): Int

    @Query(
        """
            select sum(operation.quantity)
            from storage_operations_history operation
            where operation.product_id = :productId
            and (operation_type = :operationTypeOutput or operation_type = :operationTypeSell)
        """
    )
    abstract suspend fun getOutputAndSellOperationsCount(
        productId: String,
        operationTypeOutput: EnumOperationType = EnumOperationType.Output,
        operationTypeSell: EnumOperationType = EnumOperationType.Sell
    ): Int

    open suspend fun getStorageCount(productId: String): Int {
        return getInputOperationsCount(productId) - getOutputAndSellOperationsCount(productId)
    }
}