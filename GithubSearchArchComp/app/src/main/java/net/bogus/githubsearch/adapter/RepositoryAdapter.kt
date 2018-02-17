package net.bogus.githubsearch.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.bogus.githubsearch.R
import net.bogus.githubsearch.databinding.RepositoryRowBinding
import net.bogus.githubsearch.model.Repository
import net.bogus.githubsearch.viewmodel.MainActivityViewModel

/**
 * Created by burak on 11/20/17.
 */

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {

    private var data:List<Repository> = ArrayList()

    constructor(viewModel:MainActivityViewModel) : super() {
        viewModel.result.subscribe {
            result ->
            val diffResult = DiffUtil.calculateDiff(RepositoryAdapterDiffCallback(data, result))
            data = result
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        data[position].let {
            it.let {
                holder?.bind(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.count()
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
        private val binding:RepositoryRowBinding?
        constructor(itemView: View?) : super(itemView) {
            binding = DataBindingUtil.bind<RepositoryRowBinding>(itemView)
        }

        fun bind(repository:Repository) {
            binding?.repository = repository
        }
    }

}