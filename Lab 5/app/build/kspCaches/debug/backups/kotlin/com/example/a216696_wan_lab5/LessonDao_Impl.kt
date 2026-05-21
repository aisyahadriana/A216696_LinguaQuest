package com.example.a216696_wan_lab5

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class LessonDao_Impl(
  __db: RoomDatabase,
) : LessonDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfLessonEntity: EntityInsertAdapter<LessonEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfLessonEntity = object : EntityInsertAdapter<LessonEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `lesson_history` (`id`,`lessonTitle`,`visitedAt`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: LessonEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.lessonTitle)
        statement.bindLong(3, entity.visitedAt)
      }
    }
  }

  public override suspend fun insert(lesson: LessonEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfLessonEntity.insert(_connection, lesson)
  }

  public override fun getAll(): Flow<List<LessonEntity>> {
    val _sql: String = "SELECT * FROM lesson_history ORDER BY visitedAt DESC"
    return createFlow(__db, false, arrayOf("lesson_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLessonTitle: Int = getColumnIndexOrThrow(_stmt, "lessonTitle")
        val _columnIndexOfVisitedAt: Int = getColumnIndexOrThrow(_stmt, "visitedAt")
        val _result: MutableList<LessonEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: LessonEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpLessonTitle: String
          _tmpLessonTitle = _stmt.getText(_columnIndexOfLessonTitle)
          val _tmpVisitedAt: Long
          _tmpVisitedAt = _stmt.getLong(_columnIndexOfVisitedAt)
          _item = LessonEntity(_tmpId,_tmpLessonTitle,_tmpVisitedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM lesson_history"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
