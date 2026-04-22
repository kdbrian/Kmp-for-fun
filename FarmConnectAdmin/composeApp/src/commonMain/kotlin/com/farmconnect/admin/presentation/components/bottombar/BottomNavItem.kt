package com.farmconnect.core.ui.components.bottombar

import androidx.compose.ui.graphics.vector.ImageVector
import com.farmconnect.core.util.Route


open class BottomNavItem<T : Route>(val icon: ImageVector, val label: String, val route: T)