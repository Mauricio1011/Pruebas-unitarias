package com.laboratorio.productos.repository;

import com.laboratorio.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
