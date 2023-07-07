package com.epam.rd.autocode;

import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SetMapperImpl implements SetMapper<Set<Employee>> {
    @Override
    public Set<Employee> mapSet(ResultSet resultSet) {
        Set<Employee> employees = new HashSet<>();
        int timeSet = 0;
        try {
            while (resultSet.next()) {
                timeSet++;
                BigInteger id =BigInteger.valueOf(resultSet.getInt("ID"));
                String firstName = resultSet.getString("FIRSTNAME");
                String lastName = resultSet.getString("LASTNAME");
                String middleName = resultSet.getString("MIDDLENAME");
                Position position = Position.valueOf(resultSet.getString("POSITION").toUpperCase());
                LocalDate hired = resultSet.getDate("HIREDATE").toLocalDate();
                BigDecimal salary = resultSet.getBigDecimal("SALARY");
                FullName fullName = new FullName(firstName, lastName, middleName);
                BigInteger managerId = BigInteger.valueOf(resultSet.getInt("MANAGER"));

                Employee employee = new Employee(id, fullName, position, hired, salary, findById(resultSet, managerId));
                employees.add(employee);

                for (int i = 1; i < timeSet; i++) {
                    resultSet.next();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employees;
    }


    private Employee findById(ResultSet resultSet, BigInteger managerId) throws SQLException {
        resultSet.first();
       while (resultSet.next()){

            if(BigInteger.valueOf(resultSet.getInt("ID")).equals(managerId)){
                BigInteger id =BigInteger.valueOf(resultSet.getInt("ID"));
                String firstName = resultSet.getString("FIRSTNAME");
                String lastName = resultSet.getString("LASTNAME");
                String middleName = resultSet.getString("MIDDLENAME");
                Position position = Position.valueOf(resultSet.getString("POSITION").toUpperCase());
                LocalDate hired = resultSet.getDate("HIREDATE").toLocalDate();
                BigDecimal salary = resultSet.getBigDecimal("SALARY");
                FullName fullName = new FullName(firstName, lastName, middleName);
                BigInteger managerIdInsert = BigInteger.valueOf(resultSet.getInt("MANAGER"));

                resultSet.first();
                return new Employee(id, fullName, position, hired, salary, findById(resultSet, managerIdInsert));
            }
        }

        resultSet.first();
        return null;
    }
}
