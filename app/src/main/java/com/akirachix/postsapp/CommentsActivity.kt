package com.akirachix.postsapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akirachix.postsapp.databinding.ActivityCommentsBinding
import com.akirachix.postsapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentsActivity : AppCompatActivity() {
    private var postId = 0
    private lateinit var binding: ActivityCommentsBinding
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extra = intent.extras
        if (extra != null){
            postId = extra.getInt("POST_ID")
            fetchPost(postId)
            fetchCommentsByPostID(postId)
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        commentsAdapter = CommentsAdapter(emptyList())
        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = commentsAdapter
    }

    fun fetchPost(postId: Int){
        val apiClient = ApiClient.buildApiClient(PostsApiInterface::class.java)
        val request = apiClient.fetchPostById(postId)

        request.enqueue(object : Callback<Post> {
            override fun onResponse(p0: Call<Post>, p1: Response<Post>) {
                if (p1.isSuccessful){
                    val post = p1.body()
                    binding.tvPostTitle.text = post?.title
                    binding.tvPostBody.text = post?.body
                } else {
                    Toast.makeText(this@CommentsActivity,
                        p1.errorBody()?.string(),
                        Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(p0: Call<Post>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchCommentsByPostID(postId: Int){
        val apiClient = ApiClient.buildApiClient(PostsApiInterface::class.java)
        val request = apiClient.fetchCommentsByPostId(postId)
        request.enqueue(object : Callback<List<Comments>> {
            override fun onResponse(p0: Call<List<Comments>>, p1: Response<List<Comments>>) {
                if (p1.isSuccessful){
                    val comments = p1.body() ?: emptyList()
                    if (comments.isNotEmpty()){
                        commentsAdapter.commentsList = comments
                        commentsAdapter.notifyDataSetChanged()
                    }   else {
                        Toast.makeText(this@CommentsActivity, "No comments found", Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(this@CommentsActivity, "Error:${p1.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(p0: Call<List<Comments>>, t: Throwable) {
                Toast.makeText(this@CommentsActivity, "Failure: ${t.message}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}