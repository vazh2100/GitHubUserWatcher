package com.vazheninapps.githubuserwatcher.screens


import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import com.vazheninapps.githubuserwatcher.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity() {

    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (isFirst) {
            showSplash()
        } else {
            hideSplash()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isFirst", isFirst)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isFirst = savedInstanceState.getBoolean("isFirst")
    }

    private fun showSplash() {
        splash_animation.visibility = View.VISIBLE
        nav_host_fragment.visibility = View.INVISIBLE

        val fragmentAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fragmentAnimation.duration = 700

        val splashOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        splashOutAnimation.duration = 800

        splashOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                splash_animation.visibility = View.GONE
                nav_host_fragment.startAnimation(fragmentAnimation)
                nav_host_fragment.visibility = View.VISIBLE
                isFirst = false

            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })


        splash_animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                splash_animation.startAnimation(splashOutAnimation)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        splash_animation.playAnimation()

    }

   private fun hideSplash(){
        splash_animation.visibility = View.GONE
        nav_host_fragment.visibility = View.VISIBLE
    }
}