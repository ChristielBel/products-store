package com.example.productsStore.data.repository

import com.example.productsStore.data.local.product.ProductsDao
import com.example.productsStore.data.mapper.toDomain
import com.example.productsStore.data.mapper.toEntity
import com.example.productsStore.data.remote.ProductsApi
import com.example.productsStore.domain.model.CachedProductDetails
import com.example.productsStore.domain.model.Product
import com.example.productsStore.domain.repository.ProductsRepository

class ProductsRepositoryImpl(
    private val api: ProductsApi,
    private val dao: ProductsDao,
) : ProductsRepository {

    private companion object {
        const val CACHE_LIFETIME =
            24 * 60 * 60 * 1000L // 5000L
    }

    override suspend fun getProducts(
        limit: Int,
        skip: Int
    ): List<Product> {
        return api.getProducts(
            limit = limit,
            skip = skip,
            select = "id,title,price,brand"
        ).products.map { dto ->
            dto.toDomain()
        }
    }

    override suspend fun getProductDetails(
        id: Int
    ): CachedProductDetails {

        val now = System.currentTimeMillis()

        val cached = dao.getProduct(id)

        val isCacheValid =
            cached != null &&
                    now - cached.cachedAt < CACHE_LIFETIME

        if (isCacheValid) {
            return CachedProductDetails(
                product = cached.toDomain(),
                isStale = false
            )
        }

        return try {
            val remote = api.getProductById(id)

            dao.insertProduct(
                remote.toEntity(
                    cachedAt = now
                )
            )

            CachedProductDetails(
                product = remote.toDomain(),
                isStale = false
            )
        } catch (e: Exception) {
            if (cached != null) {
                CachedProductDetails(
                    product = cached.toDomain(),
                    isStale = true
                )
            } else throw e
        }
    }
}