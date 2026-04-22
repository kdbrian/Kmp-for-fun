package com.farmconnect.core.ui.util

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.farmconnect.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.core.domain.model.CategoryItem
import com.farmconnect.core.ui.R

object Defaults {

    object Products {
        val defaultProducts = (0..10).map {
            ProductOrServiceItemDto(
                id = it.toString(),
                title = LoremIpsum(3).values.joinToString(),
                description = LoremIpsum(32).values.joinToString(),
            )
        }
    }

    object CategoryItems {
        val categoryItems = listOf(
            Cereals,
            DairyProducts,
            Fruits,
            Meat,
            Poultry,
            Hospitality
        )
        data object Cereals : CategoryItem(
            categoryName = "Cereals",
            imagePath = R.drawable.grains,
            categoryDescription = "All types of cereals",
        )

        data object DairyProducts : CategoryItem(
            categoryName = "Dairy Products",
            imagePath = R.drawable.dairy_products,
            categoryDescription = "All types of dairy products",
        )

        data object Fruits : CategoryItem(
            categoryName = "Vegetables & Fruits",
            imagePath = R.drawable.fruits,
            categoryDescription = "All types of fruits",
        )

        data object Meat : CategoryItem(
            categoryName = "Meat",
            imagePath = R.drawable.meat_products,
            categoryDescription = "All types of meat",
        )


        data object Poultry : CategoryItem(
            categoryName = "Poultry",
            imagePath = R.drawable.poultry_products,
            categoryDescription = "All types of poultry",
        )


        data object Hospitality : CategoryItem(
            categoryName = "Hospitality",
            imagePath = R.drawable.hospitality,
            categoryDescription = "Hospitality, Restaurants, Hotels, Lodging services etc",
        )


    }

}