package ge.nglunchadze.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ge.nglunchadze.todoapp.adapters.TodoListAdapter
import ge.nglunchadze.todoapp.dao.TodoDatabase
import ge.nglunchadze.todoapp.dao.TodoEntity
import ge.nglunchadze.todoapp.databinding.ActivityMainBinding
import ge.nglunchadze.todoapp.viewmodels.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TodoListAdapter
    private lateinit var pinnedAdapter: TodoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var todoDatabase = TodoDatabase.getInstance(applicationContext)
        var todoDao = todoDatabase.todoDao()
        adapter = TodoListAdapter()
        pinnedAdapter = TodoListAdapter()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observeList()
        addRecyclerViews()
//        viewModel.deleteTable()
        addTextChangedListener()
        binding.addTodoButton.setOnClickListener {
            val myIntent = Intent(binding.root.context, ItemDetailsActivity::class.java)
            myIntent.putExtra("item", TodoEntity(id=0, name = "", time = 0))
            binding.root.context.startActivity(myIntent)
        }
    }

    private fun addTextChangedListener() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
                pinnedAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun addRecyclerViews() {
        binding.recyclerView.adapter = adapter
        binding.pinnedRecyclerView.adapter = pinnedAdapter
        binding.pinnedRecyclerView.apply{
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        val spacingHorizontal = resources.getDimensionPixelSize(R.dimen.horizontal_spacing)
        val spacingVertical = resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        val itemSpacingDecoration = ItemSpacingDecoration(spacingHorizontal, spacingVertical)
        binding.recyclerView.addItemDecoration(itemSpacingDecoration)
    }

    private fun observeList() {
        viewModel.todoList.observe(this) { todoList ->
            val pinnedItems = todoList.filter{it.pinned}
            val unpinnedItems = todoList.filter{!it.pinned}
            if(pinnedItems.isNotEmpty()){
                pinnedAdapter.setItems(pinnedItems)
                adapter.setItems(unpinnedItems)
                binding.pinnedRecyclerView.visibility = View.VISIBLE
                binding.otherTextView.visibility = View.VISIBLE
                binding.pinnedTextView.visibility = View.VISIBLE
            }else{
                adapter.setItems(todoList)
                binding.pinnedRecyclerView.visibility = View.GONE
                binding.otherTextView.visibility = View.GONE
                binding.pinnedTextView.visibility = View.GONE
            }
        }
    }
}
