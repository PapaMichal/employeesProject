package com.example.employeesassignment.userlogin;

import android.os.Bundle;
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
import com.example.employeesassignment.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    private EditText etLoginUsername;
    private EditText etLoginPass;
    private FragmentLoginBinding binding;
    public Login() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static boolean checkAndPromptField(EditText et) {
        if (et.getText().toString().length() == 0) {
            if (et.getId() == R.id.et_login_username) {
                et.setError("Enter your username");
                return false;
            }
            else if (et.getId() == R.id.et_login_pass) {
                et.setError("Enter your password");
                return false;
            }
        }
        return true;
    }

    private static class EmptyTextListener implements TextView.OnEditorActionListener {
        private final EditText et;

        public EmptyTextListener(EditText editText) {
            this.et = editText;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                checkAndPromptField(et);
            }
            return false;
        }
    }

    private void setSessionInfoInNavDrawer(String username) {
        SessHelper sessHelper = SessHelper.getInstance(getActivity());
        sessHelper.login(username, SqlHandler.getInstance(binding.getRoot().getContext()).
                getEmailByUsername(username));
        ((MainActivity)(getActivity())).setNavHeaderUserAndEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        SqlHandler db = SqlHandler.getInstance(binding.getRoot().getContext());

        TextView tvErrorPrompt = binding.tvLoginErrorPrompt;
        etLoginUsername = binding.etLoginUsername;
        etLoginPass = binding.etLoginPass;
        etLoginUsername.setOnEditorActionListener(new EmptyTextListener(etLoginUsername));
        etLoginPass.setOnEditorActionListener(new EmptyTextListener(etLoginPass));


        binding.btnLogin.setOnClickListener(view -> {
            String username = binding.etLoginUsername.getText().toString();
            String pass = binding.etLoginPass.getText().toString();
            if (checkAndPromptField(etLoginUsername))
            {
                if (!db.doesUserExist(username))
                    etLoginUsername.setError("Username doesn't exist.");
                else if (checkAndPromptField(etLoginPass) && db.isPasswordCorrect(username, pass)) {
                    setSessionInfoInNavDrawer(username);
                    Navigation.findNavController(view).navigate(R.id.action_login_to_employeeList);
                } else {
                    tvErrorPrompt.setText("Wrong password. Please try again.");
                    etLoginPass.setText("");
                }
            }
            else
                checkAndPromptField(etLoginPass);
        });

        binding.tvRegistrationReferer.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_login_to_register));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}