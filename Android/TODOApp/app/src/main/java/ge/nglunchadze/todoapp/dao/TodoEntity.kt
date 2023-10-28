package ge.nglunchadze.todoapp.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var pinned: Boolean = false,
    var items: List<TodoItem> = emptyList(),
    var time: Long
): Serializable

data class TodoItem(
    var name: String,
    var isChecked: Boolean = false
): Serializable

class Converters {
    @TypeConverter
    fun fromItemList(itemList: List<TodoItem>): String {
        return Gson().toJson(itemList)
    }

    @TypeConverter
    fun toItemList(itemListString: String): List<TodoItem> {
        val itemType = object : TypeToken<List<TodoItem>>() {}.type
        return Gson().fromJson(itemListString, itemType)
    }
}