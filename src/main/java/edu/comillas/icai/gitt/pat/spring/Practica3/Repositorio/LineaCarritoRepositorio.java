package edu.comillas.icai.gitt.pat.spring.Practica3.Repositorio;

import edu.comillas.icai.gitt.pat.spring.Practica3.Entidades.LineaCarrito;
import edu.comillas.icai.gitt.pat.spring.Practica3.Entidades.LineaCarritoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineaCarritoRepositorio extends JpaRepository<LineaCarrito, LineaCarritoId> {}