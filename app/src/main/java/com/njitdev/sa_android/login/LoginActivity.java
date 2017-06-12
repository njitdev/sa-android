package com.njitdev.sa_android.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.SchoolSystemModels;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAGlobal;
import com.njitdev.sa_android.utils.SAUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Controls
        final EditText txtStudentLogin = (EditText) findViewById(R.id.txtStudentLogin);
        final EditText txtStudentPassword = (EditText) findViewById(R.id.txtStudentPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        // Fill with saved credentials if available
        String studentLogin = SAUtils.readKVStore(getApplicationContext(), "student_login");
        String studentPassword = SAUtils.readKVStore(getApplicationContext(), "student_password");
        if (studentLogin != null && studentPassword != null) {
            txtStudentLogin.setText(studentLogin);
            txtStudentPassword.setText(studentPassword);
        }

        // Events
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studentLogin = txtStudentLogin.getText().toString();
                final String studentPassword = txtStudentPassword.getText().toString();
                if (studentLogin.length() == 0 || studentPassword.length() == 0) {
                    Toast.makeText(LoginActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Busy
                setUIBusy(true);

                // Send login info
                SchoolSystemModels.submitAuthInfo(SAGlobal.installation_id, studentLogin, studentPassword, null, new ModelListener<String>() {
                    @Override
                    public void onData(String result, String message) {
                        setUIBusy(false);
                        if (result != null) {
                            // Succeeded
                            // Save session_id in memory
                            SAGlobal.student_session_id = result;

                            // Save username / password to local storage
                            SAUtils.writeKVStore(getApplicationContext(), "student_login", studentLogin);
                            SAUtils.writeKVStore(getApplicationContext(), "student_password", studentPassword);

                            // Back to home screen
                            LoginActivity.this.finish();
                        } else {
                            // Failed
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void setUIBusy(Boolean busy) {
        EditText txtUserLogin = (EditText) findViewById(R.id.txtStudentLogin);
        EditText txtUserPassword = (EditText) findViewById(R.id.txtStudentPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        // Enable / disable
        txtUserLogin.setEnabled(!busy);
        txtUserPassword.setEnabled(!busy);
        btnLogin.setEnabled(!busy);

        // Spinner
        ProgressBar pbBusy = (ProgressBar) findViewById(R.id.pbBusy);
        if (busy) {
            pbBusy.setVisibility(View.VISIBLE);
        } else {
            pbBusy.setVisibility(View.INVISIBLE);
        }
    }
}
