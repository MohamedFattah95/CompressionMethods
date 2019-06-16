package com.mohamedfattah95.compressionmethods;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RunLengthActivity extends AppCompatActivity {

    @BindView(R.id.tvResult)
    TextView tvResult;

    @BindView(R.id.etText)
    EditText etText;

    @BindView(R.id.rgType)
    RadioGroup rgType;

    @BindView(R.id.rbText)
    RadioButton rbText;

    @BindView(R.id.rbBinary)
    RadioButton rbBinary;

    @BindView(R.id.btnCompress)
    Button btnCompress;

    @BindView(R.id.btnDecompress)
    Button btnDecompress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_length);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.run_length);
        ButterKnife.bind(this);

        btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etText.getText().toString();
                if (msg.length() <= 0) {
                    Toast.makeText(getBaseContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
                } else {
                    if (rbBinary.isChecked() && (etText.getText().toString().contains("0") ||
                            etText.getText().toString().contains("1"))) {
                        tvResult.setText(binaryCompress(etText.getText().toString()));
                    } else if (rbText.isChecked()) {
                        tvResult.setText(textCompress(etText.getText().toString()));
                    } else {
                        Toast.makeText(getBaseContext(), "Wrong input", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDecompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etText.getText().toString();
                if (msg.length() <= 0) {
                    Toast.makeText(getBaseContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        if (rbBinary.isChecked()) {
                            tvResult.setText(binaryDecompress(etText.getText().toString()));
                        } else if (rbText.isChecked()) {
                            tvResult.setText(textDecompress(etText.getText().toString()));
                        }
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Wrong input", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        tvResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etText.setText(tvResult.getText().toString());
            }
        });

    }

    public String textCompress(String msg) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            int runLength = 1;
            while (i + 1 < msg.length() && msg.charAt(i) == msg.charAt(i + 1)) {
                runLength++;
                i++;
            }
            stringBuilder.append(msg.charAt(i)).append(",").append(runLength).append(" ");
        }
        return stringBuilder.toString();
    }

    public String textDecompress(String msg) {
        String[] data = msg.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        for (String aData : data) {
            String[] ch = aData.split(",");
            for (int j = 0; j < Integer.parseInt(ch[1]); j++) {
                stringBuilder.append(ch[0]);
            }
        }
        return stringBuilder.toString();
    }

    public String binaryCompress(String msg) {
        StringBuilder stringBuilder = new StringBuilder();

        if (!(msg.charAt(0) == '0')) {
            stringBuilder.append("0 ");
        }

        for (int i = 0; i < msg.length(); i++) {
            int runLength = 1;
            while (i + 1 < msg.length() && msg.charAt(i) == msg.charAt(i + 1)) {
                runLength++;
                i++;
            }
            stringBuilder.append(runLength).append(" ");
        }
        return stringBuilder.toString();
    }

    public String binaryDecompress(String msg) {
        String[] data = msg.split(" ");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < data.length; i++) {

            int x = Integer.parseInt(data[i]);
            for (int j = 0; j < x; j++) {
                if (i % 2 == 0) {
                    stringBuilder.append("0");
                } else {
                    stringBuilder.append("1");
                }
            }

        }
        return stringBuilder.toString();
    }
}
