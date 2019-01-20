package by.bsu.converters.controller;

import by.bsu.converters.controller.result.AvailableConverterTypes;
import by.bsu.converters.controller.result.ConverterResult;
import by.bsu.converters.controller.result.ErrorResult;
import by.bsu.converters.service.ConverterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ConverterConvtroller {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public AvailableConverterTypes index() {
        return new AvailableConverterTypes("Here you could see supproted converter types",
                                           new String[] {"/convert/temperature", "/convert/distance", "/convert/weight"});
    }

    @RequestMapping(value = "/convert/{converterType}")
    public ConverterResult convert(@PathVariable String converterType, @RequestParam String from,
                                   @RequestParam String to, @RequestParam double value) {
        double result = ConverterFactory.getProvider(converterType).createConverter(from, to).convert(value);
        return new ConverterResult(converterType, from, to, value, result);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ErrorResult handleError(HttpServletRequest request, Exception ex) {
        return new ErrorResult(ex.getMessage(),
                               request.getRequestURL().append("?").append(request.getQueryString()).toString());
    }

}
