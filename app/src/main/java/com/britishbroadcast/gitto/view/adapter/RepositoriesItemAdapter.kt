
package com.britishbroadcast.gitto.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.databinding.RepositoriesItemLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponseItem

class RepositoriesItemAdapter(var repositories: List<GitResponseItem>, val repositoryItemDelegate: RepositoryItemDelegate):
    RecyclerView.Adapter<RepositoriesItemAdapter.RepositoriesItemViewHolder>() {

    inner class RepositoriesItemViewHolder(val binding: RepositoriesItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    interface RepositoryItemDelegate {
        fun showCommits(login: String, name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoriesItemViewHolder {
        var binding = RepositoriesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoriesItemViewHolder(binding)
    }

    override fun getItemCount(): Int = repositories.size

    override fun onBindViewHolder(holder: RepositoriesItemViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        repositories[position].apply {
            holder.binding.repositoriesNameTextview.text = name

            holder.itemView.setOnClickListener {
                repositoryItemDelegate.showCommits(owner.login, name)
            }
        }
    }

    fun updateRepositories(repositories: List<GitResponseItem>){
        this.repositories = repositories
        notifyDataSetChanged()
    }
}