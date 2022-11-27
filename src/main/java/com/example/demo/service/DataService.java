package com.example.demo.service;

import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.vo.PaymentVO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataService {
    private final DataSource dataSource;

    private final PaymentRepository paymentRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public DataService(DataSource dataSource, PaymentRepository paymentRepository) {
        this.dataSource = dataSource;
        this.paymentRepository = paymentRepository;
        modelMapper = new ModelMapper();
    }

    public void processData(String filePath) throws IOException, CsvValidationException, SQLException {
        int rowIndex = 0;
        List<PaymentVO> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.skip(1);
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    rowIndex++;
                    PaymentVO payment = new PaymentVO();
                    payment.setStep(Integer.parseInt(line[0]));
                    payment.setType(line[1]);
                    payment.setAmount(Double.valueOf(line[2]));
                    payment.setNameOrig(line[3]);
                    payment.setOldBalanceOrg(Double.valueOf(line[4]));
                    payment.setNewBalanceOrig(Double.valueOf(line[5]));
                    payment.setNameDest(line[6]);
                    payment.setOldBalanceDest(Double.valueOf(line[7]));
                    payment.setNewBalanceDest(Double.valueOf(line[8]));
                    payment.setIsFraud(Integer.parseInt(line[9]));
                    payment.setIsFlaggedFraud(Integer.parseInt(line[10]));
                    list.add(payment);

                    if (rowIndex % 10000 == 0) {
                        log.info("processing row {}", rowIndex);
                        batchInsert(list);
                        hibernateInsert(list);
                        list.clear();
                    }
                }
                if (!list.isEmpty()) {
                    batchInsert(list);
                    hibernateInsert(list);
                }
            }
        }
    }

    private void batchInsert(List<PaymentVO> list) throws SQLException {
        long start = System.currentTimeMillis();
        String insertEmployeeSQL = "INSERT INTO /*+append*/ payment( id, step, type, amount, nameorig, oldbalanceorg, newbalanceorig, namedest, oldbalancedest, newbalancedest, isfraud, isflaggedfraud) " + "VALUES (payment_seq.nextval, ?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection(); PreparedStatement employeeStmt = connection.prepareStatement(insertEmployeeSQL)) {
            for (int i = 0; i < list.size(); i++) {
                employeeStmt.setInt(1, list.get(i).getStep());
                employeeStmt.setString(2, list.get(i).getType());
                employeeStmt.setDouble(3, list.get(i).getAmount());
                employeeStmt.setString(4, list.get(i).getNameOrig());
                employeeStmt.setDouble(5, list.get(i).getOldBalanceOrg());
                employeeStmt.setDouble(6, list.get(i).getNewBalanceOrig());
                employeeStmt.setString(7, list.get(i).getNameDest());
                employeeStmt.setDouble(8, list.get(i).getOldBalanceDest());
                employeeStmt.setDouble(9, list.get(i).getNewBalanceDest());
                employeeStmt.setInt(10, list.get(i).getIsFraud());
                employeeStmt.setInt(11, list.get(i).getIsFlaggedFraud());
                employeeStmt.addBatch();
            }
            employeeStmt.executeBatch();
        }
        long finish = System.currentTimeMillis();
        log.info("batchInsert method time = {} ms", finish - start);
    }

    private void hibernateInsert(List<PaymentVO> list) {
        long start = System.currentTimeMillis();
        paymentRepository.saveAll(list.stream().map(item -> modelMapper.map(item, Payment.class)).collect(Collectors.toList()));
        long finish = System.currentTimeMillis();
        log.info("hibernateInsert method time = {} ms", finish - start);
    }
}
