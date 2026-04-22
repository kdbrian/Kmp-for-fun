package com.farmconnect.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.farmconnect.core.domain.dto.CategoryItemDto
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import kotlinx.coroutines.launch

// ─── State machine for the gesture lifecycle ────────────────────────────────

enum class GesturePhase {
    Idle,
    LongPressHeld,   // shaking + instruction overlay visible
    Dragging,        // user is dragging downward
    Exploding,       // explosion burst playing
    Done             // callback fired, screen fades in
}


@Composable
fun AnimatedCategoryItem(
    category      : CategoryItemDto,
    isActive      : Boolean,
    activePhase   : GesturePhase,
    onPhaseChange : (GesturePhase) -> Unit,
    onNavigate    : () -> Unit,
    modifier      : Modifier = Modifier
) {
    val scope          = rememberCoroutineScope()

    // ── Shake ────────────────────────────────────────────────────────────────
    val shakeOffset    = remember { Animatable(0f) }
    val dragOffsetY    = remember { Animatable(0f) }
    val explodeScale   = remember { Animatable(1f) }
    val explodeAlpha   = remember { Animatable(1f) }

    // Start / stop shake based on phase
    LaunchedEffect(activePhase, isActive) {
        when {
            isActive && activePhase == GesturePhase.LongPressHeld -> {
                // Continuous shake loop
                while (true) {
                    shakeOffset.animateTo(
                        8f,
                        animationSpec = tween(60, easing = LinearEasing)
                    )
                    shakeOffset.animateTo(
                        -8f,
                        animationSpec = tween(60, easing = LinearEasing)
                    )
                }
            }
            isActive && activePhase == GesturePhase.Exploding -> {
                shakeOffset.snapTo(0f)
                // Explosion: scale up fast, fade out
                launch {
                    explodeScale.animateTo(
                        2.8f,
                        animationSpec = tween(380, easing = FastOutLinearInEasing)
                    )
                }
                explodeAlpha.animateTo(
                    0f,
                    animationSpec = tween(380, easing = FastOutLinearInEasing)
                )
                onNavigate()
                // Reset after navigation
                dragOffsetY.snapTo(0f)
                explodeScale.snapTo(1f)
                explodeAlpha.snapTo(1f)
                onPhaseChange(GesturePhase.Idle)
            }
            !isActive || activePhase == GesturePhase.Idle -> {
                shakeOffset.snapTo(0f)
                dragOffsetY.snapTo(0f)
                explodeScale.snapTo(1f)
                explodeAlpha.snapTo(1f)
            }
            else -> { /* dragging handled via gesture detector */ }
        }
    }

    // ── Gesture detector ─────────────────────────────────────────────────────
    val longPressJob = remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    val gestureModifier = modifier
        .offset { IntOffset(shakeOffset.value.toInt(), dragOffsetY.value.toInt()) }
        .scale(if (isActive && activePhase == GesturePhase.Exploding) explodeScale.value else 1f)
        .graphicsLayer {
            alpha = if (isActive && activePhase == GesturePhase.Exploding) explodeAlpha.value else 1f
        }
        .pointerInput(Unit) {
            // Long-press detection
            detectTapGestures(
                onLongPress = {
                    onPhaseChange(GesturePhase.LongPressHeld)
                }
            )
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { _ ->
                    if (activePhase == GesturePhase.LongPressHeld) {
                        onPhaseChange(GesturePhase.Dragging)
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    if (activePhase == GesturePhase.Dragging && dragAmount.y > 0) {
                        scope.launch {
                            dragOffsetY.snapTo((dragOffsetY.value + dragAmount.y).coerceIn(0f, 220f))
                        }
                    }
                },
                onDragEnd = {
                    if (activePhase == GesturePhase.Dragging) {
                        if (dragOffsetY.value >= 110f) {
                            // Threshold reached → explode
                            onPhaseChange(GesturePhase.Exploding)
                        } else {
                            // Didn't drag far enough, snap back
                            scope.launch {
                                dragOffsetY.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                            }
                            onPhaseChange(GesturePhase.LongPressHeld)
                        }
                    }
                },
                onDragCancel = {
                    scope.launch { dragOffsetY.animateTo(0f, spring()) }
                    onPhaseChange(GesturePhase.Idle)
                }
            )
        }

    CategoryItem(
        onClick       = onNavigate,
        categoryName  = category.categoryName,
        color         = LocalPrimaryColor.current.copy(.95f),
        contentColor  = Color.White,
        modifier      = gestureModifier,
        image         = category.imageId
    )
}