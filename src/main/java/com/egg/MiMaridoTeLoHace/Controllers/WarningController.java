package com.egg.MiMaridoTeLoHace.Controllers;

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
            Map.entry(400, "La solicitud no es válida."),
            Map.entry(401, "Necesitás autenticarte para acceder a este recurso."),
            Map.entry(403, "No tenés permisos para acceder a este recurso."),
            Map.entry(404, "No encontramos la página solicitada."),
            Map.entry(405, "El método utilizado no está permitido para este recurso."),
            Map.entry(413, "El archivo o contenido enviado es demasiado grande."),
            Map.entry(500, "Ocurrió un error interno al procesar la solicitud."),
            Map.entry(503, "El servicio no está disponible temporalmente.")
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
