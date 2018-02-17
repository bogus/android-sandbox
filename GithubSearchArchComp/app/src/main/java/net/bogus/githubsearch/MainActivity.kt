package net.bogus.githubsearch

import android.arch.lifecycle.ViewModelProviders
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
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import net.bogus.githubsearch.adapter.RepositoryAdapter
import net.bogus.githubsearch.databinding.ActivityMainBinding
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.v7.widget.DividerItemDecoration
import android.widget.TextView
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerViewAdapter
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import net.bogus.githubsearch.viewmodel.MainActivityViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject lateinit var apiClient:APIClient
    var viewModel:MainActivityViewModel? = null
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        if (viewModel?.apiClient == null) {
            viewModel?.apiClient = apiClient
        }
        viewModel?.let {
            val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            var disposable = RxRecyclerView.scrollEvents(recyclerView)
                    .subscribe({ observer ->
                        val layoutManager = observer.view().layoutManager as? LinearLayoutManager
                        it.shouldLoadNextPage(layoutManager?.childCount ?: 0,
                                layoutManager?.itemCount ?: 0,
                                layoutManager?.findFirstVisibleItemPosition() ?: 0)
                    })
            compositeDisposable.add(disposable)
            recyclerView.adapter = RepositoryAdapter(it)
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(DividerItemDecoration(this, 0))
            recyclerView.layoutManager = LinearLayoutManager(this)
            binding.viewModel = it
            it.error
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        errorString ->
                        Snackbar.make(findViewById<View>(android.R.id.content), errorString,
                                Snackbar.LENGTH_LONG).show()
                    }

            findViewById<SwipeRefreshLayout>(R.id.srl)?.setOnRefreshListener {
                it.refresh()
            }

            disposable = RxSearchView.queryTextChangeEvents(findViewById<SearchView>(R.id.searchView))
                    .subscribe({
                        observer ->
                        if(observer.isSubmitted) {
                            it.search(observer.queryText().toString())
                        }
                    })
            compositeDisposable.add(disposable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
