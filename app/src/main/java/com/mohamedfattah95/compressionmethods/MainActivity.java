package com.mohamedfattah95.compressionmethods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnHuffman)
    public void huffman() {
        startActivity(new Intent(this, HuffmanActivity.class));
    }

    @OnClick(R.id.btnRunLength)
    public void runLength() {
        startActivity(new Intent(this, RunLengthActivity.class));
    }

}
