package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.tabs_adapters
import com.google.android.material.tabs.TabLayout

class Syllabus_FragmentActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllabus_fragment)

        tabLayout = findViewById(R.id.tabs)
        viewPager  = findViewById(R.id.viewpager)

        tabLayout.addTab(tabLayout.newTab().setText("l1"))
        tabLayout.addTab(tabLayout.newTab().setText("l2"))
        tabLayout.addTab(tabLayout.newTab().setText("Tous"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = tabs_adapters(this,supportFragmentManager,tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}