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
    TextView result;

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

                String output = String.format(
                        "Base Monthly Salary: $%.2f\nPer Day Salary: $%.2f\nLeave Days: %d\nLeave Deduction: $%.2f\n" +
                        "5th Monday Bonus: $%.2f\nSalary with Bonus: $%.2f\nTax Deduction: $%.2f\nMedical Deduction: $%.2f\n" +
                        "PF Deduction (10%%): $%.2f\nDabba Deduction: $%.2f\nTotal Deductions: $%.2f\nNet Monthly Salary: $%.2f",
                        base_salary, per_day_salary, leave_days, leave_deduction,
                        fifth_monday_bonus, salary_with_bonus, tax_amount, medical_amount,
                        pf_deduction, dabba_deduction, total_deductions, net_monthly
                );

                result.setText(output);

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
