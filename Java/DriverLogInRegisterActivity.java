package hm.asha.nayeem.uberapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLogInRegisterActivity extends AppCompatActivity
{
    private TextView needDriverAccount, driverStatus;
    private Button driverLogin, driverRegister;
    private EditText driverEmailInput,driverPasswordInput;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    private DatabaseReference driverDatabaseRef;
    private String onlineDriverId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_log_in_register);

        needDriverAccount = findViewById(R.id.driverNeedAccId);
        driverStatus = findViewById(R.id.driverStatusId);

        driverLogin = findViewById(R.id.driverLoginId);
        driverRegister = findViewById(R.id.driverRegisterId);
        driverEmailInput = findViewById(R.id.driverEmailId);
        driverPasswordInput = findViewById(R.id.driverPasswordId);

        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        needDriverAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                driverRegister.setVisibility(View.VISIBLE);
                driverLogin.setVisibility(View.INVISIBLE);
                needDriverAccount.setVisibility(View.INVISIBLE);
                driverRegister.setEnabled(true);
                driverStatus.setText("Driver Register");
            }
        });

        driverRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = driverEmailInput.getText().toString();
                String password = driverPasswordInput.getText().toString();

                if (email.isEmpty())
                {
                    Toast.makeText(DriverLogInRegisterActivity.this, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty())
                {
                    Toast.makeText(DriverLogInRegisterActivity.this, "Please Enter Your Pass", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RegisterDriver(email, password);
                }
            }
        });

        driverLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = driverEmailInput.getText().toString();
                String password = driverPasswordInput.getText().toString();

                if (email.isEmpty())
                {
                    Toast.makeText(DriverLogInRegisterActivity.this, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty())
                {
                    Toast.makeText(DriverLogInRegisterActivity.this, "Please Enter Your Pass", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SignInDriver(email, password);
                }
            }
        });
    }

    private void SignInDriver(String email, String password)
    {
        loadingBar.setTitle("Driver LogIn");
        loadingBar.setMessage("Please wait while we are checking your credentials");
        loadingBar.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(DriverLogInRegisterActivity.this, DriversMapsActivity.class);
                            startActivity(intent);

                            Toast.makeText(DriverLogInRegisterActivity.this, "Driver Logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            Toast.makeText(DriverLogInRegisterActivity.this, "Driver Logged in Unsuccessful... Please Try again letter", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }

    private void RegisterDriver(String email, String password)
    {
        loadingBar.setTitle("Driver Registration");
        loadingBar.setMessage("Please wait while we are register your data");
        loadingBar.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            onlineDriverId = mAuth.getCurrentUser().getUid();
                            driverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(onlineDriverId);

                            driverDatabaseRef.setValue(true);

                            Intent intent = new Intent(DriverLogInRegisterActivity.this, DriversMapsActivity.class);
                            startActivity(intent);

                            Toast.makeText(DriverLogInRegisterActivity.this, "Driver Register Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            Toast.makeText(DriverLogInRegisterActivity.this, "Driver Register Unsuccessful... Please Try again letter", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }
}
