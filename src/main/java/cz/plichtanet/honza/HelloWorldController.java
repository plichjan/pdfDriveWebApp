package cz.plichtanet.honza;

import com.itextpdf.text.DocumentException;
import cz.plichtanet.honza.dao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HelloWorldController {
    private static final String DOWNLOAD = "/download/";
    private static final String S3_PDF_DRIVE_ROOT = "s3://pdf-drive-root/";

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @Autowired
    private JendaS3Folder s3Folder;

    @Autowired
    private IUserDao userDao;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Neplatné uživatelské jmeno a heslo!");
        }

        if (logout != null) {
            model.addObject("msg", "Byli jste ušpěšně odhlášeni");
        }
        model.setViewName("login");

        return model;

    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePassword() {
        return "changePassword";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam String password, @RequestParam String password1, @RequestParam String password2) {
        if (!password1.equals(password2)) {
            throw new BadCredentialsException("New passwords is not same.");

        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // update database with new role
        userDao.changePassword(auth.getName(), password, password1);

        return "welcome"; // TODO forward to wanted URL
    }

    @RequestMapping(value = "/addRole", method = RequestMethod.GET)
    public String addRole() {
        addRole("ROLE_USER");

        return "welcome"; // TODO forward to wanted URL
    }

    private void addRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // update database with new role
        userDao.addRole(auth.getName(), role);

        // update the current Authentication
        List<GrantedAuthority> authorities = new ArrayList<>(auth.getAuthorities());
        authorities.add(new SimpleGrantedAuthority(role));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
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

    @RequestMapping(value = "/dir", method = RequestMethod.GET)
    public String showRootDir(HttpServletRequest request, ModelMap model) throws IOException {
        model.addAttribute("dir", s3Folder.getFolderList(""));
        return "dir";
    }

    @RequestMapping(value = "/dir/**/", method = RequestMethod.GET)
    public String showAllDir(HttpServletRequest request, ModelMap model) throws IOException {
        final String parent = decodePathSuffix(request, "/dir/");
        model.addAttribute("dir", s3Folder.getFolderList(parent));
        return "dir";
    }

    @RequestMapping(value = DOWNLOAD + "**/*.pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException, DocumentException {
        String filename = decodePathSuffix(request, DOWNLOAD);
        Resource resource = this.resourcePatternResolver.getResource(S3_PDF_DRIVE_ROOT + filename);

        ByteArrayOutputStream dst = new ByteArrayOutputStream();
        String text = String.format("IP: %2$s, uzivatel: %3$s, %1$tF %1$tT %1$tz", new Date(), request.getRemoteAddr(), request.getRemoteUser());
        TransparentWatermark.manipulatePdf(resource.getInputStream(), dst, text);
        byte[] bytes = dst.toByteArray();

        String fileName = URLEncoder.encode(new File(filename).getName(), "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private String decodePathSuffix(HttpServletRequest request, String prefix) throws UnsupportedEncodingException {
        final String suffix = request.getRequestURI().substring(request.getContextPath().length() + prefix.length());
        return URLDecoder.decode(suffix, "UTF-8");
    }


    //for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied() {

        ModelAndView model = new ModelAndView();

        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            model.addObject("username", auth.getName());
        }

        model.setViewName("403");
        return model;

    }
}