package org.techtown.unsretrofit

import com.google.gson.annotations.SerializedName

class PostsData : ArrayList<PostsData.PostDataItem>() {
    data class PostDataItem(
        @SerializedName("body")
        val body: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("userId")
        val userId: Int
    )
}