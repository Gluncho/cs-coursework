package ge.nglunchadze.todoapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ge.nglunchadze.todoapp.adapters.DetailedItemsAdapter
import ge.nglunchadze.todoapp.dao.TodoEntity
import ge.nglunchadze.todoapp.dao.TodoItem
import ge.nglunchadze.todoapp.databinding.ActivityItemDetailsBinding
import ge.nglunchadze.todoapp.viewmodels.MainViewModel

class ItemDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailsBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var todo: TodoEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = MainViewModel(application)
        todo = (intent.getSerializableExtra("item") as? TodoEntity)!!
        if(todo.pinned){
            binding.pinButton.setImageResource(R.drawable.ic_pinned)
        }
        addRecyclerViews(binding, todo)
        binding.pinButton.setOnClickListener {
            todo.pinned = !todo.pinned
            if(todo.pinned){
                binding.pinButton.setImageResource(R.drawable.ic_pinned)
            }else{
                binding.pinButton.setImageResource(R.drawable.ic_pin)
            }
        }
        binding.titleTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                todo.name = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.backButton.setOnClickListener {
            goBack(binding, todo)
        }
    }

    override fun onBackPressed() {
        goBack(binding, todo)
    }

    private fun goBack(binding: ActivityItemDetailsBinding, todo: TodoEntity) {
        var adapter: DetailedItemsAdapter = binding.recyclerView.adapter as DetailedItemsAdapter
        var checkedAdapter: DetailedItemsAdapter = binding.checkedRecyclerView.adapter as DetailedItemsAdapter
        var list = ArrayList<TodoItem>(adapter.itemList)
        list.addAll(checkedAdapter.itemList)
        todo.items = list
        if(todo.id == 0){
            viewModel.insert(todo)
        }else{
            viewModel.update(todo)
        }
        binding.root.context.startActivity(Intent(binding.root.context, MainActivity::class.java))
        finish()
    }

    private fun addRecyclerViews(binding: ActivityItemDetailsBinding, todo: TodoEntity?) {
        var handler = CheckboxHandler(binding)
        var adapter = DetailedItemsAdapter(handler, binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
        if (todo != null) {
            adapter.setItems(todo.items.filter{!it.isChecked})
            binding.titleTextView.setText(todo.name)
        }
        var checkedAdapter = DetailedItemsAdapter(handler, binding.checkedRecyclerView)
        binding.checkedRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.checkedRecyclerView.adapter = checkedAdapter
        if(todo != null){
            checkedAdapter.setItems(todo.items.filter{it.isChecked})
        }
        binding.addTodoButton.setOnClickListener {
            adapter.addItem(TodoItem("", false), requestFocus=true)
        }
    }

}

class CheckboxHandler(private val binding: ActivityItemDetailsBinding){
    fun checkboxClicked(todo: TodoItem, isChecked: Boolean) {
        var adapter: DetailedItemsAdapter = binding.recyclerView.adapter as DetailedItemsAdapter
        var checkedAdapter: DetailedItemsAdapter = binding.checkedRecyclerView.adapter as DetailedItemsAdapter
        if(binding.recyclerView.isComputingLayout || binding.checkedRecyclerView.isComputingLayout) {
            return
        }
        todo.isChecked = isChecked
        if(isChecked){
            adapter.removeItem(todo)
            checkedAdapter.addItem(todo)
        }else{
            adapter.addItem(todo)
            checkedAdapter.removeItem(todo)
        }
    }
}