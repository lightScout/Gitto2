package com.britishbroadcast.gitto.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Adapter
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.UserItemLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.model.data.GitUsersResponse
import com.britishbroadcast.gitto.model.data.Item
import com.britishbroadcast.gitto.view.fragment.AddFragment
import com.britishbroadcast.gitto.view.fragment.HomeFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SearchUserItemAdapter(var usersList: List<Item>, val searchUserItemDelegate: AddFragment): RecyclerView.Adapter<SearchUserItemAdapter.SearchUserItemViewHolder>() {

    inner class SearchUserItemViewHolder(val binding: UserItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

    interface SearchUserItemDelegate{
        fun addUser(userName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserItemViewHolder {
        val binding = UserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return SearchUserItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchUserItemViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)


        holder.binding.apply {
            repoCountTextview.visibility = View.INVISIBLE
            privateRepositoryCheckbox.visibility = View.INVISIBLE

            usersList[position].let {item ->
                holder.itemView.setOnClickListener {
                    searchUserItemDelegate.addUser(item.login)
                    Toast.makeText(holder.itemView.context,"${item.login} added successfully!", Toast.LENGTH_SHORT).show()
                }
                Log.d("TAG_J", "onBindViewHolder: ${item.login}")
                userNameTextview.text = "User: ${item.login}"
                Glide.with(userAvatarImageview)
                        .setDefaultRequestOptions(RequestOptions.circleCropTransform())
                        .load(item.avatar_url)
                        .into(userAvatarImageview)
            }


        }

    }

    override fun getItemCount(): Int = usersList.size

    fun updateOwners(users: List<Item>){
        this.usersList = users
        notifyDataSetChanged()
    }

}