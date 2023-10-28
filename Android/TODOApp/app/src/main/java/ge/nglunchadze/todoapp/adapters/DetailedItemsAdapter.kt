package ge.nglunchadze.todoapp.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ge.nglunchadze.todoapp.CheckboxHandler
import ge.nglunchadze.todoapp.R
import ge.nglunchadze.todoapp.dao.TodoItem
import ge.nglunchadze.todoapp.databinding.TodoItemDetailsBinding


class DetailedItemsAdapter(private val checkboxHandler: CheckboxHandler,
                           private val recyclerView: RecyclerView):
    RecyclerView.Adapter<DetailedItemsAdapter.DetailedItemsViewHolder>() {
    var itemList: ArrayList<TodoItem> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedItemsAdapter.DetailedItemsViewHolder {
        val binding = TodoItemDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailedItemsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DetailedItemsViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class DetailedItemsViewHolder(private val binding: TodoItemDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: TodoItem) {
            binding.editText.setText(todo.name)
            binding.checkbox.isChecked = todo.isChecked
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                checkboxHandler.checkboxClicked(todo, isChecked)
            }
            if(todo.isChecked){
                binding.editText.isEnabled = false
                binding.editText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                binding.checkbox.buttonTintList = (ColorStateList.valueOf(Color.GRAY))
                return
            }
            addEditTextListeners(binding);
            binding.closeButton.setOnClickListener {
                removeItem(todo)
            }
        }
        fun requestFocusOnEditText() {
            binding.editText.requestFocus()
        }
        private fun addEditTextListeners(binding: TodoItemDetailsBinding) {
            binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    itemList[adapterPosition].name = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            binding.editText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                binding.closeButton.isClickable = hasFocus
                if (hasFocus) {
                    binding.closeButton.visibility = View.VISIBLE
                } else {
                    binding.closeButton.visibility = View.INVISIBLE
                }
            }
        }

    }

    fun setItems(todoItemList: List<TodoItem>) {
        itemList = todoItemList as ArrayList<TodoItem>
        notifyDataSetChanged()
    }

    fun removeItem(todoItem: TodoItem){
        itemList.remove(todoItem)
        notifyDataSetChanged()
    }

    fun addItem(todoItem: TodoItem, requestFocus: Boolean = false){
        itemList.add(todoItem)
        notifyDataSetChanged()
        if (requestFocus) {
            val position = itemList.indexOf(todoItem)
            recyclerView.postDelayed({
                recyclerView.scrollToPosition(itemList.size - 1)
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(itemList.size - 1)
                if (viewHolder != null && viewHolder is DetailedItemsViewHolder) {
                    viewHolder.requestFocusOnEditText()
                }
            }, 200) // Adjust this delay as needed
        }
    }
}