package net.bogus.githubsearch

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import net.bogus.githubsearch.adapter.RepositoryAdapter
import net.bogus.githubsearch.databinding.ActivityMainBinding
import net.bogus.githubsearch.model.Repository
import net.bogus.githubsearch.viewmodel.MainViewModel
import javax.inject.Inject
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.v7.widget.DividerItemDecoration


class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel:MainViewModel

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (!TextUtils.isEmpty(query)) {
                viewModel.search(query)
            }
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
    }

    val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (recyclerView?.layoutManager as LinearLayoutManager).let {
                val visibleItemCount = it.childCount
                val totalItemCount = it.itemCount
                val firstVisibleItemPosition = it.findFirstVisibleItemPosition()

                if (viewModel.isLoading.get() == false &&
                        visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    viewModel.nextPage()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(onQueryTextListener)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = RepositoryAdapter(viewModel)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(this, 0))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setOnScrollListener(recyclerViewOnScrollListener)
        binding.viewModel = viewModel
        viewModel.error
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    errorString ->
                    Snackbar.make(findViewById<View>(android.R.id.content), errorString,
                            Snackbar.LENGTH_LONG).show()
                }

        findViewById<SwipeRefreshLayout>(R.id.srl)?.setOnRefreshListener {
            viewModel.refresh()
        }
    }

}
