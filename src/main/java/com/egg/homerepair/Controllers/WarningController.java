package com.egg.homerepair.Controllers;

import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WarningController implements ErrorController {

    private static final Map<Integer, String> ERROR_MESSAGES = Map.ofEntries(
            Map.entry(400, "La solicitud no es vÃ¡lida."),
            Map.entry(401, "NecesitÃ¡s autenticarte para acceder a este recurso."),
            Map.entry(403, "No tenÃ©s permisos para acceder a este recurso."),
            Map.entry(404, "No encontramos la pÃ¡gina solicitada."),
            Map.entry(405, "El mÃ©todo utilizado no estÃ¡ permitido para este recurso."),
            Map.entry(413, "El archivo o contenido enviado es demasiado grande."),
            Map.entry(500, "OcurriÃ³ un error interno al procesar la solicitud."),
            Map.entry(503, "El servicio no estÃ¡ disponible temporalmente.")
    );

    @RequestMapping("/error")
    public ModelAndView renderErrorPage(HttpServletRequest request) {
        int status = resolveStatus(request);
        String message = ERROR_MESSAGES.getOrDefault(
                status,
                "No pudimos completar la solicitud.");

        ModelAndView errorPage = new ModelAndView("warning");
        errorPage.addObject("codigo", status);
        errorPage.addObject("mensaje", message);
        return errorPage;
    }

    private int resolveStatus(HttpServletRequest request) {
        Object attribute = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (attribute instanceof Integer) {
            return (Integer) attribute;
        }
        return 500;
    }
}

