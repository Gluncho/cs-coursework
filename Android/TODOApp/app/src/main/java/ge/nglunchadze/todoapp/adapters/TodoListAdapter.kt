package ge.nglunchadze.todoapp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ge.nglunchadze.todoapp.ItemDetailsActivity
import ge.nglunchadze.todoapp.dao.TodoEntity
import ge.nglunchadze.todoapp.databinding.TodoListItemBinding

class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>(), Filterable {
    private val todoListFull: MutableList<TodoEntity> = mutableListOf()
    private val todoListFiltered: MutableList<TodoEntity> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding =
            TodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todoListFiltered.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoListFiltered[position])
    }

    inner class TodoViewHolder(private val binding: TodoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: TodoEntity) {
            binding.todoName.text = todo.name
            binding.todoName.visibility = View.VISIBLE
            binding.item1.visibility = View.GONE
            binding.item2.visibility = View.GONE
            binding.item3.visibility = View.GONE
            binding.moreUncheckedItems.visibility = View.GONE
            binding.moreItems.visibility = View.GONE
            setCheckboxTexts(todo)
            binding.mainLayout.setOnClickListener {
                val myIntent = Intent(binding.root.context, ItemDetailsActivity::class.java)
                myIntent.putExtra("item", todo)
                binding.root.context.startActivity(myIntent)
            }
        }

        private fun setCheckboxTexts(todo: TodoEntity) {
            Log.d("tudu", todo.toString())
            if(todo.items.isNotEmpty() && !todo.items[0].isChecked){
                binding.item1.text = todo.items[0].name
                binding.item1.visibility = View.VISIBLE
            }
            if(todo.items.size > 1 && !todo.items[1].isChecked){
                binding.item2.text = todo.items[1].name
                binding.item2.visibility = View.VISIBLE
            }
            if(todo.items.size > 2 && !todo.items[2].isChecked){
                binding.item3.text = todo.items[2].name
                binding.item3.visibility = View.VISIBLE
            }
            if(todo.items.count{!it.isChecked} > 3){
                binding.moreUncheckedItems.visibility = View.VISIBLE
            }
            if(todo.items.count{it.isChecked} > 0){
                binding.moreItems.visibility = if (todo.items.size > 3) View.VISIBLE else View.GONE
                binding.moreItems.text = "+${todo.items.count{it.isChecked}} checked items"
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim() ?: ""

                val filteredList: List<TodoEntity> = if (query.isEmpty()) {
                    todoListFull
                } else {
                    todoListFull.filter { todo ->
                        todo.name.contains(query, ignoreCase = true)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                todoListFiltered.clear()
                todoListFiltered.addAll(results.values as MutableList<TodoEntity>)
                notifyDataSetChanged()
            }
        }
    }

    fun setItems(todoList: List<TodoEntity>) {
        todoListFull.clear()
        todoListFull.addAll(todoList)
//        todoListFull = todoList
//        todoListFiltered = todoList
        todoListFiltered.clear()
        todoListFiltered.addAll(todoList)
        notifyDataSetChanged()
    }
}