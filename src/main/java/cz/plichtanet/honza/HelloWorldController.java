package cz.plichtanet.honza;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

@Controller
@RequestMapping("/")
public class HelloWorldController {
    private static final String DOWNLOAD = "/download/";
    private static final String S3_PDF_DRIVE_ROOT = "s3://pdf-drive-root/";

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        model.addAttribute("greeting", "Hello World from Spring 4 MVC");
        return "welcome";
    }

    @RequestMapping(value = "/helloagain", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model) {
        model.addAttribute("greeting", "Hello World Again, from Spring 4 MVC");
        return "welcome";
    }

    @RequestMapping(value = "/pdfs", method = RequestMethod.GET)
    public String showAllPdfs(ModelMap model) throws IOException {
        model.addAttribute("pdfs", this.resourcePatternResolver.getResources(S3_PDF_DRIVE_ROOT + "**/*.pdf"));
        return "pdfs";
    }

    @RequestMapping(value = DOWNLOAD + "**/*.pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException, DocumentException {
        String filename = request.getRequestURI().substring(request.getContextPath().length() + DOWNLOAD.length());
        filename = URLDecoder.decode(filename, "UTF-8");
        Resource resource = this.resourcePatternResolver.getResource(S3_PDF_DRIVE_ROOT + filename);

        ByteArrayOutputStream dst = new ByteArrayOutputStream();
        String text = String.format("Tvoje IP adresa: %2$s, uzivate: %3$s, %1$tF %1$tT %1$tz", new Date(), request.getRemoteAddr(), request.getRemoteUser());
        TransparentWatermark.manipulatePdf(resource.getInputStream(), dst, text);
        byte[] bytes = dst.toByteArray();

        String fileName = URLEncoder.encode(new File(filename).getName(), "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

}