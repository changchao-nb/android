package cn.itcast.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.itcast.notepad.adapter.NotepadAdapter;
import cn.itcast.notepad.bean.NotepadBean;
import cn.itcast.notepad.database.SQLiteHelper;

public class NotepadActivity extends Activity {
    List<NotepadBean> list;
    ListView listView;
    SQLiteHelper mSQLiteHelper;
    NotepadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        //用于显示记录的列表
        listView = (ListView) findViewById(R.id.listview);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotepadActivity.this,RecordActivity.class);
                startActivityForResult(intent,1);
            }
        });
        initData();
    }
    protected void initData(){
        mSQLiteHelper = new SQLiteHelper(this);//创建数据库
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(NotepadActivity.this,RecordActivity.class);
                intent.putExtra("content",notepadBean.getNotepadContent());
                intent.putExtra("id",notepadBean.getId());
                intent.putExtra("time",notepadBean.getNotepadTime());
                NotepadActivity.this.startActivityForResult(intent,1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(NotepadActivity.this)
                        .setMessage("是否删除此纪录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean = list.get(position);
                                if (mSQLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);//删除对应的Item
                                    adapter.notifyDataSetChanged();//更新记事本界面
                                    Toast.makeText(NotepadActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();//关闭对话框
                            }
                        });
                dialog = builder.create();//创建对话框
                dialog.show();//显示对话框
                return true;
            }
        });
    }
    protected void showQueryData(){
        if (list!=null){
            list.clear();
        }
        //从数据库中查询数据
        list = mSQLiteHelper.query();
        adapter = new NotepadAdapter(this,list);
        listView.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1&&resultCode == 2){
            showQueryData();
        }
    }

}
