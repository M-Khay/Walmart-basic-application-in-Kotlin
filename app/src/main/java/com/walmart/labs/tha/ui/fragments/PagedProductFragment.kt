package com.walmart.labs.tha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.walmart.labs.tha.R
import com.walmart.labs.tha.di
import com.walmart.labs.tha.ui.controllers.ProductListViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.*

abstract class PagedProductFragment<T> : Fragment() {

    protected abstract val adapter: PagedListAdapter<T, *>
    protected abstract val layoutManager: RecyclerView.LayoutManager

    protected lateinit var viewModel: ProductListViewModel
    protected lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        val lm = layoutManager
        if (recyclerView.layoutManager == null || recyclerView.layoutManager != lm) {
            recyclerView.layoutManager = layoutManager
        }

        recyclerView.adapter = adapter
        return view
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        val repo = requireContext().di().productRepo
        val viewModelFactory = ProductListViewModel.Factory(repo, 10, 1)

        viewModel = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                .get(ProductListViewModel::class.java)
    }

    protected fun showContent(hasItems: Boolean) {
        if (hasItems) {
            recyclerView.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        }
    }
}