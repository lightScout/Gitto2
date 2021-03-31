package com.britishbroadcast.gitto.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.gitto.R
import com.britishbroadcast.gitto.databinding.UserItemLayoutBinding
import com.britishbroadcast.gitto.model.data.GitResponse
import com.britishbroadcast.gitto.view.fragment.UserFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserItemAdapter(var owners: MutableList<GitResponse>, val userItemDelegateHomeFragment: UserFragment): RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder>() {

    inner class UserItemViewHolder(val binding: UserItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

    }

    interface UserItemDelegate{
        fun showRepositories(login: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val binding = UserItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.binding.apply {
            owners[position].let {

                userNameTextview.text = holder.binding.root.context.getString(R.string.user_name_text, it[0].owner.login)
                repoCountTextview.text = holder.binding.root.context.getString(R.string.repository_count_text, it.size.toString())
                it.forEach { repo ->
                    if (repo.private == true)
                        privateRepositoryCheckbox.isChecked = true
                }
                Glide.with(userAvatarImageview)
                        .setDefaultRequestOptions(RequestOptions.circleCropTransform())
                    .load(it[0].owner.avatar_url)
                    .into(userAvatarImageview)
            }
        }

        holder.itemView.setOnClickListener {
            userItemDelegateHomeFragment.showRepositories(owners[position][0].owner.login)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        Log.d("TAG_J", "onDestroyView: UserFragment")
    }

    override fun getItemCount(): Int = owners.size

    fun updateOwners(owners: List<GitResponse>){
        if(this.owners.isNotEmpty()){
            owners.forEach {
                if(!this.owners.contains(it))
                    this.owners.add(it)
            }
        }else{
            var newList = mutableListOf<GitResponse>()
            owners.forEach {
                newList.add(it)
            }
            this.owners = newList
        }
        notifyDataSetChanged()
    }

}