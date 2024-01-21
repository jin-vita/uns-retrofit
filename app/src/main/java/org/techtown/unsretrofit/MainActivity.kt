package org.techtown.unsretrofit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.techtown.unsretrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

@SuppressLint("SetTextI18n")
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
        queryPostButton.setOnClickListener { getQueryPost(getRandom()) }
        pathPostButton.setOnClickListener { getPathPost(getRandom()) }
    }

    private fun getRandom(): Int = Random.nextInt(1, 150)

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
        val method = Thread.currentThread().stackTrace[2].methodName
        AppData.debug(tag, "$method called.")
        binding.resultText.text = "$method is loading..."
        TestClient.api.getPostList().enqueue(object : Callback<PostsResponse> {
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                val code = response.code()
                if (response.isSuccessful) {
                    response.body()?.apply {
                        AppData.error(tag, "$method isSuccessful. size: $size")
                        AppData.debug(tag, "data: $this")
                        this.first().apply {
                            val post = Post(id = id, userId = userId, title = title, body = body)
                            setData(code, post)
                        }
                    }
                } else binding.resultText.text = "$method isNotSuccessful. code: $code"
            }

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                binding.resultText.text = "$method isFail, ${t.message}"
            }
        })
    }

    private fun getQueryPost(postId: Int) {
        val method = Thread.currentThread().stackTrace[2].methodName
        AppData.debug(tag, "$method called. postId: $postId")
        binding.resultText.text = "$method is loading..."
        TestClient.api.getQueryPost(postId).enqueue(object : Callback<PostsResponse> {
            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                val code = response.code()
                if (response.isSuccessful) {
                    response.body()?.apply {
                        AppData.error(tag, "$method isSuccessful. size: $size")
                        AppData.debug(tag, "data: $this")

                        if (size == 0) {
                            binding.resultText.text = "There is no postId $postId post."
                            return
                        }

                        this[0].apply {
                            val post = Post(id = id, userId = userId, title = title, body = body)
                            setData(code, post)
                        }
                    }
                } else binding.resultText.text = "$method isNotSuccessful. code: $code, postId: $postId"
            }

            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                binding.resultText.text = "$method isFail, ${t.message}"
            }
        })
    }

    private fun getPathPost(postId: Int) {
        val method = Thread.currentThread().stackTrace[2].methodName
        AppData.debug(tag, "$method called. postId: $postId")
        binding.resultText.text = "$method is loading..."
        TestClient.api.getPathPost(postId).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                val code = response.code()
                if (response.isSuccessful) {
                    response.body()?.apply {
                        AppData.error(tag, "$method isSuccessful.")
                        AppData.debug(tag, "data: $this")
                        val post = Post(id = id, userId = userId, title = title, body = body)
                        setData(code, post)
                    }
                } else binding.resultText.text = "$method isNotSuccessful. code: $code, postId: $postId"
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                binding.resultText.text = "$method isFail, ${t.message}"
            }
        })
    }
}