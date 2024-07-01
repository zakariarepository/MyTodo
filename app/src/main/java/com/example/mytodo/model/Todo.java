package com.example.mytodo.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo {
    private long id;
    private Date date;
    private String content;
    private boolean isCompleted;

    // constructeurs
    public Todo(Date date, String content, boolean isCompleted) {
        this.date = date;
        this.content = content;
        this.isCompleted = isCompleted;
    }
    public Todo(String content) {
        this.content = content;
        this.date = new Date();
        this.isCompleted = false;
    }
    public Todo() {
        this("");
    }
    // méthode statique d’instanciation
    public static Todo fromCursor(Cursor cursor) {
        Todo todo = new Todo();
        todo.setContent(cursor.getString(1));
        todo.setId(cursor.getLong(0));
        todo.parseDate(cursor.getString(2));
        todo.setCompleted(cursor.getInt(3));
        return todo;
    }

    // getters et setters
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public void setCompleted(int complete){
        isCompleted = (complete==1);
    }
    public void reverseCompletion() {
        isCompleted = !isCompleted;
    }
    @Override
    public String toString() {
        return "Todo-"+id+"->" +
                "for: " + date +
                " | '" + content + '\'' +
                " | " + isCompleted ;
    }

    // fonctions & méthodes de reformatage
    public void update(String content) {
        this.setContent(content);
        this.setDate(new Date());
    }
    public int isCompletedNumber() {
        if (this.isCompleted) return 1;
        return 0;
    }
    public String getFormattedDate() {
        return new SimpleDateFormat("yyyy-MM-dd h:mm")
                .format(date);
    }
    private void parseDate(String date) {
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm")
                    .parse(date);
        } catch (ParseException e) {
            this.date = new Date();
        }
    }
    public ContentValues toContentValues(){
        ContentValues result = new ContentValues();
        result.put("content",content);
        result.put("todo_date",getFormattedDate());
        result.put("completed",isCompletedNumber());
        return result;
    }
}
