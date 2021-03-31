
package com.britishbroadcast.gitto.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.RepositoriesItemLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponseItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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
            holder.binding.apply {
                repositoriesNameTextview.text = name
                lastUpdatedTextview.text = holder.binding.root.context.getString(R.string.last_updated_text, updated_at)

                if(private == true) {
                    Glide.with(privateImageview)
                        .setDefaultRequestOptions(RequestOptions.circleCropTransform())
                        .load(R.drawable.lock_24)
                        .into(privateImageview)
                    privateImageview.visibility = View.VISIBLE
                }
            }

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