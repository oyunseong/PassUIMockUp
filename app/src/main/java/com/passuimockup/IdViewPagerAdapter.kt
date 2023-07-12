package com.passuimockup

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.disableClickFor3Seconds
import com.flipCard
import com.passuimockup.databinding.ItemIdPageBinding
import kotlin.math.ceil

class IdViewPagerAdapter(
    private val items: List<Int>,
    private val user: User,

) : RecyclerView.Adapter<IdViewPagerAdapter.ViewHolder>() {
    var isFront = true
    val MAX = 30

    inner class ViewHolder(
        private val binding: ItemIdPageBinding,
//        private val onClick: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.backCardContainer.isVisible = false
            binding.topTitle.isSelected = true
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Int, position: Int) {

            Glide.with(binding.root.context)
                .load(user.profileImage)
                .into(binding.profileImage)

            Glide.with(binding.root.context)
                .load(user.profileImage)
                .into(binding.backProfileImage)

            binding.progress.max = MAX
            binding.nickName.text = user.nickname
            binding.backNickName.text = user.nickname
            binding.cardNumber.text = user.cardNumber
            binding.backBirthday.text = user.birthday
            binding.renewal.text = user.renewal
            binding.sex.text = user.sex
            binding.idNumber.text =user.idNumber
            binding.backDateOfIssue.text = user.DateOfIssue

            startTimer()

            binding.root.disableClickFor3Seconds {
                isFront = if (isFront) {
                    flipCard(
                        binding.root.context,
                        binding.backCardContainer,
                        binding.cardContainer,
                    )
                    false
                } else {
                    flipCard(
                        binding.root.context,
                        binding.cardContainer,
                        binding.backCardContainer
                    )
                    true
                }
            }
        }

        private fun startTimer() {
            var startTime: Long = 0
            var imageChange = false
            val progressThread = Thread {
                while (true) {
                    // 30초까지 진행 상태 업데이트
                    for (i in MAX downTo 0) {
                        // UI 스레드에서 진행 상태 업데이트 실행
                        Handler(Looper.getMainLooper()).post {
                            binding.progress.progress = i
                            binding.currentTime.text = "${i}초"
                        }
                        try {
                            Thread.sleep(1000) // 1초 대기
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }

                    // 이미지 변경
                    if (!imageChange) {
                        Handler(Looper.getMainLooper()).post {
                            binding.qrCode.setImageDrawable(binding.root.context.getDrawable(R.drawable.qr2))
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            binding.qrCode.setImageDrawable(binding.root.context.getDrawable(R.drawable.qr1))
                        }
                    }
                    imageChange = !imageChange

                }
            }
            progressThread.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIdPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

}

