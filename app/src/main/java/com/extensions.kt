package com

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.annotation.Dimension
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.passuimockup.R
import kotlin.math.abs


// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "test")


fun ViewPager2.setDefaultPageTransformer() {
    val offsetBetweenPages = 30.pxFromDp
    val pageTransformer = CompositePageTransformer().apply {
        addTransformer(MarginPageTransformer(8))
        addTransformer { page, position ->
            val myOffset = position * -(2 * offsetBetweenPages)
            if (position < -1) {
                page.translationX = -myOffset
                page.translationY = 0f // y축을 고정
            } else if (position <= 1) {
                page.translationX = myOffset
                page.translationY = 0f // y축을 고정
            } else {
                page.alpha = 0f
                page.translationX = myOffset
                page.translationY = 0f // y축을 고정
            }
        }
    }
    this.setPageTransformer(pageTransformer)
    this.offscreenPageLimit = 3
}

fun ViewPager2.showOtherPageEdges() {
    val pageMarginPx = 8.pxFromDp//resources.getDimensionPixelOffset(pageMarinRes)
    val offsetPx = 12.pxFromDp//resources.getDimensionPixelOffset(offsetRes)

    setPageTransformer { page, position ->
        val viewPager = page.parent.parent as ViewPager2
        val offset = position * -(2 * offsetPx + pageMarginPx)
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -offset
            } else {
                page.translationX = offset
            }
        } else {
            page.translationY = offset
        }
    }
}


val Number.pxFromDp: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }

val Number.dpFromPx: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return this.toFloat() / (displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


fun flipCard(context: Context, visibleView: View, inVisibleView: View) {
    try {
        visibleView.visibility = View.VISIBLE
        visibleView.elevation = 0f // 그림자 제거
        inVisibleView.elevation = 0f // 그림자 제거
        val scale = context.resources.displayMetrics.density
        val cameraDist = 8000 * scale
        visibleView.cameraDistance = cameraDist
        inVisibleView.cameraDistance = cameraDist
        val flipOutAnimatorSet =
            AnimatorInflater.loadAnimator(
                context,
                R.animator.flip_out
            ) as AnimatorSet
        flipOutAnimatorSet.setTarget(inVisibleView)
        val flipInAnimatorSet =
            AnimatorInflater.loadAnimator(
                context,
                R.animator.flip_in
            ) as AnimatorSet
        flipInAnimatorSet.setTarget(visibleView)
        flipOutAnimatorSet.start()
        flipInAnimatorSet.start()
        flipInAnimatorSet.doOnEnd {
            inVisibleView.visibility = View.GONE
            visibleView.elevation =10f // 그림자 제거
            inVisibleView.elevation = 10f // 그림자 제거
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.disableClickFor3Seconds(action: () -> Unit) {
    this.setOnClickListener {
        if (this.isEnabled) {
            action.invoke()
        }
        this.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            this.isEnabled = true
        }, 2000) // 3초 동안 클릭 막기
    }
}