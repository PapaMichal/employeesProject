package com.example.employeesassignment.userlogin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.employeesassignment.MainActivity;
import com.example.employeesassignment.R;
import com.example.employeesassignment.SessHelper;
import com.example.employeesassignment.database.SqlHandler;
import com.example.employeesassignment.databinding.FragmentRegisterBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class Register extends Fragment {

    private Map<EditText, FormInput> fields = new HashMap<>();
    private EditText etUsername, etPass, etRepeatPass, etEmail;
    private FragmentRegisterBinding binding;
    private SqlHandler db;

    private boolean promptAllFields() {
        boolean areFieldsValid = true;
        for (FormInput formInput : fields.values()) {
            if (!formInput.onEnterPrompt()) {
                areFieldsValid = false;
            }
        }
        return areFieldsValid;
    }

    public Register() {
    }

    public static final Register newInstance() {
        return new Register();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean isUsernameTaken() { return db.doesUserExist(etUsername.getText().toString()); }
    private boolean isEmailTaken() { return db.doesEmailExist(etEmail.getText().toString()); }
    public boolean isPasswordConfirmationValid() {return etRepeatPass.getText().toString().compareTo(etPass.getText().toString()) == 0; }

    private boolean displayIfEmpty(EditText et) {
        if (et.getText().toString().length() == 0) {
            et.setError("This field is required.");
            return true;
        }
        return false;
    }


    private class UsernameFormInput extends FormInput {
        @Override
        public boolean onEnterPrompt() {
            if (displayIfEmpty(etUsername) || !onTextChangePrompt()) {
                return false;
            }
            if (isUsernameTaken()) {
                etUsername.setError("Username is already taken.");
                return false;
            }
            return true;
        }

        @Override
        public boolean onTextChangePrompt() {
            return InputPatterns.USERNAME.validateAndDisplay(etUsername);
        }
    }

    private class EmailFormInput extends FormInput {
        @Override
        public boolean onEnterPrompt() {
            if (displayIfEmpty(etEmail) || !onTextChangePrompt()) {
                return false;
            }
            if (isEmailTaken()) {
                etEmail.setError("Email is already taken.");
                return false;
            }
            return true;
        }

        @Override
        public boolean onTextChangePrompt() {
            return InputPatterns.EMAIL.validateAndDisplay(etEmail);
        }
    }

    private class PasswordFormInput extends FormInput {
        @Override
        public boolean onEnterPrompt() {
            return !displayIfEmpty(etPass) && onTextChangePrompt();
        }

        @Override
        public boolean onTextChangePrompt() {
            return InputPatterns.PASSWORD.validateAndDisplay(etPass);
        }
    }

    private class RepeatPasswordFormInput extends FormInput {
        @Override
        public boolean onEnterPrompt() {
            if (!isPasswordConfirmationValid()) {
                etRepeatPass.setError("Passwords don't match");
                return false;
            }
            return true;
        }

        @Override
        public boolean onTextChangePrompt() {
            return true;
        }
    }

    class OnTextChangeValidator implements TextWatcher {
        private final EditText et;
        public OnTextChangeValidator(EditText et) {
            this.et = et;
        }
        public void afterTextChanged(Editable s) {}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (!fields.get(et).onTextChangePrompt()) {
                et.requestFocus();
            }
        }
    }

    private class EmptyTextListener implements TextView.OnEditorActionListener {
        private final EditText et;

        public EmptyTextListener(EditText editText) {
            this.et = editText;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                fields.get(et).onEnterPrompt();
            }
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.colorPrimaryRegister));

        //Get edit text objects of the input fields
        etUsername = binding.etRegisterUsername;
        etPass =  binding.etRegisterPass;
        etRepeatPass = binding.etRegisterRepeatPass;
        etEmail = binding.etRegisterEmail;
        fields.put(etUsername, new UsernameFormInput());
        fields.put(etPass, new PasswordFormInput());
        fields.put(etEmail, new EmailFormInput());
        fields.put(etRepeatPass, new RepeatPasswordFormInput());

        for (EditText etInputField: fields.keySet()) {
            etInputField.addTextChangedListener(new OnTextChangeValidator(etInputField));
            etInputField.setOnEditorActionListener(new EmptyTextListener(etInputField));
        }
        db = SqlHandler.getInstance(v.getContext());
        TextView tvRegisterErrorPrompt = binding.tvRegistrationErrorPrompt;

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etEmail.getText().toString();
                if (promptAllFields()) {
                    if (db.register(username, pass, email)) {
                        Navigation.findNavController(view).navigate(R.id.action_register_to_employeeList); //Success
                        SessHelper.getInstance(getActivity()).login(username, email);
                        ((MainActivity)(getActivity())).setNavHeaderUserAndEmail();
                        return;
                    }
                }
                tvRegisterErrorPrompt.setText("Registration failed");
            }
        });

        v.findViewById(R.id.tv_login_referer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_register_to_login);
            }
        });
        return v;
    }
}