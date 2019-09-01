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

public class CustomerLoginRegisterActivity extends AppCompatActivity
{
    private TextView needCustomerAccount, customerStatus;
    private Button customerLogin, customerRegister;
    private EditText customerEmailInput,customerPasswordInput;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    private DatabaseReference customerDatabaseRef;
    private String onlineCustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_register);

        needCustomerAccount = findViewById(R.id.customerNeedAccId);
        customerStatus = findViewById(R.id.customerStatusId);

        customerLogin = findViewById(R.id.customerLoginId);
        customerRegister = findViewById(R.id.customerRegisterId);
        customerEmailInput = findViewById(R.id.customerEmailId);
        customerPasswordInput = findViewById(R.id.customerPasswordId);

        mAuth = FirebaseAuth.getInstance();


        loadingBar = new ProgressDialog(this);

        needCustomerAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                customerRegister.setVisibility(View.VISIBLE);
                customerLogin.setVisibility(View.INVISIBLE);
                needCustomerAccount.setVisibility(View.INVISIBLE);
                customerRegister.setEnabled(true);
                customerStatus.setText("Customer Register");
            }
        });

        customerRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = customerEmailInput.getText().toString();
                String password = customerPasswordInput.getText().toString();

                if (email.isEmpty())
                {
                    Toast.makeText(CustomerLoginRegisterActivity.this, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty())
                {
                    Toast.makeText(CustomerLoginRegisterActivity.this, "Please Enter Your Pass", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RegisterDriver(email, password);
                }
            }
        });

        customerLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = customerEmailInput.getText().toString();
                String password = customerPasswordInput.getText().toString();

                if (email.isEmpty())
                {
                    Toast.makeText(CustomerLoginRegisterActivity.this, "Please Enter Your Email Id", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty())
                {
                    Toast.makeText(CustomerLoginRegisterActivity.this, "Please Enter Your Pass", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SignInCustomer(email, password);
                }
            }
        });

    }

    private void SignInCustomer(String email, String password)
    {
        loadingBar.setTitle("Customer LogIn");
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
                            Intent intent = new Intent(CustomerLoginRegisterActivity.this, CustomersMapActivity.class);
                            startActivity(intent);

                            Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Logged in Unsuccessful... Please Try again letter", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }

    private void RegisterDriver(String email, String password)
    {
        loadingBar.setTitle("Customer Registration");
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
                            onlineCustomerId = mAuth.getCurrentUser().getUid();
                            customerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(onlineCustomerId);

                            customerDatabaseRef.setValue(true);

                            Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Register Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(CustomerLoginRegisterActivity.this, CustomersMapActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(CustomerLoginRegisterActivity.this, "Customer Register Unsuccessful... Please Try again letter", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }
}
