package net.bogus.githubsearch.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.subjects.PublishSubject
import net.bogus.githubsearch.R
import net.bogus.githubsearch.databinding.RepositoryRowBinding
import net.bogus.githubsearch.model.Repository
import net.bogus.githubsearch.viewmodel.MainViewModel

/**
 * Created by burak on 11/20/17.
 */

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {

    private var data:ArrayList<Repository>? = null

    constructor(viewModel:MainViewModel) : super() {
        viewModel.result.subscribe {
            result ->
            if (result.size > 0) {
                data?.addAll(result)
            } else if (viewModel.currentPage.get() == 1) {
                data = ArrayList()
            }
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        data?.get(position).let {
            it?.let {
                holder?.bind(it)
            }
        }
    }

    override fun getItemCount(): Int {
        data?.let {
            return it.size
        }

        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        parent?.let {
            val view = LayoutInflater.from(it.context).inflate(R.layout.repository_row, it, false)
            val viewHolder = ViewHolder(view)
            return viewHolder
        }
        return ViewHolder(null)
    }


    class ViewHolder : RecyclerView.ViewHolder {

        constructor(itemView: View?) : super(itemView)

        fun bind(repository:Repository) {
            val binding = DataBindingUtil.bind<RepositoryRowBinding>(itemView)
            binding.repository = repository
            binding.executePendingBindings()
        }

    }

}