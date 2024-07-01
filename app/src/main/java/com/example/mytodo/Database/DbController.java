package com.example.mytodo.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mytodo.model.Todo;

import java.util.ArrayList;

public class DbController {
    // variable de classe
    private SQLiteDatabase sqLiteDatabase;
    // constructeur
    public DbController(Context c){
        this.sqLiteDatabase = new
                DatabaseInitializer(c).generateDB();
    }
    // enregistrer dans la base de données
    public int saveToDatabase(Todo todo) {
        sqLiteDatabase.insert("todos","id",todo.toContentValues());
        return getTodoId(todo);
    }
    // Sélectionner tout les TODOs de la base de données
    public ArrayList<Todo> getAllTodos(){
        ArrayList<Todo> res = new ArrayList<>();
        String SQL = "SELECT * FROM todos ORDER BY todo_date"+
                " DESC;";
        Cursor cursor = sqLiteDatabase.rawQuery(SQL,null);
        if(cursor.moveToFirst()){
            do{
                Todo todo = Todo.fromCursor(cursor);
                res.add(todo);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    // selection par ID de la TODO
    public int getTodoId(Todo todo){
        String SQL = "SELECT * FROM todos WHERE content =\"%s"+
                "\" and todo_date = \"%s\" ;";
        SQL =
                String.format(SQL,todo.getContent(),todo.getFormattedDate());
        Cursor cursor = sqLiteDatabase.rawQuery(SQL,null);
        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }
        return -1;
    }
    // mise à jour du contenu de la TODO
    public void updateTodo(Todo todo){
        String SQL = String.format("UPDATE todos set content ="+
                        "\"%s\" , todo_date =\"%s\" WHERE id = "+todo.getId()
                ,todo.getContent(),todo.getFormattedDate());
        sqLiteDatabase.execSQL(SQL);
    }
    // suppression de la TODO
    public void deleteTodo(long id) {
        sqLiteDatabase.execSQL("DELETE FROM todos WHERE id="+id);
    }
}
