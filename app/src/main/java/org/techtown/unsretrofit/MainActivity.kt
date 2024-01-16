package org.techtown.unsretrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.techtown.unsretrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tag: String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initButton()
    }

    private fun initButton() = with(binding) {
        postListButton.setOnClickListener { getPostList() }
        queryPostButton.setOnClickListener { getQueryPost(3) }
        pathPostButton.setOnClickListener { getPathPost(20) }
    }

    private fun setData(code: Int, post: Post) = with(binding) {
        post.apply {
            StringBuilder()
                .append("CODE: ").append(code)
                .append(", ID: ").append(id)
                .append(", UserID: ").append(userId)
                .appendLine("title: ").append(title)
                .appendLine("body").appendLine(body)
                .apply { resultText.text = this }
        }
    }

    private fun getPostList() {
        val method = object {}.javaClass.enclosingMethod?.name
        TestClient.api.getPostList().enqueue(object : Callback<PostsData> {
            override fun onResponse(call: Call<PostsData>, response: Response<PostsData>) {
                val code = response.code()
                if (response.isSuccessful) {
                    response.body()?.apply {
                        AppData.error(tag, "$method isSuccessful. size: ${this.size}")
                        AppData.debug(tag, "data: $this")
                        this.first().apply {
                            val post = Post(id = id, userId = userId, title = title, body = body)
                            setData(code, post)
                        }
                    }
                } else AppData.error(tag, "$method isNotSuccessful. code: $code")
            }

            override fun onFailure(call: Call<PostsData>, t: Throwable) {
                AppData.error(tag, "$method isFail, ${t.message}")
            }
        })
    }

    private fun getQueryPost(postId: Int) {
        val method = object {}.javaClass.enclosingMethod?.name
        TestClient.api.getQueryPost(postId).enqueue(object : Callback<PostsData> {
            override fun onResponse(call: Call<PostsData>, response: Response<PostsData>) {
                val code = response.code()
                if (response.isSuccessful) {
                    response.body()?.apply {
                        AppData.error(tag, "$method isSuccessful. size: ${this.size}")
                        AppData.debug(tag, "data: $this")
                        this[0].apply {
                            val post = Post(id = id, userId = userId, title = title, body = body)
                            setData(code, post)
                        }
                    }
                } else AppData.error(tag, "$method isNotSuccessful. code: $code")
            }

            override fun onFailure(call: Call<PostsData>, t: Throwable) {
                AppData.error(tag, "$method isFail, ${t.message}")
            }
        })
    }

    private fun getPathPost(postId: Int) {
        val method = object {}.javaClass.enclosingMethod?.name
        TestClient.api.getPathPost(postId).enqueue(object : Callback<PostData> {
            override fun onResponse(call: Call<PostData>, response: Response<PostData>) {
                val code = response.code()
                if (response.isSuccessful) {
                    response.body()?.apply {
                        AppData.error(tag, "$method isSuccessful.")
                        AppData.debug(tag, "data: $this")
                        val post = Post(id = id, userId = userId, title = title, body = body)
                        setData(code, post)
                    }
                } else AppData.error(tag, "$method isNotSuccessful. code: $code")
            }

            override fun onFailure(call: Call<PostData>, t: Throwable) {
                AppData.error(tag, "$method isFail, ${t.message}")
            }
        })
    }
}