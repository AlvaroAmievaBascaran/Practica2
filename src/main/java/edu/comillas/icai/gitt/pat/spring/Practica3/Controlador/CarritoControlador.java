package edu.comillas.icai.gitt.pat.spring.Practica3.Controlador;

import edu.comillas.icai.gitt.pat.spring.Practica3.Entidades.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica3.Modelo.AddLineaRequest;
import edu.comillas.icai.gitt.pat.spring.Practica3.Modelo.CrearCarritoRequest;
import edu.comillas.icai.gitt.pat.spring.Practica3.Servicio.CarritoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoControlador {

    private final CarritoServicio service;

    public CarritoControlador(CarritoServicio service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Carrito> getCarritos() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Carrito creaCarrito(@RequestBody CrearCarritoRequest req) {
        return service.crear(req);
    }

    @GetMapping("/{idCarrito}")
    public Carrito getCarrito(@PathVariable Long idCarrito) {
        return service.obtener(idCarrito);
    }

    @DeleteMapping("/{idCarrito}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrarCarrito(@PathVariable Long idCarrito) {
        service.borrarCarrito(idCarrito);
    }

    @PostMapping("/{idCarrito}/linea")
    public Carrito anadirLinea(@PathVariable Long idCarrito, @RequestBody AddLineaRequest req) {
        return service.anadirLinea(idCarrito, req);
    }

    @DeleteMapping("/{idCarrito}/linea/{idArticulo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrarLinea(@PathVariable Long idCarrito, @PathVariable Long idArticulo) {
        service.borrarLinea(idCarrito, idArticulo);
    }
}