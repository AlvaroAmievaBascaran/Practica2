package edu.comillas.icai.gitt.pat.spring.Practica3.Servicio;

import edu.comillas.icai.gitt.pat.spring.Practica3.Entidades.Carrito;
import edu.comillas.icai.gitt.pat.spring.Practica3.Entidades.LineaCarrito;
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

        LineaCarrito linea = lineaRepo
                .findByCarritoIdCarritoAndIdArticulo(idCarrito, req.idArticulo)
                .orElse(null);

        if (linea == null) {
            linea = new LineaCarrito();
            linea.setCarrito(carrito);
            linea.setIdArticulo(req.idArticulo);
            linea.setPrecioUnitario(req.precioUnitario);
            linea.setUnidades(req.unidades);
            linea.recalcularCoste();

            carrito.getLineas().add(linea);
        } else {
            linea.setPrecioUnitario(req.precioUnitario);
            linea.setUnidades(linea.getUnidades() + req.unidades);
            linea.recalcularCoste();
        }

        carrito.recalcularTotal();
        return carritoRepo.save(carrito);
    }

    @Transactional
    public void borrarLinea(Long idCarrito, Long idArticulo) {
        Carrito carrito = obtener(idCarrito);

        LineaCarrito linea = lineaRepo
                .findByCarritoIdCarritoAndIdArticulo(idCarrito, idArticulo)
                .orElseThrow();

        carrito.getLineas().remove(linea);
        carrito.recalcularTotal();
        carritoRepo.save(carrito);
    }
}