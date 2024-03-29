package com.example.employeesassignment.employeemanagment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeesassignment.Employee;
import com.example.employeesassignment.databinding.EmployeeItemBinding;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private final ArrayList<Employee> employees;

    public EmployeeAdapter(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                EmployeeItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(employees.get(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final EmployeeItemBinding binding;

        public ViewHolder(EmployeeItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Employee employee) {
            binding.tvEmployeeCellName.setText(employee.getName());
            binding.tvEmployeeCellEmploymentRate.setText("Employment Rate: " + employee.getEmploymentRate());
            binding.tvEmployeeCellExpertise.setText(employee.getFieldsOfExpertiseAsString());
            binding.tvEmployeeCellHourlyPay.setText("Hourly Pay: " + employee.getHourlyPay());

            if (employee.getPicture() != null) {
                binding.ivEmployeeCellImg.setImageBitmap(employee.getPicture());
            }

            //Open employee profile on click
            itemView.setOnClickListener(view -> {
                EmployeeListFragmentDirections.ActionEmployeeListFragmentToEmployeeProfileFragment action =
                        EmployeeListFragmentDirections.actionEmployeeListFragmentToEmployeeProfileFragment(employee);
                Navigation.findNavController(view).navigate(action);
            });
        }
    }
}