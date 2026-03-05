package edu.comillas.icai.gitt.pat.spring.Practica3.Servicio;

import edu.comillas.icai.gitt.pat.spring.Practica3.Entidades.*;
import edu.comillas.icai.gitt.pat.spring.Practica3.Modelo.AddLineaRequest;
import edu.comillas.icai.gitt.pat.spring.Practica3.Modelo.CrearCarritoRequest;
import edu.comillas.icai.gitt.pat.spring.Practica3.Repositorio.CarritoRepositorio;
import edu.comillas.icai.gitt.pat.spring.Practica3.Repositorio.LineaCarritoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoServicio {

    private final CarritoRepositorio carritoRepo;
    private final LineaCarritoRepositorio lineaRepo;

    public CarritoServicio(CarritoRepositorio carritoRepo, LineaCarritoRepositorio lineaRepo) {
        this.carritoRepo = carritoRepo;
        this.lineaRepo = lineaRepo;
    }

    public Iterable<Carrito> listar() {
        return carritoRepo.findAll();
    }

    public Carrito obtener(Long idCarrito) {
        return carritoRepo.findById(idCarrito).orElseThrow();
    }

    public Carrito crear(CrearCarritoRequest req) {
        Carrito c = new Carrito();
        c.setIdUsuario(req.idUsuario);
        c.setCorreoUsuario(req.correoUsuario);
        c.recalcularTotal();
        return carritoRepo.save(c);
    }

    public void borrarCarrito(Long idCarrito) {
        carritoRepo.deleteById(idCarrito);
    }

    @Transactional
    public Carrito anadirLinea(Long idCarrito, AddLineaRequest req) {
        Carrito carrito = obtener(idCarrito);

        LineaCarritoId lid = new LineaCarritoId(idCarrito, req.idArticulo);
        LineaCarrito linea = lineaRepo.findById(lid).orElse(null);

        if (linea == null) {
            linea = new LineaCarrito();
            linea.setId(lid);
            linea.setCarrito(carrito);
            linea.setUnidades(0);
            carrito.getLineas().add(linea);
        }

        linea.setDescripcion(req.descripcion);
        linea.setPrecioUnitario(req.precioUnitario);
        linea.setUnidades(linea.getUnidades() + req.unidades);
        linea.recalcularCoste();

        carrito.recalcularTotal();
        return carritoRepo.save(carrito);
    }

    @Transactional
    public void borrarLinea(Long idCarrito, Long idArticulo) {
        Carrito carrito = obtener(idCarrito);

        LineaCarritoId lid = new LineaCarritoId(idCarrito, idArticulo);
        LineaCarrito linea = lineaRepo.findById(lid).orElseThrow();

        carrito.getLineas().remove(linea); // orphanRemoval borra en BD
        carrito.recalcularTotal();
        carritoRepo.save(carrito);
    }