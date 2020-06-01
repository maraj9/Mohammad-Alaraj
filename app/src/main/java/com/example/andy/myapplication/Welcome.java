package com.example.andy.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {
    private RadioButton view,insert,udpate,delete;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        view =findViewById(R.id.view);
        insert=findViewById(R.id.insert);
        udpate=findViewById(R.id.update);
        delete=findViewById(R.id.delete);
        btn=findViewById(R.id.btnsubmit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(view.isChecked())
                {

                    Intent my=new Intent(Welcome.this,MainActivity.class);
                    startActivity(my);
                }
                else if(insert.isChecked())
                {

                    Intent my=new Intent(Welcome.this,INSERT.class);
                    startActivity(my);
                }
                else if(udpate.isChecked())
                {

                }
                else if (delete.isChecked())
                {

                }

            }



        });


    }
}
