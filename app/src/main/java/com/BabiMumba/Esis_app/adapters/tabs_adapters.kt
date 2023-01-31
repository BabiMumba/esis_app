package com.BabiMumba.Esis_app.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.BabiMumba.Esis_app.fragment.tab.L2Fragment
import com.BabiMumba.Esis_app.fragment.tab.L1Fragment
import com.BabiMumba.Esis_app.fragment.tab.TousFragment

internal class  tabs_adapters (var context:Context, fm: FragmentManager?, var totalTabs:Int): FragmentPagerAdapter(
    fm!!
){
      override fun getCount(): Int {
        return  totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                L1Fragment()
            }
            1->{
                L2Fragment()
            }
            2->{
                TousFragment()
            }
            else -> getItem(position)
        }
    }


}