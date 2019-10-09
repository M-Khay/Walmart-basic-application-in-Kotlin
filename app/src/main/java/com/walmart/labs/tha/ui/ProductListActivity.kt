package com.walmart.labs.tha.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet
import com.walmart.labs.tha.R
import com.walmart.labs.tha.ui.fragments.ProductDetailFragment
import com.walmart.labs.tha.ui.fragments.ProductListFragment
import kotlinx.android.synthetic.main.activity_product_list.*


class ProductListActivity : AppCompatActivity(), ProductListFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        setSupportActionBar(toolbar)

        val fragment = getProductListFragment()

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, fragment, LIST_FRAG_TAG)
                    .commit()
        }
    }


    /**
     * Handles item click from ProductList to show shared element transition to ProductDetailsFragment
     */
    override fun onItemClicked(position: Int, sharedElementView: View, sharedElementTransitionName: String) {
        val otherFragment = ProductDetailFragment.newInstance(position).apply {
            sharedElementEnterTransition = FormTransition()
            sharedElementReturnTransition = FormTransition()
            exitTransition = Fade()
        }
        getProductListFragment().exitTransition = Fade()
        supportFragmentManager.beginTransaction()
                .addSharedElement(sharedElementView, sharedElementTransitionName)
                .replace(R.id.content, otherFragment)
                .addToBackStack(otherFragment.tag)
                .commit()
    }

    private fun getProductListFragment(): ProductListFragment {
        var fragment = supportFragmentManager.findFragmentByTag(LIST_FRAG_TAG) as? ProductListFragment
        if (fragment == null) {
            fragment = ProductListFragment()
        }
        return fragment
    }

    private class FormTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform())
        }
    }

    companion object {
        const val LIST_FRAG_TAG = "ProductListFragment"
        const val DETAIL_FRAG_TAG = "ProductDetailFragment"
    }
}
