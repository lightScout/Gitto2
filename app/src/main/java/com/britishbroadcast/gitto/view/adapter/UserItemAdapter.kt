package com.britishbroadcast.gitto.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.databinding.UserItemLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.data.Owner
import com.bumptech.glide.Glide

class UserItemAdapter(var owners: List<GitResponse>, val userItemDelegate: UserItemDelegate): RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder>() {

    inner class UserItemViewHolder(val binding: UserItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

    interface UserItemDelegate{
        fun showRepositories()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val binding = UserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.binding.apply {
            owners[position].let {

                userNameTextview.text = it[0].owner.login
                Glide.with(userAvatarImageview)
                    .load(it[0].owner.avatar_url)
                    .into(userAvatarImageview)
            }
        }

        holder.itemView.setOnClickListener {
            userItemDelegate.showRepositories()
        }
    }

    override fun getItemCount(): Int = owners.size

    fun updateOwners(owners: List<GitResponse>){
        this.owners = owners
        notifyDataSetChanged()
    }

}