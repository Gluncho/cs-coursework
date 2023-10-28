package com.example.listdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.listdemo.databinding.ActivityFragmentsBinding

class FragmentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFragmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*supportFragmentManager.commit {
            add(R.id.framgnet_container, Test3Fragment())
            addToBackStack(null)
        }*/

        binding.viewPager2.adapter = TestPagerAdapter(this).apply {

        }
    }
}

class TestPagerAdapter(activity: FragmentsActivity) : FragmentStateAdapter(activity) {
    val fragmentList = listOf(Test2Fragment(), Test3Fragment(), TestFragment())
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}