package com.example.employeesassignment.employeelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.employeesassignment.Employee;
import com.example.employeesassignment.R;
import com.example.employeesassignment.databinding.FragmentEmployeeProfileBinding;

public class EmployeeProfileFragment extends Fragment {

    private Employee selectedEmployee;
    private FragmentEmployeeProfileBinding binding;

    public EmployeeProfileFragment() {
    }
    public static EmployeeProfileFragment newInstance(String param1, String param2) {
        EmployeeProfileFragment fragment = new EmployeeProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeeProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle arguments = getArguments();
        if (!arguments.isEmpty()) {
            selectedEmployee = EmployeeProfileFragmentArgs.fromBundle(getArguments()).getEmployee();
            binding.tvEmployeeProfileName.setText("Name: " + selectedEmployee.getName());
            binding.tvEmployeeProfileAddress.setText("Address: " + selectedEmployee.getAddress());
            binding.tvEmployeeProfileEducation.setText("Education: " + selectedEmployee.getEducation());
            binding.tvEmployeeProfileEmploymentRate.setText("Employment Rate: " + Integer.toString(selectedEmployee.getEmploymentRate()));
            binding.tvEmployeeProfileFieldsOfExpertise.setText("Fields Of Expertise: " + selectedEmployee.getFieldsOfExpertiseAsString());
            binding.tvEmployeeProfileHourlyPay.setText("Hourly pay: " + Integer.toString(selectedEmployee.getHourlyPay()));
            binding.ivEmployeeProfileImage.setImageBitmap(selectedEmployee.getPicture());
        }
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.md_theme_light_secondary));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
