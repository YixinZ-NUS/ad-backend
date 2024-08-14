package sg.nus.iss.javaspring.adprojrct.Controllers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api")
public class OcrController {

	@Value("${ocr.input-volume}")
    private String inputVolume;

    @Value("${ocr.output-volume}")
    private String outputVolume;


    @PostMapping("ocr-service")
    public String callOcrService(@RequestParam("image") MultipartFile image) throws IOException, InterruptedException {
    	Path inputPath = Path.of(inputVolume, "input.jpg");
        Files.copy(new ByteArrayResource(image.getBytes()).getInputStream(), inputPath, StandardCopyOption.REPLACE_EXISTING);

        String dockerCommand = "docker run -v " + inputVolume + "\\T3.png" + ":/tmp/img.png -v " + outputVolume + ":/output jitesoft/tesseract-ocr /tmp/img.png /output/output.txt";
        Process process = Runtime.getRuntime().exec(dockerCommand);

        Thread.sleep(5000);

        File outputFile = new FileSystemResource(new File(outputVolume, "output.txt")).getFile();
        String text = new String(Files.readAllBytes(outputFile.toPath()));

        return text;
    }
}
