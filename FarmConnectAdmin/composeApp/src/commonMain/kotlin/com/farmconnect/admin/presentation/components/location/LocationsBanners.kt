package com.farmconnect.core.ui.components.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.components.FarmConnectButton
import com.farmconnect.core.ui.components.location.LocationsBanners.CompactLocationBanner
import com.farmconnect.core.ui.components.location.LocationsBanners.GradientLocationBanner
import com.farmconnect.core.ui.components.location.LocationsBanners.LocationInfoDialog
import com.farmconnect.core.ui.components.location.LocationsBanners.MultipleLocationsBanner
import com.farmconnect.core.ui.theme.AppYellow
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor

object LocationsBanners {

    @Composable
    fun MultipleLocationsBanner(
        onDismiss: () -> Unit,
        onLearnMore: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = LocalPrimaryColor.current.copy(alpha = 0.1f) // Light blue background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(LocalPrimaryColor.current, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        fontFamily = LocalFontFamily.current,
                        text = "Why multiple locations?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppYellow
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        fontFamily = LocalFontFamily.current,
                        text = "Make it easy for clients to access your products from anywhere.",
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // CTA Button
                    TextButton(
                        onClick = onLearnMore,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = LocalPrimaryColor.current
                        ),
                        contentPadding = PaddingValues(6.dp)
                    ) {
                        Text(
                            fontFamily = LocalFontFamily.current,
                            text = "Learn more",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Dismiss button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Alternative: Compact Banner
    @Composable
    fun CompactLocationBanner(
        onDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFEF3C7) // Light yellow background
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    fontFamily = LocalFontFamily.current,
                    text = "Select locations to see stores & prices near you",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF92400E),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color(0xFF92400E),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Alternative: Gradient Banner with Icon
    @Composable
    fun GradientLocationBanner(
        onDismiss: () -> Unit,
        onGetStarted: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                LocalPrimaryColor.current,
                                Color(0xFF9333EA)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                fontFamily = LocalFontFamily.current,
                                text = "Shop Local Groceries",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            fontFamily = LocalFontFamily.current,
                            text = "Choose your pickup and delivery spots to browse stores in your neighborhood and compare prices.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.95f),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FarmConnectButton(
                            onClick = onGetStarted,
                            color = Color.White,
                            contentColor = LocalPrimaryColor.current,
                            shape = RoundedCornerShape(8.dp),
                            title = "Get Started"
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // Info Dialog for "Learn More"
    @Composable
    fun LocationInfoDialog(
        onDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = LocalPrimaryColor.current,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    fontFamily = LocalFontFamily.current,
                    text = "How Location Selection Works",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LocationInfoItem(
                        icon = Icons.Default.ShoppingCart,
                        title = "Easy search",
                        description = "We advertise your products at your selected locations."
                    )

//
//                    LocationInfoItem(
//                        icon = Icons.Default.Favorite,
//                        title = "Accurate Pricing",
//                        description = "Prices vary by location. We show you the exact prices for your area."
//                    )


                    LocationInfoItem(
                        icon = Icons.Default.DateRange,
                        title = "Delivery Options",
                        description = "Clients delivery options are distributed according to distance."
                    )


                    LocationInfoItem(
                        icon = Icons.Default.FavoriteBorder,
                        title = "Save Favorites",
                        description = "Clients Save frequently used locations for faster shopping next time."
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalPrimaryColor.current
                    )
                ) {
                    Text(
                        fontFamily = LocalFontFamily.current, text = "Got it!", style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    @Composable
    private fun LocationInfoItem(
        icon: ImageVector,
        title: String,
        description: String
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF3E8FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LocalPrimaryColor.current,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    fontFamily = LocalFontFamily.current,
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    fontFamily = LocalFontFamily.current,
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
private fun PreviewMultipleLocationsBanner() {
    FarmConnectTheme {
        MultipleLocationsBanner(
            onDismiss = {},
            onLearnMore = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewCompactLocationBanner() {
    FarmConnectTheme {
        CompactLocationBanner(
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGradientLocationBanner() {
    FarmConnectTheme {
        GradientLocationBanner(
            onDismiss = {},
            onGetStarted = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLocationInfoDialog() {
    FarmConnectTheme {
        LocationInfoDialog(
            onDismiss = {}
        )
    }
}