package cz.cvut.fit.nebesluk.tjv_semestral_client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginService  {
    ClientService clientService;

    public LoginService(ClientService clientService_){
        clientService = clientService_;
    }

    public boolean Login(String username, String password, HttpServletRequest request){
        if(!clientService.Login(username,password)){
            return false;
        }

        var token =
                new UsernamePasswordAuthenticationToken(username,password);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        return true;

    }
}
