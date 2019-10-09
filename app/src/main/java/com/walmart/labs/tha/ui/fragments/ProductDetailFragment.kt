package com.walmart.labs.tha.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.walmart.labs.tha.api.testserver.data.Product
import com.walmart.labs.tha.ui.controllers.ProductListDetailAdapter

/**
 * Fragment that displays a list of products
 */
class ProductDetailFragment : PagedProductFragment<Product>() {

    companion object {

        const val TAG = "ProductListFragment"
        const val EXTRA_POSITION = "extra_position"

        fun newInstance(position: Int): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_POSITION, position)
            }
            return fragment
        }
    }

    private var startPosition: Int = 0
    private var isDisplayed = false
    private var isTransitionResumed = false

    override val adapter by lazy { ProductListDetailAdapter(::itemLoadedListener) }
    override val layoutManager
        get () = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    override fun onStart() {
        super.onStart()

        viewModel.products.observe(this, Observer<PagedList<Product>> {
            if (!isDisplayed) {
                recyclerView.layoutManager?.scrollToPosition(startPosition)
                isDisplayed = true
            }
            Log.d(TAG, "updatedPagedList: ${it?.size ?: 0}")
            showContent(it?.size ?: 0 != 0)
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stateSource = savedInstanceState ?: arguments ?: Bundle()
        startPosition = stateSource.getInt(EXTRA_POSITION, 0)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        postponeEnterTransition()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_POSITION,startPosition)

    }

    private fun itemLoadedListener() {
        if(!isTransitionResumed) {
            isTransitionResumed = true
            (adapter as? ProductListDetailAdapter)?.clearItemLoadListener()
            activity?.startPostponedEnterTransition()
        }
    }
}
