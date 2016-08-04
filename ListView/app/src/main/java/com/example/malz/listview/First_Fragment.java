package com.example.malz.listview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by malz on 6/9/16.
 */
public class First_Fragment extends Fragment {
    private onButtonclickselectedListener cont;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);


        Button button = (Button) view.findViewById(R.id.btnAboutMe);
       // Button button1 = (Button) view.findViewById(R.id.btnTask1);
        //Button button2 = (Button) view.findViewById(R.id.btnTask2);
        Button button3 = (Button) view.findViewById(R.id.btnTask3);

        cont = (onButtonclickselectedListener)getContext();
        //button.setBackground(GREEN);
        button.setOnClickListener(Button1Listener);
        //button1.setOnClickListener(Button1Listener1);
       // button2.setOnClickListener(Button1Listener2);
        button3.setOnClickListener(Button1Listener3);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("add");
        super.onCreateOptionsMenu(menu, inflater);
    }

    public  interface onButtonclickselectedListener{

        public void onButtonClickListener(int position);
    }
    View.OnClickListener Button1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cont.onButtonClickListener(0);
        }
    };
  /* // View.OnClickListener Button1Listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cont.onButtonClickListener(1);
        }
    };
    //View.OnClickListener Button1Listener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cont.onButtonClickListener(2);
        }
    };*/
    View.OnClickListener Button1Listener3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cont.onButtonClickListener(1);
        }
    };
    public First_Fragment(){

    }
}