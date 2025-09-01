package com.example.salarycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText baseSalary, taxAmount, medicalAmount, year, month, leaveDays, dabbaUnits;
    Button calculate;
    TextView result, netSalaryDescription, netSalaryAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseSalary = findViewById(R.id.baseSalary);
        taxAmount = findViewById(R.id.taxAmount);
        medicalAmount = findViewById(R.id.medicalAmount);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        leaveDays = findViewById(R.id.leaveDays);
        dabbaUnits = findViewById(R.id.dabbaUnits);

        calculate = findViewById(R.id.calculateButton);
        result = findViewById(R.id.resultText);
        netSalaryDescription = findViewById(R.id.netSalaryDescription);
        netSalaryAmount = findViewById(R.id.netSalaryAmount);

        calculate.setOnClickListener(v -> {
            try {
                double base_salary = Double.parseDouble(baseSalary.getText().toString());
                double tax_amount = Double.parseDouble(taxAmount.getText().toString());
                double medical_amount = Double.parseDouble(medicalAmount.getText().toString());
                int yr = Integer.parseInt(year.getText().toString());
                int mnth = Integer.parseInt(month.getText().toString());
                int leave_days = Integer.parseInt(leaveDays.getText().toString());
                int dabba_units = Integer.parseInt(dabbaUnits.getText().toString());
                int dabba_unit_cost = 30;

                double per_day_salary = base_salary / 30.0;
                double fifth_monday_bonus = hasFifthMonday(yr, mnth) ? per_day_salary : 0;
                double salary_with_bonus = base_salary + fifth_monday_bonus;
                double pf_deduction = base_salary * 0.10;
                double dabba_deduction = dabba_units * dabba_unit_cost;
                double leave_deduction = per_day_salary * leave_days;
                double total_deductions = tax_amount + medical_amount + pf_deduction + dabba_deduction + leave_deduction;
                double net_monthly = salary_with_bonus - total_deductions;

                String[] lines = {
                        String.format("Base Monthly Salary: ₹%.2f", base_salary),
                        String.format("Per Day Salary: ₹%.2f", per_day_salary),
                        String.format("Leave Days: %d", leave_days),
                        String.format("Leave Deduction: ₹%.2f", leave_deduction),
                        String.format("5th Monday Bonus: ₹%.2f", fifth_monday_bonus),
                        String.format("Salary with Bonus: ₹%.2f", salary_with_bonus),
                        String.format("Tax Deduction: ₹%.2f", tax_amount),
                        String.format("Medical Deduction: ₹%.2f", medical_amount),
                        String.format("PF Deduction (10%%): ₹%.2f", pf_deduction),
                        String.format("Dabba Deduction: ₹%.2f", dabba_deduction),
                        String.format("Total Deductions: ₹%.2f", total_deductions)
                };
                StringBuilder sb = new StringBuilder();
                for (String line : lines) {
                    sb.append(line).append("\n");
                }
                result.setText(sb.toString().trim());

                netSalaryDescription.setText("You will receive Net Monthly Salary");
                netSalaryAmount.setText("₹" + String.format("%.2f", net_monthly));

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please fill all fields correctly!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean hasFifthMonday(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        int mondayCount = 0;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                mondayCount++;
            }
        }
        return mondayCount >= 5;
    }
}
