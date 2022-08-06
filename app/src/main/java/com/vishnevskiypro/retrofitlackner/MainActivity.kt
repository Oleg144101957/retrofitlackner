package com.vishnevskiypro.retrofitlackner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vishnevskiypro.retrofitlackner.databinding.ActivityMainBinding
import okio.IOException
import retrofit2.HttpException

const val TAG = "Main Activity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodo()
            } catch (e: IOException){
                Log.d(TAG, "IOException")
                binding.progressBar.isVisible = false
                return@launchWhenCreated

            } catch (e: HttpException){
                Log.d(TAG, "HttpException")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null){
                todoAdapter.todos = response.body()!!
            } else {
                Log.d(TAG, "Response not successful")

            }

            binding.progressBar.isVisible = false

        }




    }



    private fun setupRecyclerView() = binding.rvTodos.apply {
        todoAdapter = TodoAdapter()
        adapter = todoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

}