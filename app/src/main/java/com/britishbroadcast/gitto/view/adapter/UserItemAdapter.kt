package com.britishbroadcast.gitto.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.databinding.UserItemLayoutBinding
import com.britishbroadcast.gitto.model.data.Owner
import com.bumptech.glide.Glide

class UserItemAdapter(private var owners: List<Owner>): RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder>() {

    inner class UserItemViewHolder(val binding: UserItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val binding = UserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.binding.apply {
            owners[position].let {
                userNameTextview.text = it.login
                Glide.with(userAvatarImageview)
                    .load(it.avatar_url)
                    .into(userAvatarImageview)
            }
        }
    }

    override fun getItemCount(): Int = owners.size

    fun updateOwners(owners: List<Owner>){
        this.owners = owners
        notifyDataSetChanged()
    }

}