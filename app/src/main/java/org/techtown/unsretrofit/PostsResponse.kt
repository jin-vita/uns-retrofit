package org.techtown.unsretrofit

import com.google.gson.annotations.SerializedName

class PostsResponse : ArrayList<PostsResponse.PostDataItem>() {
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