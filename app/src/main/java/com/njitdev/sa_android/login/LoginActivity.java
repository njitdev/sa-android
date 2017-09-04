/*
    sa-android
    Copyright (C) 2017 sa-android authors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.njitdev.sa_android.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.njitdev.sa_android.R;
import com.njitdev.sa_android.models.school.AuthInitInfo;
import com.njitdev.sa_android.models.school.AuthResult;
import com.njitdev.sa_android.models.school.SchoolSystemModels;
import com.njitdev.sa_android.utils.ModelListener;
import com.njitdev.sa_android.utils.SAGlobal;
import com.njitdev.sa_android.utils.SAUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText txtStudentLogin, txtStudentPassword, txtCaptcha;
    private ImageView imgCaptcha;
    private Button btnLogin;

    private String tempSessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Controls
        txtStudentLogin = (EditText) findViewById(R.id.txtStudentLogin);
        txtStudentPassword = (EditText) findViewById(R.id.txtStudentPassword);
        txtCaptcha = (EditText) findViewById(R.id.txtCaptcha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        imgCaptcha = (ImageView) findViewById(R.id.imgCaptcha);

        // Fill with saved credentials if available
        String studentLogin = SAUtils.readKVStore(getApplicationContext(), "student_login");
        String studentPassword = SAUtils.readKVStore(getApplicationContext(), "student_password");
        if (studentLogin != null && studentPassword != null) {
            txtStudentLogin.setText(studentLogin);
            txtStudentPassword.setText(studentPassword);
        }

        // Events
        imgCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCaptcha(tempSessionID);
            }
        });

        // Enter key
        txtCaptcha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    btnLogin.callOnClick();
                    return false;
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studentLogin = txtStudentLogin.getText().toString();
                final String studentPassword = txtStudentPassword.getText().toString();
                final String captcha = txtCaptcha.getText().toString();
                if (studentLogin.length() == 0 || studentPassword.length() == 0) {
                    Toast.makeText(LoginActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Busy
                setUIBusy(true);

                // Send login info
                SchoolSystemModels.authSubmit(SAUtils.installationID(getApplicationContext()), tempSessionID, studentLogin, studentPassword, captcha, new ModelListener<AuthResult>() {
                    @Override
                    public void onData(AuthResult result, String message) {
                        setUIBusy(false);
                        if (result.auth_result) {
                            // Succeeded
                            // Save session_id globally in memory
                            if (result.session_id != null) {
                                SAGlobal.studentSessionID = result.session_id;
                            } else {
                                SAGlobal.studentSessionID = tempSessionID;
                            }

                            // Save username / password to local storage
                            SAUtils.writeKVStore(getApplicationContext(), "student_login", studentLogin);
                            SAUtils.writeKVStore(getApplicationContext(), "student_password", studentPassword);

                            // Back to home screen
                            LoginActivity.this.finish();
                        } else {
                            // Failed
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                            // Reset auth
                            initAuth();
                        }
                    }
                });
            }
        });

        // Init auth
        initAuth();
    }

    private void setUIBusy(Boolean busy) {
        // Enable / disable
        txtCaptcha.setEnabled(!busy);
        txtStudentPassword.setEnabled(!busy);
        txtStudentLogin.setEnabled(!busy);
        btnLogin.setEnabled(!busy);

        // Spinner
        ProgressBar pbBusy = (ProgressBar) findViewById(R.id.pbBusy);
        if (busy) {
            pbBusy.setVisibility(View.VISIBLE);
        } else {
            pbBusy.setVisibility(View.INVISIBLE);
        }
    }

    private void initAuth() {
        setUIBusy(true);
        SchoolSystemModels.authInit(new ModelListener<AuthInitInfo>() {
            @Override
            public void onData(AuthInitInfo result, String message) {
                setUIBusy(false);
                if (result == null) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Assign temp session_id
                tempSessionID = result.session_id;
                // Check if captcha is required
                if (result.captcha_enabled) {
                    // Required, fetch captcha
                    fetchCaptcha(tempSessionID);
                } else {
                    EditText txtCaptcha = (EditText) findViewById(R.id.txtCaptcha);
                    txtCaptcha.setHint("无需验证码");
                    txtCaptcha.setEnabled(false);
                }
            }
        });
    }

    private void fetchCaptcha(String session_id) {
        setUIBusy(true);
        SchoolSystemModels.authCaptcha(session_id, new ModelListener<Bitmap>() {
            @Override
            public void onData(Bitmap result, String message) {
                setUIBusy(false);
                if (result == null) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                imgCaptcha.setImageBitmap(result);
            }
        });
    }
}
