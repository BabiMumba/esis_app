package com.BabiMumba.Esis_app.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

internal class  tabs_adapters (var context:Context, fm: FragmentManager?, var totalTabs:Int): FragmentPagerAdapter(
    fm!!
){
      override fun getCount(): Int {
        return  totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                PrepaFragment()
            }
            1->{
                L1Fragment()
            }
            2->{
                TousFragment()
            }
            else -> getItem(position)
        }
    }


}