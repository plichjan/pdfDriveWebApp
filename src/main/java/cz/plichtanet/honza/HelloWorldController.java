package cz.plichtanet.honza;

import com.amazonaws.util.IOUtils;
import com.itextpdf.text.DocumentException;
import cz.plichtanet.honza.dao.IUserDao;
import cz.plichtanet.honza.service.IAfterLoginUrl;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class HelloWorldController {
    private static final String DOWNLOAD = "/download/";
    private static final String DOWNLOAD_PDF = "/downloadPdf/";
    private static final String HTML = "/html/";
    private static final String PDF_DRIVE_ROOT = "/home/ec2-user/pdf-drive-root/";
    private static final String FILE_PDF_DRIVE_ROOT = "file://" + PDF_DRIVE_ROOT;

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IAfterLoginUrl afterLoginUrl;

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
        // update database password
        userDao.changePassword(auth.getName(), password, password1);

        addRole("ROLE_USER");
        return "redirect:/";
    }

    @RequestMapping(value = "/setPassword", method = RequestMethod.GET)
    public String setPassword() {
        return "setPassword";
    }

    @RequestMapping(value = "/setPassword", method = RequestMethod.POST)
    public String setPassword(@RequestParam String username, @RequestParam String password) {
        // update user password
        userDao.setPassword(username, password);
        return "redirect:/";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public String addUser() {
        return "addUser";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestParam String username, @RequestParam String password) {
        // update user password
        userDao.addUser(username, password);
        userDao.addRole(username, "ROLE_NIC");
        return "redirect:/";
    }

    @RequestMapping(value = "/nda", method = RequestMethod.GET)
    public String nda() {
        return "NDA";
    }

    @RequestMapping(value = "/nda", params = "status=ok", method = RequestMethod.GET)
    public String addRolePdfUser() {
        addRole("ROLE_PDF_USER");

        return "redirect:" + afterLoginUrl.getUrl();
    }

    private void addRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // update database with new role
        userDao.addRole(auth.getName(), role);

        // update the current Authentication
        List<GrantedAuthority> authorities = new ArrayList<>(auth.getAuthorities());
        authorities.add(new SimpleGrantedAuthority(role));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @RequestMapping(value = "/helloagain", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model) {
        model.addAttribute("greeting", "Hello World Again, from Spring 4 MVC");
        return "welcome";
    }

    @RequestMapping(value = "/pdfs", method = RequestMethod.GET)
    public String showAllPdfs(ModelMap model) throws IOException {
        model.addAttribute("pdfs", this.resourcePatternResolver.getResources(FILE_PDF_DRIVE_ROOT + "**/*.pdf"));
        return "pdfs";
    }

    @RequestMapping(value = "/dir", method = RequestMethod.GET)
    public String showRootDir(HttpServletRequest request, ModelMap model) throws IOException, URISyntaxException {
        model.addAttribute("dir", getSortedDirList(""));
        model.addAttribute("parent", "");
        model.addAttribute("root", PDF_DRIVE_ROOT);
        return "dir";
    }

    @RequestMapping(value = "/dir/**/", method = RequestMethod.GET)
    public String showAllDir(HttpServletRequest request, ModelMap model) throws IOException, URISyntaxException {
        final String parent = decodePathSuffix(request, "/dir/");
        model.addAttribute("dir", getSortedDirList(parent));
        model.addAttribute("parent", parent.endsWith("/") ? parent : (parent + "/"));
        model.addAttribute("root", PDF_DRIVE_ROOT);
        return "dir";
    }

    private File[] getSortedDirList(String parent) throws URISyntaxException {
        File[] files = new File(new File(new URI(FILE_PDF_DRIVE_ROOT)), parent).listFiles();
        if (files != null) {
            Arrays.sort(files);
        }
        return files;
    }

    @RequestMapping(value = DOWNLOAD + "**/*.pdf", method = RequestMethod.GET)
    public String download(HttpServletRequest request) throws IOException, DocumentException {
        String url = request.getServletPath().replace(DOWNLOAD, DOWNLOAD_PDF);
        if (request.isUserInRole("PDF_USER")) {
            return "forward:" + url;
        } else {
            afterLoginUrl.setUrl(url);
            return "forward:/nda";
        }
    }

    @RequestMapping(value = DOWNLOAD_PDF + "**/*.pdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadPdf(HttpServletRequest request) throws IOException, DocumentException {
        String filename = decodePathSuffix(request, DOWNLOAD_PDF);
        Resource resource = this.resourcePatternResolver.getResource(FILE_PDF_DRIVE_ROOT + filename);

        ByteArrayOutputStream dst = new ByteArrayOutputStream();
        String text = String.format("IP: %2$s\n%3$s\n%1$tF %1$tT %1$tz", new Date(), request.getRemoteAddr(), request.getRemoteUser());
        TransparentWatermark.manipulatePdf(resource.getInputStream(), dst, text);
        byte[] bytes = dst.toByteArray();

        // log download
        userDao.logDownload(request.getRemoteUser(), filename);

        String fileName = URLEncoder.encode(new File(filename).getName(), "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = HTML + "**/*.html", method = RequestMethod.GET)
    public ResponseEntity<byte[]> html(HttpServletRequest request) throws IOException, DocumentException {
        String filename = decodePathSuffix(request, HTML);
        Resource resource = this.resourcePatternResolver.getResource("s3:/" + HTML + filename);

        byte[] bytes = IOUtils.toByteArray(resource.getInputStream());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        httpHeaders.setContentLength(bytes.length);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    private String decodePathSuffix(HttpServletRequest request, String prefix) throws UnsupportedEncodingException {
        final String suffix = request.getRequestURI().substring(request.getContextPath().length() + prefix.length());
        return URLDecoder.decode(suffix, "UTF-8");
    }


    //for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied(HttpServletRequest request) throws UnsupportedEncodingException {

        ModelAndView model = new ModelAndView();

        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String url = decodePathSuffix(request, "");
            if (!request.isUserInRole("ROLE_USER")) {
                afterLoginUrl.setUrl(url);
                model.setViewName("changePassword");
                return model;
            }

            if (url != null && url.startsWith("/download/")) {
                model.setViewName("forward:/nda");
                return model;
            }
            model.addObject("username", auth.getName());
        }

        model.setViewName("403");
        return model;

    }
}