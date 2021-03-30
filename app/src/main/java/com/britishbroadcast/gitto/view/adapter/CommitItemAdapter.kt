package com.britishbroadcast.gitto.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.databinding.CommitItemLayoutBinding
import com.britishbroadcast.gitto.model.data.GitUserCommitItem

class CommitItemAdapter(var gitCommits: List<GitUserCommitItem>): RecyclerView.Adapter<CommitItemAdapter.CommitItemViewHolder>() {

    inner class CommitItemViewHolder(val binding: CommitItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitItemViewHolder {
        var binding = CommitItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommitItemViewHolder(binding)
    }


    override fun getItemCount(): Int = gitCommits.size

    override fun onBindViewHolder(holder: CommitItemViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        gitCommits[position].let {
            Log.d("TAG_X", "commit: ${it.commit.comment_count} - ${it.commit.message}")
            holder.binding.commitInfoTextview.text = it.commit.message
        }
    }

    fun updateCommits(gitItem: List<GitUserCommitItem>){
        gitCommits = gitItem
        notifyDataSetChanged()
    }
}