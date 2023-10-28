package ge.nglunchadze.todoapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ge.nglunchadze.todoapp.dao.TodoDao
import ge.nglunchadze.todoapp.dao.TodoDatabase
import ge.nglunchadze.todoapp.dao.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao: TodoDao = TodoDatabase.getInstance(application).todoDao()
    val todoList: LiveData<List<TodoEntity>> = todoDao.getTodoList()

    fun insert(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            todo.time = System.currentTimeMillis()
            todoDao.insert(todo)
        }
    }

    fun update(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            todo.time = System.currentTimeMillis()
            todoDao.update(todo)
        }
    }

    fun delete(todo: TodoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            todo.time = System.currentTimeMillis()
            todoDao.delete(todo)
        }
    }

    fun deleteTable(){
        viewModelScope.launch(Dispatchers.IO){
            todoDao.deleteTable()
        }
    }
}