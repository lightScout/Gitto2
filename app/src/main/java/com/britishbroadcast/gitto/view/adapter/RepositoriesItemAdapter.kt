
package com.britishbroadcast.gitto.view.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.RepositoriesItemLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponseItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RepositoriesItemViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        repositories[position].apply {
            holder.binding.apply {

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatted = updated_at.format(formatter)
                repositoriesNameTextview.text = name
                lastUpdatedTextview.text = holder.binding.root.context.getString(R.string.last_updated_text, formatted)
                Log.d("TAG_J", "$private" + " $name" + " formatted")
                if(private) {
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