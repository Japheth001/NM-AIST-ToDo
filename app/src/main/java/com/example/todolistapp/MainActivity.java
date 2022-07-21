package com.example.todolistapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDatabaseHelper taskHelper;
    private ListView TaskList;
    private ArrayAdapter<String> arrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskHelper = new TaskDatabaseHelper(this);
        TaskList = (ListView) findViewById(R.id.list_todo);

        updateUI();



//Added
        //view.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,TasksDoneActivity.class));
           // }
       // });

        //End
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_completed_tasks:
                Intent intent=new Intent(MainActivity.this,TasksDoneActivity.class);
                startActivity(intent);

                return true;

            case R.id.action_add_task:
                final EditText taskEdit = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task").setMessage("What do you want to do next?").setView(taskEdit)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEdit.getText());
                                SQLiteDatabase db = taskHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();

                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                                values.put(TaskContract.TaskEntry.COL_TASK_IS_DONE, "0");
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null,
                                        values, SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null).create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void markAsDone(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.title_task);
        String title = String.valueOf(taskTextView.getText());

        SQLiteDatabase db = taskHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_IS_DONE , "1");
        //Cursor cursor=db.rawQuery("UPDATE"+ TaskContract.TaskEntry.TABLE +
                        //" SET status = +2+ " + "WHERE status="1");

        db.update(TaskContract.TaskEntry.TABLE,
                values,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{title});
        db.close();
        updateUI();
    }

    public Cursor getData(){
        SQLiteDatabase db=taskHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select * from " + TaskContract.TaskEntry.TABLE +" " +
                "WHERE status = '2'", null );
        return cursor;
    }



    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = taskHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                TaskContract.TaskEntry.COL_TASK_IS_DONE + " = ?", new String[]{"0"}, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (arrAdapter == null) {
            arrAdapter = new ArrayAdapter<>(this, R.layout.todo_task, R.id.title_task, taskList);
            TaskList.setAdapter(arrAdapter);
        } else {
            arrAdapter.clear();
            arrAdapter.addAll(taskList);
            arrAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
}












