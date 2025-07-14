package com.empdept.report;

import com.empdept.entity.Department;
import com.empdept.repository.DepartmentRepository;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private DepartmentRepository departmentRepo;

    public byte[] generateEmployeeDepartmentReport() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<JasperPrint> pages = new ArrayList<>();
        List<Department> departments = departmentRepo.findAll();

        for (Department dept : departments) {
            Map<String, Object> params = new HashMap<>();
            params.put("departmentName", dept.getName());
            params.put("location", dept.getLocation());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dept.getEmployees());

            InputStream reportStream = getClass().getResourceAsStream("/jasper/department_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            pages.add(jasperPrint);
        }

        // Merge all pages into one PDF
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(pages));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();

        return baos.toByteArray();
    }
}
