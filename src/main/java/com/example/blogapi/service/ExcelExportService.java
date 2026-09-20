package com.example.blogapi.service;


import com.example.blogapi.entity.Post;
import com.example.blogapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public byte[] exportPosts(){

        List<Post> posts = postRepository.findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook()){
            var sheet = workbook.createSheet("Posts");

            var headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Title");
            headerRow.createCell(2).setCellValue("Author");
            headerRow.createCell(3).setCellValue("Status");
            headerRow.createCell(4).setCellValue("Created At");
            headerRow.createCell(5).setCellValue("Updated At");

            int rowIndex = 1;

            for (Post post : posts){
                var row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(post.getId());
                row.createCell(1).setCellValue(post.getTitle());
                row.createCell(2).setCellValue(post.getAuthor().getUsername());
                row.createCell(3).setCellValue(post.getStatus().name());
                row.createCell(4).setCellValue(post.getCreatedAt().toString());
                row.createCell(5).setCellValue(post.getUpdatedAt().toString());
            }

            for (int i = 0; i < 6; i++){
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            workbook.write(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e){
            throw new RuntimeException("Failed to export posts", e);
        }
    }
}
