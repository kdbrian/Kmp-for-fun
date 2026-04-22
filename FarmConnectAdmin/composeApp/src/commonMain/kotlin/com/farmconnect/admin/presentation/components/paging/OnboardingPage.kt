package com.farmconnect.core.ui.components.paging

import com.farmconnect.core.ui.R

/**
 * Data class representing each onboarding page
 */
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

fun getOnboardingPages(): List<OnboardingPage> {
    return listOf(
        OnboardingPage(
            imageRes = R.drawable.transparent_logo,
            title = "Bringing the best services, at the convenience of your fingertips.",
            description = ""
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding_1,
            title = "Fresh Items with fast delivery.",
            description = "We try our best level to make sure our customer happiness by fresh products."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding_2,
            title = "Wide Selection of Quality Products",
            description = "Choose from thousands of fresh products delivered to your doorstep"
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding_3,
            title = "Safe and Secure. Payment Methods",
            description = "Multiple payment options for your convenience and security"
        )
    )
}