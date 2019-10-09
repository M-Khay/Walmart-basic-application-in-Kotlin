package com.walmart.labs.tha.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walmart.labs.tha.R
import com.walmart.labs.tha.api.testserver.data.Product
import com.walmart.labs.tha.di
import com.walmart.labs.tha.ui.controllers.ProductListAdapter
import com.walmart.labs.tha.ui.controllers.ProductListViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import java.lang.ref.WeakReference

/**
 * Fragment that displays a list of products
 */
class ProductListFragment : PagedProductFragment<Product>() {

    lateinit  var initialDataSpinner : ProgressBar
    companion object {
        const val TAG = "ProductListFragment"
        const val EXTRA_POSITION = "extra_position"
    }

    private var listenerRef: WeakReference<Listener>? = null

    override val adapter by lazy { ProductListAdapter(::handleItemClicked) }
    override val layoutManager
        get () = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    override fun onStart() {
        super.onStart()

        val repo = requireContext().di().productRepo
        val viewModelFactory = ProductListViewModel.Factory(repo, 10, 1)

        viewModel = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                .get(ProductListViewModel::class.java)

        viewModel.products.observe(this, Observer<PagedList<Product>> {
            Log.d(TAG, "updatedPagedList: ${it?.size ?: 0}")
            showContent(it?.size ?: 0 != 0)
            adapter.submitList(it)
            initialDataSpinner.visibility = View.GONE

        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Attach listener (required)
        when {
            parentFragment is Listener -> setListener(parentFragment as Listener)
            context is Listener -> setListener(context as Listener)
            else -> throw IllegalStateException("ProductListFragment Host does not implement Listener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialDataSpinner = view.findViewById(R.id.loading_data_spinner)
    }

    private fun setListener(listener: Listener) {
        listenerRef = WeakReference(listener)
    }

    private fun handleItemClicked(view: View, position: Int, key: String) {
        Log.d(TAG, "itemClicked: $position")
        if (listenerRef != null) {
            val itemSelected = listenerRef?.get()
            if (itemSelected != null) {
                itemSelected.onItemClicked(position, view, key)
            } else {
                Log.d(TAG, "handleItemClicked - listener reference freed before used")
            }
        } else {
            Log.d(TAG, "handleItemClicked - listener not registered")
        }
    }

    /**
     * Interface for delegating item click to host.
     */
    interface Listener {
        /**
         * Item click event handling
         *
         * @param position the position of the item within the list that was clicked
         * @param sharedElementView the view that was clicked
         * @param sharedElementTransitionName
         */
        fun onItemClicked(position: Int, sharedElementView: View, sharedElementTransitionName: String)
    }
}
